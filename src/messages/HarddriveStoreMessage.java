package messages;

import computation.IData;

import simulation.event.IDiscreteScheduledEvent.IMessage;

/**
 * Message telling Harddrive to store information.
 * @author Alex Maskovyak
 *
 */
public class HarddriveStoreMessage 
		implements IMessage {

	/** index at which to store the data. */
	protected int _index;
	/** data to store. */
	protected IData _data;
	
	/**
	 * Default constructor.
	 * @param index at which to store the information.
	 * @param data to store.
	 */
	public HarddriveStoreMessage( int index, IData data ) {
		_index = index;
		_data = data;
	}
	
	/**
	 * Obtain the index.
	 * @return index.
	 */
	public int getIndex() {
		return _index;
	}
	
	/**
	 * Obtain the data to store.
	 * @return data to store.
	 */
	public IData getData() {
		return _data;
	}
}
