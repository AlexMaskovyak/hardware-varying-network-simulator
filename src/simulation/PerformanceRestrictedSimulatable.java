package simulation;

/**
 * Simulatables which wait and block upon a specific resource.  This resource
 * can be shared and modified by multiple parties.  An example of this would be
 * the "PerformanceResource" which restricts the number of Operations a piece of
 * hardware may perform and which is reset by the NetworkSimulator after every
 * "clock-cycle".
 * @author Alex Maskovyak
 *
 */
public class PerformanceRestrictedSimulatable 
		extends AbstractSimulatable
		implements ISimulatable {

	
	
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub

	}

}
