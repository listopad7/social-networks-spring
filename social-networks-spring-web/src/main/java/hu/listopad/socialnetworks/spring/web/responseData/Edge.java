package hu.listopad.socialnetworks.spring.web.responseData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edge {

    private String from;
    private String to;
    private String weight;

}
