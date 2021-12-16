package hu.listopad.socialnetworksspring.worker.model;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommunityDetectionResultFactoryOld {

    @Autowired
    private ObjectFactory<CommunityDetectionResult> objectFactory;

    public CommunityDetectionResult getCommunityDetectionResult(){return objectFactory.getObject();
    }
}
