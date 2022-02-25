package hu.listopad.socialnetworks.spring.web.messageing;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendGraphDataMessageSqsTest {


    @Mock
    private SqsClient sqsClient;

    private SendGraphDataMessageSqs sendGraphDataMessageSqs;

    private final String userName= "valaki";
    private final String graphName = "megegygraf";
    private final String sqsUrl = "fakeurl";

    @BeforeEach
    public void setup(){
        this.sendGraphDataMessageSqs = new SendGraphDataMessageSqs(sqsClient);
    }


    @Test
    public void testSendValidMessage() throws JsonProcessingException {

        String message = "{\"nodes\":[{\"id\":\"1\"}],\"edges\":[{\"from\":\"1\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"1\"}]}";

        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        }).when(sqsClient).sendMessage(any(SendMessageRequest.class));

        sendGraphDataMessageSqs.sendMessage(userName, graphName, message);

        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));

    }

    @Test
    public void testSendInvalidMessage() {

        String message = "{";

        Assertions.assertThrows(JsonProcessingException.class ,() ->sendGraphDataMessageSqs.sendMessage(userName, graphName, message) );

    }
}
