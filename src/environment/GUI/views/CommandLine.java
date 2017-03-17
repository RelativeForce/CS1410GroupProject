package environment.GUI.views;

import environment.model.Station;

/**
 * The <code>CommandLine</code> class displays information of the simulation
 * via the Console (<code>System.out</code>).
 * 
 * <p>
 * The <code>CommandLine</code> class implements the <code>SimulationView
 * </code> interface to allows for the status of simulation to be printed to the
 * console using <code>System.out</code>.
 * </p>
 * 
 * <p>
 * <strong>Cannot be extended.</strong>
 * </p>
 * 
 * @author 	John Berg
 * @version	15/03/2017
 * @since 	08/03/2017
 */
public final class CommandLine implements SimulatorView {
	
	/*
	 * Every simulation will print to the same console, so maybe make the
	 * CommandLine a singleton?
	 */
		
	/**
	 * Create a new <code>CommandLine</code> object.
	 * 
	 * <p>
	 * Initialise an instance of the <code>CommandLine</code> class, which
	 * allows for the status of a simulation to be displayed via the console.
	 * </p>
	 */
	public CommandLine(){
		
		//Print header
		System.out.println("");
		System.out.println("==================================");
	}
	/**
	 * Print the information of a simulation, including the time elapsed in the
	 * simulation and the status of the simulation's status.
	 * 
	 * @param time The amount of time that the simulation has been running.
	 * @param station The station to be represented.
	 */
	@Override
	public final void show(final int time, final Station station){
		
		System.out.println("Station status:");
		System.out.print(new StringBuilder()
				.append("Time: ")
				.append(time));
		System.out.println(station);
		System.out.println("----------------------------------");
	}
}
