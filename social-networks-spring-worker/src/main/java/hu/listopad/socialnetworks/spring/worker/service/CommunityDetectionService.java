package hu.listopad.socialnetworks.spring.worker.service;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 07. 27.
 */
public interface CommunityDetectionService {

	List<CommunityDetectionOnePassResult> getCommunityDetectionResults();

}
