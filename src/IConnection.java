
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
	
	/**
	 * Receives data.
	 * @param data to receive.
	 */
	public void receive(IData data);
	
	/**
	 * Sends data to connected nodes in an implementation specific way.
	 * @param sender node sending information.
	 * @param data to be sent.
	 */
	public void send(INode sender, IData data);
}
