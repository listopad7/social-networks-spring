package hu.listopad.socialnetworks.spring.worker.service;



import hu.listopad.socialnetworks.spring.data.WeightedGraph;

import java.util.*;
import java.util.stream.*;


	//this class holds information of the communities in the graph.
 	//It holds what nodes are in the graph, and information for modularity gain calculation,
	// so these values don't have to be counted again and again several times.
	// A group can be defined within a graph, so it holds which graph it belongs to.

public class Group {

	private final int id;
	private final WeightedGraph g;
	private final List<Integer> nodes;
	private int numInEdges;  //number of edges inside the group
	private int numAllEdges; //number of all edges incident to vertices in the group
	
	public Group(WeightedGraph graph, int id) {

		this.id = id;
		g = graph;
		nodes = new ArrayList<>();
		numInEdges = 0;
		numAllEdges = 0;
	}
	
	public int getNumInEdges () {
		return numInEdges;
	}
	
	public int getNumAllEdges() {
		return numAllEdges;
	}
	
	public List<Integer> getNodes() {
		return nodes;
	}

	public int getId(){
		return id;
	}
	
	
	public void addNode(int n) {
		if (!nodes.contains(n)) {
			nodes.add(n);
			Map<Integer,Integer> neighbors = g.getWgMap().get(n);
			
			Map<Boolean, Integer> numEdges = neighbors.entrySet().stream()
					.collect(Collectors.partitioningBy(e ->nodes.contains(e.getKey()),
							Collectors.reducing(0, e -> e.getValue(), (u, v) -> u+v )));
			numInEdges += numEdges.get(true)*2;
			numAllEdges += (numEdges.get(true) + numEdges.get(false));
		}
	}
	
	
	public void removeNode(int n) {
		if (nodes.contains(n)) {
			Map<Integer,Integer> neighbors = g.getWgMap().get(n);
			Map<Boolean, Integer> numEdges = neighbors.entrySet().stream()
					.collect(Collectors.partitioningBy(e ->nodes.contains(e.getKey()),
							Collectors.reducing(0, e -> e.getValue(), (u, v) -> u+v )));
			numInEdges -= numEdges.get(true)*2;
			numAllEdges -= (numEdges.get(true) + numEdges.get(false));
			nodes.remove(Integer.valueOf(n));
		}
		
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Group)) return false;
		Group group = (Group) o;
		return id == group.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
