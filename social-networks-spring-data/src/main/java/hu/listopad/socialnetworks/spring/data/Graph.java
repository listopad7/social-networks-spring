package hu.listopad.socialnetworks.spring.data;




public interface Graph {
    /* Creates a vertex with the given number. */
    public boolean addVertex(int num);
    
    /* Creates an edge from the first vertex to the second. */
    public boolean addEdge(int from, int to);


} 
