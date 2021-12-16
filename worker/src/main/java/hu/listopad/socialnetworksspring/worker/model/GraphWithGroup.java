package hu.listopad.socialnetworksspring.worker.model;

import java.util.List;

/**
 * Created by Noemi Czuczy on 2021. 08. 06.
 * Graph with results to which group nodes belong to.
 */
public class GraphWithGroup {

	private List<Vertex> vertexList;
	private List<Edge> edgeList;


	private class Vertex{

		 private int id;
		 private int group;
	}

	private class Edge{

		int from;
		int to;
		int weight;
	}


	public String toJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{ nodes: [ ");
		for (Vertex v : vertexList){
			sb.append("{ id: " + v.id + "\" group: \"" + v.group + "\" }");
		}
		sb.append("], edges: [");
		for ( Edge e: edgeList){
			sb.append("{ from: " + e.from + " to: " + e.to + " value: " + e.weight + "}");
		}
		sb.append("] }");

		return sb.toString();


	}
}
