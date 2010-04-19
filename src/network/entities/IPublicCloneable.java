package network.entities;

/**
 * Fixes the Cloneable interface.
 * @author Alex Maskovyak
 *
 */
public interface IPublicCloneable extends Cloneable {

	public Object clone();
}
