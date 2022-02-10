package hu.listopad.socialnetworks.spring.worker.dynamodb;

import hu.listopad.socialnetworks.spring.dynamo.CommunityDetectionResult;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Supplier;


@Configuration
public class CommunityDetectionResultConfig {

    @Bean
    @Scope(value = "prototype")
    public CommunityDetectionResult getCommunityDetectionResult(){
        return new CommunityDetectionResult();
    }

    @Bean
    public Supplier<CommunityDetectionResult> communityDetectionResultFactory(){
        return this::getCommunityDetectionResult;
    }

}
