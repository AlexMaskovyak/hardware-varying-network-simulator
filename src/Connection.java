import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class Connection implements IConnection {

	protected List<INode> nodes;
	protected Queue<IData> bufferIn;
	protected Queue<IData> bufferOut;
	
	public Connection() {
		nodes = new ArrayList<INode>();
		bufferIn = new LinkedList<IData>();
		bufferOut = new LinkedList<IData>();
	}
	
	@Override
	public void connect(INode... nodes) {
		for( INode node : nodes ) {
			connect(node);
		}
	}

	@Override
	public void connect(INode node) {
		nodes.add(node);
	}
	
	@Override
	public void receive(IData data) {
		bufferIn.offer(data);
	}

	public void send() {
		IData data = bufferOut.poll();
		send(null, data);
	}
	
	@Override
	public void send(INode sender, IData data) {
		for(INode node : nodes) {
			if( node == sender ) {
				continue;
			}
			node.receive(data);
		}
	}
}
