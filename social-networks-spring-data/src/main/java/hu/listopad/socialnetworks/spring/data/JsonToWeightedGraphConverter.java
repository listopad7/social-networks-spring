package hu.listopad.socialnetworks.spring.data;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JsonToWeightedGraphConverter {

    private final Configuration conf = Configuration.defaultConfiguration();
    private final Configuration conf2 = conf.addOptions(Option.SUPPRESS_EXCEPTIONS);

    public WeightedGraph convertToWeightedGraph(String json) throws GraphException{
        List<String> nodeList = JsonPath.using(conf2).parse(json).read("$.graph.nodes[*].id");
        List<String> fromList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].from");
        List<String> toList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].to");
        if (fromList.size() != toList.size()){
            throw new GraphException("Invalid edges - number of from nodes doesn't match number of to nodes");
        }
        List<String> weightList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].weight");

        WeightedGraph g = new WeightedGraph();

        try {
            nodeList.stream().forEach(s -> g.addVertex(Integer.parseInt(s)));
            if (weightList.size() == fromList.size()) {

                for (int i = 0; i < fromList.size(); i++) {
                    if (!g.addEdge(Integer.parseInt(fromList.get(i)), Integer.parseInt(toList.get(i)), Integer.parseInt(weightList.get(i)))) {
                        throw new GraphException("Invalid edge - nodes in edge list don't match node list");
                    }
                }
            } else {
                for (int i = 0; i < fromList.size(); i++) {
                    if (!g.addEdge(Integer.parseInt(fromList.get(i)), Integer.parseInt(toList.get(i)), 1)) {
                        throw new GraphException("Invalid edge - nodes in edge list don't match node list");
                    }
                }
            }
        }catch (NumberFormatException e){
            throw new GraphException("Invalid node(s) - nodes should be integer numbers");
        }

        return g;
    }
}
