package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import environment.model.locations.TestLocation;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * Tests {@link Pump}. This test class tests the functionality of:
 * 
 * <ul>
 * <li>{@link Pump#processQueue(Map)}</li>
 * <li>{@link Pump#compare(Location)}</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 * @author Karandeep_Saini
 * 
 * @version 02/05/17
 * 
 * @see environment.model.locations.Pump
 */
public class TestPump extends TestLocation {

	/**
	 * Holds an instance of {@link Pump} to be used for testing purposes.
	 * 
	 * @see environment.model.locations.Pump
	 */
	private static Pump testPump = new Pump(ShoppingArea.class);

	/**
	 * Constructs a new {@link Pump} test.
	 */
	public TestPump() {
		super(testPump);
	}

	/**
	 * This test assert that at a {@link Pump} {@link RoadUser}s will:
	 * <ul>
	 * <li>Fill up their {@link Vehicle}'s tank if are at the front of the
	 * queue</li>
	 * <li>Only be removed from the front of the queue if the {@link RoadUser}
	 * has paid.</li>
	 * </ul>
	 */
	@Test
	@Override
	public void testProcessQueue() {

		testPump.queue.clear();

		testFillFrontRoadUser();

		testRoadUserHasPaid();

	}

	/**
	 * This tests asserts that {@link Pump#compare(Location)} will return true
	 * if:
	 * <ul>
	 * <li>The parameter location is also a {@link Pump}</li>
	 * <li>The queue at the parameter location is longer than the queue at the
	 * {@link Pump} that invoked the method.</li>
	 * </ul>
	 * 
	 * @see environment.model.locations.Pump
	 * 
	 */
	@Test
	@Override
	public void testCompare() {

		// Clear the queue of the test pump
		testPump.queue.clear();

		// Create a Location for test pump to be compared to.
		Location testLocation = new Pump(ShoppingArea.class);

		// Create a road user to be used in the test.
		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Add the road user to the new pump
		testLocation.enter(testRoadUser);

		// The testPump.compare() should return true as the testLocation is also
		// a pump and has a longer queue than the test pump.
		assertTrue(testPump.compare(testLocation));

		// Swap the test location for a ShoppingArea
		testLocation = new ShoppingArea(Till.class);

		// The testPump.compare() should return false as you cannot compare a
		// pump to a ShoppingArea.
		assertTrue(!testPump.compare(testLocation));

	}

	/**
	 * Test that a valid {@link RoadUser} can be added to {@link Pump} and that
	 * it is filled when it is at the front of the queue.
	 * 
	 * Sub-Test {@link #testProcessQueue()}
	 * 
	 */
	private void testFillFrontRoadUser() {

		// A valid road user
		RoadUser testRoadUser = new Motorbike_RoadUser();

		// Assert that the number of road users that testPump has processed is
		// initially zero.
		assertTrue(testPump.getRoadUsersProcessed() == 0);

		// Add the valid parameter road user to the station where it should by
		// default be at the front of the queue.
		testPump.enter(testRoadUser);

		// Assert that the road user that was added has not been immediately
		// removed from the pump.
		assertTrue(testPump.getRoadUsersProcessed() == 0);

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		// Repeatedly process the pump until the road user is added to toMove.
		while (toMove.isEmpty()) {
			testPump.processQueue(toMove);
		}

		// Assert that the pump has not processed any road users as for a road
		// user to be processed it must have paid for its fuel.
		assertTrue(testPump.getRoadUsersProcessed() == 0);

		// Assert that the pump has one vehicle in it.
		assertTrue(testPump.queue.size() == 1);

		// Assert that the vehicle at the front of the queue has a full tank.
		assertTrue(testPump.queue.get(0).getVehicle().isFull());

		// Assert that the road user who's vehicle is at the front of the queue
		// has not paid yet.
		assertTrue(!testPump.queue.get(0).hasPaid());

	}

	/**
	 * Tests that a valid parameter {@link RoadUser} will only be removed from
	 * the front of the queue once it has paid.
	 * 
	 * Sub-Test {@link #testProcessQueue()}
	 */
	private void testRoadUserHasPaid() {

		assertTrue(testPump.queue.size() == 1);

		// Causes the road user at the front of the queue to pay.
		testPump.queue.get(0).pay();

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		// In order for the pump to remove the road user at the front it must be
		// processed.
		testPump.processQueue(toMove);

		// Now that the queue has been processed the road user that was at the
		// front of the must have been removed leaving the queue empty.
		assertTrue(testPump.queue.size() == 0);

		// There should be 1 road user processed.
		assertTrue(testPump.getRoadUsersProcessed() == 1);

		// The road user should have been deleted as a copy of it is else where
		// in the station.
		assertTrue(toMove.size() == 0);

	}

}
