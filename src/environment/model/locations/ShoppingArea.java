package environment.model.locations;

import java.util.Map;

import environment.model.roadusers.RoadUser;

/**
 * 
 * Place where a Road user is going to wait. The amount of time spent can vary
 * for different {@link RoadUser}s.
 *
 * @author Karandeep_Saini
 * @version 27/03/2017
 * @see environment.model.locations.Locations
 * @see environment.model.roadusers.RoadUser
 */
public class ShoppingArea extends Location implements Cloneable {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadusers.RoadUser
	 */
	private static final int MAX_QUEUE_SIZE = 0;

	public ShoppingArea(Class<? extends Location> nextLocation) {
		super(nextLocation, MAX_QUEUE_SIZE);

	}

	@Override
	public void processQueue(Map<RoadUser, Location> toMove) {

		// Iterates through the queue.
		for (RoadUser tempRoadUser : queue) {

			// Checks if the Road User carries out shopping.
			if (tempRoadUser.willShop()) {

				// Checks if the Road User has completed shopping.
				if (tempRoadUser.doneShopping()) {

					// Retrieves the value of the vehicle's tank and adds it to
					// the profit.
					profit += tempRoadUser.getWorth();

					// The Road User is then moved to the next location.
					toMove.put(tempRoadUser, this);

					// The Road User is then removed from the queue.
					queue.remove(tempRoadUser);

					// Otherwise
				} else {

					// The Road User's time spent in the shopping is
					// incremented.
					tempRoadUser.shop();
				}

				// Otherwise
			} else {

				// The Road User is moved to the next location.
				toMove.put(tempRoadUser, this);

				// The Road User is removed from the queue.
				queue.remove(tempRoadUser);

			}

		}

	}

	/**
	 * Places the {@link RoadUser} and adds it to the back of the queue.
	 * 
	 * @see environment.model.locations.Location#returnToQueue(environment.model.roadusers.RoadUser)
	 */

	@Override
	public void returnToQueue(RoadUser roadUser) {

		// If the queue does not contain a Road User.
		if (!queue.contains(roadUser)) {

			// Add the Road User to the back of the queue.
			queue.add(roadUser);

			// The Road user is waiting in the queue.
			roadUsersProcessed--;

		}

	}

	@Override
	public ShoppingArea clone() {
		
		ShoppingArea clone = new ShoppingArea(this.getNextLocation());
		clone.maxQueueSize = this.maxQueueSize;
		clone.profit = this.profit;
		clone.queue = super.cloneQueue();
		clone.roadUsersProcessed = this.roadUsersProcessed;

		return clone;

	}

}
