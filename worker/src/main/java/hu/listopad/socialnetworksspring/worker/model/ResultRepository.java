package hu.listopad.socialnetworksspring.worker.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;



@Repository
public class ResultRepository{

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;



    public void save(final CommunityDetectionResult communityDetectionResult){
        
        this.getTable().putItem(communityDetectionResult);
    }

    public CommunityDetectionResult getCommunityDetectionResultByKeys(final String userId, final String graphName){

        Key key = Key.builder().partitionValue(userId)
                            .sortValue(graphName)
                            .build();
        return this.getTable().getItem(key);
    }

    public void deleteCommunityDetectionResult(final String userId, final String graphName){

        Key key = Key.builder().partitionValue(userId)
                            .sortValue(graphName)
                            .build();

        DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest.builder()
                                                        .key(key)
                                                        .build();       
                                                        
        this.getTable().deleteItem(deleteRequest);
    }                        

    private DynamoDbTable<CommunityDetectionResult> getTable(){
        return dynamoDbEnhancedClient.table("result_table", TableSchema.fromBean(CommunityDetectionResult.class));
    }

    

}