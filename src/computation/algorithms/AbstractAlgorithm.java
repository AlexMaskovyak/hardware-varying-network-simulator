package computation.algorithms;

import computation.IComputer;
import computation.IHardwareComputer;

import simulation.event.IDiscreteScheduledEvent;
import network.communication.AbstractProtocolHandler;

/**
 * Abstract algorithm is a protocol handler which can be chained/installed on 
 * the protocol stack.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractAlgorithm
		extends AbstractProtocolHandler
		implements IAlgorithm {

/// Fields
	
	/** computer to use. */
	protected IHardwareComputer _computer;
	
	
/// Construction.
	
	/** Default constructor. */
	public AbstractAlgorithm() {
		super();
	}
	
	
/// IProtocol
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#getProtocal()
	 */
	@Override
	public String getProtocol() {
		return "ALGORITHM";
	}

	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#handle(java.lang.Object)
	 */
	@Override
	public abstract void handle(Object packetLikeObject);
	

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
	 * @see computation.IAlgorithm#read()
	 */
	@Override
	public void read() {
		System.out.println("READ");
	}


	public IHardwareComputer getComputer() {
		return null;
	}
}
