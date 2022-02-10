/**
 * 
 */
package hu.listopad.socialnetworks.spring.worker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Noemi Czuczy
 *
 */
public class UnweightedGraph implements Graph {

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 *
	 */
	
	private HashMap<Integer,HashSet<Integer>> graphMap;
	private int numVertices;
	private int numEdges;
	
	public UnweightedGraph() {
		graphMap = new HashMap<Integer,HashSet<Integer>>();
		numVertices = 0;
		numEdges = 0;
	}
	@Override
	public boolean addVertex(int num) {
		
		if(!graphMap.containsKey(num)) {
			HashSet<Integer> neighbors = new HashSet<Integer>();
			graphMap.put(num, neighbors);
			numVertices +=1;
			return true;
		}
		return false;
	}


	@Override
	public boolean addEdge(int from, int to) {
		
		if(graphMap.containsKey(from) && (graphMap.containsKey(to))) {
			(graphMap.get(from)).add(to);
			numEdges +=1;
			return true;
		}
		else return false;
	}


	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		
		
		return graphMap;
	}
	
	// greedy algorithm to find a dominating set
	public ArrayList<Integer> dominating_set(){
		 ArrayList<Integer> set = new ArrayList<Integer>();     // arrayList to store nodes in the dominating set
		 ArrayList<Integer> not_covered = new ArrayList<Integer>();     // store nodes that are not yet covered by the dominating set
		 for (int k : graphMap.keySet()) {    // in the beginning, add each node to not covered
			 not_covered.add(k);
		 }
		 while (!not_covered.isEmpty()) {   // perform this until no vertices remain in not_covered
			 int max = -1;   
			 Integer maxkey = 0;  // key of vertex with most uncovered vertices
			 for (int k : graphMap.keySet()) {   // loop over all vertices
				 if (!set.contains(k)) {   // do this over vertices that are not in the dominating set
					 int n = graphMap.get(k).size();   // get number of neighbors the vertice has
					 HashSet<Integer> neighbors = graphMap.get(k);
					 int x = 0;
					 for (Integer v : neighbors) {   // calculate number of already covered neighbors
						 if (!not_covered.contains(v)) {
							 x+=1;
						 }
					 }
					 n = n-x;   // n is the number of not covered neighbors
					 if (n > max) {
						 max = n;   
						 maxkey = k;   
					 }
				 }	 
			 }
			 set.add(maxkey);   // add to dominating set the vertex eith most uncovered vertices
			 if (not_covered.contains(maxkey)){  // remove this vertex from not covered, it it is still there
				 not_covered.remove(maxkey);
			 }
			 for (Integer v : graphMap.get(maxkey)) {  // remove all neighboring vertices from not covered
				 if (not_covered.contains(v)){
					 not_covered.remove(v);
				 }
			 }
			
			 
		 }
	return set;
	}
	
	// fastgreedy algorithm to find dominating set
	// this is almost the same, as the greedy one, there is a difference at only one line, so I added a comment  only there
	public ArrayList<Integer> fastgreedy_dominating_set(){
		 ArrayList<Integer> set = new ArrayList<Integer>();
		 ArrayList<Integer> not_covered = new ArrayList<Integer>();
		 for (int k : graphMap.keySet()) {
			 not_covered.add(k);
		 }
		 while (!not_covered.isEmpty()) {
			 int max = -1;
			 Integer maxkey = 0;
			 for (int k : graphMap.keySet()) {
				 if (not_covered.contains(k)) {   // we examine only vertices that are not covered yet
					 int n = graphMap.get(k).size();
					 HashSet<Integer> neighbors = graphMap.get(k);
					 int x = 0;
					 for (Integer v : neighbors) {
						 if (!not_covered.contains(v)) {
							 x+=1;
						 }
					 }
					 n = n-x;
					 if (n > max) {
						 max = n;
						 maxkey = k;
					 }
				 }	 
			 }
			 set.add(maxkey);
			 if (not_covered.contains(maxkey)){
				 not_covered.remove(maxkey);
			 }
			 for (Integer v : graphMap.get(maxkey)) {
				 if (not_covered.contains(v)){
					 not_covered.remove(v);
				 }
			 }
			
			 
		 }
	return set;		
	}
}


