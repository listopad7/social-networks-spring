package hu.listopad.socialnetworksspring.worker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Noemi Czuczy on 2021. 07. 30.
 */
public class LouvainTest {

	WeightedGraph g = new WeightedGraph();
	Louvain louvain;

	@BeforeEach
	void setUp(){
		GraphLoader.loadGraph(g, "c:/Users/noemi/repos/social-networks-spring/data/louvain_sample.txt");
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