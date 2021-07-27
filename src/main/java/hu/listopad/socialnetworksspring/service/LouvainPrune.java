package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.util.Pair;

public class LouvainPrune extends Louvain {
	

	
	LouvainPrune(WeightedGraph graph){
		
		super(graph);
		
	}
	
	@Override
	// performs Louvain prune modularity optimization until no gain in modularity can be achieved
	// in the resulting array list data from all passes are gathered
		// every pass requires a new LouvainPrune object
	public ArrayList<LouvainResult> louvainModularity() {
		ArrayList<LouvainResult> result = new ArrayList<LouvainResult>();
		Double mod1 = getModularityAtStart();
		WeightedGraph nwg = louvainPruneOptimization();   // first pass of LouvainPrune calculation
		Double mod2 = modularity();
		int numOfRounds = 0;
		while (mod2 > mod1) {
			LouvainPrune nextRound = new LouvainPrune(nwg);
			mod1 = nextRound.getModularityAtStart();
			if (numOfRounds == 0) {     // add results of first pass to the results
				ArrayList<HashSet<Integer>> communities = new ArrayList<HashSet<Integer>>();
				for (int i=0 ; i < groups.size(); i++) {
					HashSet<Integer> nodes = groups.get(i).getNodes();
					communities.add(nodes);
				}
				LouvainResult res = new LouvainResult(g, communities, mod1);
				result.add(numOfRounds, res);
			}
			WeightedGraph nnwg = nextRound.louvainPruneOptimization();   // next passes of Louvain calculation
			mod2 = nextRound.modularity();
			numOfRounds += 1;	
			if (numOfRounds > 0) {    // add results of all other passes to the results
				ArrayList<HashSet<Integer>> communities = new ArrayList<HashSet<Integer>>();
				for (int i=0 ; i < nextRound.groups.size(); i++) {
					HashSet<Integer> nodes = nextRound.groups.get(i).getNodes();
					communities.add(nodes);
				}
				LouvainResult res = new LouvainResult(nwg, communities, mod2);
				result.add(numOfRounds, res);
			}
			nwg = nnwg;   // the result of this pass will be the basis for new calculation
			
		}
		return result;		
	}
	
	// performs one pass of Louvain prune calculation
	private WeightedGraph louvainPruneOptimization() {
		ArrayList<Integer> nodeList = new ArrayList<Integer>();  //queue to keep nodes waiting for modularity change calculation
		for (int i : g.getWgMap().keySet()) {
			nodeList.add(i);
		}
		while (!nodeList.isEmpty()) {
		Integer i = nodeList.remove(0);
			if (moveNode(i)) {  // call helper method of parent class to calculate modularity change for each node. If node I changes community, perform the following calculation
				int iGroupIndex = -1;
				for (Group gr: groups) {   // find group of i
					if (gr.getNodes().contains(i)) {
						iGroupIndex = groups.indexOf(gr);
					}
				}
				for (Pair<Integer,Integer> p : g.getWgMap().get(i)) {  //find those neighbors of i that are not in the community of i, and add them to the queue
					Group gr = groups.get(iGroupIndex);
					if (!gr.getNodes().contains(p.getKey())) {
						if (!nodeList.contains(p.getKey())) {
							nodeList.add(p.getKey());  // add to nodeList those neighbors of i that are in a different community than i
						}
					}
				}
			}		
		}
		WeightedGraph ng = createNewGraphFromGroups();
		return ng;
	}
	
	public ArrayList<Group> getGroups(){
		return groups;
	}

}
