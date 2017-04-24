package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;

/**
 * 
 * @author Joshua_Eddy
 * @author Karendeep_Saini
 * 
 * @version 24/04/17
 */
public class testPump {

	Pump testPump;

	@Before
	public void setUp() throws Exception {

		testPump = new Pump(ShoppingArea.class);
	}

	@Test
	public void testProcessQueue() {

		// Assert that the number of road users that testPump has processed is
		// initially zero.
		assertTrue(testPump.getRoadUsersProcessed() == 0);

		RoadUser testRoadUser = new Motorbike_RoadUser();

		testPump.enter(testRoadUser);

		assertTrue(testPump.getRoadUsersProcessed() == 0);

		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		while (toMove.isEmpty()) {
			testPump.processQueue(toMove);
		}

		assertTrue(testPump.getRoadUsersProcessed() == 0);

		assertTrue(testRoadUser.getVehicle().isFull());

		assertTrue(testPump.queue.size() == 1);

		assertTrue(!testRoadUser.doneShopping());

		assertTrue(!testRoadUser.isShopping());

		assertTrue(!testRoadUser.hasPaid());

		toMove.remove(testRoadUser);

		assertTrue(toMove.isEmpty());

		testRoadUser.shop();

		testPump.processQueue(toMove);

		assertTrue(toMove.isEmpty());

	}

}
