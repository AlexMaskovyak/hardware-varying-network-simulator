package computation;

import javax.management.OperationsException;

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
	 * Removes a distributed algorithm from this computer.
	 */
	public void uninstall();
}
