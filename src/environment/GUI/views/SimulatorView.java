package environment.GUI.views;

/**
 * The <code>SimulatorView</code> interface is used by classes which deliver
 * information about the status of the simulation.
 * 
 * <p>
 * Classes that implement the {@link SimulationView} interface should implement
 * all the methods in the {@link SimulationView} interface.
 * </p>
 * 
 * @author John_Berg
 * @author Joshua_Eddy
 * @version 11/03/2017
 * @since 06/03/2017
 * @param <T>
 *            The type in use by the {@link SimulatorView}
 */
public interface SimulatorView<T> {

	/**
	 * Show the status of a {@link T} object.
	 * 
	 * <p>
	 * <strong>Must be overridden in classes that implement the
	 * {@link SimulationView}.</strong>
	 * </p>
	 * 
	 * @param time
	 *            The time of elapsed within the current {@link T} object.
	 * @param t
	 *            The details of the object which is an instance of {@link T}
	 *            status.
	 */
	public void show(final int time, final T t);

	/**
	 * Set the {@link SimulatorView} to terminate.
	 * 
	 * <p>
	 * Flag the {@link SimulatorView} to terminate either when:
	 * <ul>
	 * <li>This method is called</li>
	 * <li>When the {@link SimulatorView} has finished execution</li>
	 * </ul>
	 * </p>
	 */
	public void setEnd();
}