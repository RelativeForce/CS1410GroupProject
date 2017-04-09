package environment;

import java.util.Random;

import environment.GUI.UserInterface;
import environment.GUI.views.SimulatorView;
import environment.model.Station;
import environment.model.locations.*;
import environment.model.roadusers.*;

/**
 * 
 * This object runs the simulation that the user will interact with.
 * 
 * @author Joshua_Eddy
 * @version 05/04/2017
 *
 */
public final class Simulator {

	// Instance Fields -------------------------------------------------------

	/**
	 * The {@link SimulatorView} that will be used to display the results of the
	 * simulation to the user.
	 * 
	 * @see #simulate()
	 * @see environment.GUI.views.SimulatorView
	 */
	private SimulatorView view;

	/**
	 * The {@link UserInterface} that the user will input the parameters of the
	 * simulation.
	 * 
	 * @see #getSimulationDetails()
	 * @see environment.GUI.UserInterface
	 */
	private UserInterface ui;

	/**
	 * The {@link Station} which is the object of the simulation.
	 * {@link RoadUser}s will be added to it upon each tick of the simulation.
	 * 
	 * @see #simulate()
	 * @see environment.model.Station
	 */
	private Station station;

	/**
	 * The <code>int</code> amount of ticks the simulation will run for. This is
	 * initialised properly in {@link #getSimulationDetails()}.
	 */
	private int tickCount;

	/**
	 * An arbitrary value that is &lt;1 and &gt;0.
	 * 
	 * @see #addRoadUser()
	 */
	private double p;

	/**
	 * An arbitrary value that is &lt;1 and &gt;0.
	 * 
	 * @see #addRoadUser()
	 */
	private double q;

	/**
	 * The number of tills that will be added to the {@link #station}.
	 * 
	 * @see #getSimulationDetails()
	 * @see #generateSimulation()
	 */
	private int numberOfTills;

	/**
	 * The number of pumps that will be added to the {@link #station}.
	 * 
	 * @see #getSimulationDetails()
	 * @see #generateSimulation()
	 */
	private int numberOfPumps;

	/**
	 * Whether the simulation has trucks or not.
	 * 
	 * @see #getSimulationDetails()
	 */
	private boolean hasTrucks;

	// Constructor -----------------------------------------------------------

	/**
	 * Constructs a new {@link Simulator} that will also generate a
	 * {@link UserInterface} to gather information nessecary to running the
	 * simulation.
	 * 
	 * @see environment.GUI.UserInterface
	 */
	public Simulator() {

		// Create and display a window where the user will input all of the
		// parameters of the simulation.
		// Initialise the user interface to null.
		this.ui = null;

		// Pumps are given as the starting location type.
		this.station = new Station(Pump.class);

		// Initialise instance fields
		this.tickCount = 0;
		this.numberOfPumps = 0;
		this.numberOfTills = 0;
		this.hasTrucks = false;

	}

	// Inner Classes ----------------------------------------------------------

	/**
	 * Used to convey a simple {@link RuntimeException} with the data gathered
	 * from the {@link UserInterface}.
	 * 
	 * @see environment.GUI.UserInterface
	 * @see environment.model.Station
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class SimulationDetailsException extends RuntimeException {

		/**
		 * Identifies <code>this</code> {@link RuntimeException}.
		 */
		private static final long serialVersionUID = 89469236891365865L;

