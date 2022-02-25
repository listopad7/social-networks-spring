package hu.listopad.socialnetworks.spring.web.responseData;

import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CommunityDetectionToResponseTest {

    private CommunityDetectionToResponseMapper communityDetectionToResponseMapper = new CommunityDetectionToResponseMapper();


    public void setUpCommunityResponseDone(CommunityDetectionResult communityDetectionResult){

        communityDetectionResult.setUserId("elso");
        communityDetectionResult.setGraphName("elsograf");
        communityDetectionResult.setStatus(Status.DONE);

        Map<Integer, Map<Integer, Integer>> originalGraphMap= new HashMap<>();
        Map<Integer, Integer> elso = new HashMap<>();
        for (int i = 2; i <7 ; i++){
            elso.put(i, 1);
        }
        originalGraphMap.put(1,elso);
        for (int i=2; i<7; i++){
            Map<Integer, Integer> tobbi = new HashMap<>();
            tobbi.put(1,1);
            originalGraphMap.put(i, tobbi);
        }

        communityDetectionResult.setOriginalGraph(originalGraphMap);
        communityDetectionResult.setOriginalModularity(-0.30000000000000004);
        List<Double> modularityList = new ArrayList<>();
        modularityList.add(-1.734723475976807E-17);
        modularityList.add(0.0);

        List<Map<Integer, Map<Integer, Integer>>> graphList = new ArrayList<>();
        List<Map<Integer, List<Integer>>> communityList = new ArrayList<>();

        graphList.add(originalGraphMap);

        Map<Integer, Map<Integer, Integer>> resultGraph= new HashMap<>();

        resultGraph.put(2, new HashMap<>());
        resultGraph.get(2).put(2,10);
        graphList.add(resultGraph);
        Map<Integer, List<Integer>> firstCommunityMap = new HashMap<>();
        firstCommunityMap.put(2, List.of(1,2,3,4,5,6));
        communityList.add(firstCommunityMap);
        Map<Integer, List<Integer>> secondCommunityMap = new HashMap<>();
        secondCommunityMap.put(2, List.of(2));
        communityList.add(secondCommunityMap);

        communityDetectionResult.setModularityList(modularityList);
        communityDetectionResult.setGraphList(graphList);
        communityDetectionResult.setCommunityList(communityList);

    }

    public void setUpCommunityDetectionResultWhenError(CommunityDetectionResult communityDetectionResult){

        communityDetectionResult.setUserId("elso");
        communityDetectionResult.setGraphName("elsograf");
        communityDetectionResult.setStatus(Status.ERROR);
        communityDetectionResult.setErrorMessage("This is an error");
    }

    public void setUpCommunityDetectionResultWhenInProgress(CommunityDetectionResult communityDetectionResult){

        communityDetectionResult.setUserId("elso");
        communityDetectionResult.setGraphName("elsograf");
        communityDetectionResult.setStatus(Status.IN_PROGRESS);

    }

    @Test
    public void testMappingWhenDone(){

        CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
        this.setUpCommunityResponseDone(communityDetectionResult);

        CommunityDetectionResponse communityDetectionResponse =
                communityDetectionToResponseMapper.mapToResponse(communityDetectionResult);

        Assertions.assertEquals("elso", communityDetectionResponse.getUserId());
        Assertions.assertEquals("elsograf", communityDetectionResponse.getGraphName());
        Assertions.assertEquals(Status.DONE, communityDetectionResponse.getStatus());
        Assertions.assertEquals(-0.30000000000000004, communityDetectionResponse.getOriginalGraph().getModularity());
        Assertions.assertEquals(0.0, communityDetectionResponse.getResultList().get(1).getModularity());
        Assertions.assertNull(communityDetectionResponse.getErrorMessage());
        assertThat(communityDetectionResponse.getOriginalGraph().getNodeList()).hasSize(6).extracting(Node::getId).contains("1", "2");

    }

    @Test
    public void testMappingWhenError(){

        CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
        this.setUpCommunityDetectionResultWhenError(communityDetectionResult);

        CommunityDetectionResponse communityDetectionResponse =
                communityDetectionToResponseMapper.mapToResponse(communityDetectionResult);

        Assertions.assertEquals("elso", communityDetectionResponse.getUserId());
        Assertions.assertEquals("elsograf", communityDetectionResponse.getGraphName());
        Assertions.assertEquals(Status.ERROR, communityDetectionResponse.getStatus());
        Assertions.assertEquals("This is an error", communityDetectionResponse.getErrorMessage());
        Assertions.assertNull(communityDetectionResponse.getResultList());
    }

    @Test
    public void testMappingWhenInProgress(){

        CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
        this.setUpCommunityDetectionResultWhenInProgress(communityDetectionResult);

        CommunityDetectionResponse communityDetectionResponse =
                communityDetectionToResponseMapper.mapToResponse(communityDetectionResult);

        Assertions.assertEquals("elso", communityDetectionResponse.getUserId());
        Assertions.assertEquals("elsograf", communityDetectionResponse.getGraphName());
        Assertions.assertEquals(Status.IN_PROGRESS, communityDetectionResponse.getStatus());
        Assertions.assertNull(communityDetectionResponse.getErrorMessage());
        Assertions.assertNull(communityDetectionResponse.getResultList());

    }
}
