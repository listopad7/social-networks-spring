package hu.listopad.socialnetworks.spring.data.dynamo;

import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Map;

@DynamoDbBean
@Component
@EqualsAndHashCode
public class CommunityDetectionResultDynamo {

    private String userId;
    private String graphName;
    private Status status;
    private String errorMessage;
    private Map<Integer, Map<Integer, Integer>> originalGraph;
    private Double originalModularity;
    private List<Map<Integer, Map<Integer, Integer>>> graphList;
    private  List<Map<Integer, List<Integer>>> communityList;
    private List<Double> modularityList;


    @DynamoDbPartitionKey()
    @DynamoDbAttribute("UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbSortKey()
    @DynamoDbAttribute("GraphName")
    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    @DynamoDbAttribute("Status")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @DynamoDbAttribute("ErrorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @DynamoDbAttribute("OriginalGraph")
    public Map<Integer, Map<Integer, Integer>> getOriginalGraph() {
        return originalGraph;
    }

    public void setOriginalGraph(Map<Integer, Map<Integer, Integer>> originalGraph) {
        this.originalGraph = originalGraph;
    }

    @DynamoDbAttribute("OriginalModularity")
    public Double getOriginalModularity() {
        return originalModularity;
    }

    public void setOriginalModularity(Double originalModularity) {
        this.originalModularity = originalModularity;
    }

    @DynamoDbAttribute("GraphList")
    public List<Map<Integer, Map<Integer, Integer>>> getGraphList() {
        return graphList;
    }

    public void setGraphList(List<Map<Integer, Map<Integer, Integer>>> graphList) {
        this.graphList = graphList;
    }

    @DynamoDbAttribute("CommunityList")
    public List<Map<Integer, List<Integer>>> getCommunityList() {
        return communityList;
    }

    public void setCommunityList( List<Map<Integer, List<Integer>>> communityList) {
        this.communityList = communityList;
    }

    @DynamoDbAttribute("ModularityList")
    public List<Double> getModularityList() {
        return modularityList;
    }

    public void setModularityList(List<Double> modularityList) {
        this.modularityList = modularityList;
    }



}
