package hu.listopad.socialnetworks.spring.dynamo;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * Created by Noemi Czuczy on 2021. 08. 06.
 */
@DynamoDbBean
@Component
public class CommunityDetectionResult {

    private String userId;
    private String graphName;
    private Status status;
    private String originalGraph;
    private String resultList;


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

    @DynamoDbAttribute("OriginalGraph")
    public String getOriginalGraph() {
        return originalGraph;
    }

    public void setOriginalGraph(String originalGraph) {
        this.originalGraph = originalGraph;
    }


    @DynamoDbAttribute("Results")
    public String getResultList() {
        return resultList;
    }

    public void setResultList(String resultList) {
        this.resultList = resultList;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityDetectionResult that = (CommunityDetectionResult) o;
        return userId.equals(that.userId) && graphName.equals(that.graphName) && status == that.status && Objects.equals(resultList, that.resultList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, graphName, status, resultList);
    }
}


