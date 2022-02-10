package hu.listopad.socialnetworks.spring.worker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;

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
        sb.append("{\"nodes\": [ ");
        StringJoiner stringJoiner1 = new StringJoiner(",");
        vertexMap.forEach((a,b)->stringJoiner1.add("{ \"id\": \"" + a + "\", \"group\": \"" + b + "\"}"));
        sb.append(stringJoiner1);
        sb.append("], \"edges\": [");
        StringJoiner stringJoiner2 = new StringJoiner(",");
        communityDetectionOnePassResult.getGraph().getWgMap().forEach((a, b) -> {for (Integer i :b.keySet()){
                                        stringJoiner2.add("{ \"from\": \"" + a + "\", \"to\": \"" + i + "\", \"value\": \"" + b.get(i) + "\"}");
                                        }
        });
        sb.append(stringJoiner2);
        sb.append("]}");
        return sb.toString();
    }

    public String convertResultList(String payload, List<CommunityDetectionOnePassResult> communityDetectionOnePassResults){
        List<String> resultList = new ArrayList<>();

        StringBuilder finalSb = new StringBuilder();

        finalSb.append("[");
        StringJoiner stringJoiner0 = new StringJoiner(",");
        communityDetectionOnePassResults.forEach(a -> stringJoiner0.add(resultGraphToString(a)));
        finalSb.append(stringJoiner0);
        finalSb.append("]");

        communityDetectionOnePassResults.stream().map(this::resultGraphToString).forEach(resultList::add);
        return  finalSb.toString();
    }
    
}
