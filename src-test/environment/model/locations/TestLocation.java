package environment.model.locations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;

/**
 * 
 * This is the test class for {@link Location}, It tests the:
 * 
 * <ul>
 * <li>{@link Location#enter(RoadUser)}</li>
 * <li>{@link Location#returnToQueue(RoadUser)}</li>
 * <li>{@link Location#clone()}</li>
 * </ul>
 * 
 * All test classes for the subclasses of {@link Location} must extend from
 * this. {@link #TestLocation(Location)} describes how this test class is
 * implemented in the testing subclasses.
 * 
 * @author Joshua_Eddy
 * 
 * @version 02/05/2017
 * 
 * @see environment.model.locations.Location
 *
 */
public abstract class TestLocation {

	/**
	 * A {@link Location} that will be assigned to as a concrete subclass. This
	 * allows the testing of all the common functionality of {@link Location}.
	 */
	private Location testLocation;

	/**
	 * This constructs an new {@link TestLocation}. Calling this constructor
	 * initialises the parameter {@link Location} as the {@link Location} that
	 * all the tests contained with {@link TestLocation} will be performed on as
	 * these are the basic functions of a {@link Location}.
	 * 
	 * @param testLocation
	 *            An instance of a subclass of {@link Location} for which these
	 *            general tests will be performed.
	 */
	public TestLocation(Location testLocation) {

		this.testLocation = testLocation;

	}

	/**
	 * Tests the {@link Location#enter(RoadUser)} method. This method should
	 * accept a {@link RoadUser} and if it is;
	 * <ul>
	 * <li>Not NULL</li>
	 * <li>Not already in <code>this</code> {@link Location}</li>
	 * </ul>
	 * 
	 * If the {@link Location#maxQueueSize} is gretater than zero then that will
	 * be tested also. If there is any additional functionality to this method
	 * in a subclass of location then this method should be overrided and called
	 * in the test subclass.
	 */
	@Test
	public void testEnter() {

		testLocation.queue.clear();

		// Create a new road user for the test.
		RoadUser testSmallCar = new SmallCar_RoadUser();

		// Assert that the test locaton's queue is empty to start with.
		assertTrue(testLocation.queue.size() == 0);

		// Add a null road user to the location.
		testLocation.enter(null);

		// Assert that the null road user was not allowed into the location.
		assertTrue(testLocation.queue.size() == 0);

		// Add the test road user.
		testLocation.enter(testSmallCar);

		// Assert that the valid road user was added.
		assertTrue(testLocation.queue.size() == 1);

		// Attempt to add the same road user a second time.
		testLocation.enter(testSmallCar);

		// Assert that the second instance of the valid road user was not added.
		assertTrue(testLocation.queue.size() == 1);

		testLocation.queue.clear();

		// If the location has a max queue size.
		if (testLocation.maxQueueSize > 0) {
			testMaxQueueSize();
		}

	}

	/**
	 * Test the {@link Location#maxQueueSize}. If the
	 * {@link Location#maxQueueSize} is greater than zero then the
	 * {@link #testLocation} must be shorter than value.
	 * 
	 * @see Location#queue
	 * @see Location#maxQueueSize
	 */
	private void testMaxQueueSize() {

		testLocation.queue.clear();

		// Create a list to hold a collection of road users
		LinkedList<RoadUser> testQueue = new LinkedList<RoadUser>();

		// Holds the cumulative value of all road users vehicle sizes.
		double queueSize = 0.0;

		// Iterate until the size of the queue of small cars is longer than the
		// make queue size.
		while (testLocation.maxQueueSize >= queueSize) {

			// Temporary road user to be added to testQueue
			RoadUser tempRoadUser = new SmallCar_RoadUser();
			/*
			 * Add the size of the temporary road users vehicle to the leng6hof
			 * the queue then add the road user to the testQueue.
			 */
			queueSize += tempRoadUser.getVehicle().size;
			testQueue.add(tempRoadUser);
		}

		// Attempt to add each road user in the test queue
		testQueue.forEach(thisRoadUser -> {
			testLocation.enter(thisRoadUser);
		});

		/*
		 * The loop prior opens adds small cars to the test queue until it
		 * exceeds the max queue size. This means that if the test location
		 * functions properly there should be one more road user in the test
		 * queue than in the test locations queue.
		 */
		assertTrue(testLocation.queue.size() == testQueue.size() - 1);

		testLocation.queue.clear();
	}

	/**
	 * This test {@link Location#returnToQueue(RoadUser)} method.
	 * 
	 * @see environment.model.locations.Location
	 */
	@Test
	public void testReturnToQueue() {

		testLocation.queue.clear();

		RoadUser testRoadUser = new FamilySedan_RoadUser();

		testLocation.returnToQueue(testRoadUser);

		assertTrue(testLocation.queue.get(0).equals(testRoadUser));

		testLocation.queue.clear();

	}

	/**
	 * Tests whether {@link Location} increments the time spent for all of the
	 * road users inside it each time it is processed.
	 * 
	 * @see environment.model.locations.Location
	 */
	@Test
	public void testTimeIncrements() {

		testLocation.queue.clear();

		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Assert that the road user has not spent any time anywhere.
		assertTrue(testRoadUser.getTimeSpent() == 0);

		// Add the road user to the pump.
		testLocation.enter(testRoadUser);

		// Imitates the toMove map in Station
		Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();

		// Repeatedly assert that the every time the pump is processed the time
		// spent of the test road user is incremented also.
		for (int timeSpent = 1; timeSpent < 20; timeSpent++) {

			testLocation.processQueue(toMove);
			assertTrue(testRoadUser.getTimeSpent() == timeSpent);

		}

		testLocation.queue.clear();

	}

	/**
	 * This tests asserts that {@link Location#compare(Location)} will return
	 * true if:
	 * <ul>
	 * <li>The parameter {@link Location} is an instance of the same subclass of
	 * {@link Location}</li>
	 * <li>The parameter {@link Location} is better or worse that the
	 * {@link Location} that invoked the method.</li>
	 * </ul>
	 * 
	 * @see environment.model.locations.Location
	 * 
	 */
	@Test
	public abstract void testCompare();

	/**
	 * Tests {@link Location#processQueue(Map)} which is abstact because the
	 * implementation of the method depends on the subclass of {@link Location}
	 * 
	 * @see environment.model.locations.Location
	 */
	@Test
	public abstract void testProcessQueue();
}
