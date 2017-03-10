package environment.model.locations;

import java.util.HashMap;
import java.util.LinkedList;
import environment.model.roadUsers.RoadUser;

/**
 * 
 * This <code>abstract</code> class encapsulates the behaviour of a location in
 * the <code>Station</code>. This should be extended with concrete subclasses.
 * 
 * @author Joshua_Eddy
 * @version 09/03/2017
 *
 */
public abstract class Location {

	// Protected Methods -----------------------------------------------------

	/**
	 * Contains all the {@link RoadUser}s that are present in <code>this</code>
	 * {@link Location}.
	 * 
	 * @see java.util.LinkedList
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 */
	protected LinkedList<RoadUser> queue;

	/**
	 * The <code>Class</code> of the next {@link Location}. This is generic but
	 * must be a subclass of {@link Location}.
	 */
	protected Class<? extends Location> nextLocation;

	/**
	 * The number of {@link RoadUser}s that have passed through this
	 * {@link Location}. The {@link #processQueue(HashMap)} method is assumed to
	 * process the queue and eventually remove all the {@link RoadUser}s from
	 * <code>this</code> {@link Location}.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected int roadUsersProcessed;

	/**
	 * The amount of money that <code>this</code> {@link Location} has generated
	 * from processing the {@link RoadUser}s that pass through it.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected double profit;

	/**
	 * The amount of fuel distributed to the {@link RoadUser}s that pass through
	 * <code>this</code> {@link Location}.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected int fuelSold;

	/**
	 * This value holds the maximum number of units (size of {@link RoadUser}s)
	 * that this location can have at any given point. If this is initialised as
	 * <code>0</code> then the maximum size of the {@link Location}'s queue is
	 * infinite.
	 */
	protected int maxQueueSize;

	// Constructor -----------------------------------------------------------

	/**
	 * Constructs a new {@link Location} for use in the <code>Station</code>.
	 * 
	 * @param nextLocation
	 *            The <code>Class</code> of the type of the next
	 *            {@link Location} that <code>this</code> will move the
	 *            {@link RoadUser}s contained with in it too.
	 * @param maxQueueSize
	 *            The maximum number of units (size of {@link RoadUser}s) that
	 *            this location can have at any given point. If this is
	 *            initialised as <code>0</code> then the maximum size of the
	 *            {@link Location}s queue is infinite.
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.Station
	 */
	public Location(Class<? extends Location> nextLocation, int maxQueueSize) {

		// Initialise all instance fields.
		this.queue = new LinkedList<RoadUser>();
		this.nextLocation = nextLocation;
		this.roadUsersProcessed = 0;
		this.profit = 0;
		this.fuelSold = 0;
		this.maxQueueSize = maxQueueSize;

	}

	// Public Methods --------------------------------------------------------

	/**
	 * Perform a process on the queue at <code>this</code> location. This
	 * <code>abstract</code> method will be different for each location. This
	 * method will be called upon every tick of the simulation.
	 * 
	 * @param toMove
	 *            <code>HashMap&lt;RoadUser, Class&lt;? extends {@link Location}&gt;&gt;</code>
	 *            that denotes the {@link RoadUser}s that the
	 *            <code>Station</code> will move to the next {@link Location}.
	 *            The Key of the {@link HashMap} is of type {@link RoadUser} as
	 *            each road user to be moved should be unique. The value denotes
	 *            the current {@link Location} of the {@link RoadUser} key
	 *            assigned to it.
	 * 
	 * @see environment.model.roadUsers.RoadUser
	 * @see java.util.HashMap
	 * @see java.util.LinkedList
	 */
	public abstract void processQueue(HashMap<RoadUser, Location> toMove);

	/**
	 * Retrieves the <code>Class</code> of the next {@link Location} that the
	 * <code>RoadUser</code>s inside <code>this</code> {@link Location} will be
	 * moved to once they have been processed.
	 * 
	 * @return <code>Class&lt;? extends {@link Location}&gt;&gt;</code>
	 * 
	 * @see #processQueue(HashMap)
	 * @see environment.model.Station
	 */
	public Class<? extends Location> getNextLocation() {
		return nextLocation;
	}

	/**
	 * Retrieves the amount of money collected from the {@link RoadUser}s that
	 * have been processed by <code>this</code> {@link Location}.
	 * 
	 * @return <code>double</code> profit
	 * 
	 * @see #processQueue(HashMap)
	 */
	public double getProfit() {
		return profit;
	}

	/**
	 * Retrieves the amount of gallons of fuel that <code>this</code>
	 * {@link Location} has sold to the {@link RoadUser}s that have been
	 * processed.
	 * 
	 * @return <code>int</code> gallons of fuel sold.
	 */
	public int getFuelSold() {
		return fuelSold;
	}

	/**
	 * Retrieves the number of {@link RoadUser}s that have been processed and
	 * removed from <code>this</code> {@link Location}.
	 * 
	 * @return <code>int</code> number of {@link RoadUser}s that have been
	 *         processed.
	 * @see #processQueue(HashMap)
	 */
	public int getRoadUsersProcessed() {
		return roadUsersProcessed;
	}

	/**
	 * Adds a {@link RoadUser} to <code>this</code> {@link Location} only if the
	 * new {@link RoadUser} is not <code>null</code> and there is enough space
	 * in the queue for it.
	 * 
	 * @param newRoadUser
	 *            {@link RoadUser} to be added.
	 * @see #queue
	 * @see #maxQueueSize
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 */
	public void enter(RoadUser newRoadUser) {
		if (newRoadUser != null && isEnoughSpaceFor(newRoadUser) && !queue.contains(newRoadUser)) {
			queue.add(newRoadUser);
		}
	}

	/**
	 * Checks if the parameter {@link RoadUser} can be placed into
	 * <code>this</code> {@link Location} based on the size of the
	 * {@link RoadUser}'s vehicle.
	 * 
	 * @param roadUser
	 *            The {@link RoadUser} that will potentially be added to
	 *            <code>this</code> {@link Location}.
	 * @return <code>boolean</code> of whether there is enough space for the
	 *         parameter {@link RoadUser} to be added to this location.
	 * 
	 * @see environment.model.Station
	 * @see environment.model.roadUsers.RoadUser
	 */
	public boolean isEnoughSpaceFor(RoadUser roadUser) {

		// Holds the cumulative size of the queue.
		int currentQueueSize = 0;

		// Add the size of each road users vehicle to the cumulative queue size.
		for (RoadUser roadUserQueue : queue) {
			currentQueueSize += roadUserQueue.getVehicle().size;
		}

		// Returns if the size of the queue including the parameter
		// RoadUser is less than the defined maximum size of
		// the queue then return true, otherwise return false.

		// Unless the maxQueueSize is zero meaning that there is no maximum size
		// to the queue in this location.
		return (currentQueueSize + roadUser.getVehicle().size <= maxQueueSize) || (maxQueueSize == 0);
	}

	/**
	 * Used to return a {@link RoadUser} back to the front of the queue because
	 * the next {@link Location} does not have any space.
	 * 
	 * @param roadUser
	 *            {@link RoadUser} to be added back to the queue
	 * @see environment.model.roadUsers.RoadUser
	 */
	public void returnToQueue(RoadUser roadUser) {

		queue.addFirst(roadUser);

	}

}
