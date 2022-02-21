package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class LouvainTest {

	WeightedGraph g;
	Louvain louvain;


	// TODO: change hardcoded value
	@BeforeEach
	void setUp(){
		try {
			g = GraphLoader.loadGraph("src/test/resources/louvain_sample.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		louvain= new Louvain(g);
	}

	@Test
	void calculateDegrees() {
		assertEquals(4, louvain.getDegrees().get(4));
		assertEquals(5, louvain.getDegrees().get(11));
	}

	@Test
	void modularityAtStart() {
		assertEquals(-0.07142857142857142, louvain.getModularityAtStart());
	}

	@Test
	void createGroups() {
		assertEquals(16, louvain.getGroups().size());
		assert louvain.getVertexGroupMap().containsKey(15);
		assert louvain.getVertexGroupMap().get(3).getNodes().contains(3);
	}

	@Test
	void modularity() {
		louvain.louvainOptimization();
		assertEquals(0.34630102040816313, louvain.modularity());
	}

	@Test
	void louvainOptimization() {
		
	}

	@Test
	void createNewGraphFromGroups() {
	}

	@Test
	void moveNode() {
	}
}