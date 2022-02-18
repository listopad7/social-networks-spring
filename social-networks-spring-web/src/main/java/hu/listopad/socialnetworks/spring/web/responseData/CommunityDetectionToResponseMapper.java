package hu.listopad.socialnetworks.spring.web.responseData;

import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommunityDetectionToResponseMapper {

    public CommunityDetectionResponse mapToResponse(CommunityDetectionResult communityDetectionResult){

        CommunityDetectionResponse communityDetectionResponse = new CommunityDetectionResponse();

        communityDetectionResponse.setUserId(communityDetectionResult.getUserId());
        communityDetectionResponse.setGraphName(communityDetectionResult.getGraphName());
        communityDetectionResponse.setStatus(communityDetectionResult.getStatus());
        communityDetectionResponse.setErrorMessage(communityDetectionResult.getErrorMessage());

        if (communityDetectionResult.getOriginalGraph() == null || communityDetectionResult.getGraphList() == null){
            return communityDetectionResponse;
        }

        // Convert original graph to json compatible graph
        ResponseGraph originalGraph = new ResponseGraph();
        final List<Node> nodeListOriginal = new ArrayList<>();

        communityDetectionResult.getOriginalGraph().keySet().forEach(key -> nodeListOriginal.add(new Node(key.toString())));

        final List<Edge> edgeListOriginal = new ArrayList<>();

        communityDetectionResult.getOriginalGraph().forEach((a,b) -> b.forEach((c,d) -> edgeListOriginal.add(new Edge(a.toString(),c.toString(),d.toString()))));
        originalGraph.setNodeList(nodeListOriginal);
        originalGraph.setEdgeList(edgeListOriginal);
        originalGraph.setModularity(communityDetectionResult.getOriginalModularity());
        communityDetectionResponse.setOriginalGraph(originalGraph);


        //Convert list of results to json compatible graphs
        List<ResponseGraph> resultList = new ArrayList<>();

        for (int i = 0; i< communityDetectionResult.getCommunityList().size(); i++){

            Map<Integer, Integer> nodeMap= new HashMap<>();
            final List<Node> resultNodeList = new ArrayList<>();
            final List<Edge> resultEdgeList = new ArrayList<>();

            communityDetectionResult.getCommunityList().get(i).forEach((a,b) -> b.forEach(c -> nodeMap.put(c,a)));
            nodeMap.forEach((a,b) -> resultNodeList.add(new Node(a.toString(), b.toString())));
            communityDetectionResult.getGraphList().get(i).forEach((a,b) -> b.forEach((c,d) -> resultEdgeList.add(new Edge(a.toString(), c.toString(), d.toString()))));
            ResponseGraph resultResponseGraph = new ResponseGraph();
            resultResponseGraph.setNodeList(resultNodeList);
            resultResponseGraph.setEdgeList(resultEdgeList);
            resultResponseGraph.setModularity(communityDetectionResult.getModularityList().get(i));
            resultList.add(resultResponseGraph);
        }

        communityDetectionResponse.setResultList(resultList);

        return communityDetectionResponse;


    }
}
