package network.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * This map stores routes in as a series of lists. It is most useful when
 * there are few routes, otherwise using a dense representation may be more
 * valuabe.
 * @author Alex Maskovyak
 *
 * @param <T> Comparable Node.
 */
public class SparseRoutesMap<T extends Comparable<T>>
		implements IRoutesMap<T> {
	
	/** direct connections from a given T to other nodes. */
	protected Map<T, Map<T, Integer>> _directConnections;
	
	/** Default constructor. */
	public SparseRoutesMap() {
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_directConnections = new HashMap<T, Map<T, Integer>>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#addDirectRoute(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public void addDirectRoute(T start, T end, int cost) {
		Map<T, Integer> connections = _directConnections.get(start);
		if( connections == null ) {
			connections = new HashMap<T, Integer>();
			_directConnections.put(start, connections);
		}
		connections.put(end, cost);
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#addBiDirectRoute(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public void addBiDirectRoute(T node1, T node2, int cost) {
		addDirectRoute(node1, node2, cost);
		addDirectRoute(node2, node1, cost);
	}
	
	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#remove(java.lang.Object)
	 */
	@Override
	public void remove(T node) {
		// remove links from this node
		_directConnections.remove( node );
		Collection<Map<T, Integer>> costMaps = _directConnections.values();
		for( Map<T, Integer> costMap : costMaps ) {
			costMap.remove( node );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#removeBiDirectRoute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void removeBiDirectRoute(T start, T end) {
		removeDirectRoute(start, end);
		removeDirectRoute(end, start);
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#removeDirectRoute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void removeDirectRoute(T start, T end) {
		Map<T, Integer> connections = _directConnections.get(start);
		if( connections == null ) {
			// do nothing...actually this shouldn't ever happen unless someone
			// is using this improperly
			return;
		}
		connections.remove(end);
	}
	
	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#getDestinations(java.lang.Object)
	 */
	@Override
	public Collection<T> getDestinations(T node) {
		Map<T, Integer> directConnections = _directConnections.get( node );
		return ( directConnections == null )
			? new ArrayList<T>()
			: new ArrayList<T>(directConnections.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#getCost(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getCost(T start, T end) {
		Integer result = _directConnections.get(start).get(end);
		return (result != null)
				? result
				: 0;
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#getInverse()
	 */
	@Override
	public IRoutesMap<T> getInverse() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see routing.IRoutesMap#getPredecessors(java.lang.Object)
	 */
	@Override
	public List<T> getPredecessors(T node) {
		List<T> predecessors = new ArrayList<T>();
		for( Entry<T, Map<T, Integer>> start : _directConnections.entrySet() ) {
			Map<T, Integer> destinations = start.getValue();
			if( destinations.containsKey( node ) ) {
				predecessors.add( start.getKey() );
			}
		}
		return predecessors;
	}
	
	/**
	 * Test driver.
	 * @param args N/A
	 */
	public static void main(String... args) {
		IRoutesMap map = new SparseRoutesMap();
		
	}
}
