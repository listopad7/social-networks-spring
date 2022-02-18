package hu.listopad.socialnetworks.spring.worker.messagelistener;

public interface GraphMessageListener {

    void processMessage(String payload);
}
