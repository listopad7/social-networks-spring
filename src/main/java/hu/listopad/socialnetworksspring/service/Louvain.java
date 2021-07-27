package hu.listopad.socialnetworksspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


//class to perform Louvain calculation
public class  Louvain {
	
	final protected WeightedGraph g;  // store on which graph the calculation is done
	private Map<Integer,Integer> degrees; // stores the degree of each vertex in the graph
	protected ArrayList<Group> groups; // stores sub-communities
	private final Double MODULARITY_AT_START; //modularity of the groups before Louvain optimization
	private Map<Integer, Group> vertexGroupMap; //store for each vertex which group it belongs to, to make calculations faster
	
	public Louvain(WeightedGraph graph) {
		g = graph;
		degrees = calculateDegrees();
		groups = createGroups();
		MODULARITY_AT_START =modularityAtStart();
		vertexGroupMap = new HashMap<>();
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
			Double mm = (m-(Double.valueOf(degrees.get(i))*Double.valueOf(degrees.get(i)))/(tw))/(tw);
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
		Double mod = 0.0;
		int tw = g.getTotalWeight();
		for (Group gr : groups) {     // for each group in the graph
			for (int i : gr.getNodes()) {    // find edge weights inside the group and calculate modularity for them
				for (int j: gr.getNodes()) {
					int m = g.getWgMap().get(i).getOrDefault(j, 0);
					Double mm = (m-(Double.valueOf(degrees.get(i))*Double.valueOf(degrees.get(j)))/(tw))/(tw);
					mod =mod + mm;
				}
			}
		}
		return mod;
	}
	
