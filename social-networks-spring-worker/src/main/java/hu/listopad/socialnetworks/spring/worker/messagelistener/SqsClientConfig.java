package hu.listopad.socialnetworks.spring.worker.messagelistener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


import java.net.URI;

@Configuration
public class SqsClientConfig {

    @Value("${sqs.endpoint}")
    private String endpointUrl;

    @Bean
    public SqsAsyncClient localSqsClient(){
        return SqsAsyncClient.builder().endpointOverride(URI.create(endpointUrl)).build();
    }
}
