package hu.listopad.socialnetworksspring.messageing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${sqsUrl}")
    private String queueUrl;

    @Value("${localstack.endpoint}")
    private String endpointUrl;

    @Bean
    public SqsClient getMessageQueue(){
        return SqsClient.builder().endpointOverride(URI.create(endpointUrl)).build();
    }

}
