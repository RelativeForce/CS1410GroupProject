package environment.model.locations;

import java.util.Map;

import environment.model.roadUsers.RoadUser;

/**
 * 
 * Place where a Road user is going to wait. The amount of time spent can vary
 * for different {@link RoadUser}s.
 *
 * @author Karandeep_Saini
 * @version 17/03/2017
 * @see environment.model.locations.Locations
 * @see environment.model.roadUsers.RoadUser
 */
public class WaitingArea extends Location {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadUsers.RoadUser
	 */
	private static final int MAX_QUEUE_SIZE = 0;

	public WaitingArea(Class<? extends Location> nextLocation) {
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
	 * @see environment.model.locations.Location#returnToQueue(environment.model.roadUsers.RoadUser)
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

}
