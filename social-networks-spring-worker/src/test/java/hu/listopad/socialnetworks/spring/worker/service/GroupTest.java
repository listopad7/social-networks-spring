package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.GraphLoader;
import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Noemi Czuczy on 2021. 07. 29.
 */
public class GroupTest {

	WeightedGraph g1;
	Group gr1;
	WeightedGraph g2;
	Group gr2;

	//TODO: change hardcded values
	@BeforeEach
	void setUp(){
		g1 = new WeightedGraph();
		GraphLoader.loadGraph(g1, "c:/Users/noemi/repos/social-networks-spring/data/text-files/smallstar.txt");
		gr1 = new Group(g1, 1);
		g2 = new WeightedGraph();
		GraphLoader.loadGraph(g2, "c:/Users/noemi/repos/social-networks-spring/data/text-files/louvain_sample.txt");
		gr2 = new Group(g2, 2);


	}


	@Test
	void addNode() {
		gr1.addNode(1);
		assertEquals(1, gr1.getNodes().size());
		assertEquals(5, gr1.getNumAllEdges());
		assertEquals(0, gr1.getNumInEdges());
		gr1.addNode(2);
		assertEquals(2, gr1.getNodes().size());
		assertEquals(6, gr1.getNumAllEdges());
		assertEquals(2, gr1.getNumInEdges());
		gr2.addNode(4);
		assertEquals(1, gr2.getNodes().size());
		assertEquals(4, gr2.getNumAllEdges());
		assertEquals(0, gr2.getNumInEdges());
		gr2.addNode(2);
		assertEquals(2, gr2.getNodes().size());
		assertEquals(9, gr2.getNumAllEdges());
		assertEquals(2, gr2.getNumInEdges());

	}

	@Test
	void removeNode() {
		addNodes();
		gr1.removeNode(2);
		assertEquals(2, gr1.getNodes().size());
		assertEquals(6, gr1.getNumAllEdges());
		assertEquals(2, gr1.getNumInEdges());
		gr2.removeNode(5);
		assertEquals(2, gr2.getNodes().size());
		assertEquals(9, gr2.getNumAllEdges());
		assertEquals(2, gr2.getNumInEdges());
		gr2.removeNode(4);
		assertEquals(1, gr2.getNodes().size());
		assertEquals(5, gr2.getNumAllEdges());
		assertEquals(0, gr2.getNumInEdges());
	}

	private void addNodes(){
		gr1.addNode(1);
		gr1.addNode(2);
		gr1.addNode(3);
		gr2.addNode(2);
		gr2.addNode(4);
		gr2.addNode(5);
	}
}