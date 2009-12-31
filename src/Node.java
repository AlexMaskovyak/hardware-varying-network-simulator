import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.UUID;


/**
 * 
 * @author Alex Maskovyak
 *
 */
public class Node implements INode, ISimulatable {

	protected List<IConnection> _connections;
	protected Queue<IData> _bufferIn;
	protected Queue<IData> _bufferOut;
	
	protected String _id;
	
	public Node() {
		_connections = new ArrayList<IConnection>();
		_id = UUID.randomUUID().toString();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();
	}
	
	@Override
	public void connect(IConnection connect) {
		// TODO Auto-generated method stub
		_connections.add(connect);
		connect.connect(this);
	}

	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
	}

	public void send() {
		send(_bufferOut.poll());
	}
	
	@Override
	public void send(IData data) {
		for( IConnection connection : _connections ) {
			send(data, connection);
		}
	}

	@Override
	public void send(IData data, IConnection connection) {
		connection.send(this, data);		
	}

	@Override
	public void handleTickEvent(EventObject o) {
		// TODO Auto-generated method stub
		System.out.printf("Got a tick! %s", _id);
	}

}
