package hu.listopad.socialnetworks.spring.web.messageing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SendGraphDataMessageSqs implements SendGraphDataMessage {

    SqsClient sqsClient;

    @Value("${sqsUrl}")
    private String queueUrl;

    @Autowired
    public SendGraphDataMessageSqs(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Override
    public void sendMessage(String userId, String graphName, String graph) throws JsonProcessingException {



        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(graph);

        ObjectNode message = mapper.createObjectNode();
        message.put("userId", userId);
        message.put("graphName", graphName);
        message.set("graph", node);


        String messageJson = mapper.writeValueAsString(message);


        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageJson)
                .build());
    }
}
