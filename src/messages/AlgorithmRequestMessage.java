package messages;

import network.IData;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages for Algorithms.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmRequestMessage 
		implements IMessage {

	/** index of the data being request. */
	protected int _index;
	
	/**
	 * Default constructor.
	 * @param data
	 */
	public AlgorithmRequestMessage( int index ) {
		_index = index;
	}
	
	/**
	 * Obtain the index.
	 * @return index.
	 */
	public int getIndex() {
		return _index;
	}
}
