package computation;

import javax.management.OperationsException;

import network.INode;
import network.Node;


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
	
	/**
	 * Default constructor.
	 */
	public ComputerNode() {
		super();
	}
	
	/**
	 * Constructor, installs an algorithm.
	 * @param algorithm to install.
	 */
	public ComputerNode(IAlgorithm algorithm) {
		super();
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

}
