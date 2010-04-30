package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.GroupLayout.Alignment;

import network.routing.IAddress;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * Makes sure that the servers are ready so we can begin requesting data from
 * them.
 * @author Alex Maskovyak
 *
 */
public class State_Client_ConfirmServerReady 
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** servers we have found. */
	protected List<IAddress> _servers;
	/** servers that are ready. */
	protected List<IAddress> _readyServers;
	/** storage map between an address and a queue. */
	protected Map<IAddress, Queue<Integer>> _storageMap;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param servers from which to get ready confirmations.
	 */
	public State_Client_ConfirmServerReady( 
			List<IAddress> servers, 
			Map<IAddress, Queue<Integer>> storageMap ) {
		_servers = servers;
		_storageMap = storageMap;
		init();
	}
	
	/** externalize instantation. */
	protected void init() {
		_readyServers = new ArrayList<IAddress>( _servers.size() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.clientSpecifiesNonRedundant.AbstractState#handleEventDelegate(simulation.event.IDEvent)
	 */
	@Override
	public void handleEventDelegate(IDEvent event) {
		IMessage message = event.getMessage();
		if( message instanceof AlgorithmMessage ) {
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
				// tell all servers that we're through sending them info
				case DO_WORK:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "DISTR", 0, 0, 1, 1, 0, 0));
					
					// indices of the servers to tell
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.START_INDEX );
					int endIndex = (Integer)aMessage.getValue( AlgorithmMessage.END_INDEX );
					
					// ask if they are ready
					IAddress address = _servers.get( currentIndex );
					AlgorithmMessage storageComplete = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_INDICATES_DATA_STORE_COMPLETE );
					storageComplete.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					sendMessageDownStack( storageComplete, address );
					
					// if we haven't hit the end index, we need to do more work
					if( currentIndex < endIndex ) {
						System.out.println( "not done asking for confirm" + currentIndex + " " + endIndex );
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.START_INDEX, ++currentIndex );
						System.out.println( currentIndex );
						doWork.setValue( AlgorithmMessage.END_INDEX, endIndex );
						sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
					}
					break;
				// response that its ready...
				case SERVER_INDICATES_READ_READY:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "DISTR", 0, 0, 1, 1, 0, 0));
					
					// add it
					_readyServers.add( (IAddress) aMessage.getValue( AlgorithmMessage.SERVER_ADDRESS ) );
					// if the sizes are equal everyone is ready
					if( _readyServers.size() == _servers.size() ) {
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						//doWork.setValue( AlgorithmMessage.START_INDEX, 0 );
						//doWork.setValue( AlgorithmMessage.END_INDEX, getStateHolder().getDataAmount() - 1  );
						
						updateStateHolder( new State_Client_Read( _servers, _storageMap ) );
						sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
					}
					break;
				default: break;
			}
		}

	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Client_ConfirmServerReady" );
	}
}
