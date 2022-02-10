package hu.listopad.socialnetworks.spring.web.messageing;

public interface SendGraphDataMessage {

    void sendMessage(String userId, String graphName, String graph);
}
