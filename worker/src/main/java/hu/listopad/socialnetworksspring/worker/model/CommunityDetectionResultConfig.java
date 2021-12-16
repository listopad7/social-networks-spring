package hu.listopad.socialnetworksspring.worker.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Supplier;

@Configuration
public class CommunityDetectionResultConfig {

    @Bean
    public Supplier<CommunityDetectionResult> communityDetectionResultFactory(){
        return () ->new CommunityDetectionResult();
    }



}
