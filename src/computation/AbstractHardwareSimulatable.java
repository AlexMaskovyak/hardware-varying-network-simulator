package computation;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.ISimulatable;

/**
 * General implementation of IHardware.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractHardwareSimulatable
		extends AbstractSimulatable
		implements IHardware, ISimulatable {

// Fields
	
	/** quantity that we can schedule per unit time. */
	protected int _bandwidth;
	/** speed, affects how far into the future our events will be scheduled. */
	protected int _speed;
	

// Construction
	
	/** Default constructor. */
	protected AbstractHardwareSimulatable() {
		init();
	}
	
	/** Externalize instantiations. */
	protected void init() {
		_bandwidth = 0;
		_speed = 0;
	}

	
// Accessors
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#getBandwidth()
	 */
	@Override
	public int getBandwidth() {
		return _bandwidth;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#setBandwitdh(int)
	 */
	@Override
	public void setBandwitdh( int bandwidth ) {
		_bandwidth = bandwidth;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#getSpeed()
	 */
	@Override
	public int getSpeed() {
		return _speed;
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.IHardware#setSpeed(int)
	 */
	@Override
	public void setSpeed( int speed ) {
		_speed = speed;
	}
	
// Operational (must be overridden!)
	
	public abstract void receive( IData data );

	public abstract void send( IData data );


}
