package environment.model.locations;

import java.util.HashMap;
import environment.model.roadUsers.RoadUser;
/**
 * 
 * Place where a {@link RoadUser} pays for the transaction.
 * 
 * @author Karandeep_Saini
 * @version 10/03/2017
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processQueue(HashMap<RoadUser, Location> toMove) {
		// TODO Auto-generated method stub

	}

}
