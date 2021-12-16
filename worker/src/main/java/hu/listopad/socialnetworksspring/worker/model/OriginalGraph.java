package hu.listopad.socialnetworksspring.worker.model;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 08. 06.
 */
public class OriginalGraph {

	private List<Integer> vertices;
	private List<OriginalEdge> edges;
	

	public class OriginalEdge{
		private int from;
		private int to;
		private int weight;
		
	}

}
