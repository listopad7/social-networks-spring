package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


public class GroupTest {


	WeightedGraph g1;
	Group gr1;
	WeightedGraph g2;
	Group gr2;


	@BeforeEach
	void setUp(){
		try {
			g1 = GraphLoader.loadGraph("src/test/resources/smallstar.txt");
			gr1 = new Group(g1, 1);
			g2 = GraphLoader.loadGraph("src/test/resources/louvain_sample.txt");
			gr2 = new Group(g2, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}

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