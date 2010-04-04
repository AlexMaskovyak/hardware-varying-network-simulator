package network;

import java.util.Collection;

import routing.IAddress;


/**
 * An atomic network element.  Represents an information source and sink.
 * 
 * @author Alex Maskovyak
 *
 */
public interface INode 
		extends Comparable<INode> {

	/**
	 * Adds an adaptor to this node.
	 * @param adaptor to add.
	 */
	public void addAdaptor(IConnectionAdaptor adaptor);
	
	/**
	 * Removes an adaptor from this node.
	 * @param adaptor to remove.
	 */
	public void removeAdaptor(IConnectionAdaptor adaptor);
	
	/**
	 * Retrieves all adaptors installed on this Node.
	 * @return collection of installed adaptors.
	 */
	public Collection<IConnectionAdaptor> getAdaptors();
	
	/**
	 * Retrieves this node's identifier.
	 * @return node id.
	 */
	public IAddress getAddress();
	
	/**
	 * Receives data across a specific connection.
	 * @param packet to have this node receive.
	 */
	public void receive(IPacket packet);
	
	/**
	 * Sends data out to all connections.
	 * @param data to send to connected items.
	 * @param address to which to send data.
	 */
	public void send(Object data, IAddress address);	
	
	/**
	 * Factory method to allow for a "base" node to create new versions of 
	 * itself.
	 * @return new instantiations of this INode's implementing type.
	 */
	public INode createNew(IAddress address);
}
