package hu.listopad.socialnetworks.spring.web.persistence;

import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public MapStructMapperDynamo mapStructMapperDynamo(){
        return Mappers.getMapper(MapStructMapperDynamo.class);
    }

    @Bean
    public SlimCommunityDetectionResult slimCommunityDetectionResult(){
        return new SlimCommunityDetectionResult();
    }
}
