package simulation;

import computation.Cache;
import computation.Harddrive;
import computation.HardwareComputerNode;
import computation.IAlgorithm;
import computation.RandomDistributionAlgorithm;

import network.INode;

/**
 * ComputerNetworkSimulator creates ComputerNodes instead of normal Nodes.
 * @author Alex Maskovyak
 *
 */
public class ComputerNetworkSimulator 
		extends NetworkSimulator 
		implements ISimulator {

	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#init()
	 */
	protected void init() {
		super.init();
		_baseline = new HardwareComputerNode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#createNode()
	 */
	@Override
	public INode createNode() {
		HardwareComputerNode result = (HardwareComputerNode) super.createNode();
		result.install( (IAlgorithm)new RandomDistributionAlgorithm( result ) );
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
