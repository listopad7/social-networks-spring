package hu.listopad.socialnetworksspring.controller;

import hu.listopad.socialnetworksspring.mapstruct.dtos.CommunityDetectionResultDTO;
import hu.listopad.socialnetworksspring.mapstruct.mappers.MapStructMapper;
import hu.listopad.socialnetworksspring.messageing.SendGraphDataMessage;
import hu.listopad.socialnetworksspring.persistence.ResultRepository;
import hu.listopad.socialnetworksspring.persistence.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommunityDetectionController {

    private ResultRepository resultRepository;
    private SendGraphDataMessage sendGraphDataMessage;
    private MapStructMapper mapStructMapper;

    @Autowired
    public CommunityDetectionController(ResultRepository resultRepository, SendGraphDataMessage sendGraphDataMessage, MapStructMapper mapStructMapper) {
        this.resultRepository = resultRepository;
        this.sendGraphDataMessage = sendGraphDataMessage;
        this.mapStructMapper = mapStructMapper;
    }

    @Value("${sqsUrl}")
    private String queueUrl;

    @Value("${localstack.endpoint}")
    private String endpointUrl;


    @GetMapping("/{userId}/graphs")
    public ResponseEntity<List<CommunityDetectionResultDTO>> getGraphs(@PathVariable String userId){

        List<CommunityDetectionResultDTO> resultList= mapStructMapper.communityDetectionResultListToDtoList(
                resultRepository.getCommunityDetectionResultByPartitionKey(userId)
        );

        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }


    @GetMapping("/{userId}/{graphName}")
    public ResponseEntity<CommunityDetectionResultDTO> getOneResult(@PathVariable String userId,@PathVariable String graphName){

        CommunityDetectionResultDTO communityDetectionResultDTO = mapStructMapper.communityDetectionResultToDto(
                resultRepository.getCommunityDetectionResultByKeys(userId, graphName));

        return new ResponseEntity<>(communityDetectionResultDTO, HttpStatus.OK);
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


        sendGraphDataMessage.sendMessage(userId, graphName, payload);

        List<String> originalGraph= new ArrayList<>();
        originalGraph.add(payload);
        CommunityDetectionResultDTO communityDetectionResultDTO =
                new CommunityDetectionResultDTO();
        communityDetectionResultDTO.setUserId(userId);
        communityDetectionResultDTO.setGraphName(graphName);
        communityDetectionResultDTO.setStatus(Status.IN_PROGRESS);
        //communityDetectionResultDTO.setResultList(originalGraph);

        resultRepository.save(mapStructMapper.dtoToCommunityDetectionResult(communityDetectionResultDTO));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    
}
