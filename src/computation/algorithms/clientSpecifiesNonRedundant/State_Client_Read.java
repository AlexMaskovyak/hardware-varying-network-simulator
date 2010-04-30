package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.Iterator;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import messages.StorageDeviceMessage;
import network.routing.IAddress;

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

	/** servers we have found. */
	protected List<IAddress> _servers;
	/** iterator for servers. */
	protected Iterator<IAddress> _iterator;
	/** index of data. */
	protected int _currentIndex;
	/** ending index for data. */
	protected int _endIndex;
	
	
/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_Read( List<IAddress> servers ) {
		_servers = servers;
		_iterator = servers.iterator();
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
					
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.START_INDEX );
					
					// round robin server select
					if( !_iterator.hasNext() ) {
						_iterator = _servers.iterator();
					}
					
					// create a request for the current index
					AlgorithmMessage dataRequest = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA );
					dataRequest.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					dataRequest.setValue( AlgorithmMessage.INDEX, currentIndex );
					
					// send it round-robin style
					sendMessageDownStack( dataRequest, _iterator.next() );

					// keep sending the data
					if( currentIndex < _endIndex ) {
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.START_INDEX, 0 );
						doWork.setValue( AlgorithmMessage.END_INDEX, _servers.size() - 1 );
						sendEvent( getStateHolder(), doWork );
					}
					
					break;
				// getting information
				case SERVER_RESPONDS_WITH_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 1, 0, 0, 0, 0));
					
					IData data = (IData)aMessage.getValue( AlgorithmMessage.DATA );
					int index = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					
					Harddrive harddrive = getStateHolder().getComputer().getHarddrive();
					IData baseLine = harddrive.getIndex( index );
					if( baseLine.equals( data) ) {
						harddrive.deleteIndex( index );
					} else {
						System.out.println( "We received bad data!" );
					}
					
					// if we've deleted everything, we've received all data
					// and it matched baseline.
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