package hu.listopad.socialnetworks.spring.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import hu.listopad.socialnetworks.spring.data.dynamo.ResultRepository;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import hu.listopad.socialnetworks.spring.web.messageing.SendGraphDataMessage;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionResponse;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionToResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;

@RestController
public class CommunityDetectionController {

    private final ResultRepository resultRepository;
    private final SendGraphDataMessage sendGraphDataMessage;
    private final MapStructMapperDynamo mapStructMapperDynamo;
    private final CommunityDetectionToResponseMapper communityDetectionToResponseMapper;

    @Autowired
    public CommunityDetectionController(ResultRepository resultRepository, SendGraphDataMessage sendGraphDataMessage, MapStructMapperDynamo mapStructMapperDynamo, CommunityDetectionToResponseMapper communityDetectionToResponseMapper) {
        this.resultRepository = resultRepository;
        this.sendGraphDataMessage = sendGraphDataMessage;
        this.mapStructMapperDynamo = mapStructMapperDynamo;
        this.communityDetectionToResponseMapper = communityDetectionToResponseMapper;
    }


    @GetMapping("/{userId}/graphs")
    public ResponseEntity<Iterable<SlimCommunityDetectionResult>> getGraphs(@PathVariable String userId){

        Iterable<SlimCommunityDetectionResult> results= mapStructMapperDynamo.communityDetectionResultDynamoListToSlimResultList(
                resultRepository.findByPrimaryKey(userId)
        );

        return new ResponseEntity<>(results, HttpStatus.OK);

    }


    @GetMapping("/{userId}/{graphName}")
    public ResponseEntity<CommunityDetectionResponse> getOneResult(@PathVariable String userId, @PathVariable String graphName){


        try {
            CommunityDetectionResult communityDetectionResult = mapStructMapperDynamo.dynamoToCommunityDetectionResult(
                resultRepository.findByKeys(userId, graphName).get());

            return new ResponseEntity<>(communityDetectionToResponseMapper.mapToResponse(communityDetectionResult), HttpStatus.OK);


        } catch (NoSuchElementException e){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/sample")
    public String getSample(){

        //TODO implement this
        return "sample";
    }


    @DeleteMapping("/{userId}/{graphName}")
    public ResponseEntity<Void> deleteGraph(@PathVariable String userId,@PathVariable String graphName){

        resultRepository.deleteByKeys(userId, graphName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{userId}/{graphName}")
    public ResponseEntity<String> newCalculation(@PathVariable String userId, @PathVariable String graphName, @RequestBody String payload){


        try {

            sendGraphDataMessage.sendMessage(userId, graphName, payload);

        } catch (JsonProcessingException e) {

            return new ResponseEntity<>("Invalid Json", HttpStatus.BAD_REQUEST);

        }

        CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
        communityDetectionResult.setUserId(userId);
        communityDetectionResult.setGraphName(graphName);
        communityDetectionResult.setStatus(Status.IN_PROGRESS);


        resultRepository.save(mapStructMapperDynamo.communityDetectionResultToDynamo(communityDetectionResult));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



    
}
