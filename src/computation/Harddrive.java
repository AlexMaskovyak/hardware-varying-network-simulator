package computation;

import java.util.Iterator;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;

/**
 * Large storage device.  Typically it come pre-installed with some amount of 
 * indexable data.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public class Harddrive<T extends IData> 
		extends AbstractSimulatable 
		implements IHardware, ISimulatable, Iterable<T> {

/// Fields
	
	/** capacity of this device. */
	protected int _capacity;
	/** speed of the device () */
	protected int _speed;

	
/// Construction

	/** Default constructor.*/
	public Harddrive() { super(); }

	
/// Accessors/Mutators
	
	/**
	 * Obtains this harddrive's capacity in number of pieces of data that can be
	 * stored.
	 * @return number of pieces of data this harddrive can store.
	 */
	public int getCapacity() { return _capacity; }
	
	/**
	 * Sets the capacity in number of pieces of data that can be stored.
	 * @param capacity in number of pieces of data this harddrive can store.
	 */
	public void setCapacity(int capacity) { _capacity = capacity; }


/// IHardware
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#getSpeed()
	 */
	public int getSpeed() { return _speed; }
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#setSpeed(int)
	 */
	public void setSpeed(int speed) { _speed = speed; }

	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#receive(network.IData)
	 */
	@Override
	public void receive(IData data) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#send(network.IData)
	 */
	@Override
	public void send(IData data) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBandwidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBandwitdh(int bandwidth) {
		// TODO Auto-generated method stub
		
	}
}
