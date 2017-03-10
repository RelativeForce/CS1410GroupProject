package environment.model.locations;

import java.util.HashMap;

import environment.model.roadUsers.RoadUser;

/**
 * Place where {@link RoadUser}s will fill up their vehicles
 * 
 * 
 * @author Karandeep_Saini
 * @version 10/03/2017
 * 
 * @see environment.model.locations.Location
 * @see environment.model.roadUsers.RoadUser
 */
public final class Pump extends Location {

	/**
	 * The maximum amount of unit space available at <code>this</code>
	 * {@link Location}.
	 * 
	 * @see environment.model.locations.Location
	 * @see environment.model.roadUsers.RoadUser
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
	public void processQueue(HashMap<RoadUser, Location> toMove) {
		// TODO Auto-generated method stub

	}

}
