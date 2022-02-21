package hu.listopad.socialnetworks.spring.worker.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import hu.listopad.socialnetworks.spring.data.GraphException;
import hu.listopad.socialnetworks.spring.data.JsonToWeightedGraphConverter;
import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import hu.listopad.socialnetworks.spring.data.dynamo.ResultRepository;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LouvainDynamoOrchestrator implements OrchestratorService{

    private final JsonToWeightedGraphConverter jsonToWeightedGraphConverter;

    private final CommunityDetectionService communityDetectionService;

    private final ResultRepository resultRepository;

    private final Configuration conf = Configuration.defaultConfiguration();

    private final MapStructMapperDynamo mapStructMapperDynamo;


    @Autowired
    public LouvainDynamoOrchestrator(JsonToWeightedGraphConverter jsonToWeightedGraphConverter, CommunityDetectionService communityDetectionService, ResultRepository resultRepository, MapStructMapperDynamo mapStructMapperDynamo) {
        this.jsonToWeightedGraphConverter = jsonToWeightedGraphConverter;
        this.communityDetectionService = communityDetectionService;
        this.resultRepository = resultRepository;
        this.mapStructMapperDynamo = mapStructMapperDynamo;
    }

    public void detectAndSave(String payload){

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
    }catch(
    GraphException e){
        e.printStackTrace();
        CommunityDetectionResult processingError = new CommunityDetectionResult();
        processingError.setUserId(userId);
        processingError.setGraphName(graphName);
        processingError.setStatus(Status.ERROR);
        processingError.setOriginalGraph(g.getWgMap());
        processingError.setErrorMessage(e.getMessage());
        resultRepository.save(mapStructMapperDynamo.communityDetectionResultToDynamo(processingError));
    }
    }
}
