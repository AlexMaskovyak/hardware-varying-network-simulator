package messages;

import simulation.IDiscreteScheduledEvent.IMessage;

/**
 *  Messages designed for Harddrive.  Requests information.
 * @author Alex Maskovyak
 *
 */
public class HarddriveRequestMessage 
		implements IMessage {

	/** index requested. */
	protected int _sequence;
	/** unique id of this request. */
	protected int _requestId;
	
	/**
	 * Default constructor.
	 * @param index in the datastore to get, basically an index.
	 * @param requestId useful if there is more than one requester.  Unless an
	 * advanced ComputerNode is made which support multiple algorithms, that 
	 * shouldn't be the case.
	 */
	public HarddriveRequestMessage( int index, int requestId ) {
		_sequence = index;
		_requestId = requestId;
	}
	
	/**
	 * Obtains the index to retrieve.
	 * @return index to retrieve.
	 */
	public int getSequence() {
		return _sequence;
	}
	
	/**
	 * Obtains the request id.
	 * @return id of this request.
	 */
	public int getRequestId() {
		return _requestId;
	}
}
