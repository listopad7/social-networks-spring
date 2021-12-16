package hu.listopad.socialnetworksspring.worker.model;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

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
}
