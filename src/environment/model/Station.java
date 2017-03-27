package environment.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import environment.model.locations.Location;
import environment.model.locations.Till;
import environment.model.roadusers.RoadUser;

/**
 * 
 * Encapsulates the behaviour of a collection of {@link Location}s. This station
 * has {@link #toString()} which returns a data structure used by the views to
 * output the simulation to the user. This station stores and relocates
 * {@link RoadUser}s between its locations.
 * 
 * @author Joshua_Eddy
 * @version 27/03/2017
 * 
 * @see #enter(RoadUser)
 * @see #clone()
 * @see #addLocation(Location)
 * @see #getFuelProfit()
 * @see #getLostFuelProfit()
 * @see #getNumberOfLoactions()
 * @see #getSalesProfit()
 * @see #getLostSalesProfit()
 * @see #getRoadUsersRejected()
 * @see #getNumberOfRoadUsers()
 * @see environment.GUI.views
 * @see environment.model.roadusers.RoadUser
 * @see environment.model.locations.Location
 * @see java.util.LinkedList
 * @see java.util.HashMap
 * 
 *
 */
public class Station {

	// Instance Variables ----------------------------------------------------

	/**
	 * The first {@link Location} that the {@link RoadUser}s added to
	 * <code>this</code> {@link Station}.
	 * 
	 * @see #toMove
	 */
	private Class<? extends Location> startLoaction;

	/**
	 * Contains all the locations that make up the {@link Station}.
	 * 
	 * @see java.util.LinkedList
	 * @see environment.model.locations.Location
	 */
	private List<Location> locations;

	/**
	 * Contains the {@link RoadUser}s that need to be moved to the a new
	 * {@link Location}. The key for this {@link HashMap} is the
	 * {@link RoadUser} as this will be unique because the same {@link RoadUser}
	 * should not be require to move to two separate {@link Locations}s at the
	 * same time. The value of this {@link HashMap} is the current
	 * {@link Location} the key {@link RoadUser} is in.
	 * 
	 * @see java.util.HashMap
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.locations.Location
	 */
	private Map<RoadUser, Location> toMove;

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
	 * @see environment.model.roadusers.vehicles.Vehicle
	 */
	private double lostFuelProfit;

	/**
	 * The <code>double</code> amount of profit that was lost by a
	 * {@link RoadUser}s being unhappy and not shopping.
	 * 
	 * @see environment.model.roadusers.RoadUser
	 */
	private double lostSalesProfit;

	/**
	 * The <code>double</code> sales profit that <code>this</code>
	 * {@link Station} has made.
	 */
	private double salesProfit;

	/**
	 * The <code>double</code> amount of profit that was gained from selling
	 * fuel.
	 * 
	 * @see #collateFuelProfit()
	 * @see #getFuelProfit()
	 */
	private double fuelProfit;

	/**
	 * The amount of {@link RoadUser}s that are currently inside this station.
	 */
	private int numberOfRoadUsers;

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
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.locations.Location
	 * @see #enter(RoadUser)
	 */
	public Station(Class<? extends Location> startLocation) {

		// Initialise instance fields
		this.toMove = new HashMap<RoadUser, Location>();
		this.locations = new LinkedList<Location>();
		this.startLoaction = startLocation;

		// Initialise statistic instance fields
		this.fuelProfit = 0;
		this.salesProfit = 0;

		this.roadUsersRejected = 0;
		this.numberOfRoadUsers = 0;

		this.lostFuelProfit = 0;
		this.lostSalesProfit = 0;

	}

	// Public Methods --------------------------------------------------------

