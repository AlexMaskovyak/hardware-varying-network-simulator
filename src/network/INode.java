package network;
import simulation.ISimulator;


/**
 * An atomic network element.  Represents an information source and sink.
 * 
 * @author Alex Maskovyak
 *
 */
public interface INode {

	/**
	 * Connects this node to a connector and the connector to the node.
	 * @param connection to which to join.
	 */
	public void registerConnection(IConnection connection);
	
	/**
	 * Disconnects this node from a connector and the connector from the node.
	 * @param connection from which to unjoin.
	 */
	public void unregisterConnection(IConnection connection);
	
	/**
	 * Retrieves this node's identifier.
	 * @return node id.
	 */
	public String getId();
	
	/**
	 * Receives data.
	 * @param data to have this node receive.
	 */
	public void receive(IData data);
	
	/**
	 * Receives data across a specific connection.
	 * @param data to have this node receive.
	 * @param connection across which the data was sent.
	 */
	public void receive(IData data, IConnection connection);
	
	/**
	 * Sends data out to all connections.
	 * @param data to send to connected items.
	 */
	public void send(IData data);
	
	/**
	 * Sends data out across the specified connection.
	 * @param data to send to the connected item.
	 * @param connection to receive the data.
	 */
	public void send(IData data, IConnection connection);
}
