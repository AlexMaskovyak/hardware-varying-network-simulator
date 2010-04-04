package messages;

import network.IData;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages for Algorithms.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmMessage 
		implements IMessage {

	/** data for an algorithm...this should probably be something else. */
	protected IData _data;
	
	/**
	 * Data to send 
	 * @param data
	 */
	public AlgorithmMessage( IData data ) {
		_data = data;
	}
	
	public IData getData() {
		return _data;
	}
}
