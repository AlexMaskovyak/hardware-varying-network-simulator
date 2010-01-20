package network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import simulation.AbstractSimulatable;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;

public class Node2 extends AbstractSimulatable implements ISimulatable {

	/** adaptors connect us to other network elements. */
	protected List<ConnectionAdaptor> _adaptors;
	/** our identifier */
	protected String _id;
	
	/**
	 * Default constructor.
	 */
	public Node2() {
		this(UUID.randomUUID().toString());
	}
	
	/**
	 * Constructor.
	 * @param id to use for this Node.
	 */
	public Node2(String id) {
		super();
		_id = id;
	}
	
	@Override
	protected void init() {
		super.init();
		_adaptors = new ArrayList<ConnectionAdaptor>();
	}
	
	/**
	 * Obtains this Node's id.
	 * @return identifier for this Node.
	 */
	public String getId() {
		return _id;
	}

	/**
	 * 
	 * @param adaptor
	 */
	public void installAdapator(ConnectionAdaptor adaptor) {
		_adaptors.add(adaptor);
	}
}
