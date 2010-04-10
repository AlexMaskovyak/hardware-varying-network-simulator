package computation.hardware;

import computation.IData;

import simulation.simulatable.ISimulatable;

/**
 * Describes computer hardware.
 * @author Alex Maskovyak
 *
 */
public interface IHardware 
		extends ISimulatable {

// Operational
	
	/**
	 * Receive information and place it into storage.
	 * @param data to receive.
	 */
	public abstract void receive(IData data);
	
	/**
	 * Send information out.
	 * @param data to send.
	 */
	public abstract void send(IData data);

// Performance affecting
	
	/**
	 * Gets the bandwidth in #n quantity units / time, approximately how many 
	 * items can be scheduled per time event.
	 * @return bandwidth #n quantity units / time
	 */
	public abstract int getBandwidth();
	
	/**
	 * Sets the bandwidth in #n quantity units / time.
	 * @param bandwidth #n quantity units / time, approximately how many items
	 * can be scheduled per time event.
	 */
	public abstract void setBandwitdh( int bandwidth );
	
	/**
	 * Gets the speed, affecting how far in the future items can be scheduled.
	 * @return speed.
	 */
	public int getSpeed();
	
	/**
	 * Sets the speed.
	 * @param speed which affects how far in the future items can be scheduled.
	 */
	public void setSpeed(int speed);
}
