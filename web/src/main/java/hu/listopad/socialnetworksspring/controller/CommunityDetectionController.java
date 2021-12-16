package hu.listopad.socialnetworksspring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@RestController
public class CommunityDetectionController {

    @Value("${sqsUrl}")
    private String queueUrl;

    @Value("${localstack.endpoint}")
    private String endpointUrl;


    @GetMapping("/graphs")
    public String index(){
        return "tadááámmmm";
    }


    @GetMapping("/${graphName}")
    public String getOneResult(){
        return "One result";
    }

    @DeleteMapping("/${graphName}")
    public String deleteGraph(){
        return "success";
    }


    @PostMapping("/graph")
    public String newCalculation(@RequestBody String payload){

        SqsClient sqsClient = SqsClient.builder().endpointOverride(URI.create(endpointUrl)).build();
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payload)
                .build());
        return "Working on it";
    }

    
}
