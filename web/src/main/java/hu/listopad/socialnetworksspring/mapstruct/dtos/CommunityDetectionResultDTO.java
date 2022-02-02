package hu.listopad.socialnetworksspring.mapstruct.dtos;

import hu.listopad.socialnetworksspring.persistence.Status;
import lombok.*;

import java.util.List;

@Data
public class CommunityDetectionResultDTO {

    private String userId;
    private String graphName;
    private Status status;
    private List<String> resultList;


}
