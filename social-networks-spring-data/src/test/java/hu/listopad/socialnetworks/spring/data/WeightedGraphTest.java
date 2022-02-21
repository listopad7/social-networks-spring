package hu.listopad.socialnetworks.spring.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;




public class WeightedGraphTest {

	static WeightedGraph weightedGraph;

	@BeforeAll
	static void setUp() {
		weightedGraph = new WeightedGraph();
		weightedGraph.addVertex(1);
		weightedGraph.addVertex(2);
	}

	@Test
	void addVertex() {
		weightedGraph.addVertex(3);
		Assertions.assertEquals(true, weightedGraph.getWgMap().containsKey(3));
		Assertions.assertEquals(3, weightedGraph.getWgMap().keySet().size());
	}

	@Test
	void addEdge() {

		weightedGraph.addEdge(2, 1);
		Assertions.assertEquals(1, weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,4);
		Assertions.assertEquals(null, weightedGraph.getWgMap().get(2).get(4));
	}

	@Test
	void testAddEdge() {

		weightedGraph.addEdge(2,1,5);
		Assertions.assertEquals(5,  weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,1,7);
		Assertions.assertEquals(7,  weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,4);
		Assertions.assertEquals(null, weightedGraph.getWgMap().get(2).get(4));
	}
}