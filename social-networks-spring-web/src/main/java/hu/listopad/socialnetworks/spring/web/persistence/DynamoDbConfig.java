package hu.listopad.socialnetworks.spring.web.persistence;

import java.net.URI;

import hu.listopad.socialnetworks.spring.data.dynamo.ResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String endpointUrl;
    
    
    @Bean
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public ResultRepository resultRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        return new ResultRepository(dynamoDbEnhancedClient);
    }
}
