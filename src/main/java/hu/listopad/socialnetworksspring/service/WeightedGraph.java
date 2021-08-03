package hu.listopad.socialnetworksspring.service;


import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;

@Component
public class  WeightedGraph implements Graph{
	
	private HashMap<Integer, HashMap<Integer,Integer>> wgMap;
	private int numVertices;
	private int numEdges;
	private int totalWeight;
	
	public WeightedGraph() {
		wgMap = new HashMap<Integer, HashMap<Integer,Integer>>();
		numVertices = 0;
		numEdges = 0;
		totalWeight = 0;
	}
	
	public void addVertex(int num) {
		
		if(!wgMap.containsKey(num)) {
			HashMap<Integer,Integer> neighbors = new HashMap<Integer,Integer>();
			wgMap.put(num, neighbors);
			numVertices +=1;
		}
	}
	
	// create a real weighted graph
	public void addEdge(int from, int to, int weight) {
		
		if(wgMap.containsKey(from) && (wgMap.containsKey(to))) {
			(wgMap.get(from)).put(to, weight);
			numEdges +=1;
			//if (!wgMap.get(to).keySet().contains(from)) {
				totalWeight = totalWeight + weight;
			//}
		}
	}
	// creates an edge with weight 1
	public void addEdge(int from, int to) {
		
		if(wgMap.containsKey(from) && (wgMap.containsKey(to))) {
			(wgMap.get(from)).put(to, 1);
			numEdges +=1;
			//if (!wgMap.get(to).keySet().contains(from)) {
				totalWeight = totalWeight += 1;
			//}
		}
	}
	
