package simulation;

/**
 * Queue that a Simulatable holds.
 * @author Alex Maskovyak
 *
 */
public interface ISimulatableEventQueue {

	/**
	 * Tests whether this TimeScalable can currently perform an operation or 
	 * not.
	 * @return true if an operation can be performed, false otherwise.
	 */
	public boolean canPerformOperation();
}
