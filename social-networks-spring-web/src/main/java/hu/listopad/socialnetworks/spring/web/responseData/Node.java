package hu.listopad.socialnetworks.spring.web.responseData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Node {

    @NonNull private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String group;



}
