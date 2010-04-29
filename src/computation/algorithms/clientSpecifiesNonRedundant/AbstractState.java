package computation.algorithms.clientSpecifiesNonRedundant;

import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;
import computation.state.IStateHolder;

/**
 * Basis behind states.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractState
		implements IState<AbstractAlgorithm> {

	/** stateholder that possesses us. */
	protected AbstractAlgorithm _holder;
	
/// IState
	
	/*
	 * (non-Javadoc)
	 * @see computation.state.IState#getStateHolder()
	 */
	@Override
	public AbstractAlgorithm getStateHolder() {
		return _holder;
	}

	public abstract void handleEventDelegate(IDEvent event);
	
	/*
	 * (non-Javadoc)
	 * @see computation.state.IState#setStateHolder(computation.state.IStateHolder)
	 */
	@Override
	public void setStateHolder(AbstractAlgorithm holder) {
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
	 * Convenience to the algorithm's send message down stack.
	 * @param message to send.
	 * @param destination address to which to send it.
	 */
	public void sendMessageDownStack( IMessage message, IAddress destination ) {
		getStateHolder().sendMessageDownStack(message, destination);
	}
}
