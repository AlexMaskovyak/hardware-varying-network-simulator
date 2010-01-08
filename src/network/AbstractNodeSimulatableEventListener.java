package network;

import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;

public abstract class AbstractNodeSimulatableEventListener implements ISimulatableListener {

	@Override
	public void tickUpdate(ISimulatableEvent e) {
		if(e instanceof NodeSimulatableEvent) {
			tickUpdate((NodeSimulatableEvent)e);
		}
	}

	public abstract void tickUpdate(NodeSimulatableEvent e);
}
