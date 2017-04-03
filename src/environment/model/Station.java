package environment.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * @version 03/04/2017
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
	 * The amount of {@link RoadUser}s that are currently inside this station.
	 */
	private int numberOfRoadUsers;

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} value of fuels profit that was lost by
	 * each {@link RoadUser} type being rejected by the station. The
	 * {@link Class} of a sub-class of {@link RoadUser} is the key of this
	 * {@link Map}. The {@link Double} value of this {@link Map} denotes
	 * combined fuel profit lost of that type of the key {@link RoadUser} it is
	 * assigned to.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 * @see #cloneStatistic(Map)
	 */
	private Map<Class<? extends RoadUser>, Double> lostFuelprofit;

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} value of sales profit that was lost by
	 * each {@link RoadUser} type being rejected by the station. The
	 * {@link Class} of a sub-class of {@link RoadUser} is the key of this
	 * {@link Map}. The {@link Double} value of this {@link Map} denotes
	 * combined sales profit lost of that type of the key {@link RoadUser} it is
	 * assigned to.
	 * 
	 * @see environment.model.roadusers.RoadUser
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 * @see #cloneStatistic(Map)
	 */
	private Map<Class<? extends RoadUser>, Double> lostSalesProfit;

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} number of each type of {@link RoadUser}s
	 * that have been processed by the station. The {@link Class} of a sub-class
	 * of {@link RoadUser} is the key of this {@link Map}. The {@link Double}
	 * value of this {@link Map}.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 * @see #cloneStatistic(Map)
	 */
	private Map<Class<? extends RoadUser>, Double> roadUsersProcessed;

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} value of fuels profit that was gained by
	 * each {@link RoadUser} type paying for their fuel. The {@link Class} of a
	 * sub-class of {@link RoadUser} is the key of this {@link Map}. The
	 * {@link Double} value of this {@link Map} denotes combined fuel profit
	 * gained from that type of the key {@link RoadUser} it is assigned to.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 * @see #cloneStatistic(Map)
	 */
	private Map<Class<? extends RoadUser>, Double> fuelProfit;

	/**
	 * The <code>double</code> sales profit that <code>this</code>
	 * {@link Station} has made.
	 */
	private Map<Class<? extends RoadUser>, Double> salesProfit;

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
		this.fuelProfit = new HashMap<Class<? extends RoadUser>, Double>();
		this.salesProfit = new HashMap<Class<? extends RoadUser>, Double>();
		this.roadUsersProcessed = new HashMap<Class<? extends RoadUser>, Double>();
		this.lostSalesProfit = new HashMap<Class<? extends RoadUser>, Double>();
		this.lostFuelprofit = new HashMap<Class<? extends RoadUser>, Double>();
		this.roadUsersRejected = 0;
		this.numberOfRoadUsers = 0;

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
		DecimalFormat integer = new DecimalFormat("#");
		integer.setRoundingMode(RoundingMode.CEILING);

		String output = "";

		output += "Vehicles in station:         " + numberOfRoadUsers + "\n";
		output += "Vehicles rejected:           " + roadUsersRejected + "\n";
		output += "Vehicles processed:          " + integer.format(sum(roadUsersProcessed)) + "\n";
		output += "Petrol profit:              £" + money.format(sum(fuelProfit)) + "\n";
		output += "Lost petrol profit:         £" + money.format(sum(lostFuelprofit)) + "\n";
		output += "Shopping profit:            £" + money.format(sum(salesProfit)) + "\n";
		output += "Lost Shopping profit:       £" + money.format(sum(lostSalesProfit)) + "\n";
		output += "Total profit:               £" + money.format(sum(salesProfit) + sum(fuelProfit)) + "\n";
		output += "Total lost profit:          £" + money.format(sum(lostFuelprofit) + sum(lostSalesProfit)) + "\n";

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
					numberOfRoadUsers++;
					break;
				}
			}

			if (rejected) {
				// If there is no space for the road user in the station then
				// increment roadUserRejected to acknowledge a road user has
				// been rejected.
				update(lostFuelprofit, roadUser.getClass(), roadUser.getVehicle().getMaxWorth());
				update(lostSalesProfit, roadUser.getClass(), roadUser.getWorth());
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
		return sum(fuelProfit);
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
		return sum(salesProfit);
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link RoadUser}s not being happy enough with their service to
	 * spend any money in the {@link ShoppingArea}.
	 * 
	 * @return <code>double</code> lost profit.
	 */
	public double getLostSalesProfit() {
		return sum(lostSalesProfit);
	}

	/**
	 * The amount of profit that <code>this</code> {@link Station} has lost
	 * based on {@link Station} not being able to accommodate the new
	 * {@link RoadUser}.
	 * 
	 * @return <code>double</code> profit lost.
	 */
	public double getLostFuelProfit() {
		return sum(lostFuelprofit);
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

		cloneStation.lostFuelprofit = cloneStatistic(lostFuelprofit);
		cloneStation.lostSalesProfit = cloneStatistic(this.lostSalesProfit);
		cloneStation.roadUsersProcessed = cloneStatistic(this.roadUsersProcessed);
		cloneStation.fuelProfit = cloneStatistic(this.fuelProfit);
		cloneStation.salesProfit = cloneStatistic(this.salesProfit);

		// Initialise the toMove Map in the clone.
		cloneStation.toMove = cloneToMove();

		// Initialise the locations List in the clone.
		cloneStation.locations = cloneLocations();

		return cloneStation;
	}

	// Private Methods -------------------------------------------------------

	/**
	 * Clones a specified statistic {@link Map}.
	 * 
	 * @param toClone
	 *            <code>{@link Map}&lt;Class&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 *            to be cloned.
	 * @return <code>{@link Map}&lt;Class&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 *         clone.
	 */
	private Map<Class<? extends RoadUser>, Double> cloneStatistic(Map<Class<? extends RoadUser>, Double> toClone) {

		Map<Class<? extends RoadUser>, Double> clone = new HashMap<Class<? extends RoadUser>, Double>();

		for (Class<? extends RoadUser> key : toClone.keySet()) {
			clone.put(key, toClone.get(key).doubleValue());
		}
		return clone;
	}

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
					update(fuelProfit, roadUser.getClass(), roadUser.getVehicle().getMaxWorth());
				}
				// If the road user has left the shopping area add its worth to
				// the sales profit for road users of that type.
				if (currentLocation instanceof ShoppingArea) {
					update(salesProfit, roadUser.getClass(), roadUser.getWorth());
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
			numberOfRoadUsers--;
			update(roadUsersProcessed, roadUser.getClass(), 1);
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

	/**
	 * Changes the value (<code>double</code>) of a statistic assigned to a
	 * specific {@link RoadUser} type
	 * (<code>Class&lt;? extends {@link RoadUser}&gt;</code>) by a specified
	 * amount in the the parameter statistic map (<code>{@link Map}</code>). If
	 * the parameter {@link RoadUser} type and assigned value are not currently
	 * stored in the statistic map then they will be added.
	 * 
	 * @param stat
	 *            <code>{@link Map}&lt;Class&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 *            that denotes some statistic about the {@link Station}. For
	 *            example the amount of profit made from fuel sales grouped by
	 *            {@link RoadUser} type. NOT NULL
	 * @param type
	 *            <code>Class&lt;? extends {@link RoadUser}&gt;</code> that
	 *            denotes the type of {@link RoadUser} that is assigned to a to
	 *            the value that is to be updated.
	 * @param amount
	 *            <code>double</code> that denotes the change in the value.
	 */
	private void update(Map<Class<? extends RoadUser>, Double> stat, Class<? extends RoadUser> type, double amount) {

		// If the specified statistic map is not null.
		if (stat != null) {

			// If the specified statistic map has no statistics on road users of
			// the specified type. Then initialise the statistic of the
			// specified type.
			if (!stat.keySet().contains(type)) {
				stat.put(type, 0.0);
			}

			// Iterate through all the road user types in the specified
			// statistic map
			for (Class<? extends RoadUser> currentType : stat.keySet()) {

				// If the current type is the same as that of the specified
				// type.
				if (currentType == type) {

					// Initialise a Double variable to hold the current value of
					// the statistic assigned to the specified road user type.
					Double currentStat = stat.get(type);

					// Add the specified amount to that value.
					currentStat += amount;

					// Update the value of the statistic in the specified
					// statistic map.
					stat.replace(currentType, currentStat);
				}

			}
		}

	}

	/**
	 * Sums all the values ({@link Double}) of the specified statistic
	 * {@link Map} {@link RoadUser} type groups to yield one total value. For
	 * example, total profit made from fuel sales by the {@link Station}.
	 * 
	 * @param stat
	 *            <code>{@link Map}&lt;Class&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 *            that denotes some statistic about the {@link Station}. For
	 *            example the amount of profit made from fuel sales grouped by
	 *            {@link RoadUser} type. NOT NULL
	 * @return The <code>double</code> sum of all the values in the statistic
	 *         map.
	 */
	private double sum(Map<Class<? extends RoadUser>, Double> stat) {

		// Initialise an aggregation variable to store the sum of the statistic
		// maps values.
		Double total = 0.0;

		// If the statistic map is not equal to null then iterate through all
		// the values in the map and sum them.
		if (stat != null) {
			for (Class<? extends RoadUser> currentType : stat.keySet()) {
				total += stat.get(currentType);

			}
		}
		return total;

	}

}
