package computation;

import javax.management.OperationsException;

import network.INode;
import network.Node;


import routing.IAddress;
import simulation.ISimulatable;

/**
 * Simulatable Computer Node which has modular hardware components.
 * 
 * @author Alex Maskovyak
 *
 */
public class ComputerNode extends Node implements IComputer, INode, ISimulatable {

	/** installed algorithm. */
	protected IAlgorithm _algorithm;
	
	public ComputerNode() {
		this(null);
	}
	
	/**
	 * Default constructor.
	 */
	public ComputerNode(IAddress address) {
		this(address, null);
	}
	
	/**
	 * Constructor, installs an algorithm.
	 * @param algorithm to install.
	 */
	public ComputerNode(IAddress address, IAlgorithm algorithm) {
		super(address);
		install(algorithm);
	}
	
	@Override
	protected void init() {
		super.init();
		_algorithm = null;
	}
	
	@Override
	public void install(IAlgorithm algorithm) {
		_algorithm = algorithm;
	}

	@Override
	public void start() throws OperationsException {
		if( _algorithm == null ) {
			throw new OperationsException("An algorithm must be installed for computing to start.");
		}
		_algorithm.distribute();
		_algorithm.read();
	}

	@Override
	public void uninstall() {
		_algorithm = null;
	}
	
	@Override
	public INode createNew(IAddress address) {
		return new ComputerNode(address);
	}
}
