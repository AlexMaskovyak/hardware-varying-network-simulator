package computation;

import javax.management.OperationsException;

import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.IAlgorithm;

import network.communication.IProtocolHandler;
import network.entities.INode;
import network.entities.Node;
import network.routing.IAddress;


import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;

/**
 * Simulatable Computer Node which has modular hardware components.
 * 
 * @author Alex Maskovyak
 *
 */
public class ComputerNode 
		extends Node 
		implements IComputer, INode, ISimulatable {

/// Fields
	
	/** installed algorithm. */
	protected AbstractAlgorithm _algorithm;

	
/// Construction.
	
	/** Default constructor. */
	public ComputerNode() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param address to set for this node.
	 */
	public ComputerNode( IAddress address ) {
		super( address );
	}
	
	/**
	 * Constructor, installs an algorithm.
	 * @param algorithm to install.
	 */
	public ComputerNode(IAddress address, IAlgorithm algorithm) {
		super(address);
		install(algorithm);
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.Node#init()
	 */
	@Override
	protected void init() {
		super.init();
		_algorithm = null;
	}

/// IComputer
	
	/*
	 * (non-Javadoc)
	 * @see computation.IComputer#install(computation.IAlgorithm)
	 */
	@Override
	public void install(IAlgorithm algorithm) {
		_algorithm = (AbstractAlgorithm) algorithm;
		IProtocolHandler handler = (IProtocolHandler)algorithm;
		handler.installLowerHandler( _transport );
		_transport.installHigherHandler( handler );
		//install(handler, handler.getProtocol());
		_algorithm.setSimulator( getSimulator() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.IComputer#getInstalledAlgorithm()
	 */
	@Override
	public IAlgorithm getInstalledAlgorithm() {
		return _algorithm;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IComputer#start()
	 */
	@Override
	public void start() throws OperationsException {
		if( _algorithm == null ) {
			throw new OperationsException("An algorithm must be installed for computing to start.");
		}
		_algorithm.distribute();
		_algorithm.read();
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IComputer#uninstall()
	 */
	@Override
	public void uninstall() {
		_algorithm = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.Node#createNew(routing.IAddress)
	 */
	@Override
	public INode createNew(IAddress address) {
		return new ComputerNode(address);
	}

	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		return new ComputerNode();	
	}
}
