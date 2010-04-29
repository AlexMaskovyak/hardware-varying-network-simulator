package computation.algorithms.clientSpecifiesNonRedundant;

import simulation.event.IDEvent;
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
}
