
/**
 * An atomic network element.  Represents an information source and sink.
 * 
 * @author Alex Maskovyak
 *
 */
public interface INode {

	public void connect(IConnection connect);
	
	public void receive(IData data);
}
