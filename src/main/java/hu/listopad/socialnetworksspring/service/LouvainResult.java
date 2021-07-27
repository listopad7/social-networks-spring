package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.HashSet;


// class to store results of a Louvain pass
public class  LouvainResult {
	
	private Double modularityAtStart;
	private Double modularityAtEnd;
	private ArrayList<HashSet<Integer>> communities;
	private WeightedGraph g;
	
	
	public LouvainResult(WeightedGraph graph, ArrayList<HashSet<Integer>> comm, Double mod1, Double mod2) {
		g = graph;
		communities = comm;
		modularityAtStart = mod1;
		modularityAtEnd = mod2;
	}

	public Double getModularityAtStart() {
		return modularityAtStart;
	}

	public Double getModularityAtEnd() {
		return modularityAtEnd;
	}

	public ArrayList<HashSet<Integer>> getCommunities(){
		return communities;
	}
	
	public WeightedGraph getGraph() {
		return g;
	}
}



