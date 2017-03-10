package environment.model.locations;

import java.util.HashMap;

import environment.model.roadUsers.RoadUser;

/**
 * 
 * Place where a Road user is going to wait. The amount of time spent can vary
 * for different {@link RoadUser}s.
 *
 * @author Karandeep_Saini
 * @version 10/03/2017
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processQueue(HashMap<RoadUser, Location> toMove) {

		// TODO Go through the queue of RoadUser(s) and determine if they have
		// been in the queue for the amount of time they denote as as there
		// MAX_TIME_TO_SHOP. This method will be run upon each tick of the
		// simulation. 1 tick = 10 seconds in the specification. Assume that the
		// RoadUsers will are present in the queue. Add and RoadUsers to the
		// toMove HashMap with the nextLocation class as the index.

	}

}
