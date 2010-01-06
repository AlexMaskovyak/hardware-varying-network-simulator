package computation;

import network.IData;

/**
 * Describes computer hardware.
 * @author Alex Maskovyak
 *
 */
public interface IHardware {

	/**
	 * Receive information and place it into storage.
	 * @param data to receive.
	 */
	public void receive(IData data);
	
	/**
	 * Send information out.
	 * @param data to send.
	 */
	public void send(IData data);
}
