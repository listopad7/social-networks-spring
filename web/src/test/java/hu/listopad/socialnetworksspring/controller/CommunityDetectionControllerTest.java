package hu.listopad.socialnetworksspring.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommunityDetectionControllerTest {

    String testString = "{name:test}";
    String postResult="Working on it";

    @Value("${sqsUrl}")
    private String queueUrl;

    @Value("${localstack.endpoint}")
    private String endpointUrl;

    @Autowired
    CommunityDetectionController communityDetectionController;


    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"{name:testName}"})
    public void postControllerTest(String payload){

        /*SqsClient sqsClient = SqsClient.builder().endpointOverride(URI.create(endpointUrl)).build();
        String testMessage = "";
        assertEquals(postResult, communityDetectionController.newCalculation(payload));
        ReceiveMessageRequest receiveMessageRequest =ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
        for (Message message : messages){
            testMessage = message.body();
        }
        assertEquals("{name:testName}", testMessage);
        try {
            for (Message message : messages) {
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            }
        }catch (SqsException e){
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }*/


    }
    @Test
    public void emptyTest(){

    }

}
