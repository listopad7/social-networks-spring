package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.worker.messagelistener.GraphMessageListenerSqs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageListenerTest {

    @Autowired
    GraphMessageListenerSqs graphMessageListener;

    @Test
    public void TestReceiveMessage(){

    }
}
