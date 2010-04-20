package computation.algorithms;

import computation.IComputer;
import computation.IData;
import computation.IHardwareComputer;
import computation.algorithms.listeners.AlgorithmEvent;

import messages.AlgorithmDoWorkMessage;
import messages.AlgorithmRequestMessage;
import messages.AlgorithmResponseMessage;
import messages.AlgorithmStoreMessage;
import messages.HarddriveRequestMessage;
import messages.HarddriveStoreMessage;
import messages.NodeOutMessage;
import network.communication.Address;
import network.entities.INode;
import network.routing.IAddress;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;


/**
 * Distributes data to random ComputerNodes on the network and then reads back 
 * all pieces from random ComputerNodes.
 * @author Alex Maskovyak
 *
 */
public class DummyAlgorithm 
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
	 * States in which a Client can occupy.
	 * @author Alex Maskovyak
	 */
	protected enum Client_State {  IDLE, DISTRIBUTE, READ }
	
	/**
	 * States in which a Server can occupy, in this case, they await client 
	 * commands and act upon them.
	 * @author Alex Maskovyak
	 */
	protected enum Server_State { AWAIT }

	
/// Fields
	
	/** reference to the computer we are running upon. */
	protected IHardwareComputer _computer;
	/** data to send */
	protected IData _data;
	
	
	/** state of the server, if we are a server. */
	protected Client_State _serverState;
	/** role in the algorithm. */
	protected Role _role;
	
	
	/** first index of data to store and retrieve. */
	protected int _startIndex;
	/** last index of data to store and retrieve. */
	protected int _endIndex;
	/** current index. */
	protected int _currentIndex;
	
	/** total responses from the HD. */
	protected int _totalHDResponses;
	/** total responses from clients. */
	protected int _totalClientResponses;
	
	
	/** first address in the range of addresses to store and retrieve from. */
	protected IAddress _startAddress;
	/** current address to use...used during the read...we simply request things
	 *  in order. */
	protected IAddress _currentAddress;
	/** last address in the range of addresses */
	protected IAddress _endAddress;
	
	/** address of the server */
	protected IAddress _knownServerAddress;
	
	/** number of servers to use */
	protected int _serverCount;
	
	
