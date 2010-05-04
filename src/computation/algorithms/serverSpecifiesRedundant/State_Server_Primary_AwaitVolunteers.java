package computation.algorithms.serverSpecifiesRedundant;

import java.util.ArrayList;
import java.util.List;

import network.routing.IAddress;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;


/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Primary_AwaitVolunteers 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** address of the client. */
	protected IAddress _clientAddress;
	/** the address of the computer upon which we were built. */
	protected IAddress _ourAddress;
	/** number of servers. */
	protected int _serverAmount;
	/** redundancy of data for storage on servers */
	protected int _redundancy;
	/** server groups, data slice groups. */
	protected List<List<IAddress>> _serverGroups;
	/** servers we have so far. */
	protected int _volunteersFound;
	/** starting index for data. */ 
	protected int _startIndex;
	/** ending index for data. */
	protected int _endIndex;
	/** amount of data for a server to store. */
	protected int _dataPerSlice;
	/** size of all data. */
	protected int _dataSize;
	/** amount of data the last server stores. */
	protected int _dataForLastSlice;
	/** total number of data slices. */
	protected int _dataSlices;
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param clientAddress 
	 * @param serverAmount to use for dataslices (number of data slices).
	 * @param redundancy how many copies of each dataslice to hold
	 */
	public State_Server_Primary_AwaitVolunteers( 
			IAddress clientAddress, 
			IAddress ourAddress,
			int serverAmount, 
			int redundancy,
			int startIndex,
			int endIndex ) {
		_clientAddress = clientAddress;
		_ourAddress = ourAddress;
		_serverAmount = serverAmount;
		_redundancy = redundancy;
		_startIndex = startIndex;
		_endIndex = endIndex;
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_serverGroups = new ArrayList<List<IAddress>>( _serverAmount );
		for( int i = 0; i < _serverAmount; ++i ) {
			_serverGroups.add( new ArrayList<IAddress>( _redundancy ) );
		}		
		// we're a part of the first group
		_serverGroups.get( 0 ).add( _ourAddress );
		_volunteersFound = 1;
		_dataSlices = _serverAmount;
		_dataSize = ( _endIndex - _startIndex ) + 1;
		_dataPerSlice =  (int) Math.floor( ( _dataSize ) / _serverAmount );
		_dataForLastSlice = ( _dataPerSlice ) + ( _dataSize - ( _dataSlices * _dataPerSlice ));
		
	}
	
	/**
	 * Gets the next server group index.
	 * @return the next server group index to store an address.
	 */
	public int nextServerGroupIndex() {
		int index = _volunteersFound % _serverAmount;
		return ( index == _serverGroups.size() ) ? index - 1: index;
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
				// a server has volunteered
				case SERVER_VOLUNTEERS: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_VOLUNTEERS", 0, 0, 1, 1, 0, 0) );
					
					// tally it
					IAddress volunteerAddress = (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS );
					
					// indicate that they are a server now
					int index = nextServerGroupIndex();
					_serverGroups.get( index ).add( volunteerAddress );
					
					
					// tell them
					AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_ACCEPTS_VOLUNTEER_AS_SECONDARY );
					response.setValue( AlgorithmMessage.SERVER_ADDRESS, getStateHolder().getComputer().getAddress() );
					// set the amount they are to store,
					// the servers managing the last slice may have a different value...
					response.setValue( 
							AlgorithmMessage.DATA_AMOUNT, 
							( index == _serverGroups.size() ) ? _dataForLastSlice : _dataPerSlice );
					sendMessageDownStack( response, volunteerAddress );
					
					_volunteersFound++;
					
					if( _volunteersFound == _serverAmount * _redundancy ) {	
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_VOLUNTEERS", 0, 0, 1, 0, 0, 0) );
						updateStateHolder( new State_Server_Primary_AwaitStorage( _serverGroups, _clientAddress, _redundancy, _startIndex, _endIndex ) );
						
						// got all volunteers
						sendMessageDownStack(
							new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_STORE_READY ), 
							_clientAddress );
					}
					
					break;
				// check to see if we only need one volunteer
				case DO_WORK:
					// have we found all that we need?
					if( _volunteersFound == _serverAmount * _redundancy ) {	
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_VOLUNTEERS", 0, 0, 1, 0, 0, 0) );
						updateStateHolder( new State_Server_Primary_AwaitStorage( _serverGroups, _clientAddress, _redundancy, _startIndex, _endIndex ) );
						sendMessageDownStack(
							new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_STORE_READY ), 
							_clientAddress );
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
		return String.format( "State_Server_AwaitVolunteers" );
	}
}
