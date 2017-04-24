package environment.model.locations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import environment.model.roadusers.RoadUser;

/**
 * 
 * Place where a Road user is going to wait. The amount of time spent can vary
 * for different {@link RoadUser}s.
 *
 * @author Karandeep_Saini
 * @author Joshua_Eddy
 * 
 * @version 24/04/2017
 * 
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

	/**
	 * Constructs a new {@link ShoppingArea}.
	 * 
	 * @param nextLocation
	 *            <code>Class&lt;? extends {@link Location}&gt;</code> of the
	 *            next location type. Set to null if this is the last location
	 *            in the location chain.
	 */
	public ShoppingArea(Class<? extends Location> nextLocation) {
		super(nextLocation, MAX_QUEUE_SIZE);

	}

	@Override
	public void processQueue(Map<RoadUser, Location> toMove) {

		// Holds all the elements in toMove that are to be removed. This map
		// exists to overcome the ConcurrentModificationException thrown when a
		// Map is modified while being iterated through.
		List<RoadUser> toRemoveFrom_queue = new LinkedList<RoadUser>();

		// Iterates through the queue.
		for (RoadUser roadUser : queue) {

			// Checks if the Road User carries out shopping.
			if (roadUser.willShop() || roadUser.isShopping()) {

				// Checks if the Road User has completed shopping.
				if (roadUser.doneShopping()) {

					// Retrieves the value of the vehicle's tank and adds it to
					// the profit.
					profit += roadUser.getWorth();

					// The Road User is then moved to the next location.
					toMove.put(roadUser, this);

					// Add the Road User to the list of road users to be removed
					// from the queue.
					toRemoveFrom_queue.add(roadUser);

					// Otherwise
				} else {

					// The Road User's time spent in the shopping is
					// incremented.
					roadUser.shop();
				}

				// Otherwise
			} else {

				// The Road User is moved to the next location.
				toMove.put(roadUser, this);

				// Add the Road User to the list of road users to be removed
				// from the queue.
				toRemoveFrom_queue.add(roadUser);

			}

		}

		// Iterate through all the elements from toRemoveFrom_queue and remove
		// them from toMove.
		for (RoadUser roadUser : toRemoveFrom_queue) {
			queue.remove(roadUser);
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

		// Return a clone of this location as a ShoppingArea
		return (ShoppingArea) super.cloneLocation(new ShoppingArea(this.nextLocation));

	}

	@Override
	public boolean canContain(RoadUser roadUser){
		
		if(super.canContain(roadUser)){
			
			// If the next location is a Shopping Area and the current
			// road user is already shopping then don't add it to that
			// location.
			if(!(roadUser.isShopping() || roadUser.doneShopping())){
				
				return true;
			}
		}
		return false;
	}
	
}
