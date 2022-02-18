package hu.listopad.socialnetworks.spring.web.responseData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGraph {

    private List<Node> nodeList;
    private List<Edge> edgeList;
    private Double modularity;
}
