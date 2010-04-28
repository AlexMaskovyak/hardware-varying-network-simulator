package computation;

import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.IAlgorithm;
import computation.hardware.CPU;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import computation.hardware.Memory;

import network.entities.INode;
import network.entities.Node;
import network.routing.IAddress;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulator.ISimulator;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Hardware computer node has has actual hardware components.
 * @author Alex Maskovyak
 *
 */
public class HardwareComputerNode 
		extends ComputerNode 
		implements IComputer, IHardwareComputer, INode, ISimulatable {
	
/// Fields
	
	/** cpu. */
	protected CPU _cpu;
	/** cache. */
	protected Cache _cache;
	/** harddrive. */
	protected Harddrive _harddrive;
	/** memory. */
	protected Memory _memory;


/// Construction
	
	/** Default constructor. */
	public HardwareComputerNode() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param address to set for this node.
	 */
	public HardwareComputerNode( IAddress address ) {
		super( address );
	}
	
	/**
	 * Constructor, installs an algorithm.
	 * @param algorithm to install.
	 */
	public HardwareComputerNode(IAddress address, IAlgorithm algorithm) {
		super(address, algorithm);
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.ComputerNode#init()
	 */
	protected void init() {
		super.init();
		_cpu = null;
		setCache( new Cache() );
		setHarddrive( new Harddrive() );
		_memory = null;
	}

	
/// IHardwareComputer Accessors/Mutators
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#getCPU()
	 */
	@Override
	public CPU getCPU() {
		return _cpu;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#setCPU(computation.CPU)
	 */
	@Override
	public void setCPU(CPU cpu) {
		_cpu = cpu;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#getCache()
	 */
	@Override
	public Cache getCache() {
		return _cache;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#setCache(computation.Cache)
	 */
	@Override
	public void setCache(Cache cache) {
		_cache = cache;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#getHarddrive()
	 */
	@Override
	public Harddrive getHarddrive() {
		return _harddrive;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#setHarddrive(computation.Harddrive)
	 */
	@Override
	public void setHarddrive(Harddrive harddrive) {
		_harddrive = harddrive;
		_harddrive.setSimulator( getSimulator() );
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#getMemory()
	 */
	@Override
	public Memory getMemory() {
		return _memory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardwareComputer#setMemory(computation.Memory)
	 */
	@Override
	public void setMemory(Memory memory) {
		_memory = memory;
	}

	
/// ComputerNode
	
	/*
	 * (non-Javadoc)
	 * @see computation.ComputerNode#createNew(routing.IAddress)
	 */
	@Override
	public INode createNew(IAddress address) {
		return new HardwareComputerNode(address);
	}
	
	
/// ISimulator
	
	/*
	 * (non-Javadoc)
	 * @see network.Node#setSimulator(simulation.ISimulator)
	 */
	@Override
	public void setSimulator(ISimulator simulator) {
		super.setSimulator( simulator );
		if( getHarddrive() != null ) { getHarddrive().setSimulator( simulator );}
		if( getCache() != null ) { getCache().setSimulator( simulator ); }
		if( getInstalledAlgorithm() != null ) { 
			((AbstractAlgorithm)getInstalledAlgorithm()).setSimulator( simulator );}
	}

	
/// PublicCloneable 
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		HardwareComputerNode result = new HardwareComputerNode(); //super.clone();
		result.setHarddrive( (Harddrive)this.getHarddrive().clone() );
		result.setCache( (Cache)this.getCache().clone() );
		IAlgorithm algorithm = (IAlgorithm)this.getInstalledAlgorithm().clone();
		result.install( algorithm );
		algorithm.install( result );
		return result;
	}
	

/// Display

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "Node[%s]", getAddress() );
	}
}
