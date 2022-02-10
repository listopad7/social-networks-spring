package hu.listopad.socialnetworks.spring.dynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Repository
public class ResultRepository{

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${amazon.dynamodb.tablename}")
    private String tableName;

    @Autowired
    public ResultRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }



    public void save(final CommunityDetectionResult communityDetectionResult){
        
        this.getTable().putItem(communityDetectionResult);
    }

    public CommunityDetectionResult getCommunityDetectionResultByKeys(final String userId, final String graphName){

        Key key = Key.builder().partitionValue(userId)
                            .sortValue(graphName)
                            .build();
        return this.getTable().getItem(key);
    }

    public List<CommunityDetectionResult> getCommunityDetectionResultByPartitionKey(final String userId) {

        List<CommunityDetectionResult> queryResult = new ArrayList<>();

        try {
            DynamoDbTable<CommunityDetectionResult> resultTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(CommunityDetectionResult.class));


            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(userId)
                            .build());

            // Get items in the Customer table and write out the ID value.
            Iterator<CommunityDetectionResult> results = resultTable.query(r -> r.queryConditional(queryConditional)).items().iterator();

            while (results.hasNext()) {

                CommunityDetectionResult rec = results.next();
                queryResult.add(rec);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }

        return queryResult;
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
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(CommunityDetectionResult.class));
    }

    

}