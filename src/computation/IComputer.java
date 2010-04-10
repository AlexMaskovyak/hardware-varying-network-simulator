package computation;

import javax.management.OperationsException;

import computation.algorithms.IAlgorithm;

/**
 * An object capable of running a distributed algorithm.
 * 
 * @author Alex Maskovyak
 *
 */
public interface IComputer {

	/**
	 * Begin processing.
	 */
	public void start() throws OperationsException;
	
	/**
	 * Adds a distributed algorithm to drive this computer's operation.
	 * @param algorithm to install.
	 */
	public void install(IAlgorithm algorithm);

	/**
	 * Retrieves the algorithm installed on this computer.
	 * @return alogirhtm installed on this computer.
	 */
	public IAlgorithm getInstalledAlgorithm();
	
	/**
	 * Removes a distributed algorithm from this computer.
	 */
	public void uninstall();
}