	/**
	 * Adds a specified {@link Location} to <code>this</code> {@link Station}.
	 * 
	 * @param newLocation
	 *            {@link Location}
	 */
	public void addLocation(Location newLocation) {
		if (newLocation != null && !locations.contains(newLocation)) {
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
	 * @see environment.model.roadusers.RoadUser
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

		// Update all financial statistics
		collateFinances();

	}

	/**
	 * Retrieves a <code>String</code> data structure that is used by the
	 * {@link SimulatorView} to display the simulation.
	 * 
	 * @see environment.GUI.views.CommandLine
	 */
	@Override
	public String toString() {

		String output = "";

		output += "Number of Vehicles:          " + numberOfRoadUsers + "\n";
		output += "Number of Rejected Vehicles: " + roadUsersRejected + "\n";
		output += "Petrol profit:              �" + fuelProfit + "\n";
		output += "Lost petrol profit:         �" + lostFuelProfit + "\n";
		output += "Shopping profit:            �" + salesProfit + "\n";
		output += "Total profit:               �" + (salesProfit + fuelProfit) + "\n";
		output += "Total lost profit:          �" + (lostFuelProfit + lostSalesProfit) + "\n";

		return output;
	}

	/**
	 * Adds a {@link RoadUser} to <code>this</code> {@link Station}. But only if
	 * there is the nessecary space in the starting {@link Location}.
	 * 
	 * @param roadUser
	 *            {@link RoadUser} to be added.
	 * 
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.locations.Location
	 * 
	 */
	public void enter(RoadUser roadUser) {

		if (roadUser != null) {

			// If there is enough space in the station for the parameter
			// RoadUser
			if (this.canContain(roadUser)) {

				// Iterates through all the locations in the station.
				for (Location currentLocation : locations) {

					// If the current location is of the same type as the start
					// location of the station and there is enough space in that
					// location for that road user.
					if (currentLocation.getClass() == startLoaction && currentLocation.canContain(roadUser)) {

						// Add the road user to that location and break to
						// prevent
						// adding that road user to multiple locations.
						currentLocation.enter(roadUser);
						numberOfRoadUsers++;
						break;
					}
				}

			} else {
				// If there is no space for the road user in the station then
				// increment roadUserRejected to acknowledge a road user has
				// been
				// rejected.
				lostFuelProfit += roadUser.getVehicle().getMaxWorth();
				lostSalesProfit += roadUser.getWorth();
				roadUsersRejected++;
			}
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

	/**
	 * Retrieves the number of {@link RoadUser}s are currently in the
	 * {@link RoadUser}.
	 * 
	 * @return <code>int</code> amount of {@link RoadUser}s in the
	 *         {@link Station}.
	 * 
	 *
	 */
	public int getNumberOfRoadUsers() {
		return numberOfRoadUsers;
	}

	/**
	 * Returns the number of {@link Location}s in <code>this</code>
	 * {@link Station}.
	 * 
	 * @return <code>int</code> number of locations.
	 * 
	 * @see #locations
	 * @see #addLocation(Location)
	 */
	public int getNumberOfLoactions() {
		return locations.size();
	}

	/**
	 * Retrieves the sales profit <code>this</code> {@link Station} has
	 * generated from {@link RoadUser}s spending money in {@link ShoppingArea}.
	 * 
	 * @return <code>double</code> profit.
	 * 
	 * @see #salesProfit
	 * @see #locations
	 * @see #fuelProfit
	 */
	public double getSalesProfit() {
		return salesProfit;
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link RoadUser}s not being happy enough with their service to
	 * spend any money in the {@link ShoppingArea}.
	 * 
	 * @return <code>double</code> lost profit.
	 */
	public double getLostSalesProfit() {
		return lostSalesProfit;
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link Station} not being able to accommodate the new
	 * {@link RoadUser}.
	 * 
	 * @return <code>double</code> profit lost.
	 */
	public double getLostFuelProfit() {
		return lostFuelProfit;
	}

	@Override
	public boolean equals(Object o) {

		// If the parameter object is a Station
		if (o instanceof Station) {

			// Create a local Station to remove casting to Station repeatedly
			Station station = (Station) o;

			// If the fuel profit and losses of the stations are the same.
			if (this.fuelProfit == station.fuelProfit && this.lostFuelProfit == station.lostFuelProfit) {

				// If the sales profit and losses of the stations are the same.
				if (this.lostSalesProfit == station.lostSalesProfit && this.salesProfit == station.salesProfit) {

					// If both stations have rejected the same amount of road
					// users.
					if (this.roadUsersRejected == station.roadUsersRejected) {

						// If the stations contain the same number of road
						// users.
						if (this.numberOfRoadUsers == station.numberOfRoadUsers) {

							// If the locations in the station are the same.
							if (this.locations.equals(station.locations)) {

								// If the stations have the same road users to
								// be moved to their next locations.
								if (this.toMove.equals(station.toMove)) {

									// If both stations have the same starting
									// location.
									if (this.startLoaction == station.startLoaction) {

										// If all these conditions are met then
										// the parameter station and this are
										// identical.
										return true;

									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Create a deep clone of <code>this</code> {@link Station}.
	 * 
	 * @see java.util.HashMap
	 * @see java.util.LinkList
	 * @see java.util.List
	 * @see java.util.Map
	 */
	@Override
	public Station clone() {

		// Initialise the new Station that will be used as the clone of this.
		Station cloneStation = new Station(this.startLoaction);

		// Clone the instance fields of this into the clone.
		cloneStation.fuelProfit = this.fuelProfit;
		cloneStation.salesProfit = this.salesProfit;
		cloneStation.numberOfRoadUsers = this.numberOfRoadUsers;
		cloneStation.lostFuelProfit = this.lostFuelProfit;
		cloneStation.lostSalesProfit = this.lostSalesProfit;
		cloneStation.roadUsersRejected = this.roadUsersRejected;

		// Initialise a new Map to be used in the clone as toMove.
		Map<RoadUser, Location> cloneToMove = new HashMap<RoadUser, Location>();

		// Iterate through all the elements in the toMove Map in this.
		for (RoadUser roadUser : this.toMove.keySet()) {

			// Add the current element in toMove into the clone of toMove.
			cloneToMove.put(roadUser.clone(), this.toMove.get(roadUser).clone());

		}

		// Initialise the toMove Map in the clone.
		cloneStation.toMove = cloneToMove;

		// Initialise a new List to be used in the clone an locations.
		List<Location> cloneLocations = new LinkedList<Location>();

		// Iterate through all the locations in this station.
		while (this.locations.iterator().hasNext()) {

			// Add the current location to the clone's version of locations.
			cloneLocations.add(this.locations.iterator().next().clone());

		}

		// Initialise the locations List in the clone.
		cloneStation.locations = cloneLocations;

		return cloneStation;
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
	 * @see environment.model.roadusers.RoadUser
	 * @see #toMove
	 * 
	 */
	private void relocateRoadUsers() {

		// Retrieve all the road users from toMove and iterate through them.
		for (RoadUser roadUser : toMove.keySet()) {

			// Retrieve the current location assigned to the current road user.
			Location currentLocation = toMove.get(roadUser);

			// Locate and store the road user.
			findDestinationLocation(roadUser, currentLocation.getNextLocation());

		}

		// If there are any road users that are still in toMove there must be no
		// space in the locations next in the chain, this means that they should
		// be returned to their prior location.
		if (!toMove.isEmpty()) {
			returnToPriorLocation();
		}

	}

	/**
	 * Iterates through all the {@link Location}s in <code>this</code>
	 * {@link Station} to find and store the specified {@link RoadUser}'s next
	 * {@link Location}.
	 * 
	 * @param roadUser
	 *            The {@link RoadUser} to be relocated its next
	 *            {@link Location}.
	 * @param nextLocation
	 *            The <code>Class</code> of the next {@link Location} of the
	 *            specified {@link RoadUser}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 * @see #relocateRoadUsers()
	 * @see environment.model.locations.Location#canContain(RoadUser)
	 * @see environment.model.locations.Location#enter(RoadUser)
	 */
	private void findDestinationLocation(RoadUser roadUser, Class<? extends Location> nextLocation) {

		if (nextLocation != null) {

			// Iterate though all the locations in the station
			for (Location location : locations) {

				// If the type of the current location is the same at the
				// next location the road user needs to be put in and there
				// is enough space for the road user in that location.Then
				// add it to said location and remove it from toMove.
				if (location.getClass() == nextLocation && location.canContain(roadUser)) {

					location.enter(roadUser);
					toMove.remove(roadUser);
					break;

				}
			}

		} else {

			// The road user has no next location, There for it will leave
			// the station.
			numberOfRoadUsers--;
			toMove.remove(roadUser);

		}

	}

	/**
	 * Return all the {@link RoadUser}s in {@link #toMove} to the front of the
	 * queue in their previous {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see #toMove
	 * @see environment.model.roadusers.RoadUser
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
	 * 
	 * @see #processLocations()
	 */
	private void collateFinances() {

		// Reset all of the profit to zero.
		fuelProfit = 0;
		salesProfit = 0;

		// Iterate through all the locations in the station.
		for (Location location : locations) {

			// if the current location is a Till add its profit to the specific
			// fuel profit.
			if (location instanceof Till) {
				fuelProfit += location.getProfit();
			}

			// Add all profit to the general station profit.
			salesProfit = location.getProfit();
		}

	}

}
