package hu.listopad.socialnetworks.spring.data.communitydetection;

import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/**
 * Created by Noemi Czuczy on 2021. 08. 06.
 */
@EqualsAndHashCode
public class CommunityDetectionResult {

    private String userId;
    private String graphName;
    private Status status;
    private String errorMessage;
    private Map<Integer, Map<Integer, Integer>> originalGraph;
    private Double originalModularity;
    private List<Map<Integer, Map<Integer, Integer>>> graphList;
    private List<Map<Integer, List<Integer>>> communityList;
    private List<Double> modularityList;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<Integer, Map<Integer, Integer>> getOriginalGraph() {
        return originalGraph;
    }

    public void setOriginalGraph(Map<Integer, Map<Integer, Integer>> originalGraph) {
        this.originalGraph = originalGraph;
    }


    public Double getOriginalModularity() {
        return originalModularity;
    }

    public void setOriginalModularity(Double originalModularity) {
        this.originalModularity = originalModularity;
    }


    public List<Map<Integer, Map<Integer, Integer>>> getGraphList() {
        return graphList;
    }

    public void setGraphList(List<Map<Integer, Map<Integer, Integer>>> graphList) {
        this.graphList = graphList;
    }


    public List<Map<Integer, List<Integer>>> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<Map<Integer, List<Integer>>> communityList) {
        this.communityList = communityList;
    }


    public List<Double> getModularityList() {
        return modularityList;
    }

    public void setModularityList(List<Double> modularityList) {
        this.modularityList = modularityList;
    }


}