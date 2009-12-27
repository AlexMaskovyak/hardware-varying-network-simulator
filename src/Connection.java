import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Connection implements IConnection {

	protected List<INode> nodes;
	protected Stack<IData> buffer;
	
	public Connection() {
		nodes = new ArrayList<INode>();
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
		buffer.push(data);
	}

	@Override
	public void send(INode sender, IData data) {
		for(INode node : nodes) {
			node.receive(data);
		}
	}
}
