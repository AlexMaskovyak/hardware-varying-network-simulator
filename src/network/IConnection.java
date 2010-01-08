package network;


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
	 * Disconnects a single node.
	 * @param node to disconnect.
	 */
	public void disconnect(INode node);
	
	/**
	 * Obtains an array of INodes connected to this IConnection.
	 * @return INodes connected.
	 */
	public INode[] getConnectedNodes();
	
	/**
	 * Receives data.
	 * @param data to receive.
	 */
	public void receive(IData data);
	
	/**
	 * Recevies data from the specified node.
	 * @param sender who sent data.
	 * @param data to receive.
	 */
	public void receive(INode sender, IData data);
	
	/**
	 * Sends data to connected nodes in an implementation specific way.
	 * @param sender node sending information.
	 * @param data to be sent.
	 */
	public void send(INode sender, IData data);
}
