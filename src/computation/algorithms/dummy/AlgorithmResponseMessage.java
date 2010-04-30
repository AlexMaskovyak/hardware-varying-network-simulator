package computation.algorithms.dummy;

import computation.IData;

import simulation.event.IDEvent.IMessage;

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
