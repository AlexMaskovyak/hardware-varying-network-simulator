package timing;

/**
 * Scales TimeScalable objects so that they perform only one operations per
 * tick in the environment.
 * @author Alex Maskovyak
 *
 */
public interface IOperationTimeScaler {

	/**
	 * Registers a TimeScalable object with this Scaler causing it to be added
	 * to the TimeScalables pool.  TimeScalables added to the pool will have
	 * their operations scaled such that they will perform only one per 
	 * time-event.
	 * @param timeScalable to add to this management pool.
	 */
	public void register(ITimeScalable timeScalable);
	
	/**
	 * Unregisters a TimeScalable object with this Scaler causing it to be
	 * removed from the TimeScalables pool.  TimeScalables that remain in the 
	 * pool should have their operations rescaled such that they will perform
	 * only one per time-event.
	 * @param timeScalable to remove from this management pool.
	 */
	public void unregister(ITimeScalable timeScalable);
	
	/**
	 * Rescales all registered items to meet the criteria above.
	 */
	public void rescale();
}
