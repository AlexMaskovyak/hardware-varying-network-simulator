package computation;

import network.INode;
import routing.IAddress;
import simulation.ISimulatable;
import simulation.ISimulator;
import simulation.ISimulatorEvent;

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
		_cache = null;
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
		if( getHarddrive() != null ) {
			getHarddrive().setSimulator( simulator );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.Node#handleTickEvent(simulation.ISimulatorEvent)
	 */
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		// flow should be: node gets info in buffer > mainmemory > cpu > harddrive		
		super.handleTickEvent(o);
	}	
}
