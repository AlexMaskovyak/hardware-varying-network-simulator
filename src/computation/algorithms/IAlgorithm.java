package computation.algorithms;

import network.entities.IPublicCloneable;
import computation.IComputer;
import computation.IData;
import computation.IHardwareComputer;

/**
 * Represents an algorithm for distributed I/O runnable on an IComputer.
 * @author Alex Maskovyak
 *
 */
public interface IAlgorithm extends IPublicCloneable {

	/**
	 * Sets the initial data which is to be distributed and then read back from
	 * the network servers.
	 * @param data to set for distribution and reading.
	 */
	public void setInitialData( IData... data );
	
	/**
	 * Places data on the network.
	 */
	public void distribute();
	
	/**
	 * Allows this algorithm access to the hardware it is installed upon.
	 * @param computer on which we are loaded.
	 */
	public void install(IComputer computer);

	/**
	 * Get the computer on which we are loaded.
	 * @return computer on which we are loaded.
	 */
	public IHardwareComputer getComputer();
	
	/**
	 * Reads back information previously placed on the network.
	 */
	public void read();
	
	/**
	 * Obtains the protocal for this algorithm.
	 * @return protocal this algorithm is associated with.
	 */
	public String getProtocol();

}
