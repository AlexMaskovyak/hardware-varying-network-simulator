package computation;

import java.util.Iterator;

import messages.AlgorithmDoWorkMessage;
import messages.AlgorithmRequestMessage;
import messages.AlgorithmResponseMessage;
import messages.AlgorithmStoreMessage;
import messages.HarddriveRequestMessage;
import messages.HarddriveStoreMessage;
import messages.NodeInMessage;
import messages.NodeOutMessage;
import network.AbstractProtocolHandler;
import network.Address;
import network.IData;

import simulation.AbstractSimulatable;
import simulation.DefaultDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.PerformanceRestrictedSimulatable;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Distributes data to random ComputerNodes on the network and then reads back 
 * all pieces from random ComputerNodes.
 * @author Alex Maskovyak
 *
 */
public class RandomDistributionAlgorithm 
		extends AbstractAlgorithm
		implements IAlgorithm, ISimulatable {

/// Custom values
	
	/**
	 * Role defines the operations of the Algorithm.  Servers distribute, and 
	 * then request data from interested parties.  Clients are responsible only
	 * for storing data and then retrieving it for an interested party.
	 * @author Alex Maskovyak
	 */
	protected enum Role { SERVER, CLIENT }
	
	/**
	 * States in which a Server can occupy.
	 * @author Alex Maskovyak
	 */
	protected enum Server_State {  IDLE, DISTRIBUTE, READ }
	
	/**
	 * States in which a Client can occupy.
	 * @author Alex Maskovyak
	 */
	protected enum Client_State { AWAIT }

	
/// Fields
	
	/** reference to the computer we are running upon. */
	protected IHardwareComputer _computer;
	/** data to send */
	protected IData _data;
	
	
	/** state of the server, if we are a server. */
	protected Server_State _serverState;
	/** role in the algorithm. */
	protected Role _role;
	
	
/// Construction
	
	/** Default constructor. */
	public RandomDistributionAlgorithm() {
		super();
	}
	
	/** Constructor.
	 * @param computer which we are installed  upon.
	 */
	public RandomDistributionAlgorithm( IHardwareComputer computer ) {
		super();
		_computer = computer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#init()
	 */
	@Override
	protected void init() {
		super.init();
		_serverState = Server_State.IDLE;
		_role = Role.CLIENT;
	}
	

/// Accesors
	
	/**
	 * Get the computer on which we are loaded.
	 * @return computer on which we are loaded.
	 */
	public IHardwareComputer getComputer() {
		return _computer;
	}
	
	
/// IProtocolHandler
	
	/*
	 * (non-Javadoc)
	 * @see computation.AbstractAlgorithm#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return "DISTR_ALGORITHM";
	}

	/*
	 * (non-Javadoc)
	 * @see computation.AbstractAlgorithm#handle(java.lang.Object)
	 */
	@Override
	public void handle(Object packetLikeObject) {
		handleEvent( (IDiscreteScheduledEvent) packetLikeObject );
	}

	
/// IAlgorithm
		
	/**
	 * Distribute here queues up a distribute event to kick-start disribution 
	 * operations.
	 * @see computation.IAlgorithm#distribute()
	 */
	@Override
	public void distribute() {
		_role = Role.SERVER;						// distributors are servers	
		_serverState = Server_State.DISTRIBUTE;		// inside distribution
		sendDoWork();								// kick-start us.
	}
	

	/**
	 * Read, queues up a read event to kick-start read-back operations.
	 * (non-Javadoc)
	 * @see computation.AbstractAlgorithm#read()
	 */
	@Override
	public void read() {
		_serverState = Server_State.READ;			// inside read
		sendDoWork();								// kick-start us
	}

	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.PerformanceRestrictedSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		super.handleEvent( e );
	}


/// Distribution Algorithm's methods
	
	/**
	 * This override implements functionality for client and server.
	 * @see simulation.PerformanceRestrictedSimulatable#subclassHandle(simulation.IDiscreteScheduledEvent)
	 */
	protected void subclassHandle( IDiscreteScheduledEvent e) {
		System.out.println( "subclass handle" );
		if( _role == Role.SERVER ) {
			serverHandle( e );
		} else {
			clientHandle( e );
		}
	}
	
	/**
	 * Handles operation when a client.
	 * @param e event to handle.
	 */
	protected void clientHandle( IDiscreteScheduledEvent e ) {
		IMessage message = e.getMessage();
		// store information into memory
		if( message instanceof AlgorithmStoreMessage ) {
			AlgorithmStoreMessage aMessage = (AlgorithmStoreMessage)message;
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<HarddriveStoreMessage>(
					this, 
					getComputer().getHarddrive(), 
					e.getEventTime() + getTransitTime(), 
					getSimulator(), 
					new HarddriveStoreMessage(
						aMessage.getIndex(), 
						aMessage.getData())));
		} 
		// previously stored information is requested
		else if( message instanceof AlgorithmRequestMessage ) {
			AlgorithmRequestMessage aMessage = (AlgorithmRequestMessage)message;
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<HarddriveRequestMessage>(
					this, 
					getComputer().getHarddrive(), 
					e.getEventTime() + getTransitTime(), 
					getSimulator(), 
					new HarddriveRequestMessage(
						aMessage.getIndex(),
						-1)));
		} 
		// information requested from memory has arrived
		else if( message instanceof AlgorithmResponseMessage ) {
			AlgorithmResponseMessage aMessage = (AlgorithmResponseMessage)message;
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<IMessage>(
					this, 
					(ISimulatable)getComputer(), 
					e.getEventTime() + getTransitTime(), 
					getSimulator(), 
					new AlgorithmResponseMessage(
						aMessage.getData())));
		}
	}
	
	/**
	 * Handles operations when a server.
	 * @param e event to handle.
	 */
	protected void serverHandle( IDiscreteScheduledEvent e ) {
		IMessage message = e.getMessage();
		switch( _serverState ) {
			case DISTRIBUTE:
				// grab more information
				if( message instanceof AlgorithmDoWorkMessage ) {
					// do we have more?
					if( haveMoreToDistribute() ) {
						
					} else {
						_serverState = Server_State.IDLE;	// don't move on yet
					}
				}
				break;
			case READ:
				break;
		}
	}
	
	/**
	 * Determine if there is more to distribute.
	 * @return true if there is more to distribute, false otherwise.
	 */
	protected boolean haveMoreToDistribute() {
		return true;
	}

	/**
	 * Determine if there is more to read back from the network.
	 * @return true if there is more to read, false otherwise.
	 */
	protected boolean haveMoreToRead() {
		return true;
	}
	
	/**
	 * Send ourselves a new message to distribute.  We send this to ourselves at
	 * current simulator time, with the understanding that the underlying model
	 * of renewal/blocking we are built upon will limit exactly how much we can
	 * do.
	 */
	protected void sendDoWork() {
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<IMessage>(
				this, 
				this, 
				getSimulator().getTime(), 
				getSimulator(), 
				new AlgorithmDoWorkMessage()));
	}
}
