package environment.GUI.views;

import environment.model.Station;

/**
 * The <code>CommandLine</code> class displays information of the simulation
 * via the Console (<code>System.out</code>).
 * 
 * <p>
 * The <code>CommandLine</code> class implements the <code>SimulationView
 * </code> interface ...
 * </p>
 * 
 * <p>
 * <strong>Cannot be extended.</strong>
 * </p>
 * 
 * @author 	John Berg
 * @version	08/03/2017
 * @since 	08/03/2017
 */
public final class CommandLine implements SimulatorView {
		
	/**
	 * Create a new <code>CommandLine</code> 
	 */
	public CommandLine(){
		
		//Print header
		System.out.println("");
		System.out.println("==================================");
	}
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
