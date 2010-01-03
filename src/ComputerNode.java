import javax.management.OperationsException;

/**
 * Simulatable Computer Node which has modular hardware components.
 * 
 * @author Alex Maskovyak
 *
 */
public class ComputerNode extends NodeOld implements IComputer {

	/** Algorithm to run. */
	protected IAlgorithm _algorithm;
	
	@Override
	public void install(IAlgorithm algorithm) {
		_algorithm = algorithm;
	}

	@Override
	public void start() throws OperationsException {
		if( _algorithm == null ) {
			throw new OperationsException("An algorithm must be installed for computing to start.");
		}
		_algorithm.distribute();
		_algorithm.read();
	}

	@Override
	public void uninstall() {
		_algorithm = null;
	}

}
