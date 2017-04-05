package environment.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import environment.GUI.views.SimulatorView;
import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
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
 * @version 05/04/2017
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
 * @see environment.GUI
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
	 * The {@link Statistic} that denotes the amount of {@link RoadUser}s that
	 * <code>this</code> {@link Station} could not accommodate and therefore
	 * must have been rejected.
	 * 
	 */
	private Statistic roadUsersRejected;

	/**
	 * The amount of {@link RoadUser}s that are currently inside this station.
	 */
	private Statistic numberOfRoadUsers;

	/**
	 * Stores the {@link Double} value of fuels profit that was lost by each
	 * {@link RoadUser} type being rejected by the station.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 */
	private Statistic lostFuelprofit;

	/**
	 * Stores the {@link Double} value of sales profit that was lost by each
	 * {@link RoadUser} type being rejected by the station.
	 * 
	 * @see environment.model.roadusers.RoadUser
	 */
	private Statistic lostSalesProfit;

	/**
	 * Stores the {@link Double} number of each type of {@link RoadUser}s that
	 * have been processed by the station.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 */
	private Statistic roadUsersProcessed;

	/**
	 * Stores the {@link Double} value of fuels profit that was gained by each
	 * {@link RoadUser} type paying for their fuel.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 */
	private Statistic fuelProfit;

	/**
	 * The <code>double</code> sales profit that <code>this</code>
	 * {@link Station} has made.
	 */
	private Statistic salesProfit;

	// Constructor ------------------------------------------------------------

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
		this.fuelProfit = new Statistic();
		this.salesProfit = new Statistic();
		this.roadUsersProcessed = new Statistic();
		this.lostSalesProfit = new Statistic();
		this.lostFuelprofit = new Statistic();
		this.roadUsersRejected = new Statistic();
		this.numberOfRoadUsers = new Statistic();

	}

	// Public Methods ---------------------------------------------------------

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

	}

	/**
	 * Retrieves a <code>String</code> data structure that is used by the
	 * {@link SimulatorView} to display the simulation.
	 * 
	 * @see environment.GUI.views.CommandLine
	 */
	@Override
	public String toString() {

		DecimalFormat money = new DecimalFormat("#.##");
		money.setRoundingMode(RoundingMode.CEILING);
		DecimalFormat integer = new DecimalFormat("####");
		integer.setRoundingMode(RoundingMode.CEILING);

		String output = "";

		output += "Vehicles in station:         " + integer.format(numberOfRoadUsers.sum()) + "\n";
		output += "Vehicles rejected:           " + integer.format(roadUsersRejected.sum()) + "\n";
		output += "Vehicles processed:          " + integer.format(roadUsersProcessed.sum()) + "\n";
		output += "Petrol profit:              £" + money.format(fuelProfit.sum()) + "\n";
		output += "Lost petrol profit:         £" + money.format(lostFuelprofit.sum()) + "\n";
		output += "Shopping profit:            £" + money.format(salesProfit.sum()) + "\n";
		output += "Lost Shopping profit:       £" + money.format(lostSalesProfit.sum()) + "\n";
		output += "Total profit:               £" + money.format(salesProfit.sum() + fuelProfit.sum()) + "\n";
		output += "Total lost profit:          £" + money.format(lostFuelprofit.sum() + lostSalesProfit.sum()) + "\n";

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

		// Stores whether the station has rejected the road user attempting to
		// enter the station. It is true until the road user is placed in a
		// location.
		boolean rejected = true;

		if (roadUser != null) {

			// If there is enough space in the station for the parameter
			// RoadUser

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
					rejected = false;
					numberOfRoadUsers.update(roadUser.getClass(), 1);
					break;
				}
			}

			if (rejected) {
				// If there is no space for the road user in the station then
				// increment roadUserRejected to acknowledge a road user has
				// been rejected.
				lostFuelprofit.update(roadUser.getClass(), roadUser.getVehicle().getMaxWorth());
				lostSalesProfit.update(roadUser.getClass(), roadUser.getWorth());
				roadUsersRejected.update(roadUser.getClass(), 1);
				;
			}
		}

	}

	/**
	 * Retrieve the number of rejected {@link RoadUser}s.
	 * 
	 * @return The number of {@link RoadUser}s that this {@link Station} has
	 *         rejected.
	 */
	public Statistic getRoadUsersRejected() {
		return roadUsersRejected;
	}

	/**
	 * Retrieves the amount of profit <code>this</code> {@link Station} has
	 * generated from fuel sales.
	 * 
	 * @return {@link Statistic} fuel profit.
	 * 
	 */
	public Statistic getFuelProfit() {
		return fuelProfit;
	}

	/**
	 * Retrieves the number of {@link RoadUser}s are currently in the
	 * {@link RoadUser}.
	 * 
	 * @return {@link Statistic} amount of {@link RoadUser}s in the
	 *         {@link Station}.
	 */
	public Statistic getNumberOfRoadUsers() {
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
	 * @return {@link Statistic} sales profit.
	 * 
	 * @see #salesProfit
	 * @see #locations
	 * @see #fuelProfit
	 */
	public Statistic getSalesProfit() {
		return salesProfit;
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link RoadUser}s not being happy enough with their service to
	 * spend any money in the {@link ShoppingArea}.
	 * 
	 * @return {@link Statistic} lost profit.
	 */
	public Statistic getLostSalesProfit() {
		return lostSalesProfit;
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link Station} not being able to accommodate the new
	 * {@link RoadUser}.
	 * 
	 * @return {@link Statistic} profit lost.
	 */
	public Statistic getLostFuelProfit() {
		return lostFuelprofit;
	}

	@Override
	public boolean equals(Object o) {

		// If the parameter object is a Station
		if (o instanceof Station) {

			// Create a local Station to remove casting to Station repeatedly
			Station station = (Station) o;

			// If the fuel profit and losses of the stations are the same.
			if (this.fuelProfit.equals(station.fuelProfit) && this.lostFuelprofit.equals(station.lostFuelprofit)) {

				// If the sales profit and losses of the stations are the same.
				if (this.lostSalesProfit.equals(station.lostSalesProfit)
						&& this.salesProfit.equals(station.salesProfit)) {

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
	 * Retrieves the {@link Location}s in <code>this</code> {@link Station.java}
	 * 
	 * @return <code>{@link List}&lt;{@link Location}&gt;</code> containing all
	 *         the locations in <code>this</code> {@link Station}.
	 */
	public List<Location> getLocations() {

		return locations;

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
		cloneStation.numberOfRoadUsers = this.numberOfRoadUsers;
		cloneStation.roadUsersRejected = this.roadUsersRejected;

		cloneStation.lostFuelprofit = this.lostFuelprofit.clone();
		cloneStation.lostSalesProfit = this.lostSalesProfit.clone();
		cloneStation.roadUsersProcessed = this.roadUsersProcessed.clone();
		cloneStation.fuelProfit = this.fuelProfit.clone();
		cloneStation.salesProfit = this.salesProfit.clone();

		// Initialise the toMove Map in the clone.
		cloneStation.toMove = cloneToMove();

		// Initialise the locations List in the clone.
		cloneStation.locations = cloneLocations();

		return cloneStation;
	}

	// Private Methods -------------------------------------------------------

	/**
	 * Clones {@link #locations} in <code>this</code> {@link Station}.
	 * 
	 * @return {@link List}&lt;{@link Location}&gt; clone of {@link #locations}
	 */
	private List<Location> cloneLocations() {

		// Initialise a new List to be used in the clone an locations.
		List<Location> cloneLocations = new LinkedList<Location>();

		// Iterate through all the locations in this station.
		for (Location location : this.locations) {

			// Add the current location to the clone's version of locations.
			cloneLocations.add(location.clone());

		}
		return cloneLocations;
	}

	/**
	 * Clones the {@link #toMove} field of <code>this</code> {@link Station}.
	 * 
	 * @return <code>{@link Map}&lt;{@link RoadUser}, {@link Location}&gt;</code>
	 *         A clone of {@link #toMove}
	 */
	private Map<RoadUser, Location> cloneToMove() {

		// Initialise a new Map to be used in the clone as toMove.
		Map<RoadUser, Location> cloneToMove = new HashMap<RoadUser, Location>();

		// Iterate through all the elements in the toMove Map in this.
		for (RoadUser roadUser : this.toMove.keySet()) {

			// Add the current element in toMove into the clone of toMove.
			cloneToMove.put(roadUser.clone(), this.toMove.get(roadUser).clone());

		}
		return cloneToMove;

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

			// Locate and store the road user and if it is successfully added to
			// its next location than update the statistics.
			if (findDestinationLocation(roadUser, currentLocation.getNextLocation())) {

				// If the road user has left the till then add the vehicles
				// value to the fuel profit of the vehicles of that type.
				if (currentLocation instanceof Till) {
					fuelProfit.update(roadUser.getClass(), roadUser.getVehicle().getMaxWorth());
				}
				// If the road user has left the shopping area add its worth to
				// the sales profit for road users of that type.
				if (currentLocation instanceof ShoppingArea) {
					salesProfit.update(roadUser.getClass(), roadUser.getWorth());
				}

				if (currentLocation instanceof Pump) {

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
	 * @return <code>boolean</code> whether the {@link RoadUser} was added to
	 *         its next location.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 * @see #relocateRoadUsers()
	 * @see environment.model.locations.Location#canContain(RoadUser)
	 * @see environment.model.locations.Location#enter(RoadUser)
	 */
	private boolean findDestinationLocation(RoadUser roadUser, Class<? extends Location> nextLocation) {

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
					return true;

				}
			}
			return false;

		} else {

			// The road user has no next location, There for it will leave
			// the station.
			numberOfRoadUsers.update(roadUser.getClass(), -1);
			;
			roadUsersProcessed.update(roadUser.getClass(), 1);
			toMove.remove(roadUser);
			return true;
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

}
