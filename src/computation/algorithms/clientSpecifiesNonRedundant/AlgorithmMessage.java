package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.HashMap;
import java.util.Map;

import simulation.event.IDEvent.IMessage;

/**
 * Messages for Algorithms.  The approach is to have values stored in a map 
 * instead of enumerating all of them and adding accessors/getters.  This 
 * approach hides what a message can contain and forces someone to read the
 * states to ascertain a message's potential contents.  On the other hand, this
 * is a far lighter-weight type of message.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmMessage 
		implements IMessage {

/// Class Definitions
	
	/**
	 * Message types.
	 * @author Alex Maskovyak
	 *
	 */
	public static enum TYPE { 
		SET_CLIENT,
		DO_WORK, 
		VOLUNTEER_REQUEST, 
		VOLUNTEER_ACCEPTED, 
		VOLUNTEER_REJECTED,
		SERVER_SELECT,
		SERVER_READ_READY,
		SERVER_DATA_STORE,
		SERVER_DATA_STORE_COMPLETE,
		SERVER_DATA_REQUEST,
		SERVER_DATA_RESPONSE 
	};
	
	/// SET CLIENT
	public static final String SERVERS = "servers";
	
	/// VOLUNTEER ACCEPTED
	public static final String VOLUNTEER_ADDRESS = "volunteer_address";
	public static final String CLIENT_ADDRESS = "client_address";
	
	/// DATA STORE 
	public static final String INDEX = "index";
	public static final String DATA = "data";
	
	// DATA REQUEST
	public static final String START_INDEX = "startIndex";
	public static final String END_INDEX = "endIndex";
	
	
/// Fields
	
	/** type of this message, all messages have this. */
	protected TYPE _type;
	/** stores arbitrary content. */
	protected Map<String, Object> _content;
		
/// Construction
		
	/**
	 * Default constructor.
	 * @param type of this message.
	 */
	public AlgorithmMessage( TYPE type ) {
		_type = type;
	}
	
	/** Externalize instantiation. */
	public void init() {
		_content = new HashMap<String, Object>();
	}
	
/// accessors 

	/**
	 * Obtains the type of this message.
	 * @return type of the message.
	 */
	public TYPE getType() {
		return _type;
	}
	
	/**
	 * Gets a value stored with this message.
	 * @param key associated with a value.
	 * @return value associated with the key.
	 */
	public Object getValue( String key ) {
		return _content.get( key );
	}
	
	/**
	 * Adds the value to be stored in this message.
	 * @param key for the value.
	 * @param value to store with the key.
	 */
	public void setValue( String key, Object value ) {
		_content.put( key, value );
	}
}
