package network.communication;

import network.entities.IPublicCloneable;
import network.routing.IAddress;

/**
 * Packets are the primary unit that is exchanged.
 * @author Alex Maskovyak
 *
 * @param <T> the payload of this packet.
 */
public interface IPacket<T> 
		extends IPublicCloneable {

	/**
	 * Obtains the payload of this packet.
	 * @return packet's payload.
	 */
	public abstract T getContent();
	
	/**
	 * Sets the payload of this packet.
	 * @param content of the packet.
	 */
	public abstract void setContent(T content);

	/**
	 * Obtains the source address of this packet.
	 * @return source address of this packet.
	 */
	public abstract IAddress getSource();

	/**
	 * Sets the source address for this packet.
	 * @param source address for this packet.
	 */
	public abstract void setSource(IAddress source);
	
	/**
	 * Obtains the destination address of this packet.
	 * @return destination address of this packet.
	 */
	public abstract IAddress getDestination();
	
	/**
	 * Sets the destination adddress for this packet.
	 * @param destination of the packet.
	 */
	public abstract void setDestination(IAddress destination);

	/**
	 * Obtains the protocol that understands/generated this packet.
	 * @return protocol that can handle this packet.
	 */
	public abstract String getProtocol();

	/**
	 * Sets the protocol that understands/generated this packet.
	 * @param protocol that can handle this packet.
	 */
	public abstract void setProtocol(String protocol);
	
	/**
	 * Obtains the Time to Live count of this packet.
	 * @return time to live (in hops) of this packet.
	 */
	public abstract int getTTL();

	/**
	 * Sets the Time to Live count for this packet.
	 * @param ttl (in hops) for this packet.
	 */
	public abstract void setTTL(int ttl);
	
	/**
	 * Obtains the sequence number of this packet.
	 * @return sequence number of the packet.
	 */
	public abstract long getSequence();
	
	/**
	 * Sets the sequence number for this packet.
	 * @param sequence number for this packet.
	 */
	public abstract void setSequence(long sequence);
}