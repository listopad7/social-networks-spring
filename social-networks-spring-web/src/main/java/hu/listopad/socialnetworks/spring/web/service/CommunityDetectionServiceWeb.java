package hu.listopad.socialnetworks.spring.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionResponse;

public interface CommunityDetectionServiceWeb {

    Iterable<SlimCommunityDetectionResult> getGraphs(String userId);

    CommunityDetectionResult getOneResponse(String userId, String graphName);

    void deleteGraph(String userId, String graphName);

    String newCalculation(String userId, String graphName, String payload) throws JsonProcessingException;

    String getSample();


}
