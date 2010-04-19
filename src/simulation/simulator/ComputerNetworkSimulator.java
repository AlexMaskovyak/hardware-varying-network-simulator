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

/// Fields
	
	/** path to be used for outputting log files. */
	protected String _outputPath;

	
/// Construction

	/** Constructor. */
	public ComputerNetworkSimulator() {
		
	}
	
	//public ComputerNetworkSimulator() {
		
	//}
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#init()
	 */
	protected void init() {
		super.init();
		_baseNode = new HardwareComputerNode();
	}

	/**
	 * Sets the output path to which reporters will log information.
	 * @param outPath for reporters to store their information.
	 */
	public void setOutputPath( String outPath ) {
		_outputPath = outPath;
	}
	
	/**
	 * Gets the output path to which reporters will log information.
	 * @return output path for information logging.
	 */
	public String getOutputPath() {
		return _outputPath;
	}
	
	
/// Factory methods
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#createNode()
	 */
	@Override
	public INode createNode() {
		HardwareComputerNode result = null;
		try { result = (HardwareComputerNode) super.createNode();
			DummyAlgorithm algorithm = new DummyAlgorithm( result );
			algorithm.addListener( new AlgorithmListener( getOutputPath(), algorithm ) );
			result.install( (IAlgorithm)algorithm );
			result.setHarddrive( createHarddrive() );
		} catch( Exception e ) { e.printStackTrace(); }
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
