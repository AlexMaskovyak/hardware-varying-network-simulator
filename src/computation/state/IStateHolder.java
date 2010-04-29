package computation.state;

/**
 * Possessor of an IState.  The internal state and behavior of a stateholder is
 * subject to the IState it possesses.
 * @author Alex Maskovyak
 *
 */
public interface IStateHolder<T extends IStateHolder<T>> {

	
	/**
	 * Alters the state of the stateholder.  A state will call this on its 
	 * holder to modify the holder.
	 * @param newState to assign to this state holder.
	 */
	public void setIState( IState<T> newState );
	
	/**
	 * Obtains the current state of the stateholder.
	 * @return the StateHolder's current state.
	 */
	public IState<T> getIState();
}
