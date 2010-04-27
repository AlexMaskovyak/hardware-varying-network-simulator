package network.communication;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import messages.ProtocolHandlerMessage;
import network.routing.IAddress;

/**
 * Handles transport layer responsibilities.
 * @author Alex Maskovyak
 *
 */
public class TransportProtocolHandler 
		extends AbstractProtocolHandler<IPacket, IPacket<IPacket>>
		implements IProtocolHandler<IPacket, IPacket<IPacket>> {
	
/// Fields
	
	/** protocol for this handler. */
	protected static String PROTOCOL = "TRANSPORT";
	/** local address. */
	protected IAddress _address;

	/** sequence number to assign to new packets. */
	protected long _currentSequenceNumber;
	/** maximum number of sequence numbers to store. */
	protected int _sequenceNumberStorageLimit;
	/** the last several sequence numbers we've seen, these should be dropped, 
	 *  since they are repeated values. */
	protected Queue<Long> _sequenceNumbersSeen;
	
	
/// Construction
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#init()
	 */
	@Override
	protected void init() {
		super.init();
		_currentSequenceNumber = (new Random(System.nanoTime())).nextLong();
		_sequenceNumberStorageLimit = 10;
		_sequenceNumbersSeen = new LinkedList<Long>();
		setTransitTime( .0000001 );
	}
	
/// Accessors/Mutators
	
	/**
	 * Sets the address of this transport layer handler.
	 * @param address to assign.
	 */
	public void setAddress( IAddress address ) {
		_address = address;
	}
	
	/**
	 * Gets the address of this transport layer handler.
	 * @return address assigned to this handler.
	 */
	public IAddress getAddress() {
		return _address;
	}

	/**
	 * Adds a sequence number to the sequence number queue.  We only store a 
	 * small subset of the last several sequences.
	 * @param sequenceNumber to say we have seen.
	 */
	public void addSequenceNumber( long sequenceNumber ) {
		if( _sequenceNumbersSeen.size() == _sequenceNumberStorageLimit ) {
			_sequenceNumbersSeen.poll();
		}
		_sequenceNumbersSeen.add( sequenceNumber );
	}
	
	/**
	 * Creates a new sequence number for use and adds it to our storage.
	 * @return next sequence number to use.
	 */
	public long nextSequenceNumber() {
		addSequenceNumber( _currentSequenceNumber );
		long result = _currentSequenceNumber++;
		return result;
	}
	
	
/// PerformanceRestrictedSimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	protected void handleEventDelegate( IDiscreteScheduledEvent e ) {
		ProtocolHandlerMessage message = (ProtocolHandlerMessage)e.getMessage();
		switch( message.getType() ) {
		
			case HANDLE_HIGHER: handleHigher( message.getPacket(), message.getCaller() ); break;
			case HANDLE_LOWER: handleLower( message.getPacket(), message.getCaller() ); break;
			default: throw new RuntimeException( "Unsupported message type." );
		}	
	}

	
/// IProtocolHandler

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return TransportProtocolHandler.PROTOCOL;
	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleHigher(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleHigher(IPacket packet, IProtocolHandler caller) {
		IPacket segment = 
			new Packet( 
				packet, 
				getAddress(), 
				packet.getDestination(), 
				getProtocol(), 
				-1, 
				nextSequenceNumber() );
		
		sendEvent(
				(ISimulatable)getLowerHandler(), 
				new ProtocolHandlerMessage( 
					ProtocolHandlerMessage.TYPE.HANDLE_HIGHER, 
					segment, 
					this ));
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleLower(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleLower(IPacket<IPacket> packet, IProtocolHandler caller) {
		//System.out.println( "transport handle lower");
		// if we've seen this sequence number before that means we previously
		// sent it
		//if( _sequenceNumbersSeen.contains( packet.getSequence() ) ) { System.out.println("already has sequence"); return; }
		
		//System.out.println( "transport handle lower");
		
		addSequenceNumber( packet.getSequence() );
		sendEvent( 
			(ISimulatable)getHigherHandler(), 
			(IMessage) packet.getContent().getContent() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("TransportPH [%s]", getAddress() );
	}
}
