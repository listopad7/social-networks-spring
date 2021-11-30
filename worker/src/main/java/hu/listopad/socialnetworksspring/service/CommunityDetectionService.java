package hu.listopad.socialnetworksspring.service;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 07. 27.
 */
public interface CommunityDetectionService {

	List<LouvainResult> getCommunityDetectionResults();

}
