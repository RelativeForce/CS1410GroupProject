package environment.model;

import java.util.HashMap;
import java.util.LinkedList;
import environment.model.locations.Location;
import environment.model.roadUsers.RoadUser;

/**
 * 
 * Encapsulates the behaviour of a collection of {@link Location}s. This station
 * has {@link #toString()} which returns a data structure used by the views to
 * output the simulation to the user. This station stores and relocates
 * {@link RoadUser}s between its locations.
 * 
 * @author Joshua_Eddy
 * @version 09/03/2017
 * 
 * @see environment.GUI.views
 * @see environment.model.roadUsers.RoadUser
 * @see java.util.LinkedList
 * @see java.util.HashMap
 * @see environment.model.locations.Location
 *
 */
public class Station {

	// Private Variables -----------------------------------------------------

	/**
	 * Contains all the locations that make up the {@link Station}.
	 * 
	 * @see java.util.LinkedList
	 * @see environment.model.locations.Location
	 */
	private LinkedList<Location> locations;

	/**
	 * Contains the {@link RoadUser}s that need to be moved to the a new
	 * {@link Location}. The key for this {@link HashMap} is the
	 * {@link RoadUser} as this will be unique because the same {@link RoadUser}
	 * should not be require to move to two separate {@link Locations}s at the
	 * same time. The value of this {@link HashMap} is the current
	 * {@link Location} the key {@link RoadUser} is in.
	 * 
	 * @see java.util.HashMap
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.locations.Location
	 */
	private HashMap<RoadUser, Location> toMove;

	/**
	 * The <code>int<code> amount of {@link RoadUser}s that <code>this</code>
	 * {@link Station} could not accommodate and therefore must have been
	 * rejected.
	 * 
	 */
	private int roadUsersRejected;

	/**
	 * The <code>double</code> amount of profit that was lost by
	 * {@link RoadUser}s being rejected by the station.
	 * 
	 * @see environment.model.roadUsers.vehicles.Vehicle
	 */
	private double lostFuelProfit;

	/**
	 * The <code>double</code> amount of profit that was gained from selling
	 * fuel.
	 * 
	 * @see #collateFuelProfit()
	 * @see #getFuelProfit()
	 */
	private double fuelProfit;

	/**
	 * The first {@link Location} that the {@link RoadUser}s added to
	 * <code>this</code> {@link Station}.
	 * 
	 * @see #toMove
	 */
	private Class<? extends Location> startLoaction;

	// Constructor -----------------------------------------------------------

	/**
	 * Constructs a <code>new</code> {@link Station}.
	 * 
	 * @param startLocation
	 *            The <code>Class</code> of a subclass of type {@link Location}
	 *            that denotes the type of the first {@link Location} new
	 *            {@link RoadUser}s are added to in the station if a
	 *            {@link Location} of that type exists in the station.
	 * 
	 * @see #addLocation(Location)
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.locations.Location
	 * @see #enter(RoadUser)
	 */
	public Station(Class<? extends Location> startLocation) {

		// Initialise instance fields
		this.toMove = new HashMap<RoadUser, Location>();
		this.locations = new LinkedList<Location>();
		this.roadUsersRejected = 0;
		this.startLoaction = startLocation;
		this.fuelProfit = 0;

	}

	// Public Methods --------------------------------------------------------

	/**
	 * Adds a specified {@link Location} to <code>this</code> {@link Station}.
	 * 
	 * @param newLocation
	 *            {@link Location}
	 */
	public void addLocation(Location newLocation) {
		if (newLocation != null) {
			locations.add(newLocation);
		}
	}

	/**
	 * Iterate through all of the {@link Location}s that make up the
	 * {@link Station} and cause them to process their respective queues. If a
	 * {@link RoadUser} is required to be moved to another {@link Location} then
	 * it will be assuming there is enough space for it in its next
	 * {@link Location}.
	 * 
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.locations.Location
	 */
	public void processLocations() {

		// Move all the road users from toMove to their allocated next location.
		relocateRoadUsers();

		// Iterates through each location in the station.
		for (Location currentLocation : locations) {

			// Process the queue at each location.
			currentLocation.processQueue(toMove);

		}

	}

	/**
	 * Retrieves a <code>String</code> data structure that is used by the
	 * SimulatorView to display the simulation.
	 * 
	 * @see environment.GUI.views.SimulatorView
	 */
	@Override
	public String toString() {

		return "";
	}

