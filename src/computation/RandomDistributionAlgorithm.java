package computation;

import java.util.Iterator;

import messages.AlgorithmDoDistributeMessage;
import messages.AlgorithmResponseMessage;
import messages.AlgorithmStoreMessage;
import messages.HarddriveRequestMessage;
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
		extends PerformanceRestrictedSimulatable
		implements IAlgorithm, ISimulatable {

/// Custom values
	
	/**
	 * Role defines the operations of the Algorithm.  Servers distribute, and 
	 * then request data from interested parties.  Clients are responsible only
	 * for storing data and then retrieving it for an interested party.
	 * @author Alex Maskovyak
	 *
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
	/** are we the server of files? */
	protected boolean _isServer;
	/** data to send */
	protected IData _data;
	
	
	/** state of the server. */
	protected Server_State _state;

	
/// Construction
	
	/**
	 * 
	 * @param data
	 */
	public RandomDistributionAlgorithm( 
			IHardwareComputer computer ) {
		_computer = computer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#init()
	 */
	@Override
	protected void init() {
		_state = Server_State.IDLE;
	}


/// IAlgorithm
	
	@Override
	public void install(IComputer computer) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Distribute here queues up a distribute event to kick-start operation.
	 * 
	 * @see computation.IAlgorithm#distribute()
	 */
	@Override
	public void distribute() {
		_state = Server_State.DISTRIBUTE;
		
		
		// schedule distribution
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<AlgorithmDoDistributeMessage>(
				this, 
				this, 
				getSimulator().getTime() + .0001, 
				getSimulator(), 
				new AlgorithmDoDistributeMessage()));
	}

	
	@Override
	public void read() {
		//boolean 
		//while
	}

	@Override
	public String getProtocol() {
		return "DISTR_ALGORITHM";
	}

	boolean doit = true;
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		super.handleEvent( e );
	}

	/**
	 * This override implements functionality for client and server.
	 * @see simulation.PerformanceRestrictedSimulatable#subclassHandle(simulation.IDiscreteScheduledEvent)
	 */
	protected void subclassHandle( IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		switch( _state ) {
			case DISTRIBUTE:
				if( message instanceof AlgorithmDoDistributeMessage ) {
					
				} else if( message instanceof AlgorithmResponseMessage ) {
					
				}
				break;
			case READ:
				break;
		}
	}
	
	/**
	 * Handles operation when a client.
	 * @param e event to handle.
	 */
	protected void clientHandle( IDiscreteScheduledEvent e ) {
		IMessage message = e.getMessage();
		if( message instanceof AlgorithmStoreMessage ) {
			
		}
	}
	
	/**
	 * Handles operations when a server.
	 * @param e event to handle.
	 */
	protected void serverHandle( IDiscreteScheduledEvent e ) {
		
	}
	
	protected void doWork() {
		
	}
	
	
	protected boolean hasWork() {
		return true;
	}
	
	public void handle(IData data) {
		
	}

}
