package computation.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import network.entities.IPublicCloneable;
import network.entities.Node;

import computation.Data;
import computation.IData;

import messages.AlgorithmResponseMessage;
import messages.HarddriveRequestMessage;
import messages.HarddriveStoreMessage;
import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulator.DESimulator;

/**
 * Large storage device.  Typically it come pre-installed with some amount of 
 * indexable data.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public class Harddrive<T extends IData> 
		extends PerformanceRestrictedSimulatable 
		implements IHardware, ISimulatable, Iterable<T>, IPublicCloneable {

/// Fields
	
	/** capacity of this device. */
	protected int _capacity;
	/** speed of the device () */
	protected int _speed;
	/** values. */
	protected Map<Integer, IData> _data;
	
/// Construction

	/** Default constructor.*/
	public Harddrive() { super(); }

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#init()
	 */
	@Override
	protected void init() {
		super.init();
		_data = new HashMap<Integer, IData>();
		_data.put(0, new Data( 0, new byte[] { 0, 0, 0 } ) );
		_data.put(1, new Data( 1, new byte[] { 0, 0, 1 } ) );
		_data.put(2, new Data( 2, new byte[] { 0, 1, 0 } ) );
		_data.put(3, new Data( 3, new byte[] { 0, 1, 1 } ) );
		_data.put(4, new Data( 4, new byte[] { 1, 0, 0 } ) );
		_data.put(5, new Data( 5, new byte[] { 1, 0, 1 } ) );
		super.setTransitTime( 5 );
		super.setMaxAllowedOperations( 3 );
		super.setRefreshInterval( 1 );
	}
	
	
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

	/**
	 * Gets the indexed value.
	 * @return the value at the index.
	 */
	public IData getIndex(int index) {
		return _data.get(index);
	}
	
	/**
	 * Sets the value at the index.
	 * @param index at which to store information.
	 * @param data to store at index.
	 */
	public void setIndex(Integer index, IData data) {
		_data.put( index, data );
	}
	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	protected void handleEventDelegate( IDiscreteScheduledEvent e ) {
		IMessage message = e.getMessage();
		if( message instanceof HarddriveRequestMessage ) {
			HarddriveRequestMessage hdMessage = (HarddriveRequestMessage)message;
			int index = hdMessage.getSequence();
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<IMessage>(
					this, 
					e.getSource(), 
					getSimulator().getTime() + getTransitTime(), 
					getSimulator(), 
					new AlgorithmResponseMessage(getIndex(index))));
		} else if( message instanceof HarddriveStoreMessage ) {
			HarddriveStoreMessage hdMessage = (HarddriveStoreMessage)message;
			int index = hdMessage.getIndex();
			IData data = hdMessage.getData();
			setIndex( index, data );			
		}
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
	
	public Object clone() {
		return super.clone();
	}
	
	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		Harddrive result = new Harddrive();
		result.setCapacity( this.getCapacity() );
		return result;
	}
}
