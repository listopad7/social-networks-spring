package hu.listopad.socialnetworks.spring.worker.messagelistener;

import hu.listopad.socialnetworks.spring.data.JsonToWeightedGraphConverter;
import hu.listopad.socialnetworks.spring.data.mapstruct.mappers.MapStructMapperDynamo;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphMessageListenerConfig {

    @Bean
    public JsonToWeightedGraphConverter jsonToWeightedGraphConverter(){
        return new JsonToWeightedGraphConverter();
    }

    @Bean
    public MapStructMapperDynamo getMapStructMapperDynamo(){
        return Mappers.getMapper(MapStructMapperDynamo.class);
    }




}
