package visual;

class SimulationError extends RuntimeException{
	/**
	 * This will write the error message if there is a simulation error
	 */
	private static final long serialVersionUID = 1L;

	public SimulationError(String msg) {
        super(msg);
    }
}