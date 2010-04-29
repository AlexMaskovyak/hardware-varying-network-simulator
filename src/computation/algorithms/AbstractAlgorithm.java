package computation.algorithms;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import computation.IComputer;
import computation.IHardwareComputer;
import computation.state.IState;
import computation.state.IStateHolder;

import messages.ProtocolHandlerMessage;
import network.communication.AbstractProtocolHandler;
import network.communication.IPacket;
import network.communication.IProtocolHandler;
import network.communication.Packet;
import network.entities.IPublicCloneable;
import network.routing.IAddress;

/**
 * Abstract algorithm is a protocol handler which can be chained/installed on 
 * the protocol stack.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractAlgorithm
		extends AbstractProtocolHandler
		implements IAlgorithm, IPublicCloneable, IStateHolder {

/// Fields
	
	/** computer to use. */
	protected IHardwareComputer _computer;
	/** number of servers to use */
	protected int _serverCount;
	/** amount of data the client is to distribute. */
	protected int _clientDistributionAmount;
	/** current state of the algorithm. */
	protected IState _iState;
	
/// Construction.
	
	/** Default constructor. */
	public AbstractAlgorithm() {
		super();
	}
	
	protected void init() {
		super.init();
		_protocol = "ALGORITHM";
	}
	

/// IAlgorithm
	
	/*
	 * (non-Javadoc)
	 * @see computation.IAlgorithm#distribute()
	 */
	@Override
	public void distribute() {
		System.out.println("DISTRIBUTE!");
		
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IAlgorithm#install(computation.IComputer)
	 */
	@Override
	public void install(IComputer computer) {
		_computer = (IHardwareComputer)computer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.IAlgorithm#getComputer()
	 */
	@Override
	public IHardwareComputer getComputer() {
		return _computer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.IAlgorithm#read()
	 */
	@Override
	public void read() {
		System.out.println("READ");
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

	/**
	 * Sets the amount of data the client node is to generate, send, and then
	 * read back from the network.
	 * @param dataAmount for the client to generate, send, and read.
	 */
	public void setDataAmount( int dataAmount ) {
		_clientDistributionAmount = dataAmount;
	}
	
	/**
	 * Gets the amount of data the client node is to generate, send, and then
	 * read back from the network.
	 * @return amount of data the client is to send/receive.
	 */
	public int getDataAmount() {
		return _clientDistributionAmount;
	}

	
/// IStateHolder

	/*
	 * (non-Javadoc)
	 * @see computation.state.IStateHolder#setState(computation.state.IState)
	 */
	@Override
	public void setIState( IState newState ) {
		_iState = newState;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.state.IStateHolder#getIState()
	 */
	@Override
	public IState getIState() {
		return _iState;
	}
	
	/**
	 * Sends messages down the stack without having to specify ttl or sequence.
	 * @param message for the algorithm on another node.
	 * @param destination address on which the other algorithm is running.
	 */
	public void sendMessageDownStack( IMessage message, IAddress destination ) {
		sendMessageDownStack( message, destination, -1, -1);
	}
	
	/**
	 * Quick and easy way to send messages down the stack.  Encapsulates the 
	 * message into a packet which will be sent in a scheduled ProtocolHandler
	 * Message.
	 * @param message for the algorithm on another node.
	 * @param destination address on which the other algorithm is running.
	 * @param ttl to determine how many hops this packet should live for.
	 * @param sequence number of this packet.
	 */
	public void sendMessageDownStack( 
			IMessage message, 
			IAddress destination, 
			int ttl, 
			int sequence ) {
		// wrap message in a packet
		IPacket packet = 
			new Packet(
				message, 
				this.getComputer().getAddress(),
				destination, 
				getProtocol(),
				ttl,
				sequence);
		ProtocolHandlerMessage phMessage 
			= new ProtocolHandlerMessage( 
					ProtocolHandlerMessage.TYPE.HANDLE_HIGHER, packet, this );
		sendEvent( (ISimulatable)getLowerHandler(), phMessage );
	}
}
