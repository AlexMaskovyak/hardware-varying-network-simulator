package network.entities;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import network.communication.IPacket;

import messages.ConnectionAdaptorMessage;
import messages.ConnectionMediumMessage;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Bidirectional, multicast connection.
 * @author Alex Maskovyak
 *
 */
public class ConnectionMedium 
		extends PerformanceRestrictedSimulatable 
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
	 * @see network.entities.IConnectionMedium#areConnected(network.entities.INode, network.entities.INode)
	 */
	@Override
	public boolean areConnected(INode node1, INode node2) {
		boolean node1Found = false;
		boolean node2Found = false;
		for( IConnectionAdaptor adaptor : getConnectedAdaptors() ) {
			node1Found = node1Found || adaptor.getConnectedNode().equals( node1 );
			node2Found = node2Found || adaptor.getConnectedNode().equals( node2 );
		}
		return node1Found && node2Found;
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
		send(null, packet);
	}
	
	
/// ISimulatable

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
		IMessage message = e.getMessage();
		if( message instanceof ConnectionMediumMessage ) {
			ConnectionMediumMessage cmMessage = (ConnectionMediumMessage)message;
			IPacket packet = cmMessage.getPacket();
			receive(packet);
		}
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
			if( !ca.getAddress().equals( packet.getSource() ) ) {
				getSimulator().schedule(
					new DefaultDiscreteScheduledEvent<IMessage>(
						(ISimulatable)this, 
						(ISimulatable)ca, 
						getSimulator().getTime() + getTransitTime(), 
						getSimulator(), 
						new ConnectionAdaptorMessage( packet )));
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
