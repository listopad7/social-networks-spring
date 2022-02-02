package hu.listopad.socialnetworksspring.messageing;

public interface SendGraphDataMessage {

    void sendMessage(String userId, String graphName, String graph);
}
