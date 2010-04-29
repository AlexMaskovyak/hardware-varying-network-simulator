package computation.state;

import simulation.event.IDEvent;

/**
 * Internal state for a state-holding class.
 * @author Alex Maskovyak
 *
 */
public interface IState<T extends IStateHolder> {

	/**
	 * Handles an event for a state.
	 * @param event
	 */
	public void handleEventDelegate( IDEvent event );
	
	/**
	 * Gets the state holder containing this state.
	 * @return the state holder who is holding us.
	 */
	public T getStateHolder();
	
	/**
	 * Sets the state holder.
	 * @param holder for this state.
	 */
	public void setStateHolder( T holder );
	
	/**
	 * Updates the state holder with the new state.
	 * @param state for the stateholder to contain.
	 */
	public void updateStateHolder( IState state );
}
