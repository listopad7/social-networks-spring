package hu.listopad.socialnetworks.spring.web.controller;

import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunityDetectionConfig {

    @Bean
    public CommunityDetectionResult communityDetectionResult(){

        return new CommunityDetectionResult();
    }
}