	/**
	 * Adds a {@link RoadUser} to <code>this</code> {@link Station}. But only if
	 * there is the nessecary space in the starting {@link Location}.
	 * 
	 * @param roadUser
	 *            {@link RoadUser} to be added.
	 * 
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.locations.Location
	 * 
	 */
	public void enter(RoadUser roadUser) {

		// If there is enough space in the station for the parameter RoadUser
		if (this.canContain(roadUser)) {

			// Iterates through all the locations in the station.
			for (Location currentLocation : locations) {

				// If the current location is of the same type as the start
				// location of the station and there is enough space in that
				// location for that road user.
				if (currentLocation.getClass() == startLoaction && currentLocation.canContain(roadUser)) {

					// Add the road user to that location and break to prevent
					// adding that road user to multiple locations.
					currentLocation.enter(roadUser);
					break;
				}
			}

		} else {
			// If there is no space for the road user in the station then
			// increment roadUserRejected to acknowledge a road user has been
			// rejected.
			roadUsersRejected++;
		}

	}

	/**
	 * Retrieve the number of rejected {@link RoadUser}s.
	 * 
	 * @return The number of {@link RoadUser}s that this {@link Station} has
	 *         rejected.
	 */
	public int getRoadUsersRejected() {
		return roadUsersRejected;
	}

	/**
	 * Retrieves the amount of profit <code>this</code> {@link Station} has
	 * generated from fuel sales.
	 * 
	 * @return <code>double</code> fuel profit.
	 * 
	 * @see #
	 */
	public double getFuelProfit() {
		return fuelProfit;
	}

	// Private Methods -------------------------------------------------------

	/**
	 * Checks if there is enough space for a specified {@link RoadUser} inside
	 * <code>this</code> {@link RoadUser}.
	 * 
	 * @param newRoadUser
	 *            {@link RoadUser} to be added to this {@link Station}.
	 * @return <code>boolean</code> whether there is enough space or not.
	 */
	private boolean canContain(RoadUser newRoadUser) {

		// Iterate through all the locations in this station.
		for (Location currentLocation : locations) {

			// If current location is the same type as this stations start
			// location.
			if (currentLocation.getClass() == startLoaction) {

				// If there is enough space for the road user in one of the
				// start location then there is enough space in the station.
				if (currentLocation.canContain(newRoadUser)) {
					return true;
				}
			}
		}

		// Otherwise there is not enough space in the station.
		return false;
	}

	/**
	 * Moves {@link RoadUser}s stored in {@link #toMove} to there assigned next
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadUsers.RoadUser
	 * @see #toMove
	 * 
	 */
	private void relocateRoadUsers() {

		// Retrieve all the road users from toMove and iterate through them.
		for (RoadUser roadUser : toMove.keySet()) {

			// Retrieve the current location assigned to the current
			// road user.
			Location currentLocation = toMove.get(roadUser);

			// Iterate though all the locations in the station
			for (Location location : locations) {

				// If the type of the current location is the same at the next
				// location the road user needs to be put in and there is enough
				// space for the road user in that location.Then add it to said
				// location and remove it from toMove.
				if (location.getClass() == currentLocation.getNextLocation() && location.canContain(roadUser)) {

					location.enter(roadUser);
					toMove.remove(roadUser);
					break;

				}
			}
		}

		// If there are any road users that are still in toMove there must be no
		// space in the locations next in the chain, this means that they should
		// be returned to their prior location.
		if (!toMove.isEmpty()) {
			returnToPriorLocation();
		}

	}

	/**
	 * Return all the {@link RoadUser}s in {@link #toMove} to the front of the
	 * queue in their previous {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see #toMove
	 * @see environment.model.roadUsers.RoadUser
	 */
	private void returnToPriorLocation() {

		// Iterate through all the road users that couldn't be moved to their
		// next location.
		for (RoadUser roadUser : toMove.keySet()) {

			// Get the previous location of the current road user.
			Location pastLocation = toMove.get(roadUser);

			// Return the road user to the its previous location. Then remove it
			// from to move.
			pastLocation.returnToQueue(roadUser);
			toMove.remove(roadUser);

		}

	}

	/**
	 * Collects the fuel profit of all the {@link Location}s that produce fuel
	 * profit and stores the result in {@link #fuelProfit}.
	 */
	private void collateFuelProfit() {

	}

}
