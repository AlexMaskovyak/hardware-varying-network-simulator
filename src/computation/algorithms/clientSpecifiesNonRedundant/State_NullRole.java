package computation.algorithms.clientSpecifiesNonRedundant;

import network.communication.Address;
import network.communication.Packet;
import messages.ProtocolHandlerMessage;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_NullRole
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
			
				case SET_CLIENT: 
					int servers = (Integer)aMessage.getValue( AlgorithmMessage.SERVERS );
					updateStateHolder( new State_Client_FindServers( servers ) ); 
					break;
				case VOLUNTEER_REQUEST: 
					AbstractAlgorithm algorithm = getStateHolder();
					algorithm.sendEvent(
						(ISimulatable)algorithm.getLowerHandler(), 
						new ProtocolHandlerMessage( 
							ProtocolHandlerMessage.TYPE.HANDLE_HIGHER, 
							new Packet(
								new AlgorithmMessage( 
									AlgorithmMessage.TYPE.VOLUNTEER_REQUEST),
								algorithm.getComputer().getAddress(),
								Address.BROADCAST, 
								algorithm.getProtocol(),
								-1,
								-1
								), 
							algorithm ) );
					updateStateHolder( new State_Server_Volunteered() );
					break;
			}
		}
	}
}
