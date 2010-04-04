package computation;

import java.util.Iterator;

import messages.AlgorithmMessage;
import network.AbstractProtocolHandler;
import network.IData;

import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Distributes data to random ComputerNodes on the network and then reads back 
 * all pieces from random ComputerNodes.
 * @author Alex Maskovyak
 *
 */
public class RandomDistributionAlgorithm 
		extends AbstractAlgorithm
		implements IAlgorithm, ISimulatable {

	/**
	 * Source of data to distribute.
	 */
	protected Iterable<IData> _data;
	/** reference to the computer we are running upon. */
	protected IHardwareComputer _computer;
	/** */
	protected int _dataCount;
	
	/**
	 * 
	 * @param data
	 */
	public RandomDistributionAlgorithm( IHardwareComputer computer) {
		//_data = data;
		_computer = computer;
	}
	
	@Override
	public void distribute() {
		// distribute the data to nodes in a random fashion
		for( IData datum : _data ) {
			// schedule an event to hard drive to get information to us
			
			// schedule an event to the Node to send
			
		}
	}

	@Override
	public void install(IComputer computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void read() {
		//boolean 
		//while
	}

	@Override
	public String getProtocal() {
		return "DISTR_ALGORITHM";
	}

	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof AlgorithmMessage ) {
			System.out.println("DISTR GOT INFO!");			
		}
	}

	public void handle(IData data) {
		
	}


}
