
public interface IDistributedAlgorithm {

	/**
	 * Distribute data to elements in the network.
	 */
	public void distribute();
	
	/**
	 * Read back data from sources in the network.
	 */
	public void read();
}