	// performs one pass of Louvain calculation
	private WeightedGraph louvainOptimization() {
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
	
	// performsLouvain modularity optimization until no gain in modularity can be achieved
	// in the resulting array list data from all passes are gathered
	// every pass requires a new Louvain object
	public List<LouvainResult> louvainModularity() {
		List<LouvainResult> result = new ArrayList<>();
		Double mod1 = MODULARITY_AT_START;	
		WeightedGraph nwg = louvainOptimization();  // first pass of Louvain calculation
		Double mod2 = modularity();
		int numOfRounds = 0;
		while (mod2 > mod1) {
			Louvain nextRound = new Louvain(nwg);
			mod1 = nextRound.MODULARITY_AT_START;
			if (numOfRounds == 0) {       // add results of first pass to the results
				ArrayList<HashSet<Integer>> communities = new ArrayList<>();
				for (int i =0; i < groups.size(); i++) {
					HashSet<Integer> nodes = groups.get(i).getNodes();
					communities.add(nodes);
				}
				LouvainResult res = new LouvainResult(g, communities, mod1);
				result.add(numOfRounds, res);
			}
			WeightedGraph nnwg = nextRound.louvainOptimization();  // next passes of Louvain calculation
			mod2 = nextRound.modularity();
			numOfRounds += 1;
			if (numOfRounds > 0) {		// add results of all other passes to the results
				ArrayList<HashSet<Integer>> communities = new ArrayList<>();
				for (int i=0 ; i < nextRound.groups.size(); i++) {
					HashSet<Integer> nodes = nextRound.groups.get(i).getNodes();
					communities.add(nodes);
				}
				LouvainResult res = new LouvainResult(nwg, communities, mod2);
				result.add(numOfRounds, res);
			}
			nwg = nnwg; // the result of this pass will be the basis for new calculation
		}
		return result;		
	}
	
	
	// helper method to create new graph from communities created in phase 1 of Louvain calculations
	protected WeightedGraph createNewGraphFromGroups() {

		//groups.removeIf(gr ->gr.getNodes().isEmpty());    // some groups remain empty as a result of calculations, we get rid of them here
		WeightedGraph nwg = new WeightedGraph();
		for (Group gr : groups) {             // for each group not empty , add a new vertex
			if (!gr.getNodes().isEmpty()) {
				nwg.addVertex(groups.indexOf(gr));
			}
		}
		for (Group gr : groups) {
			if (!gr.getNodes().isEmpty()) {        //calculate edge weights between new vertices (and self loops also)
				HashSet<Integer> members = gr.getNodes();
				HashMap<Integer, Integer> edges = new HashMap<>();   //edge weight values are stored in a HashMap. Keys are groups ( or vertices in the new graph)
				for (Integer i : members) {                         // calculate edge weights for all neighbouring groups
					for (Integer j : g.getWgMap().get(i).keySet()) {
						Group jGroup = vertexGroupMap.get(j);             // can do this because group ids are equal to the groups' index in groups arraylist
						edges.merge(jGroup.getId(), g.getWgMap().get(i).get(j), (m, n) -> m+n);
					}
				}

				for (Integer i : edges.keySet()) {
					nwg.addEdge(groups.indexOf(gr), i, edges.get(i)); // add edges to the graphs with the calculated edge weights
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
		Group toGroup = null;
		Double maxModChange = -1.0;
		Group gr = vertexGroupMap.get(i);   // find group of i, and remove it from there
		neighbourGroups.add(gr);
		gr.removeNode(i);
		for (int j : g.getWgMap().get(i).keySet()){  // find neighbours of i, and their communities, and put them to neighbour groups
			neighbourGroups.add(vertexGroupMap.get(j));
		}

		for (Group ngr : neighbourGroups) {
			Set<Integer> neighbourNodes = new HashSet(ngr.getNodes());
			neighbourNodes.retainAll(g.getWgMap().get(i).keySet());    //retain in the set only those nodes, which are neighbours of i.
			Double modChange = 0.0;
			int numToEdges = 0;
			for (int k : neighbourNodes) {
				numToEdges += g.getWgMap().get(i).get(k);      // count the edge wights from i to group k
			}
				Double degree = Double.valueOf(degrees.get(i));
				Double numInEdges = (double) ngr.getNumInEdges();
				Double totalWeight = (double) g.getTotalWeight();
				Double numAllEdges = (double) ngr.getNumAllEdges();
				modChange = (numInEdges+numToEdges)/(2*totalWeight)-((numAllEdges+degree)/(2*totalWeight))*((numAllEdges+degree)/(2*totalWeight))-(numInEdges/(2*totalWeight)-(numAllEdges/(2*totalWeight))*(numAllEdges/(2*totalWeight))-(degree/(2*totalWeight))*(degree/(2*totalWeight)));
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
	
	// create CapGraphs from communities obtained
	// so on these graphs, dominating set search can be performed
	// goal is to find out which nodes from the original graph belong to communities found in the last pass
	// for this we have to iterate over results get at all passes
	public ArrayList<UnweightedGraph> createCapGraphsFromCommunities(List<LouvainResult> res){
		ArrayList<UnweightedGraph> cgList = new ArrayList<>();  //new arrayList to store resulting graphs
		int size = res.size();
		ArrayList<HashSet<Integer>> comm = res.get(res.size()-1).getCommunities();
		for (int i=0; i<comm.size(); i++) {    // iterate over communities found 
			HashSet<Integer> firstNodes = comm.get(i);
			int counter = size-1;   // keep track of number of Louvain passes
			while (counter > 0) {   // iterate over results found at different passes
				ArrayList<HashSet<Integer>> comm2 = res.get(counter-1).getCommunities();
				ArrayList<HashSet<Integer>> comm3 = new ArrayList<HashSet<Integer>>();
				HashSet<Integer> nextNodes = new HashSet<Integer>();
				for (int j : firstNodes) {
					comm3.add(comm2.get(j));
				}
				for (int k=0; k<comm3.size(); k++) {
					for (int l : comm3.get(k)) {
						nextNodes.add(l);
					}
				}
				firstNodes = nextNodes;
				counter -=1;
				
			}
			UnweightedGraph cg = new UnweightedGraph();    // add a node in the new graph for each element in the set
			for (Integer m : firstNodes) {
				cg.addVertex(m);
			}
			for (Integer m : firstNodes) {   // edges are added based on edges of the original graph
				HashSet<Pair<Integer,Integer>> neighbors = g.getWgMap().get(m);
				for (Pair<Integer,Integer> p : neighbors) {
					cg.addEdge(m,p.getKey());
				}
			}
			cgList.add(cg);
		}
		return cgList;
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
