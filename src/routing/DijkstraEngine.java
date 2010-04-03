package routing;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import network.INode;
import network.Node;

/**
 * An implementation of Dijkstra's shortest path algorithm. It computes the shortest path (in distance)
 * to all cities in the map. The output of the algorithm is the shortest distance from the start T 
 * to every other T, and the shortest path from the start T to every other.
 * <p>
 * Upon calling
 * {@link #execute(T, T)}, 
 * the results of the algorithm are made available by calling
 * {@link #getPredecessor(T)}
 * and 
 * {@link #getShortestDistance(T)}.
 * 
 * To get the shortest path between the T <var>destination</var> and
 * the source T after running the algorithm, one would do:
 * <pre>
 * ArrayList&lt;T&gt; l = new ArrayList&lt;T&gt;();
 *
 * for (T T = destination; T != null; T = engine.getPredecessor(T))
 * {
 *     l.add(T);
 * }
 *
 * Collections.reverse(l);
 *
 * return l;
 * </pre>
 * 
 * @see #execute(T, T)
 * 
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @version $Id: DijkstraEngine.java 2379 2007-08-23 19:06:29Z renaud $
 */

public class DijkstraEngine<T extends Comparable<T>> {
    
	/** Infinity value for distances. */
    public static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

    /** Some value to initialize the priority queue with. */
    private static final int INITIAL_CAPAT = 8;
    
    /**
     * Comparator which orders items according to shortest link cost in 
     * ascending order. If two items have equivalent cost, the comparables 
     * themselves are compared.
     */
    private final Comparator<T> shortestDistanceComparator = 
    	new Comparator<T>() {
            public int compare(T left, T right) {
                // note that this trick doesn't work for huge distances, close to Integer.MAX_VALUE
                int result = getShortestDistance(left) - getShortestDistance(right);
                
                return (result == 0) ? left.compareTo(right) : result;
            }
        };
    
    /** graph. */
    private final IRoutesMap<T> map;
    
    /** working set of items, kept ordered by shortest distance. */
    private final PriorityQueue<T> unsettledNodes = new PriorityQueue<T>(INITIAL_CAPAT, shortestDistanceComparator);
    
    /** set of cities for which the shortest distance to the source has been 
     * found. */
    private final Set<T> settledNodes = new HashSet<T>();
    
    /** currently known shortest distance for all cities. */
    private final Map<T, Integer> shortestDistances = new HashMap<T, Integer>();

    /** predecessors list: maps a T to its predecessor in the spanning tree of
     * shortest paths. */
    private final Map<T, T> predecessors = new HashMap<T, T>();
    
    /** Default constructor. */
    public DijkstraEngine(IRoutesMap<T> map) {
        this.map = map;
    }

    /**
     * Initialize all data structures used by the algorithm.
     * 
     * @param start the source node
     */
    private void init(T start) {
        settledNodes.clear();
        unsettledNodes.clear();
        
        shortestDistances.clear();
        predecessors.clear();
        
        // add source
        setShortestDistance(start, 0);
        unsettledNodes.add(start);
    }
    
    /**
     * Run Dijkstra's shortest path algorithm on the map.
     * The results of the algorithm are available through
     * {@link #getPredecessor(T)}
     * and 
     * {@link #getShortestDistance(T)}
     * upon completion of this method.
     * 
     * @param start the starting T
     * @param destination the destination T. If this argument is <code>null</code>, the algorithm is
     * run on the entire graph, instead of being stopped as soon as the destination is reached.
     */
    public void execute(T start, T destination) {
        init(start);
        
        // the current node
        T u;
        
        // extract the node with the shortest distance
        while ((u = unsettledNodes.poll()) != null) {
            assert !isSettled(u);
            
            // destination reached, stop
            if (u.equals(destination)) break;
            
            settledNodes.add(u);
            
            relaxNeighbors(u);
        }
    }

