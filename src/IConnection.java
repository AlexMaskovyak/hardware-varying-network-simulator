
/**
 * An atomic network element.  Represents an information propagation mechanism.
 * 
 * @author Alex Maskovyak
 *
 */
public interface IConnection {

	/**
	 * Connects a series of nodes.
	 * @param nodes to connect.
	 */
	public void connect(INode... nodes);
	
	/**
	 * Connects a single node.
	 * @param node to connect.
	 */
	public void connect(INode node);
}
