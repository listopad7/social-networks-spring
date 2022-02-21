package hu.listopad.socialnetworks.spring.worker.messagelistener;

import com.jashmore.sqs.spring.decorator.visibilityextender.AutoVisibilityExtender;
import com.jashmore.sqs.argument.payload.Payload;
import com.jashmore.sqs.spring.container.basic.QueueListener;
import hu.listopad.socialnetworks.spring.worker.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class GraphMessageListenerSqs implements GraphMessageListener{

       private final OrchestratorService orchestratorService;

       @Autowired
        public GraphMessageListenerSqs(OrchestratorService orchestratorService) {
                this.orchestratorService = orchestratorService;
        }


        @QueueListener(value = "${sqsUrl}", concurrencyLevel = 2)
        //@AutoVisibilityExtender(visibilityTimeoutInSeconds = 60, maximumDurationInSeconds = 300, bufferTimeInSeconds = 30)
        public void processMessage(@Payload final String payload){

                orchestratorService.detectAndSave(payload);
                log.info("Payload: {}", payload);
        }

}
