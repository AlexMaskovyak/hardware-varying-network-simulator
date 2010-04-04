package computation;

import javax.management.OperationsException;

import network.INode;
import network.IProtocolHandler;
import network.Node;


import routing.IAddress;
import simulation.ISimulatable;

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
		this.install(handler, handler.getProtocal());
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
	
	@Override
	public INode createNew(IAddress address) {
		return new ComputerNode(address);
	}
}
