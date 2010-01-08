package network;

import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;

public interface INodeSimulatableListener extends ISimulatableListener {

	@Override
	public void tickHandledUpdate(ISimulatableEvent e);

	public void receiveUpdate(NodeSimulatableEvent e);
	
	public void sendUpdate(NodeSimulatableEvent e);
}
