package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import network.routing.IAddress;

import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.hardware.Harddrive;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_Read
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** iterator for servers. */
	protected Iterator<IAddress> _iterator;
	/** ending index for data. */
	protected int _endIndex;
	
	/** servers we have found. */
	protected List<IAddress> _servers;
	
	/** storage map. */
	protected Map<IAddress, Queue<Integer>> _storageMap; 
	
	/** the address of the server we are sending to. */
	protected IAddress _currentAddress;
	/** index of data. */
	protected Integer _currentIndex;
	
	
/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_Read( List<IAddress> servers, Map<IAddress, Queue<Integer>> storageMap ) {
		_storageMap = storageMap;
		_servers = servers;
		_iterator = servers.iterator();
	}
	
	/**
	 * Updates the address and index requests values for the requesting phase.
	 */
	public void updateRequestInfo() {
		int emptyLists = 0;
		// do while we haven't exhausted all lists
		// and we haven't yet gotten our values.
		while( emptyLists <= _servers.size() && _currentAddress == null && _currentIndex == null ) {
			// update iterator if no next
			if( !_iterator.hasNext() ) {
				_iterator = _servers.iterator();
			}
			
			_currentAddress = _iterator.next();
			_currentIndex = _storageMap.get( _currentAddress ).poll();
			
			// if null then the index is empty
			if( _currentIndex == null ) {
				emptyLists++;
			}
		}
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
				// requesting
				case DO_WORK:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 0, 1, 0, 0, 0) );
					
					// update info for requests
					updateRequestInfo();
					
					// check if there is anything to send, 
					// if there is send it and schedule more work
					if( _currentIndex == null ) {
						return;
					}

					// create a request for the current index
					AlgorithmMessage dataRequest = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA );
					dataRequest.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					dataRequest.setValue( AlgorithmMessage.INDEX, _currentIndex );
					
					// send it round-robin style
					sendMessageDownStack( dataRequest, _currentAddress );

					_currentAddress = null;
					_currentIndex = null;
					
					AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
					sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
					
					//System.out.println( _currentIndex );
					
					break;
				// getting information
				case SERVER_RESPONDS_WITH_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 1, 0, 0, 0, 0));
					
					IData data = (IData)aMessage.getValue( AlgorithmMessage.DATA );
					int index = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					IAddress server = (IAddress)aMessage.getValue( AlgorithmMessage.SERVER_ADDRESS );
					
					// this needn't be done in the traditional way through an 
					// event since this is only here to demonstrate correctness.
					Harddrive harddrive = getStateHolder().getComputer().getHarddrive();
					IData baseLine = harddrive.getIndex( index );
					if( baseLine == null ) {
						//System.out.printf( "%d is a bad index. Data from: %s. Awaiting %d pieces.\n", index, server, harddrive.getSize() );
					} else {
						if( baseLine.equals( data ) ) {
							//System.out.println( "We got good data! " + data );
							harddrive.deleteIndex( index );
							// System.out.printf( "We received good data %s.  Awaiting %d pieces.\n", data, harddrive.getSize() );
						} else {
							System.out.printf( "We received bad data! Expected: %s. Received %s. From %s. Awaiting %d pieces.\n", baseLine, data, server, harddrive.getSize() );
						}
					}
					
					
					// if we've deleted everything, we've received all data
					// and it matched baseline.
					// again, this is not an event since this is more for
					// simulation than actual algorithmic operation
					if( harddrive.getSize() == 0 ) {
						getStateHolder().getSimulator().stop();
						Thread.currentThread().interrupt();
					}
					
					// end condition
					break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Client_Read" );
	}
}