package hu.listopad.socialnetworks.spring.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionResponse;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionToResponseMapper;
import hu.listopad.socialnetworks.spring.web.service.CommunityDetectionServiceWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;

@RestController
public class CommunityDetectionController {

    private final CommunityDetectionServiceWeb communityDetectionServiceWeb;
    private final CommunityDetectionToResponseMapper communityDetectionToResponseMapper;

    @Autowired
    public CommunityDetectionController(CommunityDetectionServiceWeb communityDetectionServiceWeb, CommunityDetectionToResponseMapper communityDetectionToResponseMapper) {
        this.communityDetectionServiceWeb = communityDetectionServiceWeb;
        this.communityDetectionToResponseMapper = communityDetectionToResponseMapper;
    }


    @GetMapping("/{userId}/graphs")
    public ResponseEntity<Iterable<SlimCommunityDetectionResult>> getGraphs(@PathVariable String userId){

        Iterable<SlimCommunityDetectionResult> results= communityDetectionServiceWeb.getGraphs(userId);
        return new ResponseEntity<>(results, HttpStatus.OK);

    }


    @GetMapping(value = "/{userId}/{graphName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommunityDetectionResponse> getOneResult(@PathVariable String userId, @PathVariable String graphName){


        try {
            CommunityDetectionResult communityDetectionResult = communityDetectionServiceWeb.getOneResponse(userId, graphName);

            return new ResponseEntity<>(communityDetectionToResponseMapper.mapToResponse(communityDetectionResult), HttpStatus.OK);


        } catch (NoSuchElementException e){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/sample")
    public String getSample(){

        return communityDetectionServiceWeb.getSample();
    }


    @DeleteMapping("/{userId}/{graphName}")
    public ResponseEntity<Void> deleteGraph(@PathVariable String userId,@PathVariable String graphName){

        communityDetectionServiceWeb.deleteGraph(userId, graphName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{userId}/{graphName}")
    public ResponseEntity<String> newCalculation(@PathVariable String userId, @PathVariable String graphName, @RequestBody String payload){


        try {

            return new ResponseEntity<>(communityDetectionServiceWeb.newCalculation(userId, graphName, payload),
                    HttpStatus.ACCEPTED);

        } catch (JsonProcessingException e) {

            return new ResponseEntity<>("Invalid Json", HttpStatus.BAD_REQUEST);

        }


    }



    
}