	// creates an UnweightedGraph from the weighted graph ignoring edge weights
	private UnweightedGraph convertToUnweightedGraph() {
		UnweightedGraph ug = new UnweightedGraph();
		for (Integer a : this.wgMap.keySet()){
			ug.addVertex(a);
			HashMap<Integer,Integer> neighbor = this.wgMap.get(a);
			for (Integer to : neighbor.keySet()) {
				ug.addEdge(a, to);
			}
		}
		return ug;
	}
	

	

	
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		
		UnweightedGraph ug = this.convertToUnweightedGraph();
		return ug.exportGraph();
	}
	
	public HashMap<Integer, HashMap<Integer,Integer>> getWgMap(){
		return wgMap;
	}
	
	public int getTotalWeight() {
		return totalWeight;
	}
	
	
	public static void main (String[] args) {
		WeightedGraph smallGraph = new WeightedGraph();
		GraphLoader.loadGraph(smallGraph, "c:/Users/Daniel/repos/social-networks-spring/data/facebook_ucsd.txt");
		LouvainService smCalc = new LouvainService(smallGraph);
		for (LouvainResult ls : smCalc.getCommunityDetectionResults()){
			System.out.println(ls);
		}
		System.out.println("");


		WeightedGraph crclks = new WeightedGraph();
		for (int i = 0 ; i < 150; i++) {
			crclks.addVertex(i);
		}
		for (int i=0; i<146; i=i+5) {
			for (int j=i+1; j<i+5; j++) {
				crclks.addEdge(i, j);
				crclks.addEdge(j, i);
				for (int k=j+1; k<i+5; k++) {
					crclks.addEdge(j, k);
					crclks.addEdge(k, j);
				}
			}
			int l = i+3;
			int m = i+4;
			crclks.addEdge(l, m);
			crclks.addEdge(m, l);
			if (i<142) {
				crclks.addEdge(m, i+5);
				crclks.addEdge(i+5, m);
			}
			else {
				crclks.addEdge(m, 0);
				crclks.addEdge(0, m);
			}
		}

		LouvainService circle = new LouvainService(crclks);
		for (LouvainResult ls : circle.getCommunityDetectionResults()){
			System.out.println(ls);
		}




		/*Louvain smCalc2 = new LouvainPrune(smallGraph);
		long startTime1 = System.nanoTime();
		List<LouvainResult> res =smCalc.louvainModularity();
		long endTime1 = System.nanoTime();
		long startTime2 = System.nanoTime();
		List<LouvainResult> res2 = smCalc2.louvainModularity();
		long endTime2 = System.nanoTime();
		for (int i=0; i<res.size(); i++) {
			int j = i+1;
			System.out.println("Louvain pass: " + j);
			System.out.println("Modularity at end of pass: " + res.get(i).getModularity());
			System.out.println("Communities found at this pass: " + res.get(i).getCommunities());
			//System.out.println("New graph created at this pass: " + res.get(i).getGraph().wgMap);
		}
		for (int i=0; i<res2.size(); i++) {
			int j = i+1;
			System.out.println("Louvain prune pass: " + j);
			System.out.println("Modularity at end of pass: " + res2.get(i).getModularity());
			System.out.println("Communities found at this pass: " + res2.get(i).getCommunities());
			//System.out.println("New graph created at this pass: " + res2.get(i).getGraph().wgMap);
		}
		long durationInNano1 = (endTime1 - startTime1);
		long durationInNano2 = (endTime2 - startTime2);
		System.out.println("Louvain execution time: " + durationInNano1);
		System.out.println("Louvain prune execution time: " + durationInNano2);
		ArrayList<UnweightedGraph> communities = smCalc.createCapGraphsFromCommunities(res);
		ArrayList<Integer> domSet = communities.get(communities.size()-1).dominating_set();
		System.out.print("nodes in the last community found in Louvain community search:");
		for (int i : communities.get(communities.size()-1).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the last community: " + domSet);
		ArrayList<UnweightedGraph> communities2 = smCalc2.createCapGraphsFromCommunities(res2);
		ArrayList<Integer> domSet2 = communities2.get(communities2.size()-1).dominating_set();
		System.out.print("nodes in the last community found in Louvain prune community search:");
		for (int i : communities2.get(communities2.size()-1).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the last community: " +domSet2);*/
		
// This section uses the UCSD Facebook data
// It takes around 2 hours to run
		/*WeightedGraph largeGraph = new WeightedGraph();
		GraphLoader.loadGraph(largeGraph, "data/facebook_ucsd.txt");
		Louvain smCalc = new Louvain(largeGraph);
		Louvain smCalc2 = new LouvainPrune(largeGraph);
		long startTime1 = System.nanoTime();
		ArrayList<LouvainResult> res =smCalc.louvainModularity();
		long endTime1 = System.nanoTime();
		long startTime2 = System.nanoTime();
		ArrayList<LouvainResult> res2 = smCalc2.louvainModularity();
		long endTime2 = System.nanoTime();
		long durationInNano1 = (endTime1 - startTime1);
		long durationInNano2 = (endTime2 - startTime2);
		System.out.println("Louvain execution time: " + durationInNano1);
		System.out.println("Louvain prune execution time: " + durationInNano2);
		ArrayList<CapGraph> communities = smCalc.createCapGraphsFromCommunities(res);
		ArrayList<Integer> domSet = communities.get(communities.size()-1).dominating_set();
		System.out.print("nodes in the last community found in Louvain community search:");
		for (int i : communities.get(communities.size()-1).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the last community: " + domSet);
		ArrayList<CapGraph> communities2 = smCalc2.createCapGraphsFromCommunities(res2);
		ArrayList<Integer> domSet2 = communities2.get(communities2.size()-1).dominating_set();
		System.out.print("nodes in the last community found in Louvain prune community search:");
		for (int i : communities2.get(communities2.size()-1).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the last community: " +domSet2);
		*/
		
// This section creates a ring of 30 cliques. Each clique consists of 5 interconnected nodes (each node is connected to all of the others)
// This is one of the example graphs the original article describing Louvain algorithm uses		
		/*WeightedGraph crclks = new WeightedGraph();
		for (int i = 0 ; i < 150; i++) {
			crclks.addVertex(i);
		}
		for (int i=0; i<146; i=i+5) {
			for (int j=i+1; j<i+5; j++) {
				crclks.addEdge(i, j);
				crclks.addEdge(j, i);
				for (int k=j+1; k<i+5; k++) {
					crclks.addEdge(j, k);
					crclks.addEdge(k, j);
				}
			}
			int l = i+3;
			int m = i+4;
			crclks.addEdge(l, m);
			crclks.addEdge(m, l);
			if (i<142) {
				crclks.addEdge(m, i+5);	
				crclks.addEdge(i+5, m);
			}
			else {
				crclks.addEdge(m, 0);
				crclks.addEdge(0, m);
			}
		}
		Louvain cliks = new Louvain(crclks);
		Louvain cliks2 = new LouvainPrune(crclks);
		ArrayList<LouvainResult> res =cliks.louvainModularity();
		ArrayList<LouvainResult> res2 = cliks2.louvainModularity();
		for (int i=0; i<res.size(); i++) {
			int j = i+1;
			System.out.println("Louvain pass: " + j);
			System.out.println("Modularity at end of pass: " + res.get(i).getModularity());
			System.out.println("Communities found at this pass: " + res.get(i).getCommunities());
			System.out.println("New graph created at this pass: " + res.get(i).getGraph().wgMap);
		}
		for (int i=0; i<res2.size(); i++) {
			int j = i+1;
			System.out.println("Louvain prune pass: " + j);
			System.out.println("Modularity at end of pass: " + res2.get(i).getModularity());
			System.out.println("Communities found at this pass: " + res2.get(i).getCommunities());
			System.out.println("New graph created at this pass: " + res2.get(i).getGraph().wgMap);
		}
		ArrayList<CapGraph> communities = cliks.createCapGraphsFromCommunities(res);
		System.out.println(communities.get(0).exportGraph());
		ArrayList<Integer> domSet = communities.get(0).dominating_set();
		System.out.print("nodes in the first community found in Louvain community search:");
		for (int i : communities.get(0).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the first community: " + domSet);
		ArrayList<CapGraph> communities2 = cliks2.createCapGraphsFromCommunities(res2);
		ArrayList<Integer> domSet2 = communities2.get(0).dominating_set();
		System.out.print("nodes in the first community found in Louvain prune community search:");
		for (int i : communities2.get(0).exportGraph().keySet()) {
			System.out.print(" " + i + ",");
			}
		System.out.println(" ");
		System.out.println("Dominating set for the first community: " +domSet2);
		*/
					
	}
}
