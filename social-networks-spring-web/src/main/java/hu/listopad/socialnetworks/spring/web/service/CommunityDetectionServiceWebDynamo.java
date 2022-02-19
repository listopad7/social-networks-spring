package hu.listopad.socialnetworks.spring.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import hu.listopad.socialnetworks.spring.data.dynamo.ResultRepository;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import hu.listopad.socialnetworks.spring.web.messageing.SendGraphDataMessage;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommunityDetectionServiceWebDynamo implements CommunityDetectionServiceWeb{

    private final ResultRepository resultRepository;
    private final SendGraphDataMessage sendGraphDataMessage;
    private final MapStructMapperDynamo mapStructMapperDynamo;

    public CommunityDetectionServiceWebDynamo(ResultRepository resultRepository, SendGraphDataMessage sendGraphDataMessage, MapStructMapperDynamo mapStructMapperDynamo) {
        this.resultRepository = resultRepository;
        this.sendGraphDataMessage = sendGraphDataMessage;
        this.mapStructMapperDynamo = mapStructMapperDynamo;
    }


    @Override
    public Iterable<SlimCommunityDetectionResult> getGraphs(String userId) {

        return mapStructMapperDynamo.communityDetectionResultDynamoListToSlimResultList(
                resultRepository.findByPrimaryKey(userId));
    }

    @Override
    public CommunityDetectionResult getOneResponse(String userId, String graphName) throws NoSuchElementException {

        return mapStructMapperDynamo.dynamoToCommunityDetectionResult(
                resultRepository.findByKeys(userId, graphName).get());
    }

    @Override
    public void deleteGraph(String userId, String graphName) {

        resultRepository.deleteByKeys(userId, graphName);
    }

    @Override
    public String newCalculation(String userId, String graphName, String payload) throws JsonProcessingException{

        sendGraphDataMessage.sendMessage(userId, graphName, payload);

        CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
        communityDetectionResult.setUserId(userId);
        communityDetectionResult.setGraphName(graphName);
        communityDetectionResult.setStatus(Status.IN_PROGRESS);

        resultRepository.save(mapStructMapperDynamo.communityDetectionResultToDynamo(communityDetectionResult));

        return "Accepted";
    }

    @Override
    public String getSample() {

        // TODO: implement this.
        return null;
    }
}
