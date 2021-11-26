package hu.listopad.socialnetworksspring.model;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * Created by Noemi Czuczy on 2021. 08. 06.
 * DAO
 */
@DynamoDbBean
public class CommunityDetectionResult {

	private String userId;
	private String graphName;
	private Status status;
	private List<String> resultList;
	

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

	@DynamoDbAttribute("status")
	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}
	

	@DynamoDbAttribute("Results")
	public List<String> getResultList() {
		return resultList;
	}

	public void setResultList(List<String> resultList) {
		this.resultList = resultList;
	}
	
	
	

}


