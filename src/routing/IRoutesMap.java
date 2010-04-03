package routing;


import java.util.Collection;
import java.util.List;

/**
 * This interface defines the object storing the graph of all routes in the
 * system.
 * 
 * @author Alex Maskovyak (modified from: Renaud Waldura)
 */
public interface IRoutesMap<T> {
	
	/**
	 * Enter a new segment in the graph.
	 */
	public void addDirectRoute(T start, T end, int cost);
	
	/**
	 * Enters new segments into the graph, both from node1 to node2 and from 
	 * node2 to node1.
	 * @param node1 to connect.
	 * @param node2 to connect.
	 * @param cost associated with this connection.
	 */
	public void addBiDirectRoute(T node1, T node2, int cost);
	
	/**
	 * Removes all segments containing the specified node.
	 * @param node to remove.
	 */
	public void remove(T node);
	
	/**
	 * Removes a segment from the graph.
	 * @param start of the segment.
	 * @param end of the segment.
	 */
	public void removeDirectRoute(T start, T end);
	
	/**
	 * Removes both segments from a graph, the one from start to end, and from
	 * end to start.
	 * @param start node.
	 * @param end node.
	 */
	public void removeBiDirectRoute(T start, T end);
	
	/**
	 * Get the value of a segment.
	 * @param start node of the segment.
	 * @param end node of the segment.
	 * @return cost between the segments.
	 */
	public int getCost(T start, T end);
	
	/**
 	 * Get the list of nodes that can be reached from the given node. 
	 * @param node to use as a source/start point.
	 * @return nodes which can be reached from the specified node.
	 */
	public Collection<T> getDestinations(T node); 
	
	/** Get the list of cities that lead to the given node. */
	public List<T> getPredecessors(T node);
	
	/**
	 * @return the transposed graph of this graph, as a new RoutesMap instance.
	 */
	public IRoutesMap<T> getInverse();
}
