package hu.listopad.socialnetworks.spring.worker.service;


import hu.listopad.socialnetworks.spring.data.WeightedGraph;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 07. 28.
 */
public class LouvainPruneService implements CommunityDetectionService{




	@Override
	public List<CommunityDetectionOnePassResult> getCommunityDetectionResults(WeightedGraph g) {
		return null;
	}
}
