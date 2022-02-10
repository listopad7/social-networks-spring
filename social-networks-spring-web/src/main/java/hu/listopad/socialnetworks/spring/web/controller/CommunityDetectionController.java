package hu.listopad.socialnetworks.spring.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hu.listopad.socialnetworks.spring.web.messageing.SendGraphDataMessage;
import hu.listopad.socialnetworks.spring.web.mapstruct.dtos.CommunityDetectionResultDTO;
import hu.listopad.socialnetworks.spring.web.mapstruct.mappers.MapStructMapper;
import hu.listopad.socialnetworks.spring.dynamo.ResultRepository;
import hu.listopad.socialnetworks.spring.dynamo.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CommunityDetectionController {

    private final ResultRepository resultRepository;
    private final SendGraphDataMessage sendGraphDataMessage;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public CommunityDetectionController(ResultRepository resultRepository, SendGraphDataMessage sendGraphDataMessage, MapStructMapper mapStructMapper) {
        this.resultRepository = resultRepository;
        this.sendGraphDataMessage = sendGraphDataMessage;
        this.mapStructMapper = mapStructMapper;
    }


    @GetMapping("/{userId}/graphs")
    public ResponseEntity<List<CommunityDetectionResultDTO>> getGraphs(@PathVariable String userId){

        List<CommunityDetectionResultDTO> resultList= mapStructMapper.communityDetectionResultListToDtoList(
                resultRepository.getCommunityDetectionResultByPartitionKey(userId)
        );

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }


    @GetMapping("/{userId}/{graphName}")
    public ResponseEntity<String> getOneResult(@PathVariable String userId,@PathVariable String graphName){

        ObjectMapper objectMapper = new ObjectMapper();

        CommunityDetectionResultDTO communityDetectionResultDTO = mapStructMapper.communityDetectionResultToDto(
                resultRepository.getCommunityDetectionResultByKeys(userId, graphName));
        try {
            System.out.println(objectMapper.writeValueAsString(communityDetectionResultDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            return new ResponseEntity<>(objectMapper.writeValueAsString(communityDetectionResultDTO), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(500));
        }

    }


    @GetMapping("/sample")
    public String getSample(){

        //TODO implement this
        return "sample";
    }


    @DeleteMapping("/{userId}/{graphName}")
    public ResponseEntity<Void> deleteGraph(@PathVariable String userId,@PathVariable String graphName){

        resultRepository.deleteCommunityDetectionResult(userId, graphName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{userId}/{graphName}")
    public ResponseEntity<Void> newCalculation(@PathVariable String userId, @PathVariable String graphName, @RequestBody String payload){
        System.out.println(payload);
        sendGraphDataMessage.sendMessage(userId, graphName, payload);

        String jsonFormattedString = "";



        CommunityDetectionResultDTO communityDetectionResultDTO =
                new CommunityDetectionResultDTO();
        communityDetectionResultDTO.setUserId(userId);
        communityDetectionResultDTO.setGraphName(graphName);
        communityDetectionResultDTO.setStatus(Status.IN_PROGRESS);
        communityDetectionResultDTO.setOriginalGraph(payload);

        resultRepository.save(mapStructMapper.dtoToCommunityDetectionResult(communityDetectionResultDTO));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    
}
