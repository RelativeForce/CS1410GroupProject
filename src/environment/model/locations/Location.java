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
 * @version 08/03/2017
 *
 */
public abstract class Location {

	/**
	 * Contains all the <code>RoadUser</code>s that are present in
	 * <code>this Location</code>.
	 * 
	 * @see java.util.LinkedList
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 */
	protected LinkedList<RoadUser> queue;

	/**
	 * The <code>Class</code> of the next <code>Location</code>. This is generic
	 * but must be a subclass of <code>Location</code>.
	 */
	protected Class<? extends Location> nextLocation;

	/**
	 * The number of <code>RoadUser</code>s that have passed through this
	 * <code>Location</code>. The {@link #processQueue(HashMap)} method is
	 * assumed to process the queue and eventually remove all the
	 * <code>RoadUser</code>s from <code>this Location</code>.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected int roadUsersProcessed;

	/**
	 * The amount of money that <code>this Location</code> has generated from
	 * processing the <code>RoadUser</code>s that pass through it.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected double profit;

	/**
	 * The amount of fuel distributed to the <code>RoadUser</code>s that pass
	 * through <code>this Location</code>.
	 * 
	 * @see #processQueue(HashMap)
	 */
	protected int fuelSold;

	/**
	 * This value holds the maximum number of units (size of
	 * <code>RoadUser</code>s) that this location can have at any given point.
	 * If this is initialised as <code>0</code> then the maximum size of the
	 * <code>Location</code>s queue is infinite.
	 */
	protected int maxQueueSize;

	/**
	 * Constructs a new <code>Location</code> for use in the
	 * <code>Station</code>.
	 * 
	 * @param nextLocation
	 *            The <code>Class</code> of the type of the next
	 *            <code>Location</code> that <code>this</code> will move the
	 *            <code>RoadUser</code>s contained with in it too.
	 * @param maxQueueSize
	 *            The maximum number of units (size of <code>RoadUser</code>s)
	 *            that this location can have at any given point. If this is
	 *            initialised as <code>0</code> then the maximum size of the
	 *            <code>Location</code>s queue is infinite.
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 * @see environment.model.Station
	 */
	public Location(Class<? extends Location> nextLocation, int maxQueueSize) {

		queue = new LinkedList<RoadUser>();
		this.nextLocation = nextLocation;
		roadUsersProcessed = 0;
		profit = 0;
		fuelSold = 0;
		this.maxQueueSize = maxQueueSize;

	}

	/**
	 * Perform a process on the queue at <code>this</code> location. This
	 * <code>abstract</code> method will be different for each location. This
	 * method will be called upon every tick of the simulation.
	 * 
	 * @param toMove
	 *            <code>HashMap&lt;RoadUser, Class&lt;? extends Location&gt;&gt;</code>
	 *            that denotes the <code>RoadUser</code>s that the
	 *            <code>Station</code> will move to the next
	 *            <code>Location</code>. The Key of the HashMap is of type
	 *            <code>RoadUser<code> as each road
	 *            user to be moved should be unique. The <code>Class&lt;? extends Location&gt;&gt;</code>
	 *            denotes the next <code>Location</code> of the
	 *            <code>RoadUser</code> key assigned to it.
	 * 
	 * @see environment.model.roadUsers.RoadUser
	 * @see java.util.HashMap
	 * @see java.util.LinkedList
	 */
	public abstract void processQueue(HashMap<RoadUser, Class<? extends Location>> toMove);

	/**
	 * Retrieves the <code>Class</code> of the next <code>Location</code> that
	 * the <code>RoadUser</code>s inside <code>this Location</code> will be
	 * moved to once they have been processed.
	 * 
	 * @return <code>Class&lt;? extends Location&gt;&gt;</code>
	 * 
	 * @see #processQueue(HashMap)
	 * @see environment.model.Station
	 */
	public Class<? extends Location> getNextLocation() {
		return nextLocation;
	}

	/**
	 * Retrieves the amount of money collected from the <code>RoadUser</code>s
	 * that have been processed by <code>this Location</code>.
	 * 
	 * @return <code>double</code> profit
	 * 
	 * @see #processQueue(HashMap)
	 */
	public double getProfit() {
		return profit;
	}

	/**
	 * Retrieves the amount of gallons of fuel that <code>this Location</code>
	 * has sold to the <code>RoadUser</code>s that have been processed.
	 * 
	 * @return <code>int</code> gallons of fuel sold.
	 */
	public int getFuelSold() {
		return fuelSold;
	}

	/**
	 * Retrieves the number of <code>RoadUser</code>s that have been processed
	 * and removed from <code>this Location</code>.
	 * 
	 * @return <code>int</code> number of <code>RoadUser</code>s that have been
	 *         processed.
	 * @see #processQueue(HashMap)
	 */
	public int getRoadUsersProcessed() {
		return roadUsersProcessed;
	}

	/**
	 * Adds a <code>RoadUser</code> to <code>this Location</code> only if the
	 * new <code>RoadUser</code> is not <code>null</code> and there is enough
	 * space in the queue for it.
	 * 
	 * @param newRoadUser
	 *            <code>RoadUser</code> to be added.
	 * @see #queue
	 * @see #maxQueueSize
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadUsers.RoadUser
	 */
	public void enter(RoadUser newRoadUser) {
		if (newRoadUser != null && isEnoughSpaceFor(newRoadUser)) {
			queue.add(newRoadUser);
		}
	}

	/**
	 * Checks if the parameter <code>RoadUser</code> can be placed into
	 * <code>this Location</code> based on the size of the
	 * <code>RoadUser</code>s vehicle.
	 * 
	 * @param roadUser
	 *            The <code>RoadUser</code> that will potentially be added to
	 *            <code>this Location</code>.
	 * @return <code>boolean</code> of whether there is enough space for the
	 *         parameter <code>RoadUser</code> to be added to this location.
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

}
