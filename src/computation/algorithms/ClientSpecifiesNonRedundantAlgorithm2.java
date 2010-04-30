package computation.algorithms;

import javax.swing.GroupLayout.Alignment;

import computation.IData;
import computation.IHardwareComputer;
import computation.algorithms.clientSpecifiesNonRedundant.AlgorithmMessage;
import computation.algorithms.clientSpecifiesNonRedundant.State_NullRole;
import computation.state.IStateHolder;

import messages.AlgorithmDoWorkMessage;
import network.communication.IPacket;
import network.communication.IProtocolHandler;
import network.routing.IAddress;

import simulation.event.IDEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;


/**
 * Distributes data to random ComputerNodes on the network and then reads back 
 * all pieces from random ComputerNodes.
 * @author Alex Maskovyak
 *
 */
public class ClientSpecifiesNonRedundantAlgorithm2 
		extends AbstractAlgorithm
		implements IAlgorithm, IProtocolHandler, ISimulatable, IStateHolder {

	
/// Fields
	
	/** reference to the computer we are running upon. */
	protected IHardwareComputer _computer;
	/** data to send */
	protected IData[] _data;

	
/// Construction
	
	/** Default constructor. */
	public ClientSpecifiesNonRedundantAlgorithm2() {
		this( null );
	}
	
	/** Constructor.
	 * @param computer which we are installed  upon.
	 */
	public ClientSpecifiesNonRedundantAlgorithm2( IHardwareComputer computer ) {
		super();
		_computer = computer;
		reset();
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#init()
	 */
	@Override
	protected void init() {
		super.init();
		setTransitTime( .0000001 );
		setIState( new State_NullRole() );
	}
	
	
/// Accesors / Mutators
	
	/**
	 * Obtains the next address to use for information storage.
	 * @return next address to store information.
	 */
	public IAddress getNextStorageAddress() {
		//IAddress current = _currentAddress;
		//_currentAddress = new Address(((Address)_currentAddress).getRepresentation() + 1);
		return null; //current;
	}
	
	/**
	 * Obtains the next address to query for information during the read phase.
	 * @return next address to ask for information.
	 */
	public IAddress getNextRetrievalAddress() {
		return getNextStorageAddress();
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


/// IAlgorithm

	/*
	 * (non-Javadoc)
	 * @see computation.AbstractAlgorithm#distribute()
	 */
	@Override
	public void distribute() {
		distribute( 1, 5 );
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.IAlgorithm#setInitialData(computation.IData[])
	 */
	@Override
	public void setInitialData(IData... data) { /* not used, we just generate it */	}
	
	/**
	 * Distribute here queues up a distribute event to kick-start disribution 
	 * operations.
	 * @see hardware.IAlgorithm#distribute()
	 */
	public void distribute( int startIndex, int endIndex ) {
		// load data to harddrive
		getComputer().getHarddrive().generateAndLoadData( getDataAmount() );
		sendEvent( this, new AlgorithmMessage( AlgorithmMessage.TYPE.SET_CLIENT ) );
		//sendDoWork();								// kick-start us.
	}
	
	/**
	 * Read, queues up a read event to kick-start read-back operations.
	 * (non-Javadoc)
	 * @see hardware.AbstractAlgorithm#read()
	 */
	@Override
	public void read() {
		//System.out.println("in read call.");
		//_serverState = Server_State.READ;			// inside read
		//sendDoWork();								// kick-start us
	}

	
/// Distribution Algorithm's methods
	
	/**
	 * This override implements functionality for client and server.
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDEvent)
	 */
	protected void handleEventDelegate( IDEvent e) {
		getIState().handleEventDelegate( e );
	}
	

	@Override
	public void handleHigher(Object payload, IProtocolHandler sender) {
		// does not exist
		// we should never have something installed atop us.
	}

	@Override
	public void handleLower(Object message, IProtocolHandler sender) {
		IPacket packet = (IPacket)message;
		packet.getContent();
	}
	
	/**
	 * Send ourselves a new message to distribute.  We send this to ourselves at
	 * current simulator time, with the understanding that the underlying model
	 * of renewal/blocking we are built upon will limit exactly how much we can
	 * do.
	 */
	protected void sendDoWork() {
		sendEvent( this, new AlgorithmDoWorkMessage() );
	}
	

/// IPublicCloneable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		ClientSpecifiesNonRedundantAlgorithm2 result = new ClientSpecifiesNonRedundantAlgorithm2();
		result.setServerCount( this.getServerCount() );
		result.setDataAmount( this.getDataAmount() );
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "Node[%s] State[ %s ]", getComputer().getAddress(), getIState() );
	}
}
