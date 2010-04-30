package computation.hardware;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import network.entities.IPublicCloneable;

import computation.Data;
import computation.IData;

import messages.AlgorithmResponseMessage;
import messages.StorageDeviceMessage;
import simulation.event.IDEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;


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

	/**
	 * Gets the current size of all items on disk.
	 * @return number of indices on disk.
	 */
	public int getSize() {
		return _data.size();
	}

	
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
	
	/**
	 * Deletes the index and data there specified.
	 * @param index and the corresponding data to delete.
	 */
	public void deleteIndex(Integer index) {
		_data.remove( index );
	}
	
	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	protected void handleEventDelegate( IDEvent e ) {
		
		StorageDeviceMessage message = (StorageDeviceMessage)e.getMessage();
		switch( message.getType() ) {
			case RETRIEVE:
				sendEvent( 
					e.getSource(), 
					new StorageDeviceMessage( StorageDeviceMessage.TYPE.RESPONSE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, message.getIndex(), message.getRequestId(), getIndex( message.getIndex() ) ) );
				break;
			case STORE:
				setIndex( message.getIndex(), message.getData() );
				break;
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
	
	/**
	 * Generates the quantity of data specified.
	 * @param amount
	 * @return
	 */
	public void generateAndLoadData( int amount ) {
		for( int i = 0; i < amount; ++i ) {
			setIndex( i, new Data( i, new byte[] { Byte.parseByte( String.format( "%d", i % 128 ) ) } ) );
		}
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
