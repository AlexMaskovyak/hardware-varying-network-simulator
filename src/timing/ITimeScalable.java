package timing;

/**
 * Device which can perform some # operations per time event.  This device can
 * be scaled such that they only perform an operation every n time events so 
 * that this device and every other device in a pool of Scalables will only
 * ever be atomic.
 * @author Alex Maskovyak
 *
 */
public interface ITimeScalable {

	/**
	 * Sets the number of ticks that must be received before this Scalable
	 * can perform an operation.
	 * @param ticks required between the performing of a consecutive 
	 * operations.
	 */
	public void scaleTickResponse(int ticks);
	
	/**
	 * Gets the number of ticks that must be received before this Scalable
	 * can perform an operation.
	 * @return ticks required between the performing of consecutive operations.
	 */
	public int getScaledPerformance();
	
	/**
	 * Gets the number of operations per time unit that this device can perform
	 * before scaling flattens it to 1 per operation.
	 * @return number of baseline operations per time unit.
	 */
	public int getBaselinePerformance();
	
	/**
	 * Tests whether this TimeScalable can currently perform an operation or 
	 * not.
	 * @return true if an operation can be performed, false otherwise.
	 */
	public boolean canPerformOperation();
}
