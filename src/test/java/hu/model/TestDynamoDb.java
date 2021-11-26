package hu.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.listopad.socialnetworksspring.SocialNetworksSpringApplication;
import hu.listopad.socialnetworksspring.model.CommunityDetectionResult;
import hu.listopad.socialnetworksspring.model.ResultRepository;
import hu.listopad.socialnetworksspring.model.Status;

@SpringBootTest(classes= SocialNetworksSpringApplication.class)
public class TestDynamoDb {

    @Autowired
    ResultRepository resultRepository;

    
    CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();

    String userId = "user1";
    String graphName = "firstGraph";
    Status status = Status.IN_PROGRESS;

    @BeforeEach
    public void cleunUp(){
        if (resultRepository.getCommunityDetectionResultByKeys(userId, graphName) != null){
            resultRepository.deleteCommunityDetectionResult(userId, graphName);
        }
    }

    @Test
    public void dbTest(){
        communityDetectionResult.setUserId(userId);
        communityDetectionResult.setGraphName(graphName);
        resultRepository.save(communityDetectionResult);
        assertEquals("user1", resultRepository.getCommunityDetectionResultByKeys(userId, graphName).getUserId());
        assertEquals("firstGraph", resultRepository.getCommunityDetectionResultByKeys(userId, graphName).getGraphName());
    }
    
}
