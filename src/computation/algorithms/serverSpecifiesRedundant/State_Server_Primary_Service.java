package computation.algorithms.serverSpecifiesRedundant;

import java.util.List;
import java.util.Random;

import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.listeners.ISimulatableListener;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.algorithms.listeners.AlgorithmListener;
import computation.state.IState;
import computation.state.IStateHolder;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Primary_Service 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm>  {	

/// Fields
	
	/** server groups, data slice groups. */
	protected List<List<IAddress>> _serverGroups;
	/** total size of the data */
	protected int _dataSize;
	/** total number of data slices (servergroups) */
	protected int _dataSlices;
	/** total amount of data each slice handles. */
	protected int _dataPerSlice;

	/** delegate for service. */
	State_Server_Secondary_Service _serviceDelegate;
	/** random number generator. */
	public Random _rng;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 */
	public State_Server_Primary_Service( 
			List<List<IAddress>> serverGroups,
			int startIndex,
			int endIndex ) {
		_serverGroups = serverGroups;
		_dataSize = ( endIndex - startIndex ) + 1;
		_dataSlices = _serverGroups.size();
		_dataPerSlice = ( _dataSize / _dataSlices );		
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_serviceDelegate = new State_Server_Secondary_Service();
		_rng = new Random();
	}
	
	/**
	 * Overriden for the delegate we possess.
	 * @param stateHolder
	 */
	public void setStateHolder( ServerSpecifiesRedundantAlgorithm stateHolder ) {
		super.setStateHolder( stateHolder );
		_serviceDelegate.setStateHolder( stateHolder );
	}
	
/// Helper Methods
	
	/**
	 * Obtains the index of the servergroup that handles the data associated 
	 * with that index.
	 * @param dataIndex associated with the data.
	 * @return index of the servergroup which lists addresses which are to 
	 * receive the data.
	 */
	public int getServerGroupIndex( int dataIndex ) {
		return dataIndex / _dataPerSlice;
	}
	
	
/// IState

	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.clientSpecifiesNonRedundant.AbstractState#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEventDelegate(IDEvent event) {
		IMessage message = event.getMessage();
		if( message instanceof AlgorithmMessage ) {
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
			// case where the client requests a range of data
				case CLIENT_REQUESTS_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "PRIMARY_SERVICE", 0, 0, 0, 1, 0, 0) );
					
					// get the index of the servergroup
					int dataIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					int serverIndex = getServerGroupIndex(dataIndex);
					
					// get a server address
					List<IAddress> serverGroup = _serverGroups.get( serverIndex );
					int serverGroupIndex = _rng.nextInt( serverGroup.size() );
					IAddress server = serverGroup.get( serverGroupIndex );
					
					// did we select ourselves?
					if( server.equals( getStateHolder().getComputer().getAddress() ) ) {
						_serviceDelegate.handleEventDelegate( event );
					// give it to the server that we selected
					} else {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "PRIMARY_SERVICE", 0, 0, 1, 1, 0, 0) );
						sendMessageDownStack( aMessage, server );
					}					
					break;
				
				default: break;
			} 
		} else {
			_serviceDelegate.handleEventDelegate( event );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Server_Primary_Service" );
	}
}
