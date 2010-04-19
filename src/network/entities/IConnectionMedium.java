package network.entities;

import network.communication.IPacket;


/**
 * An atomic network element.  Represents an information propagation mechanism.
 * 
 * @author Alex Maskovyak
 *
 */
public interface IConnectionMedium 
		extends IPublicCloneable {

	/**
	 * Connects a series of adaptors.
	 * @param nodes to connect.
	 */
	public void connect(IConnectionAdaptor... adaptors);
	
	/**
	 * Connects a single adaptor.
	 * @param node to connect.
	 */
	public void connect(IConnectionAdaptor adaptor);
	
	/**
	 * Disconnects a single adaptor.
	 * @param node to disconnect.
	 */
	public void disconnect(IConnectionAdaptor adaptor);
	
	/**
	 * Obtains an array of INodes connected to this IConnection.
	 * @return INodes connected.
	 */
	public IConnectionAdaptor[] getConnectedAdaptors();
	
	/**
	 * Determines whether two nodes are connected by this medium.
	 * @param node1 endpoint one.
	 * @param node2 endpoint two.
	 * @return true if the two are connected by this medium, false otherwise.
	 */
	public boolean areConnected(INode node1, INode node2);
	
	/**
	 * Receives data.
	 * @param packet to receive.
	 */
	public void receive(IPacket packet);
	
	/**
	 * Receives data from the specified node.
	 * @param sender who sent data.
	 * @param packet to receive.
	 */
	public void receive(IConnectionAdaptor sender, IPacket packet);
	
	/**
	 * Sends data to connected nodes in an implementation specific way.
	 * @param sender node sending information.
	 * @param packet to be sent.
	 */
	public void send(IConnectionAdaptor sender, IPacket packet);
}
