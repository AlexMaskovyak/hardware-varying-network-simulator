package computation;

import network.INode;
import routing.IAddress;
import simulation.ISimulatable;
import simulation.ISimulatorEvent;

public class HardwareComputerNode 
		extends ComputerNode 
		implements IComputer, IHardwareComputer, INode, ISimulatable {

	protected CPU _cpu;
	protected Cache _cache;
	protected Harddrive _harddrive;
	protected Memory _memory;

	public HardwareComputerNode(IAddress address, IAlgorithm algorithm) {
		super(address, algorithm);
	}
	
	protected void init() {
		super.init();
		_cpu = null;
		_cache = null;
		_harddrive = null;
		_memory = null;
	}
	
	@Override
	public CPU getCPU() {
		return _cpu;
	}

	@Override
	public void setCPU(CPU cpu) {
		_cpu = cpu;
	}

	@Override
	public Cache getCache() {
		return _cache;
	}

	@Override
	public void setCache(Cache cache) {
		_cache = cache;
	}

	@Override
	public Harddrive getHarddrive() {
		return _harddrive;
	}

	@Override
	public void setHarddrive(Harddrive harddrive) {
		_harddrive = harddrive;
	}

	@Override
	public Memory getMemory() {
		return _memory;
	}
	
	@Override
	public void setMemory(Memory memory) {
		_memory = memory;
	}
	
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		
		
		// flow should be: node gets info in buffer > mainmemory > cpu > harddrive
		
		super.handleTickEvent(o);
	}
}
