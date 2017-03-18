package environment;

import java.util.Random;

import environment.GUI.UserInterface;
import environment.GUI.views.CommandLine;
import environment.GUI.views.Graph;
import environment.GUI.views.SimulatorView;
import environment.model.Station;
import environment.model.locations.*;
import environment.model.roadusers.*;

/**
 * 
 * This object runs the simulation that the user will interact with.
 * 
 * @author Joshua_Eddy
 * @version 17/03/2017
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
	 * @see #getSimulatiorPreferences()
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
	 * initialised properly in {@link #getSimulatiorPreferences()}.
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
	 * @see #getSimulatiorPreferences()
	 * @see #generateSimulation()
	 */
	private int numberOfTills;

	/**
	 * The number of pumps that will be added to the {@link #station}.
	 * 
	 * @see #getSimulatiorPreferences()
	 * @see #generateSimulation()
	 */
	private int numberOfPumps;

	/**
	 * Whether the simulation has trucks or not.
	 * 
	 * @see #getSimulatiorPreferences()
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
		this.ui = new UserInterface();

		// Pumps are given as the starting location type.
		this.station = new Station(Pump.class);

		// Initialise instance fields
		this.tickCount = 0;
		this.numberOfPumps = 0;
		this.numberOfTills = 0;
		this.hasTrucks = false;

	}

	// Private Methods -------------------------------------------------------

	/**
	 * Simulates the ticks passing for the {@link Station} by repeating the
	 * process of generating <code>new</code> {@link RoadUser}s that will be
	 * added to the {@link Station} and then processes all the {@link Location}s
	 * in that {@link Station}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.Station
	 */
	private void simulate() {

		// Iterates that amount of ticks that the user specified.
		for (int tickIndex = 0; tickIndex < tickCount; tickIndex++) {

			addRoadUser();

			station.processLocations();

			view.show(tickIndex, station);
		}

	}

	/**
	 * Generates all the {@link RoadUser}s that will be added to the
	 * {@link Station} in a given tick.
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
		if (Motorbike_RoadUser.exists(numberOfPumps, q, value)) {
			station.enter(new Motorbike_RoadUser());
		}

		// If exists is true then add a new family sedan to the station.
		if (FamilySedan_RoadUser.exists(p, q, value)) {
			station.enter(new FamilySedan_RoadUser());
		}
		
		// If exists is true then add a new Truck to the station.
		if(Truck_RoadUser.exists(p, q, value) && hasTrucks){
			station.enter(new Truck_RoadUser());
		}

	}

	/**
	 * Retrieves all the simulation parameters from the {@link UserInterface}
	 * and assigns them to the local variables.
	 * 
	 * @see environment.GUI.UserInterface
	 */
	private void getSimulatiorPreferences() {
                                                                                                                                                                                   
		// Wait for user interface to be ready for information to be retrieved.
		while (!this.ui.isReady()) {
		}

		// Get the tickCount from the user interface
		tickCount = ui.getTickCount();

		// Get p from user interface.
		p = ui.getP();

		// Get q from user interface.
		q = ui.getQ();

		// Get number of pumps from user interface.
		numberOfPumps = ui.getNumberOfPumps();

		// Get number of tills from user interface.
		numberOfTills = ui.getNumberOfTills();

		// Get whether trucks are included in the simulation.
		hasTrucks = ui.hasTrucks();

		// Select the view.
		if (ui.getView().equals("Command Line")) {
			view = new CommandLine();
		} else if (ui.getView().equals("Graph")) {
			view = new Graph();
		}
		
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

		// Create a location for road users to wait before they go to the Till.
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

		// Retrieve all the inputs from the
		simulation.getSimulatiorPreferences();

		// Generates the Locations that will make up the station.
		simulation.generateSimulation();

		// Starts the simulation.
		simulation.simulate();
	}

}
