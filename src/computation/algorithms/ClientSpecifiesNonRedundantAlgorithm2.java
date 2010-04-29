package computation.algorithms;

import computation.HardwareComputerNode;
import computation.IComputer;
import computation.IData;
import computation.IHardwareComputer;
import computation.algorithms.clientSpecifiesNonRedundant.State_NullRole;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;
import computation.state.IStateHolder;

import messages.AlgorithmDoWorkMessage;
import messages.AlgorithmRequestMessage;
import messages.AlgorithmResponseMessage;
import messages.AlgorithmStoreMessage;
import messages.StorageDeviceDataRequestMessage;
import messages.StorageDeviceDataStoreMessage;
import messages.NodeOutMessage;
import messages.ProtocolHandlerMessage;
import network.communication.Address;
import network.communication.IPacket;
import network.communication.IProtocolHandler;
import network.communication.Packet;
import network.entities.INode;
import network.routing.IAddress;

import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
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
		IAddress current = _currentAddress;
		_currentAddress = new Address(((Address)_currentAddress).getRepresentation() + 1);
		return current;
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
	public void setInitialData(IData... data) {
		
	}
	
	/**
	 * Distribute here queues up a distribute event to kick-start disribution 
	 * operations.
	 * @see hardware.IAlgorithm#distribute()
	 */
	public void distribute( int startIndex, int endIndex ) {
		sendDoWork();								// kick-start us.
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
		if( _role == Role.CLIENT ) {
			clientHandle( e );
		} else {
			serverHandle( e );
		}
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
	 * Handles operation when a client.
	 * @param e event to handle.
	 */
	protected void serverHandle( IDEvent e ) {
		IMessage message = e.getMessage();
		// store information into memory
		if( message instanceof AlgorithmStoreMessage ) {
			AlgorithmStoreMessage aMessage = (AlgorithmStoreMessage)message;
			// store the server's address
			_knownServerAddress = aMessage.getServer();
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 0, 1, 1, 0, 0, 0) );
			sendEvent( 
				getComputer().getHarddrive(),
				new StorageDeviceDataStoreMessage(
					aMessage.getIndex(), 
					aMessage.getData()) );
		} 
		// previously stored information is requested
		else if( message instanceof AlgorithmRequestMessage ) {
			AlgorithmRequestMessage aMessage = (AlgorithmRequestMessage)message;
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 0, 0, 0, 0, 1, 1) );
			sendEvent( 
				getComputer().getHarddrive(), 
				new StorageDeviceDataRequestMessage( aMessage.getIndex(), -1 ) );
		} 
		// information requested from memory has arrived
		else if( message instanceof AlgorithmResponseMessage ) {
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 1, 0, 0, 1, 0, 0) );
			System.out.println("server send!");
			sendMessageDownStack( (AlgorithmResponseMessage)message, _knownServerAddress, -1, -1);
		}
	}
	
	
	/**
	 * Handles operations when a server.
	 * @param e event to handle.
	 */
	protected void clientHandle( IDEvent e ) {
		IMessage message = e.getMessage();
		switch( _serverState ) {
			case DISTRIBUTE:
				// grab more information
				if( message instanceof AlgorithmDoWorkMessage ) {
					// do we have more?
					if( haveMoreToDistribute() ) {
						// read index from harddrive
						sendHarddriveRequest( _currentIndex++ );
						notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "DISTR", 0, 0, 0, 0, 1, 0 ) );
						sendDoWork();
					} 
					
				} else if( message instanceof AlgorithmResponseMessage ) {
					IData data = ((AlgorithmResponseMessage)message).getData();
					sendDataToClient( data, getNextStorageAddress() );
					
					_totalHDResponses++;
					
					notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "DISTR", 1, 0, 0, 1, 0, 0));
					
					// check if we've received and distributed everything
					if( _totalHDResponses == ( _endIndex - _startIndex + 1 ) ) {
						_serverState = Client_State.READ;	// read round
						_currentIndex = _startIndex;
						_currentAddress = _startAddress;
						sendDoWork();
					}
				}
				break;
			case READ:
				if( message instanceof AlgorithmDoWorkMessage ) {
					if( haveMoreToRead() ) {
						sendDoWork();
						IAddress address = getNextRetrievalAddress();
						sendClientRequest( _currentIndex++, address );
						notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "READ", 0, 0, 0, 0, 1, 0));
						System.out.println("send remote read");
					} 
				} else if( message instanceof AlgorithmResponseMessage ) {
					_totalClientResponses++;
					
					notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "READ", 0, 1, 0, 0, 0, 0));
					
					if( _totalClientResponses == ( _endIndex - _startIndex + 1 ) ) {
						System.out.println("hurray.");
					}
					if( _totalClientResponses > ( _endIndex - _startIndex + 1 ) ) {
						System.out.println("additional invalid response.");
					}
				}
				break;
		}
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
}