		public SimulationDetailsException(String s) {
			super(s + " [Simulation Error]");
		}

	}

	// Private Methods -------------------------------------------------------

	/**
	 * Simulates the ticks passing for the {@link Station} by repeating the
	 * process of generating <code>new</code> {@link RoadUser}s that will be
	 * added to the {@link Station} and then processes all the {@link Location}s
	 * in that {@link Station}.
	 * 
	 * @see #addRoadUser()
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.Station
	 */
	private void simulate() {

		// Iterates that amount of ticks that the user specified.
		for (int tickIndex = 0; tickIndex < tickCount; tickIndex++) {

			addRoadUser();

			station.processLocations();

			// Here a clone of the station is passed to the simulator view to
			// ensure that any changes made to the station that is passed to the
			// SimulatorView do not effect the simulation.
			view.show(tickIndex, station.clone());
		}
		
		// Display the final tick of the simulations details.
		view.setEnd();
		view.show(tickCount, station);

	}

	/**
	 * Creates a <code>new</code> {@link UserInterface} and displays it to the
	 * user.
	 * 
	 * @see #ui
	 * @see #getSimulationDetails()
	 */
	private void displayUserInterface() {

		// Initialise an new user interface.
		this.ui = new UserInterface();

	}

	/**
	 * Generates a {@link RoadUser} that will be added to the {@link Station} in
	 * a given tick.
	 * 
	 * @see environment.model.Station
	 * @see environment.model.roadusers.RoadUser
	 * @see #between(double)
	 */
	private void addRoadUser() {

		double value = (new Random()).nextDouble();

		// If value is lower than or equal to p then add a new small car to the
		// station.
		if (SmallCar_RoadUser.exists(p, q, value)) {
			station.enter(new SmallCar_RoadUser());
		}

		// If value is between p and 2p then add a new motor bike to the
		// station.
		if (Motorbike_RoadUser.exists(p, q, value)) {
			station.enter(new Motorbike_RoadUser());
		}

		// If exists is true then add a new family sedan to the station.
		if (FamilySedan_RoadUser.exists(p, q, value)) {
			station.enter(new FamilySedan_RoadUser());
		}

		// If exists is true then add a new Truck to the station.
		if (Truck_RoadUser.exists(p, q, value) && hasTrucks) {
			station.enter(new Truck_RoadUser());
		}

	}

	/**
	 * Retrieves all the simulation parameters from the {@link UserInterface}
	 * and assigns them to the local variables.
	 * 
	 * 
	 * @see environment.GUI.UserInterface
	 */
	private void getSimulationDetails() {

		// Wait for user interface to be ready for information to be retrieved.
		while (!ui.isReady()) {

		}

		// Get the tickCount from the user interface
		tickCount = ui.getTickCount();

		// If the tick count is invalid, throw a runtime exception.
		if (tickCount < 0) {
			throw new SimulationDetailsException("The number of ticks must be non-negative");
		}

		// Get p from user interface.
		p = ui.getP();

		// If the p is invalid, throw a runtime exception.
		if (p < 0) {
			throw new SimulationDetailsException("p must be non-negative");
		}

		// Get q from user interface.
		q = ui.getQ();

		// If the q is invalid, throw a runtime exception.
		if (q < 0) {
			throw new SimulationDetailsException("q must be non-negative");
		}

		// Get number of pumps from user interface.
		numberOfPumps = ui.getNumberOfPumps();

		// If the number of pumps is invalid, throw a runtime exception.
		if (numberOfPumps < 0) {
			throw new SimulationDetailsException("The number of pumps must be non-negative");
		}

		// Get number of tills from user interface.
		numberOfTills = ui.getNumberOfTills();

		// If the number of tills is invalid, throw a runtime exception.
		if (numberOfTills < 0) {
			throw new SimulationDetailsException("The number of tills must be non-negative");
		}

		// Get whether trucks are included in the simulation.
		hasTrucks = ui.hasTrucks();

		// Get the view.
		view = ui.getView();

		// If the view is invalid, throw a runtime exception.
		if (view == null) {
			throw new SimulationDetailsException("No Simulator View is specified");
		}

		// Close the user interface.
		ui.dispose();

	}

	/**
	 * Generates all of the necessary {@link Location}s for a station.
	 * 
	 * @see environment.model.locations.Location
	 */
	private void generateSimulation() {

		// Create all of the pumps and add them to the station.
		for (int pumpIndex = 0; pumpIndex < numberOfPumps; pumpIndex++) {
			station.addLocation(new Pump(ShoppingArea.class));
		}

		// Create a location for road users to shop before they go to the Till.
		station.addLocation(new ShoppingArea(Till.class));

		// Create all of the Tills and add them to the station.
		for (int tillIndex = 0; tillIndex < numberOfTills; tillIndex++) {
			station.addLocation(new Till(null));
		}

	}

	// Static Methods --------------------------------------------------------

	/**
	 * Runs the simulation.
	 * 
	 * @param args
	 *            unused.
	 */
	public static void main(String[] args) {

		// Creates a new Simulation.
		Simulator simulation = new Simulator();

		// Displays the user interface.
		simulation.displayUserInterface();

		// Retrieve all the inputs from the
		simulation.getSimulationDetails();

		// Generates the Locations that will make up the station.
		simulation.generateSimulation();

		// Starts the simulation.
		simulation.simulate();

		// Exit the application.
		System.exit(0);
	}

}
