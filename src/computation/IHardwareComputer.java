package computation;

import computation.hardware.CPU;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import computation.hardware.Memory;

/**
 * ComputerNode with explicitely assignable hardware components.  CPU, Harddrive, Cache, etc.
 * @author Alex Maskovyak
 *
 */
public interface IHardwareComputer 
		extends IComputer {

	/**
	 * Obtains the CPU installed.
	 * @return the CPU installed.
	 */
	public CPU getCPU();
	
	/**
	 * Installs a CPU
	 * @param cpu to install.
	 */
	public void setCPU(CPU cpu);

	/**
	 * Obtains the Harddrive installed.
	 * @return the Harddrive installed.
	 */
	public Harddrive getHarddrive();
	
	/**
	 * Installs a harddrive.
	 * @param harddrive to install.
	 */
	public void setHarddrive(Harddrive harddrive);
	
	/**
	 * Obtains the Cache installed.
	 * @return the Cache installed.
	 */
	public Cache getCache();
	
	/**
	 * Installs a cache.
	 * @param cache to install.
	 */
	public void setCache(Cache cache);
	
	/**
	 * Obtains the Memory installed.
	 * @return the Memory installed.
	 */
	public Memory getMemory();
	
	/**
	 * Installs a main memory.
	 * @param memory to install.
	 */
	public void setMemory(Memory memory);
}
