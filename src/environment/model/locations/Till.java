package environment.model.locations;

import java.util.Map;
import java.util.Random;

import environment.model.roadusers.RoadUser;

/**
 * 
 * Place where a {@link RoadUser} pays for the transaction.
 * 
 * @author Karandeep_Saini
 * @author Joshua_Eddy
 * 
 * @version 13/04/2017
 * 
 * @see environment.model.locations.Location
 * @see environment.model.roadusers.RoadUser
 *
 */
public class Till extends Location implements Cloneable {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 */
	private static final int MAX_QUEUE_SIZE = 0;

	/**
	 * A {@link Random} generator for all the {@link Till}s to use.
	 */
	private static final Random GEN = new Random();

	/**
	 * The minimum time a {@link RoadUser} must spend in at the front of the
	 * {@link Till}s {@link Location#queue}.
	 */
	private static final int MIMIMUM_TIME_TO_SPEND = 12;

	/**
	 * The range of time from the maximum to minimum time the {@link RoadUser}
	 * must spend in at the front of the {@link Till}s {@link Location#queue}.
	 * 
	 * @see #MIMIMUM_TIME_TO_SPEND
	 */
	private static final int RANGE_OF_TIME_TO_SPEND = 6;

	/**
	 * The number of ticks remaining that the {@link RoadUser} must spend at the
	 * front of the for the {@link Till}s {@link Location#queue}.
	 */
	private int timeToSpend;

	/**
	 * Constructs a new {@link Till}.
	 * 
	 * @param nextLocation
	 *            <code>Class&lt;? extends {@link Location}&gt;</code> of the
	 *            next location type. Set to null if this is the last location
	 *            in the location chain.
	 */
	public Till(Class<? extends Location> nextLocation) {
		super(nextLocation, MAX_QUEUE_SIZE);
		this.timeToSpend = GEN.nextInt(RANGE_OF_TIME_TO_SPEND) + MIMIMUM_TIME_TO_SPEND;
	}

	/**
	 * The {@link RoadUser} pays for the transaction
	 * {@link getVehicle.getMaxWorth} and moves to the <code>this</code>
	 * {@link Location} The transaction is added to the {@link profit}
	 * 
	 * @see environment.model.roadusers.RoadUser
	 * @see environment.model.locations.Location
	 */
	@Override
	public void processQueue(Map<RoadUser, Location> toMove) {

		/*
		 * Retrieves and removes the Road User from the queue and move it to the
		 * next Location. The value of the vehicle's tank is retrieved and is
		 * added to the profit The time spent by Road User is then incremented
		 *
		 */
		// If the first element of the queue in not null.
		if (queue.peek() != null) {

			// If the time to spend at the front of the queue is zero.
			if (timeToSpend == 0) {

				// The Road User (first element of the queue) is then retrieved
				// and removed.
				RoadUser tempRoadUser = queue.poll();

				// The Road User is then moved to the next location.
				toMove.put(tempRoadUser, this);

				// Retrieves the value of the vehicle's tank and adds it to the
				// profit.
				profit += tempRoadUser.getVehicle().getMaxWorth();

				// Accepts the Road User's payment for fuel.
				tempRoadUser.pay();

				// The number of Road Users passing through the queue is then
				// incremented.
				roadUsersProcessed++;

				timeToSpend = GEN.nextInt(RANGE_OF_TIME_TO_SPEND) + MIMIMUM_TIME_TO_SPEND;

			} else {

				// Wait a tick
				timeToSpend--;

			}

		}

	}

	@Override
	public Till clone() {

		// Return a clone of this location as a Till.
		return (Till) super.cloneLocation(new Till(this.getNextLocation()));

	}

}
