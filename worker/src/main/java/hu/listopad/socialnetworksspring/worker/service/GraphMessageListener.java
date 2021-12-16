package hu.listopad.socialnetworksspring.worker.service;

import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import hu.listopad.socialnetworksspring.worker.model.CommunityDetectionResult;
import hu.listopad.socialnetworksspring.worker.model.ResultRepository;
import hu.listopad.socialnetworksspring.worker.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Slf4j
public class GraphMessageListener {

        private final JsonToWeightedGraphConverter jsonToWeightedGraphConverter;

        private final Function<WeightedGraph, CommunityDetectionService> communityDetectionServiceFactory;

        private final ResultRepository resultRepository;

        private final ResultConverter resultConverter;

        private final Supplier<CommunityDetectionResult> communityDetectionResultSupplier;

        private final Configuration conf = Configuration.defaultConfiguration();





        @Autowired
        public GraphMessageListener(JsonToWeightedGraphConverter jsonToWeightedGraphConverter,
                                    Function<WeightedGraph, CommunityDetectionService> communityDetectionServiceFactory,
                                    ResultRepository resultRepository,
                                    ResultConverter resultConverter,
                                    Supplier<CommunityDetectionResult> communityDetectionResultSupplier, CommunityDetectionResult communityDetectionResult){
                this.jsonToWeightedGraphConverter = jsonToWeightedGraphConverter;
                this.communityDetectionServiceFactory = communityDetectionServiceFactory;
                this.resultRepository = resultRepository;
                this.communityDetectionResultSupplier = communityDetectionResultSupplier;
                this.resultConverter = resultConverter;


        }

        @QueueListener(value = "${sqsUrl}", concurrencyLevel = 2)
        //@AutoVisibilityExtender(visibilityTimeoutInSeconds = 60, maximumDurationInSeconds = 300, bufferTimeInSeconds = 30)
        public void processMessage(@Payload final String payload){

                String userId = JsonPath.using(conf).parse(payload).read("$.userId");
                String graphName = JsonPath.using(conf).parse(payload).read("$.graphName");
                CommunityDetectionResult notYetProcessed = communityDetectionResultSupplier.get();
                notYetProcessed.setUserId(userId);
                notYetProcessed.setGraphName(graphName);
                notYetProcessed.setStatus(Status.IN_PROGRESS);
                resultRepository.save(notYetProcessed);

                try {
                        CommunityDetectionService communityDetectionService = communityDetectionServiceFactory
                                .apply(jsonToWeightedGraphConverter
                                        .convertToWeightedGraph(payload));
                        List<CommunityDetectionOnePassResult> communityDetectionResults= communityDetectionService.getCommunityDetectionResults();
                        List<String> graphResultStrings = resultConverter.convertResultList(JsonPath.using(conf.addOptions(Option.SUPPRESS_EXCEPTIONS)).parse(payload).read("$.graph").toString(), communityDetectionResults);
                        CommunityDetectionResult processed =communityDetectionResultSupplier.get();
                        processed.setUserId(userId);
                        processed.setGraphName(graphName);
                        processed.setStatus(Status.DONE);
                        processed.setResultList(graphResultStrings);
                        resultRepository.save(processed);
                }catch(GraphException e){
                        CommunityDetectionResult processingError =communityDetectionResultSupplier.get();
                        processingError.setUserId(userId);
                        processingError.setGraphName(graphName);
                        processingError.setStatus(Status.ERROR);
                        resultRepository.save(processingError);
                }

            // process the message payload here
                log.info("Payload: {}", payload);
        }

}
