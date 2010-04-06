package messages;

import network.IData;
import simulation.IDiscreteScheduledEvent.IMessage;

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
	
	/**
	 * Default constructor.
	 * @param index at which to store the data.
	 * @param data to store.
	 */
	public AlgorithmStoreMessage( int index, IData data ) {
		_index = index;
		_data = data;
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
}
