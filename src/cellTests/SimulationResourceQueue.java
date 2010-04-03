package cellTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.PriorityBlockingQueue;

import simulation.ISimulatable;

/**
 * Acts as a resource that Simulatables can take and replenish.
 * @author Alex Maskovyak
 *
 */
public abstract class SimulationResourceQueue {

/// Fields
	
	/** underlying resource. */
	protected int _resource;
	/** replenishment amount. */
	protected int _replenishment;
	/** simulatables which need to be renotified of availability. */
	protected PriorityBlockingQueue<ISimulatable> _waitingSimulatables;


/// Construction
	
	/** Default constructor */
	public SimulationResourceQueue() {
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_waitingSimulatables = new PriorityBlockingQueue<ISimulatable>();
	}

	
/// Methods
	
	/** Replenish some amount of the resource. */
	public void replenish() {
		_resource += _replenishment;
		
		if( !_waitingSimulatables.isEmpty() ) {
			ISimulatable simulatable = _waitingSimulatables.poll();
			
		}
	}
	
	/**
	 * Consume some amount of resource if permissiable.
	 * @param amount to attempt to consume.
	 * @return amount actually consumed.
	 */
	public int consume( int amount ) {
		if( _resource > amount && amount <= allowedConsumption() ) {
			_resource -= amount;
			return amount;
		} else {
			return 0;
		}
	}
	
	/**
	 * Returns the amount of a resources that are allowed to be consumed.
	 * @return amount of resources allowed to be consumed.
	 */
	protected abstract int allowedConsumption();
	
	/**
	 * Adds a simulatable to the waiting list.  It will be informed of resource
	 * availability once more resources become available.
	 * @param simulatable to add to the waiting list.
	 */
	public void addToWaitList(ISimulatable simulatable) {
		_waitingSimulatables.add( simulatable );
	}
}
