package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;

/**
 * Tests {@link ShoppingArea}. This test class tests the functionality of:
 * 
 * <ul>
 * <li>{@link ShoppingArea#processQueue(Map)}</li>
 * <li>{@link ShoppingArea#compare(Location)}</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 * @author Karandeep_Saini
 * 
 * @version 02/05/17
 * 
 * @see environment.model.locations.ShoppingArea
 */
public class TestShoppingArea extends TestLocation {

	/**
	 * Holds an instance of {@link ShoppingArea} to be used for testing
	 * purposes.
	 * 
	 * @see environment.model.locations.ShoppingArea
	 */
	private static Location testShoppingArea = new ShoppingArea(Till.class);

	/**
	 * Constructs a new {@link ShoppingArea} test.
	 */
	public TestShoppingArea() {
		super(testShoppingArea);
	}

	/**
	 * This tests asserts that {@link ShoppingArea#compare(Location)} will
	 * return true if:
	 * <ul>
	 * <li>The parameter location is also a {@link ShoppingArea}</li>
	 * <li>The size of the queue in the {@link ShoppingArea} does not effect
	 * whether one {@link ShoppingArea} is better than another.</li>
	 * </ul>
	 * 
	 * @see environment.model.locations.ShoppingArea
	 * 
	 */
	@Test
	@Override
	public void testCompare() {

		// Clear the queue of the test pump
		testShoppingArea.queue.clear();

		// Create a Location for test pump to be compared to.
		Location testLocation = new ShoppingArea(ShoppingArea.class);

		// Create a road user to be used in the test.
		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Add the road user to the new pump
		testLocation.enter(testRoadUser);

		/*
		 * The testShoppingArea.compare() should return true as the testLocation
		 * is also a pump and the number of road users in the shop have no
		 * effect on one shopping area being more preferable than another.
		 */
		assertTrue(testShoppingArea.compare(testLocation));
		assertTrue(testLocation.compare(testShoppingArea));

		// Swap the test location for a ShoppingArea
		testLocation = new Pump(ShoppingArea.class);

		/*
		 * The testShoppingArea.compare() should return false as you cannot
		 * compare a pump to a ShoppingArea.
		 */
		assertTrue(!testShoppingArea.compare(testLocation));

	}

	/**
	 * This test asserts that {@link ShoppingArea}s:
	 * 
	 * <ul>
	 * <li>All the {@link RoadUser}s in the {@link ShoppingArea} are processed
	 * on every tick like a list not a queue.</li>
	 * <li>{@link RoadUser}s that will not shop are removed from the
	 * {@link ShoppingArea} after the first time
	 * {@link ShoppingArea#processQueue(Map)} is called.</li>
	 * </ul>
	 * 
	 * @see environment.model.locations.ShoppingArea
	 */
	@Test
	@Override
	public void testProcessQueue() {

		// Clear the shopping area queue
		testShoppingArea.queue.clear();

		// Create a road user to be used in the test.
		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Add the test road user to the shopping area
		testShoppingArea.enter(testRoadUser);

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		// Process the queue
		testShoppingArea.processQueue(toMove);

		// If the toMove map is empty then the road user is shopping, If there
		// is a road user in toMove that means that the road user did not shop.
		if (toMove.isEmpty()) {

			// Assert that the road user that was added is shopping.
			assertTrue(testRoadUser.isShopping());

			// Iterate until the road user is finished shopping.
			while (toMove.isEmpty()) {
				testShoppingArea.processQueue(toMove);
			}

			// Assert that the road user is done shopping.
			assertTrue(testRoadUser.doneShopping());
		}

	}

	/**
	 * Tests whether {@link Till} increments the time spent for all of the road
	 * users inside it each time it is processed.
	 * 
	 * @see environment.model.locations.Till
	 */
	@Test
	@Override
	public void testTimeIncrements() {

		testShoppingArea.queue.clear();

		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Assert that the road user has not spent any time anywhere.
		assertTrue(testRoadUser.getTimeSpent() == 0);

		// Add the road user to the pump.
		testShoppingArea.enter(testRoadUser);

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		testShoppingArea.processQueue(toMove);

		if (toMove.isEmpty()) {
			
			// Repeatedly assert that the every time the pump is processed the
			// time spent of the test road user is incremented also.
			for (int timeSpent = 2; timeSpent < 20; timeSpent++) {

				testShoppingArea.processQueue(toMove);
				assertTrue(testRoadUser.getTimeSpent() == timeSpent);

			}
		}

		testShoppingArea.queue.clear();

	}

}
