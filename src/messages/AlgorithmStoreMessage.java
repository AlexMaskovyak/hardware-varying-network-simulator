package messages;

import computation.IData;

import network.routing.IAddress;
import simulation.event.IDEvent.IMessage;

/**
 * Storage message for an algorithm.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmStoreMessage 
		implements IMessage {

	/** index at which to store it. */
	protected int _index;
	/** data to store. */
	protected IData _data;
	/** address of the server requesting storage. */
	protected IAddress _server;
	
	/**
	 * Default constructor.
	 * @param index at which to store the data.
	 * @param data to store.
	 */
	public AlgorithmStoreMessage( int index, IData data, IAddress server ) {
		_index = index;
		_data = data;
		_server = server;
	}
	
	/**
	 * Retrieve index.
	 * @return index at which to store.
	 */
	public int getIndex() {
		return _index;
	}
	
	/**
	 * Gets the data to store.
	 * @return data to store.
	 */
	public IData getData() {
		return _data;
	}
	
	/**
	 * Gets the address of the server who sent this.
	 * @return address of the server.
	 */
	public IAddress getServer() {
		return _server;
	}
}
