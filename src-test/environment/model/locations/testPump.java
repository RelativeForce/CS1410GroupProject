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

public class testPump {

	Pump testPump;

	@Before
	public void setUp() throws Exception {

		testPump = new Pump(ShoppingArea.class);
	}

	@Test
	public void testRoadUsersProcessed() {

		// Assert that the number of road users that testPump has processed is
		// initially zero.
		assertTrue(testPump.getRoadUsersProcessed() == 0);
		
		testPump.enter(new Motorbike_RoadUser());
		
		assertTrue(testPump.getRoadUsersProcessed() == 0);
		
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();
		
		testPump.processQueue(toMove);
		testPump.processQueue(toMove);
		
		//...
		
		assertTrue(testPump.getRoadUsersProcessed() == 1);
		

	}

}
