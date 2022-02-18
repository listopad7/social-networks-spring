package hu.listopad.socialnetworks.spring.web.messageing;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SendGraphDataMessage {

    void sendMessage(String userId, String graphName, String graph) throws JsonProcessingException;
}
