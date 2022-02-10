package hu.listopad.socialnetworks.spring.worker.service;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 07. 28.
 */
public class LouvainPruneService implements CommunityDetectionService{


	WeightedGraph g;

	@Override
	public List<CommunityDetectionOnePassResult> getCommunityDetectionResults() {
		return null;
	}
}
