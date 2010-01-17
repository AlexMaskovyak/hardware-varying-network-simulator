package network;

public class ConnectionHandler {

	protected RoutingTable _table;
	protected IAddress _address;
	
	public ConnectionHandler(IAddress address) {
		_address = address;
		_table = new RoutingTable();
	}
	
	public void install(INode node, IConnection connection) {
		
	}
	
}
