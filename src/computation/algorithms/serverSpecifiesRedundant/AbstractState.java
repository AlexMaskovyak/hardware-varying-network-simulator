package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;

/**
 * Basis behind states.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractState<T extends AbstractAlgorithm>
		implements IState<T> {

/// Fields
	
	/** stateholder that possesses us. */
	protected T _holder;
	
	
/// IState
	
	/*
	 * (non-Javadoc)
	 * @see computation.state.IState#getStateHolder()
	 */
	@Override
	public T getStateHolder() {
		return _holder;
	}

	public abstract void handleEventDelegate(IDEvent event);
	
	/*
	 * (non-Javadoc)
	 * @see computation.state.IState#setStateHolder(computation.state.IStateHolder)
	 */
	@Override
	public void setStateHolder(T holder) {
		_holder = holder;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.state.IState#updateStateHolder(computation.state.IState)
	 */
	@Override
	public void updateStateHolder(IState state) {
		_holder.setIState( state );
	}
	

/// Convenience
	
	/**
	 * Convenience to the algorithms send event.
	 * @param destination to which to send the event.
	 * @param message to send.
	 */
	public void sendEvent( ISimulatable destination, IMessage message ) {
		getStateHolder().sendEvent( destination, message );
	}
	
	/**
	 * Sends a instant event to ourselves.
	 * @param message to send to our self.
	 */
	public void sendInstantEvent( IMessage message ) {
		getStateHolder().sendEvent( getStateHolder(), message, 0.000000001, DEvent.INTERNAL );
	}
	
	/**
	 * Convenience to the algorithms send event.
	 * @param destination to which to send the event.
	 * @param message to send.
	 * @param priority of the message.
	 */
	public void sendEvent( ISimulatable destination, IMessage message, int priority ) {
		getStateHolder().sendEvent( destination, message, priority );
	}
	
	/**
	 * Convenience to the algorithm's send message down stack.
	 * @param message to send.
	 * @param destination address to which to send it.
	 */
	public void sendMessageDownStack( IMessage message, IAddress destination ) {
		getStateHolder().sendMessageDownStack( message, destination );
	}
}
