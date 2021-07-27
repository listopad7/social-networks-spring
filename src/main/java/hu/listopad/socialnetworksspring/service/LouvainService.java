package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 07. 27.
 */
public class LouvainService {

	WeightedGraph g;


	public List<LouvainResult> getCommunityDetectionResults() {
		List<LouvainResult> result = new ArrayList<>();
		Louvain firstLouvain =new Louvain(g);


	}
}
