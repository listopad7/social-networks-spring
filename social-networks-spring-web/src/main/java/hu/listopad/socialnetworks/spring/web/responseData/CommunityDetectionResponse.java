package hu.listopad.socialnetworks.spring.web.responseData;

import com.fasterxml.jackson.annotation.JsonInclude;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import lombok.Data;

import java.util.List;

@Data
public class CommunityDetectionResponse {

    private String userId;
    private String graphName;
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;
    private ResponseGraph originalGraph;
    private List<ResponseGraph> resultList;
}
