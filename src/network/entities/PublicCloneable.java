package network.entities;

/**
 * Fixes the Cloneable interface.
 * @author Alex Maskovyak
 *
 */
public interface PublicCloneable extends Cloneable {

	public Object clone();
}
