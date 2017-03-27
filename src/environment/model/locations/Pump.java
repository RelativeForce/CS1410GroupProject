package environment.model.locations;

import java.util.Map;

import environment.model.roadusers.RoadUser;

/**
 * Place where {@link RoadUser}s will fill up their vehicles
 * 
 * 
 * @author Karandeep_Saini
 * @version 27/03/2017
 * 
 * @see environment.model.locations.Location
 * @see environment.model.roadusers.RoadUser
 */
public final class Pump extends Location implements Cloneable {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 */
	private static final int MAX_QUEUE_SIZE = 3;

	/**
	 * Constructs a new {@link Pump} which is a concrete subclass of
	 * {@link Location}.
	 * 
	 * @param nextLocation
	 *            The <code>Class</code> of the type of the next
	 *            {@link Location} that <code>this</code> will move the
	 *            {@link RoadUser}s contained with in it too.
	 * @see environment.model.locations.Location
	 * 
	 */
	public Pump(Class<? extends Location> nextLocation) {
		super(nextLocation, MAX_QUEUE_SIZE);

	}

	@Override
	public void processQueue(Map<RoadUser, Location> toMove) {

		// Retrieves the Road User from the queue.
		RoadUser roadUserAtPump = queue.peek();

		if (roadUserAtPump != null) {

			// Checks if the Vehicle has finished filling.
			if (roadUserAtPump.getVehicle().isFull()) {

				// Checks if the Road User has completed the shopping.
				if (roadUserAtPump.hasPaid()) {

					// The Road User is removed from the queue.
					queue.removeFirst();
				} else {

					// The road user is moved to the next location.
					toMove.put(roadUserAtPump, this);
				}
			} else {

				// The Road User is required to fill the vehicle.
				roadUserAtPump.getVehicle().fill();

				// The time spent by each Road User is then incremented.
				queue.forEach(RoadUser::spendTime);
			}
		}
	}

	/**
	 * Create an exact copy of this {@link Pump} which is a subclass of
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Pump
	 */
	@Override
	public Pump clone() {

		Pump clone = new Pump(this.getNextLocation());
		clone.maxQueueSize = this.maxQueueSize;
		clone.profit = this.profit;
		clone.queue = super.cloneQueue();
		clone.roadUsersProcessed = this.roadUsersProcessed;

		return clone;
	}

}
