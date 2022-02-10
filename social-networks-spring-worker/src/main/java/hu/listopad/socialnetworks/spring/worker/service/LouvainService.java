package hu.listopad.socialnetworks.spring.worker.service;

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



}
