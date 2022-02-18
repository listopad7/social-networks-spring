package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Noemi Czuczy on 2021. 07. 29.
 */

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
		assertEquals(true, weightedGraph.getWgMap().containsKey(3));
		assertEquals(3, weightedGraph.getWgMap().keySet().size());
	}

	@Test
	void addEdge() {

		weightedGraph.addEdge(2, 1);
		assertEquals(1, weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,4);
		assertEquals(null, weightedGraph.getWgMap().get(2).get(4));
	}

	@Test
	void testAddEdge() {

		weightedGraph.addEdge(2,1,5);
		assertEquals(5,  weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,1,7);
		assertEquals(7,  weightedGraph.getWgMap().get(2).get(1));
		weightedGraph.addEdge(2,4);
		assertEquals(null, weightedGraph.getWgMap().get(2).get(4));
	}
}