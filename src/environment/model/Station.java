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
	 * same time. The value of this {@link HashMap} is the <code>Class</code> of
	 * the next {@link Location} for the key {@link RoadUser} assigned to it.
	 * 
	 * @see java.util.HashMap
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.locations.Location
	 */
	private HashMap<RoadUser, Class<? extends Location>> toMove;

	/**
	 * The <code>int<code> amount of {@link RoadUser}s that <code>this</code>
	 * {@link Station} could not accommodate and therefore must have been
	 * rejected.
	 * 
	 */
	private int roadUsersRejected;

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
	 * @see #enterStation(RoadUser)
	 */
	public Station(Class<? extends Location> startLocation) {

		// Initialise instance fields
		this.toMove = new HashMap<RoadUser, Class<? extends Location>>();
		this.locations = new LinkedList<Location>();
		this.roadUsersRejected = 0;
		this.startLoaction = startLocation;

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

			// Check if the next location after current location in the chain of
			// locations has space for the road users added toMove by the most
			// recent add to.
			checkNextLocationForSpace(currentLocation);

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
	public void enterStation(RoadUser roadUser) {

		// If there is enough space in the station for the parameter RoadUser
		if (isSpaceInStation(roadUser)) {

			// Iterates through all the locations in the station.
			for (Location currentLocation : locations) {

				// If the current location is of the same type as the start
				// location of the station and there is enough space in that
				// location for that road user.
				if (currentLocation.getClass() == startLoaction && currentLocation.isEnoughSpaceFor(roadUser)) {

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

	// Private Methods -------------------------------------------------------

	/**
	 * Checks if there is enough space for a specified {@link RoadUser} inside
	 * <code>this</code> {@link RoadUser}.
	 * 
	 * @param newRoadUser
	 *            {@link RoadUser} to be added to this {@link Station}.
	 * @return <code>boolean</code> whether there is enough space or not.
	 */
	private boolean isSpaceInStation(RoadUser newRoadUser) {

		// Iterate through all the locations in this station.
		for (Location currentLocation : locations) {

			// If current location is the same type as this stations start
			// location.
			if (currentLocation.getClass() == startLoaction) {

				// If there is enough space for the road user in one of the
				// start location then there is enough space in the station.
				if (currentLocation.isEnoughSpaceFor(newRoadUser)) {
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

			// Retrieve the class of the next location assigned to the current
			// road user.
			Class<? extends Location> nextLocation = toMove.get(roadUser);

			// Iterate though all the locations in the station
			for (Location location : locations) {

				// if the type of the location of the current location is the
				// same at the next location the road user needs to be put in
				// and there is enough space for the road user in that location.
				// Then add it to said location and remove it from toMove
				if (location.getClass() == nextLocation && location.isEnoughSpaceFor(roadUser)) {

					location.enter(roadUser);
					toMove.remove(roadUser);
					break;

				}
			}
		}

	}

	/**
	 * Checks if the {@link Location} after a specified {@link Location} has the
	 * space require to house the {@link RoadUser}s that have been added to
	 * {@link #toMove} by the current {@link Location}
	 * 
	 * @param currentLocation
	 *            {@link Location} that added the {@link RoadUser}s to be moved
	 *            to the next {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadUsers.RoadUser
	 */
	private void checkNextLocationForSpace(Location currentLocation) {

		// Retrieve all the road users from toMove and iterate through them.
		for (RoadUser roadUser : toMove.keySet()) {

			// Retrieve the class of the next location assigned to the current
			// road user.
			Class<? extends Location> nextLocationClass = toMove.get(roadUser);

			// If the next location of the current location is the same as the
			// next location as the current road user.
			if (currentLocation.getNextLocation() == nextLocationClass) {

				// Remove the current road user from toMove
				toMove.remove(roadUser);

				// Iterate through all the locations in the station.
				for (Location nextLocation : locations) {

					// if the type of the next location of the next location of
					// the road user are the same and there is not enough space
					// for the road user in that location. Then return it to
					// said location to toMove
					if (nextLocation.getClass() == nextLocationClass && nextLocation.isEnoughSpaceFor(roadUser)) {

						toMove.put(roadUser, nextLocationClass);

					}
				}

				// If the road user is not inside toMove then it cannot be moved
				// to its next location and thus must wait for another tick in
				// its current location.
				if (!toMove.containsKey(roadUser)) {
					currentLocation.returnToQueue(roadUser);
				}

			}
		}

	}

}
