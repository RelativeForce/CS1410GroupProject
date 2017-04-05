package environment.GUI.views;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import environment.model.Station;
import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

/**
 * The <code>CommandLine</code> class displays information of the simulation via
 * the Console (<code>System.out</code>).
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
 * @author John_Berg
 * @author Joshua_Eddy
 * 
 * @version 05/04/2017
 * @since 08/03/2017
 */
public final class CommandLine implements SimulatorView {

	/*
	 * Every simulation will print to the same console, so maybe make the
	 * CommandLine a singleton?
	 */

	/**
	 * Represents whether the {@link CommandLine} should display the final more
	 * detailed view of the {@link Station}. When this is set to
	 * <code>false</code>, this {@link SimulatorView} will display the real time
	 * tick details but if this is set to <code>true</code> then the
	 * {@link SimulatorView} will display the final view.
	 * 
	 * @see environment.model.Station
	 * @see environment.Simulator
	 */
	private boolean end;

	/**
	 * Create a new <code>CommandLine</code> object.
	 * 
	 * <p>
	 * Initialise an instance of the <code>CommandLine</code> class, which
	 * allows for the status of a simulation to be displayed via the console.
	 * </p>
	 */
	public CommandLine() {

		this.end = false;

		// Print header
		System.out.println("");
		System.out.println("==================================");
	}

	/**
	 * Print the information of a simulation, including the time elapsed in the
	 * simulation and the status of the simulation's status.
	 * 
	 * @param time
	 *            The amount of time that the simulation has been running.
	 * @param station
	 *            The station to be represented.
	 */
	@Override
	public final void show(final int time, final Station station) {

		// If the simulation has ended.
		if (this.end) {

			DecimalFormat money = new DecimalFormat("#.##");
			money.setRoundingMode(RoundingMode.CEILING);
			DecimalFormat integer = new DecimalFormat("####");
			integer.setRoundingMode(RoundingMode.CEILING);

			// Display the final ticks details.

			System.out.println("Simulation Results:");
			System.out.println("----------------------------------");
			System.out.println(
					"Vehicle Type\tProcessed\tRejected\tFuel Profit\tSales Profit\tLost Fuel Profit\tLost Sales Profit");

			// Processed and Rejected
			System.out.println(
					"Small Car\t" + integer.format(station.getRoadUsersProcessed().get(SmallCar_RoadUser.class))
							+ "\t\t" + integer.format(station.getRoadUsersRejected().get(SmallCar_RoadUser.class))
							+ "\t\t£" + money.format(station.getFuelProfit().get(SmallCar_RoadUser.class)) + "\t\t£"
							+ money.format(station.getSalesProfit().get(SmallCar_RoadUser.class)) + "\t\t£"
							+ money.format(station.getLostFuelProfit().get(SmallCar_RoadUser.class)) + "\t\t\t£"
							+ money.format(station.getLostSalesProfit().get(SmallCar_RoadUser.class)));

			System.out.println(
					"Family Sedan\t" + integer.format(station.getRoadUsersProcessed().get(FamilySedan_RoadUser.class))
							+ "\t\t" + integer.format(station.getRoadUsersRejected().get(FamilySedan_RoadUser.class))
							+ "\t\t£" + money.format(station.getFuelProfit().get(FamilySedan_RoadUser.class)) + "\t\t£"
							+ money.format(station.getSalesProfit().get(FamilySedan_RoadUser.class)) + "\t\t£"
							+ money.format(station.getLostFuelProfit().get(FamilySedan_RoadUser.class)) + "\t\t\t£"
							+ money.format(station.getLostSalesProfit().get(FamilySedan_RoadUser.class)));

			System.out.println(
					"Motorbike\t" + integer.format(station.getRoadUsersProcessed().get(Motorbike_RoadUser.class))
							+ "\t\t" + integer.format(station.getRoadUsersRejected().get(Motorbike_RoadUser.class))
							+ "\t\t£" + money.format(station.getFuelProfit().get(Motorbike_RoadUser.class)) + "\t\t£"
							+ money.format(station.getSalesProfit().get(Motorbike_RoadUser.class)) + "\t\t£"
							+ money.format(station.getLostFuelProfit().get(Motorbike_RoadUser.class)) + "\t\t\t£"
							+ money.format(station.getLostSalesProfit().get(Motorbike_RoadUser.class)));

			System.out.println("Truck\t\t" + integer.format(station.getRoadUsersProcessed().get(Truck_RoadUser.class))
					+ "\t\t" + integer.format(station.getRoadUsersRejected().get(Truck_RoadUser.class)) + "\t\t£"
					+ money.format(station.getFuelProfit().get(Truck_RoadUser.class)) + "\t\t£"
					+ money.format(station.getSalesProfit().get(Truck_RoadUser.class)) + "\t\t£"
					+ money.format(station.getLostFuelProfit().get(Truck_RoadUser.class)) + "\t\t\t£"
					+ money.format(station.getLostSalesProfit().get(Truck_RoadUser.class)));

		} else {

			// Display a normal tick details.
			System.out.println("Station status:");
			System.out.print(new StringBuilder().append("Time: ").append(time).append("\n"));
			System.out.println(station);
			System.out.println("----------------------------------");

		}
	}

	@Override
	public final void setEnd() {
		this.end = true;
	}
}
