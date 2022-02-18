package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LouvainResultConverter {

    public static void setCalculationResults(List<CommunityDetectionOnePassResult> communityDetectionOnePassResultList, CommunityDetectionResult communityDetectionResult){

        communityDetectionResult.setOriginalModularity(communityDetectionOnePassResultList.get(0).getModularityAtStart());


        List<Map<Integer, Map<Integer, Integer>>> graphList = new ArrayList<>();

        List<Map<Integer, List<Integer>>> communityList = new ArrayList<>();

        List<Double> modularityList = new ArrayList<>();

        for (CommunityDetectionOnePassResult cdopr : communityDetectionOnePassResultList){
            graphList.add(cdopr.getGraph());
            communityList.add(cdopr.getCommunities());
            modularityList.add(cdopr.getModularityAtEnd());
        }

        communityDetectionResult.setGraphList(graphList);
        communityDetectionResult.setCommunityList(communityList);
        communityDetectionResult.setModularityList(modularityList);
    }
}
