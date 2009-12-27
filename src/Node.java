import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Stack;
import java.util.UUID;


/**
 * 
 * @author Alex Maskovyak
 *
 */
public class Node implements INode, ISimulatable {

	protected List<IConnection> connections;
	protected Stack<IData> buffer;
	
	protected String id;
	
	public Node() {
		connections = new ArrayList<IConnection>();
		id = UUID.randomUUID().toString();
	}
	
	@Override
	public void connect(IConnection connect) {
		// TODO Auto-generated method stub
		connections.add(connect);
		connect.connect(this);
	}

	@Override
	public void receive(IData data) {
		buffer.push(data);
	}

	@Override
	public void send(IData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(IData data, IConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleTickEvent(EventObject o) {
		// TODO Auto-generated method stub
		System.out.printf("Got a tick! %s", id);
	}

}
