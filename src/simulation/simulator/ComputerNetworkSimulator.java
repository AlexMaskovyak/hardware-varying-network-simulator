package simulation.simulator;

import computation.HardwareComputerNode;
import computation.algorithms.IAlgorithm;
import computation.algorithms.DummyAlgorithm;
import computation.algorithms.listeners.AlgorithmListener;
import computation.hardware.Cache;
import computation.hardware.Harddrive;

import network.entities.INode;

/**
 * ComputerNetworkSimulator creates ComputerNodes instead of normal Nodes.
 * @author Alex Maskovyak
 *
 */
public class ComputerNetworkSimulator 
		extends NetworkSimulator 
		implements ISimulator {

/// Construction
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#init()
	 */
	protected void init() {
		super.init();
		_baseline = new HardwareComputerNode();
	}

	/**
	 * Sets the output path for reporters to store information.
	 * @param outPath for reporters to store their information.
	 */
	public void setOutputPath( String outPath ) {
		
	}
	
	
/// Factory methods
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#createNode()
	 */
	@Override
	public INode createNode() {
		HardwareComputerNode result = (HardwareComputerNode) super.createNode();
		DummyAlgorithm algorithm = new DummyAlgorithm( result );
		algorithm.addListener( new AlgorithmListener( System.out ) );
		result.install( (IAlgorithm)algorithm );
		result.setHarddrive( createHarddrive() );
		return result;
	}
	
	/**
	 * Creates the default harddrive for use/installation into nodes.
	 * @return default harddrive.
	 */
	public Harddrive createHarddrive() {
		Harddrive harddrive = new Harddrive();
		registerSimulatable( harddrive );
		return harddrive;
	}
	
	/**
	 * Creates the default harddrive for use/installation into nodes.
	 * @return default cache;
	 */
	public Cache createCache() {
		Cache cache = new Cache();
		registerSimulatable( cache );
		return cache;
	}
}
