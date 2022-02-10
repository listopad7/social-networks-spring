package hu.listopad.socialnetworks.spring.worker.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class LouvainServiceConfig {

    @Bean
    public Function<WeightedGraph, CommunityDetectionService> communityDetectionServiceFactory(){
        return this::communityDetectionService;
    }

    @Bean
    @Scope(value = "prototype")
    public CommunityDetectionService communityDetectionService(WeightedGraph weightedGraph){
      return new LouvainService(weightedGraph);
    }


}
