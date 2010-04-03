package routing;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This map stores routes in a matrix, a nxn array. It is most useful when
 * there are lots of routes, otherwise using a sparse representation is
 * recommended.
 * 
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @version $Id: DenseRoutesMap.java 2367 2007-08-20 21:47:25Z renaud $
 */

class DenseRoutesMap<T>
		implements IRoutesMap<T> {
	
	private final int[][] distances;
	
	
	public DenseRoutesMap(int numNodes) {
		distances = new int[numNodes][numNodes];
	}
	
	/**
	 * Link two nodes with a direct route with the given cost.
	 */
	public void addDirectRoute(T start, T end, int cost) {
		distances[start.hashCode()][end.hashCode()] = cost;
	}
	
	/**
	 * @return the cost between the two cities, or 0 if no path exists.
	 */
	public int getCost(T start, T end) {
		return distances[start.hashCode()][end.hashCode()];
	}
	
	/**
	 * @return the list of all valid destinations from the given city.
	 */
	public Collection<T> getDestinations(T node) {
		List<T> list = new ArrayList<T>();
		
		for (int i = 0; i < distances.length; i++) {
			if (distances[node.hashCode()][i] > 0) {
				//list.add( City.valueOf(i) );
			}
		}
		
		return list;
	}

	/**
	 * @return the list of all cities leading to the given city.
	 */
	public List<T> getPredecessors(T node) {
		List<T> list = new ArrayList<T>();
		
		for (int i = 0; i < distances.length; i++) {
			if (distances[i][node.hashCode()] > 0) {
				//list.add( City.valueOf(i) );
			}
		}
		
		return list;
	}
	
	/**
	 * @return the transposed graph of this graph, as a new RoutesMap instance.
	 */
	public IRoutesMap getInverse() {
		DenseRoutesMap transposed = new DenseRoutesMap(distances.length);
		
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances.length; j++) {
				transposed.distances[i][j] = distances[j][i];
			}
		}
		
		return transposed;
	}

	@Override
	public void addBiDirectRoute(T node1, T node2, int cost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(T node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBiDirectRoute(T start, T end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDirectRoute(T start, T end) {
		// TODO Auto-generated method stub
		
	}
	
	
}
