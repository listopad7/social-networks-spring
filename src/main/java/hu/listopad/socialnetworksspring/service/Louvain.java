package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


//class to perform Louvain calculation
public class  Louvain {
	
	final protected WeightedGraph g;  // store on which graph the calculation is done
	private Map<Integer,Integer> degrees; // stores the degree of each vertex in the graph
	protected ArrayList<Group> groups; // stores sub-communities
	private final Double MODULARITY_AT_START; //modularity of the groups before Louvain optimization
	protected Map<Integer, Group> vertexGroupMap; //store for each vertex which group it belongs to, to make calculations faster
	
	public Louvain(WeightedGraph graph) {
		g = graph;
		vertexGroupMap = new HashMap<>();
		degrees = calculateDegrees();
		groups = createGroups();
		MODULARITY_AT_START =modularityAtStart();

	}
	
	// calculate degree of each vertex and put them in hash map
	protected Map<Integer,Integer> calculateDegrees() {
		/*Map<Integer,Integer> deg = new HashMap<>();
		Map<Integer, HashMap<Integer,Integer>> map = g.getWgMap();
		for (int from : map.keySet()) {
			Integer d = 0;
			for (Map.Entry<Integer, Integer> to : map.get(from).entrySet()) {
					d = d + to.getValue();
			}
			deg.put(from, d);
		}
		return deg;*/

		return g.getWgMap().entrySet().stream()
				.collect(Collectors.groupingBy(Map.Entry::getKey,
						Collectors.flatMapping(e -> e.getValue().values().stream(),
						Collectors.reducing(0, Integer::sum))));
	}
	
	// this is a quick method for calculating modularity of the original graph, when every vertex is in its own community
	protected Double modularityAtStart() {
		double mod = 0.0;
		int tw = g.getTotalWeight();
		for (int i : g.getWgMap().keySet()) {  // at this point there is only one vertex in each group, so we have to iterate over vertices
			int m = 0;
			if (g.getWgMap().get(i).containsKey(i)){  //check for self loops, as these are the only edges inside groups
				m=g.getWgMap().get(i).get(i);
			}
			double mm = (m-(Double.valueOf(degrees.get(i))*Double.valueOf(degrees.get(i)))/(tw))/(tw);
			mod = mod + mm;
			
		}
		return mod;
	}
	
	// creates a new group for each vertex and adds it to the group list
	protected ArrayList<Group> createGroups(){
		ArrayList<Group> groups = new ArrayList<>();
		for (Integer i : g.getWgMap().keySet()) {
			Group gr = new Group(g, i);
			gr.addNode(i);
			groups.add(gr);
			vertexGroupMap.put(i, gr);  // insert values to vertexGroupMap as well
		}
		return groups;
	}

	
	// before Louvain calculation this gives the same value as modularityAtStart
	// after rearranging groups it calculates the new, improved modularity
	public Double modularity() {
		double mod = 0.0;
		int tw = g.getTotalWeight();
		for (Group gr : groups) {     // for each group in the graph
			for (int i : gr.getNodes()) {    // find edge weights inside the group and calculate modularity for them
				for (int j: gr.getNodes()) {
					int m = g.getWgMap().get(i).getOrDefault(j, 0);
					double mm = (m-(Double.valueOf(degrees.get(i))*Double.valueOf(degrees.get(j)))/(tw))/(tw);
					mod =mod + mm;
				}
			}
		}
		return mod;
	}
	
	// performs one pass of Louvain calculation
	 WeightedGraph louvainOptimization() {
		int moved = 1;
		while (moved > 0) {    // while loop performs first phase of louvain calculation
			moved = 0;
			for (int i : g.getWgMap().keySet()) {		
				if (moveNode(i)) {     // call helper method to calculate modularity change for each node
					moved += 1;
				}
			}
		}
		return createNewGraphFromGroups();
	}
	

	
	
	// helper method to create new graph from communities created in phase 1 of Louvain calculations
	protected WeightedGraph createNewGraphFromGroups() {

		//groups.removeIf(gr ->gr.getNodes().isEmpty());    // some groups remain empty as a result of calculations, we get rid of them here
		WeightedGraph nwg = new WeightedGraph();
		for (Group gr : groups) {             // for each group not empty , add a new vertex
			if (!gr.getNodes().isEmpty()) {
				nwg.addVertex(gr.getId());
			}
		}
		for (Group gr : groups) {
			if (!gr.getNodes().isEmpty()) {        //calculate edge weights between new vertices (and self loops also)
				HashSet<Integer> members = gr.getNodes();
				HashMap<Integer, Integer> edges = new HashMap<>();   //edge weight values are stored in a HashMap. Keys are groups ( or vertices in the new graph)
				for (Integer i : members) {                         // calculate edge weights for all neighbouring groups
					for (Integer j : g.getWgMap().get(i).keySet()) {
						Group jGroup = vertexGroupMap.get(j);
						edges.merge(jGroup.getId(), g.getWgMap().get(i).get(j), Integer::sum);
					}
				}

				for (Integer i : edges.keySet()) {
					nwg.addEdge(gr.getId(), i, edges.get(i)); // add edges to the graphs with the calculated edge weights
				}
			}
		}
		return nwg;
	}

	
	// calculates, if removing a node from its community, and placing it to another one increases modularity or not
	// It also places the node to the best community
	// returns true if the node changes its community, and false otherwise
	protected boolean moveNode(int i) {
		Set<Group> neighbourGroups = new HashSet<>();

		double maxModChange = -1.0;
		Group gr = vertexGroupMap.get(i);
		Group toGroup = gr;// find group of i, and remove it from there
		neighbourGroups.add(gr);
		gr.removeNode(i);
		for (int j : g.getWgMap().get(i).keySet()){  // find neighbours of i, and their communities, and put them to neighbour groups
			neighbourGroups.add(vertexGroupMap.get(j));
		}

		for (Group ngr : neighbourGroups) {
			Set<Integer> neighbourNodes = new HashSet<>(ngr.getNodes());
			neighbourNodes.retainAll(g.getWgMap().get(i).keySet());    //retain in the set only those nodes, which are neighbours of i.
			int numToEdges = 0;
			for (int k : neighbourNodes) {
				numToEdges += g.getWgMap().get(i).get(k);      // count the edge wights from i to group k
			}
				Double degree = Double.valueOf(degrees.get(i));
				double numInEdges = ngr.getNumInEdges();
				double totalWeight = g.getTotalWeight();
				double numAllEdges = ngr.getNumAllEdges();
				double modChange = (numInEdges+numToEdges)/(2*totalWeight)-((numAllEdges+degree)/(2*totalWeight))*((numAllEdges+degree)/(2*totalWeight))-(numInEdges/(2*totalWeight)-(numAllEdges/(2*totalWeight))*(numAllEdges/(2*totalWeight))-(degree/(2*totalWeight))*(degree/(2*totalWeight)));
				if (modChange > maxModChange) {  //keep track of maximum modularity change
					maxModChange = modChange;
					toGroup = ngr;   // keep track of group with maximum modularity change
				}
			}
		toGroup.addNode(i);
		if (toGroup == gr) {
			return false;
		}
		else {
			vertexGroupMap.replace(i, toGroup);  //update vertexGroupMap
			return true;
		}
	}
	

	public Double getModularityAtStart() {
		return MODULARITY_AT_START;
	}
	
	public ArrayList<Group> getGroups(){
		return groups;
	}
	
	public WeightedGraph getGraph() {
		return g;
	}
	
	public Map<Integer,Integer> getDegrees(){
		return degrees;
	}
	
	
}