    /**
	 * Compute new shortest distance for neighboring nodes and update if a shorter
	 * distance is found.
	 * 
	 * @param u the node
	 */
    private void relaxNeighbors(T u) {
    	for (T v : map.getDestinations(u)) {
            // skip node already settled
            if (isSettled(v)) continue;
            
            int shortDist = getShortestDistance(u) + map.getCost(u, v);
            
            if (shortDist < getShortestDistance(v)) {
            	// assign new shortest distance and mark unsettled
                setShortestDistance(v, shortDist);
                                
                // assign predecessor in shortest path
                setPredecessor(v, u);
            }  
        }        
    }

	/**
	 * Test a node.
	 * 
     * @param v the node to consider
     * 
     * @return whether the node is settled, ie. its shortest distance
     * has been found.
     */
    private boolean isSettled(T v) {
        return settledNodes.contains(v);
    }

    /**
     * @return the shortest distance from the source to the given node, or
     * {@link DijkstraEngine#INFINITE_DISTANCE} if there is no route to the destination.
     */    
    public int getShortestDistance(T node) {
        Integer d = shortestDistances.get(node);
        return (d == null) ? INFINITE_DISTANCE : d;
    }
    
	/**
	 * Set the new shortest distance for the given node,
	 * and re-balance the queue according to new shortest distances.
	 * 
	 * @param T the node to set
	 * @param distance new shortest distance value
	 */        
    private void setShortestDistance(T node, int distance) {
        /*
         * This crucial step ensures no duplicates are created in the queue
         * when an existing unsettled node is updated with a new shortest 
         * distance.
         * 
         * Note: this operation takes linear time. If performance is a concern,
         * consider using a TreeSet instead instead of a PriorityQueue. 
         * TreeSet.remove() performs in logarithmic time, but the PriorityQueue
         * is simpler. (An earlier version of this class used a TreeSet.)
         */
        unsettledNodes.remove(node);

        /*
         * Update the shortest distance.
         */
        shortestDistances.put(node, distance);
        
		/*
		 * Re-balance the queue according to the new shortest distance found
		 * (see the comparator the queue was initialized with).
		 */
        unsettledNodes.add(node);
    }
    
    /**
     * @return the node leading to the given node on the shortest path, or
     * <code>null</code> if there is no route to the destination.
     */
    public T getPredecessor(T node) {
        return predecessors.get(node);
    }
    
    /**
     * Obtains the next hop from the source node to the destination node.
     * @param dest node to get to.
     * @return next hop.
     */
    public T getNextHop(T source, T dest) {
    	T temp = dest;
    	T nextHop = temp;
    	while( temp != null && !temp.equals( source ) ) {
    		nextHop = temp;
    		temp = getPredecessor(temp);
    	}
    	return nextHop;
    }
    
    private void setPredecessor(T a, T b) {
        predecessors.put(a, b);
    }

    public static void main(String... args) {
    	Node a = new Node();
    	Node b = new Node();
    	Node c = new Node();
    	Node d = new Node();
    	Node e = new Node();
    	
    	
    	IRoutesMap<INode> map = new SparseRoutesMap<INode>();
    	DijkstraEngine<INode> engine = new DijkstraEngine<INode>(map);
    	
    	map.addDirectRoute(a, b, 5);
    	map.addDirectRoute(b, c, 4);
    	map.addDirectRoute(c, b, 2);
    	map.addDirectRoute(c, d, 7);
    	map.addDirectRoute(d, a, 3);
    	map.addDirectRoute(d, e, 5);
    	map.addDirectRoute(e, c, 5);
    	///map.addDirectRoute(start, end, cost)
    	engine.execute(a, d);
    	Node current;
    	
    	System.out.println( a.getAddress() );
    	System.out.println( b.getAddress() );
    	System.out.println( c.getAddress() );

    	System.out.println( d.getAddress() );
    	System.out.println( engine.getPredecessor(d).getAddress() );
    	System.out.println( engine.getShortestDistance(d));
    }
}
