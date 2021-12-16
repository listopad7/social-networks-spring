package hu.listopad.socialnetworksspring.worker.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Noemi Czuczy on 2021. 07. 27.
 */

// performsLouvain modularity optimization until no gain in modularity can be achieved
// in the resulting array list data from all passes are gathered
// every pass requires a new Louvain object

public class LouvainService implements CommunityDetectionService{

	WeightedGraph g;

	public LouvainService(WeightedGraph g) {
		this.g = g;
	}

	@Override
	public List<CommunityDetectionOnePassResult> getCommunityDetectionResults() {
		List<CommunityDetectionOnePassResult> result = new ArrayList<>();
		WeightedGraph graphToUse = g;
		double mod1;
		double mod2;

		do {
			Louvain louvain = new Louvain(graphToUse);
			mod1 = louvain.getModularityAtStart();
			WeightedGraph nwg = louvain.louvainOptimization();
			mod2 = louvain.modularity();

			Map<Integer, HashSet<Integer>> groupsAndNodes = louvain.
															getGroups().
															stream().
															filter(gr -> !gr.getNodes().isEmpty()).
															collect(Collectors.toMap(Group::getId, Group::getNodes));
			CommunityDetectionOnePassResult louvainResult = new CommunityDetectionOnePassResult(graphToUse, groupsAndNodes, mod1, mod2);
			result.add(louvainResult);
			graphToUse = nwg;
		}while(mod2>mod1);

		return result;
	}


	// create CapGraphs from communities obtained
	// so on these graphs, dominating set search can be performed
	// goal is to find out which nodes from the original graph belong to communities found in the last pass
	// for this we have to iterate over results get at all passes
	/*public ArrayList<UnweightedGraph> createCapGraphsFromCommunities(List<LouvainResult> res){
		ArrayList<UnweightedGraph> cgList = new ArrayList<>();  //new arrayList to store resulting graphs
		int size = res.size();
		Map<Integer, HashSet<Integer>> comm = res.get(res.size()-2).getCommunities();
		for (int i=0; i<comm.size(); i++) {    // iterate over communities found
			HashSet<Integer> firstNodes = comm.get(i);
			int counter = size-1;   // keep track of number of Louvain passes
			while (counter > 0) {   // iterate over results found at different passes
				ArrayList<HashSet<Integer>> comm2 = res.get(counter-1).getCommunities();
				ArrayList<HashSet<Integer>> comm3 = new ArrayList<HashSet<Integer>>();
				HashSet<Integer> nextNodes = new HashSet<Integer>();
				for (int j : firstNodes) {
					comm3.add(comm2.get(j));
				}
				for (int k=0; k<comm3.size(); k++) {
					for (int l : comm3.get(k)) {
						nextNodes.add(l);
					}
				}
				firstNodes = nextNodes;
				counter -=1;

			}
			UnweightedGraph cg = new UnweightedGraph();    // add a node in the new graph for each element in the set
			for (Integer m : firstNodes) {
				cg.addVertex(m);
			}
			for (Integer m : firstNodes) {   // edges are added based on edges of the original graph
				HashSet<Pair<Integer,Integer>> neighbors = g.getWgMap().get(m);
				for (Pair<Integer,Integer> p : neighbors) {
					cg.addEdge(m,p.getKey());
				}
			}
			cgList.add(cg);
		}
		return cgList;
	}*/

}
