package environment.GUI.views;

import environment.model.Station;

/**
 * The <code>SimulatorView</code> interface 
 * 
 * 
 * @author John Berg
 * @author Joshua Eddie
 * @version	08/03/2017
 * @since	06/03/2017
 */
public interface SimulatorView {
	
	/**
	 * 
	 * 
	 * @param station The details of ...
	 */
	public void show(final Station station);
}