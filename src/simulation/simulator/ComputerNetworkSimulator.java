package simulation.simulator;

import computation.HardwareComputerNode;
import computation.algorithms.AbstractAlgorithm;
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
	/** base algorithm to use for construction/factories. */
	protected IAlgorithm _baseAlgorithm;
	/** base harddrive to use for construction/factories. */
	protected Harddrive _baseHarddrive;
	/** base cache to use for construction/factories. */
	protected Cache _baseCache;
	
	
/// Construction

	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#init()
	 */
	protected void init() {
		super.init();
		setBaseAlgorithm( new DummyAlgorithm() );
		setBaseNode( new HardwareComputerNode() );
		setBaseHarddrive( new Harddrive() );
		setBaseCache( new Cache() );
	}

	
/// Accessors/Mutators

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
	
	/**
	 * Sets the base algorithm which will be used for construction/factory 
	 * methods.
	 * @param algorithm to install onto nodes. 
	 */
	public void setBaseAlgorithm(IAlgorithm algorithm) {
		_baseAlgorithm = algorithm;
	}

	/**
	 * Gets the base algorithm which will be used for construction/factory 
	 * methods.
	 * @return algorithm to install onto nodes.
	 */
	public IAlgorithm getBaseAlgorithm() {
		return _baseAlgorithm;
	}
	
	/**
	 * Sets the base harddrive to be used in construction/factory methods.
	 * @param baseHarddrive to use as a base in factory methods.
	 */
	public void setBaseHarddrive( Harddrive baseHarddrive ) {
		_baseHarddrive = baseHarddrive;
	}
	
	/**
	 * Gets the base harddrive to be used in construction/factory methods.
	 * @return base harddrive to use as a base in factory methods.
	 */
	public Harddrive getBaseHarddrive() {
		return _baseHarddrive;
	}
	
	/**
	 * Sets the base cache to be used in construction/factory methods.
	 * @param baseCache to use as a base in factory methods.
	 */
	public void setBaseCache( Cache baseCache ) {
		_baseCache = baseCache;
	}
	
	/**
	 * Gets the base cache to be use in construction/factory methods.
	 * @return base cache for factory use.
	 */
	public Cache getBaseCache() {
		return _baseCache;
	}
	
	
/// Factory methods
	
	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#createNode()
	 */
	@Override
	public INode createNode() {
		HardwareComputerNode result = null;
		try { 
			result = (HardwareComputerNode) super.createNode();
			IAlgorithm algorithm = (IAlgorithm)getBaseAlgorithm().clone();
			algorithm.install( result );
			((AbstractAlgorithm)algorithm).addListener( 
				new AlgorithmListener( getOutputPath(), algorithm ) );
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
		Harddrive harddrive = (Harddrive)getBaseHarddrive().clone();
		registerSimulatable( harddrive );
		return harddrive;
	}
	
	/**
	 * Creates the default harddrive for use/installation into nodes.
	 * @return default cache;
	 */
	public Cache createCache() {
		Cache cache = (Cache)getBaseCache().clone();
		registerSimulatable( cache );
		return cache;
	}
}
