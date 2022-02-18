package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Noemi Czuczy on 2021. 07. 27.
 */

// performsLouvain modularity optimization until no gain in modularity can be achieved
// in the resulting array list data from all passes are gathered
// every pass requires a new Louvain object
@Service
public class LouvainService implements CommunityDetectionService{


	@Override
	public List<CommunityDetectionOnePassResult> getCommunityDetectionResults(WeightedGraph g) {
		List<CommunityDetectionOnePassResult> result = new ArrayList<>();
		WeightedGraph graphToUse = g;
		double mod1;
		double mod2;

		do {
			Louvain louvain = new Louvain(graphToUse);
			mod1 = louvain.getModularityAtStart();
			WeightedGraph nwg = louvain.louvainOptimization();
			mod2 = louvain.modularity();

			Map<Integer, List<Integer>> groupsAndNodes = louvain.
															getGroups().
															stream().
															filter(gr -> !gr.getNodes().isEmpty()).
															collect(Collectors.toMap(Group::getId, Group::getNodes));
			CommunityDetectionOnePassResult louvainResult = new CommunityDetectionOnePassResult(graphToUse.getWgMap(), groupsAndNodes, mod1, mod2);
			result.add(louvainResult);
			graphToUse = nwg;
		}while(mod2>mod1);

		return result;
	}



}
