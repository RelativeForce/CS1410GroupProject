package environment.model.locations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import environment.model.roadusers.RoadUser;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * 
 * This <code>abstract</code> class encapsulates the behaviour of a location in
 * the <code>Station</code>. This should be extended with concrete subclasses.
 * 
 * @author Joshua_Eddy
 * @version 24/04/2017
 *
 */
public abstract class Location implements Cloneable {

	// Static Fields ---------------------------------------------------------

	/**
	 * The next <code>int</code> of the next {@link Location}s {@link #id}.
	 * 
	 * @see #id
	 */
	private static int nextID = 0;

	// Public Fields ---------------------------------------------------------

	/**
	 * The <code>Class</code> of the next {@link Location}. This is generic but
	 * must be a subclass of {@link Location}.
	 */
	public final Class<? extends Location> nextLocation;

	// Private Fields --------------------------------------------------------

	/**
	 * The <code>int</code> that uniquely identifies <code>this</code>
	 * {@link Location}.
	 */
	private int id;

	// Protected Fields ------------------------------------------------------

	/**
	 * Contains all the {@link RoadUser}s that are present in <code>this</code>
	 * {@link Location}.
	 * 
	 * @see java.util.LinkedList
	 * @see #processQueue(HashMap)
	 * @see environment.model.roadusers.RoadUser
	 */
	protected LinkedList<RoadUser> queue;

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
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.Station
	 */
	public Location(Class<? extends Location> nextLocation, int maxQueueSize) {

		// Initialise all instance fields.
		this.nextLocation = nextLocation;
		this.roadUsersProcessed = 0;
		this.profit = 0;
		this.maxQueueSize = maxQueueSize;
		this.queue = new LinkedList<RoadUser>();
		this.id = nextID++;

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
	 * @see environment.model.roadusers.RoadUser
	 * @see java.util.HashMap
	 * @see java.util.LinkedList
	 */
	public abstract void processQueue(Map<RoadUser, Location> toMove);

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
	 * @see environment.model.roadusers.RoadUser
	 */
	public void enter(RoadUser newRoadUser) {
		if (newRoadUser != null && canContain(newRoadUser) && !queue.contains(newRoadUser)) {
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
	 *         parameter {@link RoadUser} to be added to this location. If the
	 *         {@link RoadUser} is already in <code>this</code> {@link Location}
	 *         returns false.
	 * 
	 * @see environment.model.Station
	 * @see environment.model.roadusers.RoadUser
	 */
	public boolean canContain(RoadUser roadUser) {
		/*
		 * Returns true if the size of the queue including the parameter
		 * RoadUser is less than the defined maximum size of the queue or the
		 * maxQueueSize is zero meaning that there is no maximum size to the
		 * queue in this location. Otherwise return false. Also return false if
		 * the RoadUser is already present in this location. + 0.05 is the
		 * margin of error that may arise from repeated double additions.
		 */
		return ((getQueueLength() + roadUser.getVehicle().size <= maxQueueSize + 0.05) || (maxQueueSize == 0))
				&& !queue.contains(roadUser);
	}

	/**
	 * Used to return a {@link RoadUser} back to the front of the queue because
	 * the next {@link Location} does not have any space.
	 * 
	 * @param roadUser
	 *            {@link RoadUser} to be added back to the queue
	 * @see environment.model.roadusers.RoadUser
	 */
	public void returnToQueue(RoadUser roadUser) {

		// If the parameter road user is not already in the queue, return it to
		// the start of the queue.
		if (!queue.contains(roadUser)) {
			queue.addFirst(roadUser);
		}
	}

	/**
	 * Performs a deep clone on <code>this</code> {@link Location}. This is
	 * implemented in the subclasses of <code>this</code>.
	 * 
	 * @see environment.model.locations.Location
	 * @see java.util.LinkedList
	 */
	@Override
	public abstract Location clone();

	/**
	 * Clones <code>this</code> {@link Location}'s details onto the parameter
	 * {@link Location}. This method should be called by the subclasses of
	 * {@link Location} passing an new instance of them selves as a parameter.
	 * 
	 * The <code>Class&lt;? extends {@link Location}&gt;&gt;</code>
	 * {@link #nextLocation} is assumed to be correct as it will be initialised
	 * in the constructor of the new instance of {@link Location} that is passed
	 * as a parameter.
	 * 
	 * @return A {@link Location} identical to <code>this</code>
	 *         {@link Location}.
	 */
	protected Location cloneLocation(Location clone) {

		// Initialise the LinkedList that will be used as the cloned
		// LinkedList
		LinkedList<RoadUser> cloneQueue = new LinkedList<RoadUser>();

		// Iterate through all the elements in the LinkedList
		for (RoadUser roadUser : this.queue) {

			// Add the clone of the current element to the clone
			// LinkedList.
			cloneQueue.add(roadUser.clone());

		}

		// Clone the instance fields.
		clone.queue = cloneQueue;
		clone.id = this.id;
		clone.maxQueueSize = this.maxQueueSize;
		clone.profit = this.profit;
		clone.roadUsersProcessed = this.roadUsersProcessed;

		return clone;

	}

	/**
	 * Retrieves the {@link RoadUser}s in the queue
	 * 
	 * @return
	 */
	public List<RoadUser> getQueue() {
		return queue;
	}

	/**
	 * Retrieves whether a specified {@link Location} is more optimal for a
	 * {@link RoadUser} to enter than <code>this</code>. This method compares
	 * these two {@link Location}s differently depending on the type of
	 * {@link Location}.
	 * 
	 * @param location
	 *            {@link Location} for <code>this</code> to be compared to.
	 * @return <code>boolean this</code> if better that the specified
	 *         {@link Location}.
	 */
	public abstract boolean compare(Location location);

	/**
	 * Retrieves the current length of the {@link #queue} at <code>this</code>
	 * {@link Location} denoted by the sum of all the {@link RoadUser}s in
	 * <code>this</code> {@link Location}'s {@link Vehicle#size}.
	 * 
	 * @return <code>double</code> size of the {@link #queue}.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 */
	protected double getQueueLength() {

		// Holds the cumulative size of the queue.
		double currentQueueSize = 0;

		// Add the size of each road users vehicle to the cumulative queue
		// size.
		for (RoadUser roadUserQueue : queue) {
			currentQueueSize += roadUserQueue.getVehicle().size;
		}

		return currentQueueSize;
	}
}