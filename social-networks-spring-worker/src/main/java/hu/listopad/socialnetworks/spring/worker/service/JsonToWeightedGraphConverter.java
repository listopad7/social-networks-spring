package hu.listopad.socialnetworks.spring.worker.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonToWeightedGraphConverter {

    private Configuration conf = Configuration.defaultConfiguration();
    private Configuration conf2 = conf.addOptions(Option.SUPPRESS_EXCEPTIONS);

    public WeightedGraph convertToWeightedGraph(String json) throws GraphException{
        List<String> nodeList = JsonPath.using(conf2).parse(json).read("$.graph.nodes[*].id");
        List<String> fromList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].from");
        List<String> toList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].to");
        if (fromList.size() != toList.size()){
            throw new GraphException("Invalid edge");
        }
        List<String> weightList = JsonPath.using(conf2).parse(json).read("$.graph.edges[*].weight");

        WeightedGraph g = new WeightedGraph();

        nodeList.stream().forEach(s ->g.addVertex(Integer.valueOf(s)));

        if (weightList.size() == fromList.size()) {

            for (int i = 0; i < fromList.size(); i++) {
                if(!g.addEdge(Integer.valueOf(fromList.get(i)), Integer.valueOf(toList.get(i)), Integer.valueOf(weightList.get(i)))){
                    throw new GraphException("Invalid edge");
                }
            }
        }else{
            for (int i = 0; i < fromList.size(); i++) {
                if(!g.addEdge(Integer.valueOf(fromList.get(i)), Integer.valueOf(toList.get(i)), 1)){
                    throw new GraphException("Invalid edge");
                }
            }
        }

        return g;
    }
}
