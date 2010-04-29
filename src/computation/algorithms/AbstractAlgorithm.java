package computation.algorithms;

import simulation.event.IDEvent;
import computation.IComputer;
import computation.IHardwareComputer;
import computation.state.IState;
import computation.state.IStateHolder;

import network.communication.AbstractProtocolHandler;
import network.entities.IPublicCloneable;

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
	 * 
	 * @param event
	 */
	//public void sendMessageDownStack( IMessage message ) {
		
	//}
}
