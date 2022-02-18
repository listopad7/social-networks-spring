package hu.listopad.socialnetworks.spring.worker.messagelistener;

import com.jashmore.sqs.spring.decorator.visibilityextender.AutoVisibilityExtender;
import hu.listopad.socialnetworks.spring.data.*;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import hu.listopad.socialnetworks.spring.data.dynamo.ResultRepository;
import hu.listopad.socialnetworks.spring.worker.service.CommunityDetectionOnePassResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import hu.listopad.socialnetworks.spring.worker.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;


@Service
@Slf4j
public class GraphMessageListenerSqs implements GraphMessageListener{

        private final JsonToWeightedGraphConverter jsonToWeightedGraphConverter;

        private final CommunityDetectionService communityDetectionService;

        private final ResultRepository resultRepository;

        private final Configuration conf = Configuration.defaultConfiguration();

        private final MapStructMapperDynamo mapStructMapperDynamo;



        @Autowired
        public GraphMessageListenerSqs(JsonToWeightedGraphConverter jsonToWeightedGraphConverter,
                                       CommunityDetectionService communityDetectionService,
                                       ResultRepository resultRepository,
                                       MapStructMapperDynamo mapStructMapperDynamo){
                this.jsonToWeightedGraphConverter = jsonToWeightedGraphConverter;
                this.communityDetectionService = communityDetectionService;
                this.resultRepository = resultRepository;
                this.mapStructMapperDynamo = mapStructMapperDynamo;
        }

        @QueueListener(value = "${sqsUrl}", concurrencyLevel = 2)
        @AutoVisibilityExtender(visibilityTimeoutInSeconds = 60, maximumDurationInSeconds = 300, bufferTimeInSeconds = 30)
        public void processMessage(@Payload final String payload){

                String userId = JsonPath.using(conf).parse(payload).read("$.userId");
                String graphName = JsonPath.using(conf).parse(payload).read("$.graphName");

                WeightedGraph g = new WeightedGraph();

                try {
                        g = jsonToWeightedGraphConverter.convertToWeightedGraph(payload);

                        List<CommunityDetectionOnePassResult> communityDetectionResults =
                                communityDetectionService.getCommunityDetectionResults(g);

                        CommunityDetectionResult processed = new CommunityDetectionResult();
                        processed.setUserId(userId);
                        processed.setGraphName(graphName);
                        processed.setStatus(Status.DONE);
                        processed.setOriginalGraph(g.getWgMap());
                        LouvainResultConverter.setCalculationResults(communityDetectionResults, processed);


                        resultRepository.save(mapStructMapperDynamo.communityDetectionResultToDynamo(processed));
                }catch(GraphException e){
                        e.printStackTrace();
                        CommunityDetectionResult processingError = new CommunityDetectionResult();
                        processingError.setUserId(userId);
                        processingError.setGraphName(graphName);
                        processingError.setStatus(Status.ERROR);
                        processingError.setOriginalGraph(g.getWgMap());
                        processingError.setErrorMessage(e.getMessage());
                        resultRepository.save(mapStructMapperDynamo.communityDetectionResultToDynamo(processingError));
                }

                log.info("Payload: {}", payload);
        }

}
