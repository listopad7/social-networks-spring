package hu.listopad.socialnetworks.spring.data;

import java.util.*;

public class  WeightedGraph implements Graph {

	private final Map<Integer, Map<Integer, Integer>> wgMap;
	private int numVertices;
	private int numEdges;
	private int totalWeight;

	public WeightedGraph() {
		wgMap = new HashMap<Integer, Map<Integer, Integer>>();
		numVertices = 0;
		numEdges = 0;
		totalWeight = 0;
	}

	public boolean addVertex(int num) {
		if (!wgMap.containsKey(num)) {
			HashMap<Integer, Integer> neighbors = new HashMap<>();
			wgMap.put(num, neighbors);
			numVertices += 1;
			return true;
		} else
			return false;
	}

	// create a real weighted graph
	public boolean addEdge(int from, int to, int weight) {

		if (wgMap.containsKey(from) && (wgMap.containsKey(to))) {
			(wgMap.get(from)).put(to, weight);
			numEdges += 1;
			//if (!wgMap.get(to).keySet().contains(from)) {
			totalWeight = totalWeight + weight;
			//}
			return true;
		} else return false;
	}

	// creates an edge with weight 1
	public boolean addEdge(int from, int to) {

		if (wgMap.containsKey(from) && (wgMap.containsKey(to))) {
			(wgMap.get(from)).put(to, 1);
			numEdges += 1;
			//if (!wgMap.get(to).keySet().contains(from)) {
			totalWeight += 1;
			//}
			return true;
		} else return false;
	}


	public Map<Integer, Map<Integer, Integer>> getWgMap() {
		return wgMap;
	}

	public int getTotalWeight() {
		return totalWeight;
	}



}