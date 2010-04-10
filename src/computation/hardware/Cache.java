package computation.hardware;

import computation.IData;

import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;

public class Cache 
		extends PerformanceRestrictedSimulatable 
		implements IHardware {
	
	protected int _capacity;
	protected int _speed;
	
	public Cache() { super(); }
	
	public int getCapacity() {
		return _capacity;
	}
	
	public void setCapacity(int capacity) {
		_capacity = capacity;
	}
	
	public int getSpeed() {
		return _speed;
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
	public void handleEventDelegate(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBandwidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBandwitdh(int bandwidth) {
		// TODO Auto-generated method stub
		
	}
}
