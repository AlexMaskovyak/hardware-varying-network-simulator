package messages;

import computation.IData;

import simulation.event.IDEvent.IMessage;

/**
 *  Messages designed for Harddrive.  Requests information.
 * @author Alex Maskovyak
 *
 */
public class StorageDeviceMessage 
		implements IMessage {

/// Class variables
	
	public static enum TYPE { STORE, RETRIEVE, RESPONSE, FREE_SPACE, DELETE, RETRIEVE_AND_REMOVE };
	public static enum DEVICE_TYPE { CACHE, HARDDRIVE };
	
/// Fields
	
	/** type of this message. */
	protected TYPE _type;
	/** type of the device sending this message. */
	protected DEVICE_TYPE _deviceType;
	/** index requested. */
	protected int _index;
	/** unique id of this request. */
	protected int _requestId;
	/** data. */
	protected IData _data;
	/** amount of freespace. */
	protected int _freeSpace;

/// Construction
	
	/**
	 * Default constructor.
	 * @param index in the datastore to get, basically an index.
	 * @param requestId useful if there is more than one requester.  Unless an
	 * advanced ComputerNode is made which support multiple algorithms, that 
	 * shouldn't be the case.
	 */
	public StorageDeviceMessage( TYPE type, DEVICE_TYPE device, int index, int requestId, IData data ) {
		_type = type;
		_deviceType = device;
		_index = index;
		_requestId = requestId;
		_data = data;
	}
	

/// Accessors

	/**
	 * Obtains the type of this messae.
	 * @return type of this message.
	 */
	public TYPE getType() {
		return _type;
	}
	
	/**
	 * Obtains the type of this messae.
	 * @return type of this message.
	 */
	public DEVICE_TYPE getDeviceType() {
		return _deviceType;
	}
	
	/**
	 * Obtains the index to retrieve.
	 * @return index to retrieve.
	 */
	public int getIndex() {
		return _index;
	}
	
	/**
	 * Obtains the request id.
	 * @return id of this request.
	 */
	public int getRequestId() {
		return _requestId;
	}
	
	/**
	 * Obtains any data in this message.
	 * @return data stored.
	 */
	public IData getData() {
		return _data;
	}
	
	/**
	 * Total free space of the storage device, if requested.
	 * @return total free space.
	 */
	public int getFreeSpace() {
		return _freeSpace;
	}
	
	/**
	 * Sets the total free space of the storage device.
	 * @param freeSpace available on the device.
	 */
	public void setFreeSpace( int freeSpace ) {
		_freeSpace = freeSpace;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "SDMessage %s %d %d %s", getDeviceType(), getIndex(), getRequestId(), getData() );
	}
}
