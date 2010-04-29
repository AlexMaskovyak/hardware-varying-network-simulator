package computation.hardware;

import computation.IData;

import simulation.event.IDEvent;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;

public class CPU 
		extends PerformanceRestrictedSimulatable 
		implements IHardware, ISimulatable {

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
	public void handleEvent(IDEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBandwidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBandwitdh(int bandwidth) {
		// TODO Auto-generated method stub
		
	}
}
