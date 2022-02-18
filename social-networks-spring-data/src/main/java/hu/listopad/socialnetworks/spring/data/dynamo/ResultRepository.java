package hu.listopad.socialnetworks.spring.data.dynamo;

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
import java.util.Optional;


@Repository
public class ResultRepository{

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${amazon.dynamodb.tablename}")
    private String tableName;

    @Autowired
    public ResultRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }


    public void save(final CommunityDetectionResultDynamo communityDetectionResultDynamo){
        
        this.getTable().putItem(communityDetectionResultDynamo);
    }


    public Optional<CommunityDetectionResultDynamo> findByKeys(final String userId, final String graphName){

        Key key = Key.builder().partitionValue(userId)
                            .sortValue(graphName)
                            .build();
        return Optional.ofNullable(this.getTable().getItem(key));
    }


    public Iterable<CommunityDetectionResultDynamo> findByPrimaryKey(final String userId) {

        List<CommunityDetectionResultDynamo> queryResult = new ArrayList<>();

        try {
            DynamoDbTable<CommunityDetectionResultDynamo> resultTable = this.getTable();


            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(userId)
                            .build());


            Iterator<CommunityDetectionResultDynamo> results = resultTable.query(r -> r.queryConditional(queryConditional)).items().iterator();

            while (results.hasNext()) {

                CommunityDetectionResultDynamo rec = results.next();
                queryResult.add(rec);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }

        return queryResult;
    }



    public void deleteByKeys(final String userId, final String graphName){

        Key key = Key.builder().partitionValue(userId)
                            .sortValue(graphName)
                            .build();

        DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest.builder()
                                                        .key(key)
                                                        .build();       
                                                        
        this.getTable().deleteItem(deleteRequest);
    }                        


    private DynamoDbTable<CommunityDetectionResultDynamo> getTable(){
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(CommunityDetectionResultDynamo.class));
    }

    

}