package network.listeners;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

public interface INodeSimulatableListener extends ISimulatableListener {

	@Override
	public void tickHandledUpdate(ISimulatableEvent e);

	public void receiveUpdate(NodeSimulatableEvent e);
	
	public void sendUpdate(NodeSimulatableEvent e);
}
