package network.communication;

import network.routing.IAddress;

/**
 * Packets are the primary unit that is exchanged.
 * @author Alex Maskovyak
 *
 * @param <T> the payload of this packet.
 */
public interface IPacket<T> {

	/**
	 * Obtains the payload of this packet.
	 * @return packet's payload.
	 */
	public abstract T getContent();

	/**
	 * Obtains the source address of this packet.
	 * @return source address of this packet.
	 */
	public abstract IAddress getSource();

	/**
	 * Obtains the destination address of this packet.
	 * @return destination address of this packet.
	 */
	public abstract IAddress getDestination();

	/**
	 * Obtains the protocol that understands/generated this packet.
	 * @return protocol that can handle this packet.
	 */
	public abstract String getProtocol();

	/**
	 * Obtains the Time to Live count of this packet.
	 * @return time to live (in hops) of this packet.
	 */
	public abstract int getTTL();

	/**
	 * Obtains the sequence number of this packet.
	 * @return sequence number of the packet.
	 */
	public abstract int getSequence();
}