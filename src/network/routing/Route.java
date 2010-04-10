package network.routing;


import java.util.ArrayList;

/**
 * This class models a route. A route has the following properties:
 * <ul>
 * <li> a list of cities, maybe empty
 * <li> a length, its number of stops
 * <li> a distance, the total distance of all its segments
 * </ul>
 * Route instances are created by the {@link RouteBuilder}.
 * 
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @version $Id: Route.java 2368 2007-08-20 22:08:03Z renaud $
 */

public final class Route<T>
		implements Cloneable {
	
	/** concrete type needed for field cloneability */
	private ArrayList<T> _nodes = new ArrayList<T>();
	/** total cost associated with traversing this route end-to-end */ 
	private int _cost = 0;
	
	/** Instances of this class are created by the {@link RouteBuilder}. */	
	public Route() {}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Route<T> clone() {
		Route<T> newInstance = null;
		
		try {
			 newInstance = (Route<T>) super.clone();	
		}
		catch (CloneNotSupportedException cnfe) {
			// we are Cloneable: this should never happen
			assert false : cnfe;
		}
		
		newInstance._nodes = (ArrayList<T>) _nodes.clone();
		return newInstance;
	}
	
	/**
	 * Add a new stop to this route with the given distance.
	 * If this is the first stop (i.e. the starting point), the
	 * <code>distance</code> argument is meaningless.
	 * 
	 * @param stop the next node on this route.
	 * @param cost the distance between the previous node and this one.
	 */
	void addStop(T stop, int cost) {
		if (!_nodes.isEmpty()) {
			this._cost += cost;		
		}
		
		_nodes.add(stop);
	}
	
	/**
	 * @return the total distance of this route.
	 */
	public int getCost() {
		return _cost;	
	}
	
	/**
	 * @return the number of stops on this route. The starting city is not
	 * considered a stop and thus is not counted.
	 */
	public int getLength() {
		return (_nodes.isEmpty()) 
			? 0 
			: _nodes.size() - 1;
	}
	
	/**
	 * @return the last stop on this route. The last stop may be the
	 * starting point if there are no other stops, or NULL is this route
	 * has no stops.
	 */
	public T getLastStop() {
		return (_nodes.isEmpty())
			? null
			: _nodes.get(_nodes.size() - 1);
	}
	
	/**
	 * @return whether this route goes through the given city.
	 */
	public boolean hasNode(T node) {
		return _nodes.contains(node);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer temp = new StringBuffer();
		
		temp.append("l=").append( getLength() )
			.append(" d=").append( getCost() )
			.append(" ").append(_nodes);
			
		return temp.toString();
	}
}
