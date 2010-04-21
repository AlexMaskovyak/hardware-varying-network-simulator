package simulation.simulator;

import simulation.simulatable.ISimulatable;
import computation.HardwareComputerNode;
import computation.IComputer;
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
	
	/** designated client node. */
	protected IComputer _client;
	/** amount of information for the client to distribute. */
	protected int _clientDistributionAmount;
	
	
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
	
	/**
	 * Sets the client computer which is to begin distribution.
	 * @param client computer which is to distribute/read data to/from the
	 * network.
	 */
	public void setClient( IComputer client ) {
		_client = client;
	}
	
	/**
	 * Gets the client computer responsible for distribution start.
	 * @return client computer which is to distribute/read data to/from the
	 * network.
	 */
	public IComputer getClient() {
		return _client;
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
	
	
/// Utility
	
	/**
	 * Adds an algorithm listener to the specified node if an output path has
	 * been previously specified.
	 * @param node to whose Algorithm is to have a listener installed.
	 */
	public void addAlgorithmListener( INode node ) {
		try {
			HardwareComputerNode cNode = (HardwareComputerNode)node;

			if( getOutputPath() != null ) {
				AbstractAlgorithm algorithm = 
					(AbstractAlgorithm)cNode.getInstalledAlgorithm();
				algorithm.addListener( 
					new AlgorithmListener( getOutputPath(), algorithm ) );
			}
		} catch( Exception e ) { e.printStackTrace(); }
	}
	
	/**
	 * Adds algorithm listeners to all nodes being simulated.  Useful if we 
	 * don't know what the output path is going to be until later.
	 */
	public void addAlgorithmListeners() {
		for( ISimulatable simulatable : this ) {
			if( simulatable instanceof HardwareComputerNode ) {
				addAlgorithmListener( (HardwareComputerNode)simulatable );
			}
		}
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
			result.install( (IAlgorithm)algorithm );
			addAlgorithmListener( result );
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
