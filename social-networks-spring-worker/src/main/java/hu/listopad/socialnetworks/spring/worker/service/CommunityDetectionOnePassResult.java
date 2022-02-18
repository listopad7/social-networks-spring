package hu.listopad.socialnetworks.spring.worker.service;


import java.util.List;
import java.util.Map;

import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;


// class to store results of a Louvain pass
@DynamoDbBean
@Setter
public class CommunityDetectionOnePassResult {

	@JsonIgnore
	private Double modularityAtStart;
	private Double modularityAtEnd;
	private Map<Integer, List<Integer>> communities;
	private Map<Integer, Map<Integer, Integer>> g;

	public CommunityDetectionOnePassResult(){}
	
	
	public CommunityDetectionOnePassResult(Map<Integer, Map<Integer, Integer>> graph, Map<Integer, List<Integer>> comm, Double mod1, Double mod2) {
		g = graph;
		communities = comm;
		modularityAtStart = mod1;
		modularityAtEnd = mod2;
	}
	@DynamoDbAttribute("ModularityAtStart")
	public Double getModularityAtStart() {
		return modularityAtStart;
	}

	@DynamoDbAttribute("Modularity")
	public Double getModularityAtEnd() {
		return modularityAtEnd;
	}

	@DynamoDbAttribute("Communities")
	public Map<Integer, List<Integer>> getCommunities(){
		return communities;
	}


	public Map<Integer, Map<Integer, Integer>> getGraph() {
		return g;
	}

	@Override
	public String toString() {
		return "LouvainResult{" +
				"modularityAtStart=" + modularityAtStart +
				", modularityAtEnd=" + modularityAtEnd +
				", communities=" + communities +
				'}';
	}
}



