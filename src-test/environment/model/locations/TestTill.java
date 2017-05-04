package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import environment.model.locations.TestLocation;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;

/**
 * Tests {@link Till}. This test class tests the functionality of:
 * 
 * <ul>
 * <li>{@link Till#processQueue(Map)}</li>
 * <li>{@link Till#compare(Location)}</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 * @author Karandeep_Saini
 * 
 * @version 02/05/17
 * 
 * @see environment.model.locations.Till
 */
public class TestTill extends TestLocation {

	/**
	 * Holds an instance of {@link Till} to be used for testing purposes.
	 * 
	 * @see environment.model.locations.Till
	 */
	private static Location testTill = new Till(null);

	/**
	 * Constructs a new {@link Till} test.
	 */
	public TestTill() {
		super(testTill);
	}

	/**
	 * This tests asserts that {@link Till#compare(Location)} will return true
	 * if:
	 * <ul>
	 * <li>The parameter location is also a {@link Till}</li>
	 * <li>The queue at the parameter location is longer than the queue at the
	 * {@link Till} that invoked the method.</li>
	 * </ul>
	 * 
	 * @see environment.model.locations.Till
	 * 
	 */
	@Test
	@Override
	public void testCompare() {

		// Clear the queue of the test Till
		testTill.queue.clear();

		// Create a Location for test Till to be compared to.
		Location testLocation = new Till(null);

		// Create a road user to be used in the test.
		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Add the road user to the new Till
		testLocation.enter(testRoadUser);

		// The testTill.compare() should return true as the testLocation is also
		// a Till and has a longer queue than the test Till.
		assertTrue(testTill.compare(testLocation));

		// Swap the test location for a ShoppingArea
		testLocation = new ShoppingArea(Till.class);

		// The testTill.compare() should return false as you cannot compare a
		// pump to a ShoppingArea.
		assertTrue(!testTill.compare(testLocation));

	}

	/**
	 * Tests {@link Till#processQueue(Map)}:
	 * <ul>
	 * <li>Makes the {@link RoadUser} at the end of the queue wait for 12-18
	 * ticks</li>
	 * </ul>
	 */
	@Test
	@Override
	public void testProcessQueue() {

		testTill.queue.clear();

		RoadUser testRoadUser = new SmallCar_RoadUser();

		testTill.enter(testRoadUser);

		int mimumumTicks = 12;

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		for (int ticksSpent = 0; ticksSpent < mimumumTicks; ticksSpent++) {
			testTill.processQueue(toMove);
			assertTrue(!testRoadUser.hasPaid());
		}
		
		

	}

}
