package computation;

/**
 * Represents an algorithm for distributed I/O runnable on an IComputer.
 * @author Alex Maskovyak
 *
 */
public interface IAlgorithm {

	/**
	 * Places data on the network.
	 */
	public void distribute();
	
	/**
	 * Allows this algorithm access to the hardware it is installed upon.
	 * @param computer
	 */
	public void install(IComputer computer);
	
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
