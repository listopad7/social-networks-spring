package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LouvainPrune extends Louvain {

	
	LouvainPrune(WeightedGraph graph){
		
		super(graph);
		
	}
	

	// performs one pass of Louvain prune calculation
	@Override
	WeightedGraph louvainOptimization() {
		List<Integer> nodeList = new LinkedList<>(g.getWgMap().keySet());  //queue to keep nodes waiting for modularity change calculation
		while (!nodeList.isEmpty()) {
		Integer i = nodeList.remove(0);
			if (moveNode(i)) {  // call helper method of parent class to calculate modularity change for each node. If node I changes community, perform the following calculation
				int iGroupIndex = vertexGroupMap.get(i).getId();

				for (Integer neighbor : g.getWgMap().get(i).keySet()) {  //find those neighbors of i that are not in the community of i, and add them to the queue
					int neighborGroupIndex = vertexGroupMap.get(neighbor).getId();
					if(iGroupIndex != neighborGroupIndex){
						nodeList.add(neighbor);
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
