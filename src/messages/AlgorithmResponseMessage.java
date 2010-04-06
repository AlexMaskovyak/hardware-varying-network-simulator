package messages;

import network.IData;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages for Algorithms.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmResponseMessage 
		implements IMessage {

	/** payload for the algorithm... */
	protected IData _data;
	
	/**
	 * Default constructor.
	 * @param data to send as a response.
	 */
	public AlgorithmResponseMessage( IData data ) {
		_data = data;
	}
	
	/**
	 * Obtain data.
	 * @return data sent as response.
	 */
	public IData getData() {
		return _data;
	}
}
