package hu.listopad.socialnetworks.spring.web.mapstruct.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hu.listopad.socialnetworks.spring.dynamo.Status;
import hu.listopad.socialnetworks.spring.web.controller.MySerializer;
import lombok.*;

import java.util.List;

@Data
public class CommunityDetectionResultDTO {

    private String userId;
    private String graphName;
    private Status status;
    @JsonSerialize(using = MySerializer.class)
    private String originalGraph;
    @JsonSerialize(using = MySerializer.class)
    private String resultList;


}
