package computation.algorithms.clientSpecifiesNonRedundant;

import network.routing.IAddress;
import messages.StorageDeviceMessage;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * State where the server awaits storage requests from the Client.
 * @author Alex Maskovyak
 *
 */
public class State_Server_AwaitStorage 
		extends AbstractState
		implements IState<AbstractAlgorithm> {

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
			
				// store data for the client
				case CLIENT_REQUESTS_DATA_STORE: 
					AbstractAlgorithm algorithm = getStateHolder();
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_AWAIT_STORAGE", 0, 1, 0, 0, 1, 0) );
					
					StorageDeviceMessage store = 
						new StorageDeviceMessage( 
								StorageDeviceMessage.TYPE.STORE,
								StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE,
								(Integer)aMessage.getValue(AlgorithmMessage.INDEX),
								-1,
								(IData)aMessage.getValue(AlgorithmMessage.DATA) );
					sendEvent( 
						algorithm.getComputer().getHarddrive(), 
						store );
					sendEvent(
						algorithm.getComputer().getCache(),
						store );
					AlgorithmMessage ack = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_ACKNOWLEDGES);
					ack.setValue( AlgorithmMessage.INDEX, aMessage.getValue( AlgorithmMessage.INDEX ));
					sendMessageDownStack( 
						ack, 
						(IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS ) );
					
					
					
					break;
				// acknowledge done storing data, move to service state
				case CLIENT_INDICATES_DATA_STORE_COMPLETE:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_AWAIT_STORAGE", 0, 0, 1, 1, 0, 0) );
					
					updateStateHolder( new State_Server_Service() );
					AlgorithmMessage sendReady = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_READ_READY );
					sendReady.setValue( AlgorithmMessage.SERVER_ADDRESS, getStateHolder().getComputer().getAddress() );
					sendMessageDownStack( 
						sendReady, 
						(IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS ));
					
					AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
					doWork.setValue( AlgorithmMessage.AMOUNT, getStateHolder().getComputer().getCache().freeSpace() );
					sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
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
		return String.format( "State_Server_AwaitStorage" );
	}
}
