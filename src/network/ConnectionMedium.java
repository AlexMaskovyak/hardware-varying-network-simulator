package network;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.AbstractSimulatable;

/**
 * Bidirectional, multicast connection.
 * @author Alex Maskovyak
 *
 */
public class ConnectionMedium 
		extends AbstractSimulatable 
		implements IConnectionMedium, ISimulatable {
	
/// Fields
	
	/** connected to connectionadapators. */
	protected Collection<IConnectionAdaptor> _adaptors;	
	/** determines whether this connection can receive/send packets. */
	protected boolean _active;

/// Construction.
	
	/**
	 * Default constructor.
	 */
	public ConnectionMedium() {
		super();
	}
	
	/** Externalize instantiations. */
	protected void init() {
		super.init();
		_listeners = new HashSet<ISimulatableListener>();
		_adaptors = new HashSet<IConnectionAdaptor>();
		_active = true;
	}
	
/// IConnectionMedium
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#connect(network.IConnectionAdaptor[])
	 */
	@Override
	public void connect(IConnectionAdaptor... adaptors) {
		for( IConnectionAdaptor adaptor : adaptors ) {
			connect(adaptor);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#connect(network.IConnectionAdaptor)
	 */
	@Override
	public void connect(IConnectionAdaptor adaptor) {
		_adaptors.add(adaptor);
	}

	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#disconnect(network.IConnectionAdaptor)
	 */
	@Override
	public void disconnect(IConnectionAdaptor adaptor) {
		_adaptors.remove(adaptor);
	}

	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#getConnectedAdaptors()
	 */
	@Override
	public IConnectionAdaptor[] getConnectedAdaptors() {
		IConnectionAdaptor[] adaptors = new IConnectionAdaptor[_adaptors.size()];
		adaptors = _adaptors.toArray(adaptors);
		return adaptors;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#receive(network.IPacket)
	 */
	@Override
	public void receive(IPacket packet) {
		System.out.printf("Connection got %d\n", packet.getSequence());
	}
	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleTickEvent(simulation.ISimulatorEvent)
	 */
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		//send();
		// TODO 
		super.handleTickEvent(o);
	}

	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#receive(network.IConnectionAdaptor, network.IPacket)
	 */
	@Override
	public void receive(IConnectionAdaptor sender, IPacket packet) {
		send(sender, packet);
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#canPerformOperation()
	 */
	@Override
	public boolean canPerformOperation() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionMedium#send(network.IConnectionAdaptor, network.IPacket)
	 */
	@Override
	public void send(IConnectionAdaptor sender, IPacket packet) {
		for( IConnectionAdaptor ca : _adaptors ) {
			if( ca != sender ) {
				ca.receive(packet);
			}
		}
	}

	
/// Equality
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if( object instanceof IConnectionMedium ) {
			return equals((IConnectionMedium)object);
		}
		return false;
	}
	
	/**
	 * Equality with another ConnectionMedium.
	 * @param medium to which to compare.
	 * @return true if the two ConnectionMediums are equivalent in adaptors
	 * connected, false otherwise.
	 */
	public boolean equals(ConnectionMedium medium) {
		return equals((IConnectionMedium)medium);
	}
	
	/**
	 * Equality with another IConnectionMedium.
	 * @param medium to which to compare.
	 * @return true if the two IConnectionMediums are equivalent in adapators
	 * connected, false otherwise.
	 */
	public boolean equals(IConnectionMedium medium) {
		Set<IConnectionAdaptor> theirConnectionAdaptors = new HashSet<IConnectionAdaptor>();
		for( IConnectionAdaptor theirAdaptor : medium.getConnectedAdaptors() ) {
			theirConnectionAdaptors.add( theirAdaptor );
		}
		
		return _adaptors.equals(theirConnectionAdaptors);
	}
}
