package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


// class to store results of a Louvain pass
public class  LouvainResult {
	
	private Double modularityAtStart;
	private Double modularityAtEnd;
	private Map<Integer,HashSet<Integer>> communities;
	private WeightedGraph g;
	
	
	public LouvainResult(WeightedGraph graph, Map<Integer, HashSet<Integer>> comm, Double mod1, Double mod2) {
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

	public Map<Integer, HashSet<Integer>> getCommunities(){
		return communities;
	}
	
	public WeightedGraph getGraph() {
		return g;
	}

	@Override
	public String toString() {
		return "LouvainResult{" +
				"modularityAtStart=" + modularityAtStart +
				", modularityAtEnd=" + modularityAtEnd +
				", communities=" + communities +
				'}';
	}
}