/// Construction
	
	/** Default constructor. */
	public DummyAlgorithm() {
		this( null );
	}
	
	/** Constructor.
	 * @param computer which we are installed  upon.
	 */
	public DummyAlgorithm( IHardwareComputer computer ) {
		super();
		_computer = computer;
		setMaxAllowedOperations( 20 );
		setRefreshInterval( 1 );
		reset();
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#init()
	 */
	@Override
	protected void init() {
		super.init();
		_serverState = Client_State.IDLE;
		_role = Role.SERVER;
	}
	
	
/// Accesors / Mutators
	
	/**
	 * Sets the range of addresses available for use.
	 * @param startAddress of the address range.
	 * @param endAddress of the address range.
	 */
	public void setAddressRange( IAddress startAddress, IAddress endAddress ) {
		_startAddress = startAddress;
		_endAddress = endAddress;
	}
	
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
	
	/**
	 * Gets the count of servers this algorithm is to set up, distribute to, and
	 * read back from.
	 * @return number of servers to be set up.
	 */
	public int getServerCount() {
		return _serverCount;
	}
	
	/**
	 * Sets the count of servers this algorithm is to set up, distribute to, and
	 * read back from.
	 * @param serverCount to use.
	 */
	public void setServerCount( int serverCount ) {
		_serverCount = serverCount;
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
		_startIndex = startIndex;
		_endIndex = endIndex;
		_currentIndex = startIndex;
		
		_currentAddress = _startAddress;
		
		_totalClientResponses = 0;
		_totalHDResponses = 0;
		
		_role = Role.CLIENT;						// distributors are servers	
		_serverState = Client_State.DISTRIBUTE;		// inside distribution
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
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	protected void handleEventDelegate( IDiscreteScheduledEvent e) {
		if( _role == Role.CLIENT ) {
			clientHandle( e );
		} else {
			serverHandle( e );
		}
	}
	
	/**
	 * Handles operation when a client.
	 * @param e event to handle.
	 */
	protected void serverHandle( IDiscreteScheduledEvent e ) {
		IMessage message = e.getMessage();
		// store information into memory
		if( message instanceof AlgorithmStoreMessage ) {
			AlgorithmStoreMessage aMessage = (AlgorithmStoreMessage)message;
			// store the server's address
			_knownServerAddress = aMessage.getServer();
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 0, 1, 1, 0, 0, 0) );
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
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 0, 0, 0, 0, 1, 1) );
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
			notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "SERVER", 1, 0, 0, 1, 0, 0) );
			AlgorithmResponseMessage aMessage = (AlgorithmResponseMessage)message;
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<IMessage>(
					this, 
					(ISimulatable)getComputer(), 
					e.getEventTime() + getTransitTime(), 
					getSimulator(), 
					new NodeOutMessage(
						new AlgorithmResponseMessage( aMessage.getData()),
						_knownServerAddress, 
						getProtocol())));
		}
	}
	
	/**
	 * Handles operations when a server.
	 * @param e event to handle.
	 */
	protected void clientHandle( IDiscreteScheduledEvent e ) {
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
					} 
					sendDoWork();
					
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
					} 
				} else if( message instanceof AlgorithmResponseMessage ) {
					_totalClientResponses++;
					
					notifyListeners( new AlgorithmEvent(this, e.getEventTime(), "READ", 0, 1, 0, 0, 0, 0));
					
					if( _totalClientResponses == ( _endIndex - _startIndex + 1 ) ) {
						System.out.println("hurray.");
					}
				}
				break;
		}
	}
	
	/**
	 * Determine if there is more to distribute.
	 * @return true if there is more to distribute, false otherwise.
	 */
	protected boolean haveMoreToDistribute() {
		return ( _currentIndex <= _endIndex );
	}

	/**
	 * Determine if there is more to read back from the network.
	 * @return true if there is more to read, false otherwise.
	 */
	protected boolean haveMoreToRead() {
		return ( _currentIndex <= _endIndex );
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
	
	/**
	 * Sends a request to the harddrive for a specific index of data.
	 * @param index to retrieve from the harddrive.
	 */
	protected void sendHarddriveRequest( int index ) {
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<IMessage>(
				this, 
				getComputer().getHarddrive(), 
				getSimulator().getTime() + getTransitTime(), 
				getSimulator(), 
				new HarddriveRequestMessage( 
					index, 
					-1 )));
	}
	
	/**
	 * Sends a data storage request to the client with data.
	 * @param data for the client to store.
	 * @param address of the client which is to store it.
	 */
	protected void sendDataToClient( IData data, IAddress address ) {
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<IMessage>(
				this, 
				(ISimulatable)getComputer(), 
				getSimulator().getTime() + getTransitTime(),
				getSimulator(), 
				new NodeOutMessage(
					new AlgorithmStoreMessage( 
						data.getID(), 
						data,
						((INode)getComputer()).getAddress()),
					address,
					getProtocol())));
	}
	
	/**
	 * Sends a request to a client at the specified address for data with the
	 * index value.
	 * @param index to request. 
	 * @param address of the client which is to field the request.
	 */
	protected void sendClientRequest( int index, IAddress address ) {
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<IMessage>(
				this, 
				(ISimulatable)getComputer(), 
				getSimulator().getTime() + getTransitTime(), 
				getSimulator(), 
				new NodeOutMessage(
					new AlgorithmRequestMessage( index ),
					address,
					getProtocol())));
	}
	
	

/// IPublicCloneable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		DummyAlgorithm result = new DummyAlgorithm();
		result.setServerCount( this.getServerCount() );
		return result;
	}

}
