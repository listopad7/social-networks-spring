package hu.listopad.socialnetworks.spring.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonToWeightedGraphConverterTest {

    String json = "{\"graph\":{\"nodes\":[{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"}],\"edges\":[{\"from\":\"1\",\"to\":\"2\"},{\"from\":\"2\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"3\"},{\"from\":\"3\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"4\"},{\"from\":\"4\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"5\"},{\"from\":\"5\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"6\"},{\"from\":\"6\",\"to\":\"1\"}]}}";
    JsonToWeightedGraphConverter jsonToWeightedGraphConverter = new JsonToWeightedGraphConverter();

    @Test
    public void testJsonToWeightedGraphConversion() throws GraphException {

        WeightedGraph weightedGraph = jsonToWeightedGraphConverter.convertToWeightedGraph(json);

        for (int i=1; i<=6; i++){
            assertTrue(weightedGraph.getWgMap().keySet().contains(i));
        }

        for (int i=2; i<6; i++){
            assertTrue(weightedGraph.getWgMap().get(1).get(i). equals(1));
        }

        assertTrue(weightedGraph.getWgMap().get(4).get(1).equals(1));
    }

}
