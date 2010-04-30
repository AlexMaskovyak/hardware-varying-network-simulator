package computation.hardware;

import simulation.simulatable.PerformanceRestrictedSimulatable;

public class Cache 
		extends Harddrive 
		implements IHardware {
	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		Cache result = new Cache();
		result.setCapacity( this.getCapacity() );
		return result;
	}
}
