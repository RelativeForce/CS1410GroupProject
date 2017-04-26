package environment.GUI.views;

import environment.model.Station;

/**
 * The <code>SimulatorView</code> interface is used by classes
 * which deliver information about the status of the simulation.
 * 
 * <p>
 * Classes that implement the {@link SimulationView} interface
 * should implement all the methods in the {@link SimulationView}
 * interface.
 * </p>
 * 
 * @author John Berg
 * @author Joshua Eddy
 * @version	11/03/2017
 * @since	06/03/2017
 */
public interface SimulatorView {
	
	/**
	 * Show the status of a {@link Station} object.
	 * 
	 * <p>
	 * <strong>Must be overriden in classes that implement the
	 * {@link SimulationView}.</strong>
	 * </p>
	 * 
	 * @param time The time of elpased within the current {@link Station}.
	 * @param station The details of the {@link Station} status.
	 */
	public void show(final int time, final Station station);
	
	/**
	 * Set the {@link SimulatorView} to terminate.
	 * 
	 * <p>
	 * Flag the {@link SimulatorView} to terminate either when:
	 * <ul>
	 * 		<li>This method is called</li>
	 * 		<li>When the {@link SimulatorView} has finished execution</li>
	 * </ul>
	 * </p>
	 */
	public void setEnd();
}