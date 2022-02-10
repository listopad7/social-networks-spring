package hu.listopad.socialnetworks.spring.worker.dynamodb;

import hu.listopad.socialnetworks.spring.dynamo.ResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Component
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String endpointUrl;
    
    
    @Bean
    public DynamoDbClient getDynamoDbClient(){
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public ResultRepository getResultRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        return new ResultRepository(dynamoDbEnhancedClient);
    }
}
