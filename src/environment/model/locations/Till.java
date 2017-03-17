package environment.model.locations;

import java.util.Map;

import java.util.Random;

import environment.model.roadUsers.RoadUser;

/**
 * 
 * Place where a {@link RoadUser} pays for the transaction.
 * 
 * @author Karandeep_Saini
 * @version 17/03/2017
 * 
 * @see environment.model.locations.Location
 * @see environment.model.roadUsers.RoadUser
 *
 */
public class Till extends Location {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadUsers.RoadUser
	 */
	private static final int MAX_QUEUE_SIZE = 0;

	public Till(Class<? extends Location> nextLocation) {
		super(nextLocation, MAX_QUEUE_SIZE);

	}

	/**
	 * The {@link RoadUser} pays for the transaction
	 * {@link getVehicle.getMaxWorth} and moves to the <code>this</code>
	 * {@link Location} The transaction is added to the {@link profit}
	 * 
	 * @see environment.model.roadUsers.RoadUser
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

			// The Road User (first element of the queue) is then retrieved and
			// removed.
			RoadUser tempRoadUser = queue.poll();

			// The Road User is then moved to the next location.
			toMove.put(tempRoadUser, this);

			// Retrieves the value of the vehicle's tank and adds it to the
			// profit.
			profit += tempRoadUser.getVehicle().getMaxWorth();

			// Accepts the Road User's payment for fuel.
			tempRoadUser.pay();

			// The time spent by the Road User is then incremented.
			tempRoadUser.spendTime();

			// The number of Road Users passing through the queue is then
			// incremented.
			roadUsersProcessed++;

		}

	}

}
