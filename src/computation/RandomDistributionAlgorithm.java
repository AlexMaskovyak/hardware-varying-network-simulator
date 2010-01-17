package computation;

import java.util.Iterator;

import network.IData;

/**
 * Distributes data to random ComputerNodes on the network and then reads back all pieces from random ComputerNodes.
 * @author Alex Maskovyak
 *
 */
public class RandomDistributionAlgorithm implements IAlgorithm {

	/**
	 * Source of data to distribute.
	 */
	protected Iterable<IData> _data;
	protected IHardwareComputer _computer;
	
	/**
	 * 
	 * @param data
	 */
	public RandomDistributionAlgorithm(Iterable<IData> data, IHardwareComputer computer) {
		_data = data;
		_computer = computer;
	}
	
	@Override
	public void distribute() {
		// distribute the data to nodes in a random fashion
		for( IData datum : _data ) {
			
		}
	}

	@Override
	public void install(IComputer computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void read() {
		
	}

}
