package computation;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;

public class CPU extends AbstractSimulatable implements IHardware, ISimulatable {

	protected int _speed;
	
	public CPU() {
		super();
	}
	
	public void setSpeed(int speed) {
		_speed = speed;
	}

	@Override
	public void receive(IData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(IData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}
}
