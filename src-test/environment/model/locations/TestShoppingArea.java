package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;

public class TestShoppingArea extends TestLocation {

	private static Location testShoppingArea = new ShoppingArea(Till.class);

	public TestShoppingArea() {
		super(testShoppingArea);
	}

	@Test
	public void testRoadUsersProcessed() {

		assertTrue(testShoppingArea.getRoadUsersProcessed() == 0);

		testShoppingArea.enter(new Motorbike_RoadUser());

		assertTrue(testShoppingArea.getRoadUsersProcessed() == 0);

		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		testShoppingArea.processQueue(toMove);

		assertTrue(testShoppingArea.getRoadUsersProcessed() == 1);

	}

}
