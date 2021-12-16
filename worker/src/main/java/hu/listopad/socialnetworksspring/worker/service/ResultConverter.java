package hu.listopad.socialnetworksspring.worker.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ResultConverter {

    private String resultGraphToString(CommunityDetectionOnePassResult communityDetectionOnePassResult){

        HashMap<Integer, Integer> vertexMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (Integer i : communityDetectionOnePassResult.getCommunities().keySet()){

            for (Integer j : communityDetectionOnePassResult.getCommunities().get(i)){

                vertexMap.put(j,i);
            }

        }
        sb.append("{ nodes: [ ");
        vertexMap.forEach((a,b)->sb.append("{ id: " + a + "\" group: \"" + b + "\" }"));
        sb.append("], edges: [");
        communityDetectionOnePassResult.getGraph().getWgMap().forEach((a, b) -> {for (Integer i :b.keySet()){
                                        sb.append("{ from: " + a + " to: " + i + " value: " + b.get(i) + "}");
                                        }
        });
        sb.append("] }");
        return sb.toString();
    }

    public List<String> convertResultList(String originalGraph, List<CommunityDetectionOnePassResult> communityDetectionOnePassResults){
        List<String> resultList = new ArrayList<>();
        resultList.add(originalGraph);
        communityDetectionOnePassResults.stream().map(this::resultGraphToString).forEach(resultList::add);
        return  resultList;
    }
    
}
