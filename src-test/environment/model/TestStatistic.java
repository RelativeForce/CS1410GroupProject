package environment.model;

import static org.junit.Assert.*;

import org.junit.Test;

import environment.Statistic;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

/**
 * Tests for all the aspects of the {@link Statistic}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 21/04/17
 * 
 * @see environment.Statistic
 * @see environment.model.roadusers.RoadUser
 *
 */
public class TestStatistic {

	/**
	 * A global {@link Statistic} for use and reassignment by multiple tests.
	 */
	private Statistic<RoadUser> statistic;

	/**
	 * Initialise {@link #statistic}.
	 */
	private void setUp() {
		statistic = new Statistic<RoadUser>();
	}

	/**
	 * Test {@link Statistic#sum()}.
	 */
	@Test
	public void testSum() {

		setUp();

		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;

		// The statistic should initially have a sum of zero.
		assertTrue(statistic.sum() == 0.0);

		statistic.update(testClass1, 1);
		statistic.update(testClass1, 3);
		statistic.update(testClass1, 1);

		// 1 + 3 + 1 = 5
		assertTrue(statistic.sum() == 5.0);

		statistic.update(Truck_RoadUser.class, 2);
		statistic.update(Motorbike_RoadUser.class, 3);

		// 5 + 2 + 3 = 10
		assertTrue(statistic.sum() == 10.0);

		statistic.update(testClass1, -3);

		// 10 - 3 = 7
		assertTrue(statistic.sum() == 7.0);

		statistic.update(testClass1, -3.6);

		// 7 - 3.6 = 3.4
		assertTrue(statistic.sum() == 3.4);

	}

	/**
	 * Test {@link Statistic#get(Class)}.
	 */
	@Test
	public void testGet() {

		setUp();

		// Two variables to hold the repeatedly user Class(es)
		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;
		Class<? extends RoadUser> testClass2 = Truck_RoadUser.class;

		// Test that when there is no value for a class zero is returned.
		assertTrue(statistic.get(testClass1) == 0.0);

		// Assign zero to the class
		statistic.update(testClass1, 0);
		assertTrue(statistic.get(testClass1) == 0.0);

		// Adds 2 to that statistic's class value.
		statistic.update(testClass1, 2);
		assertTrue(statistic.get(testClass1) == 2);

		// Adds -2 to the second test class.
		statistic.update(testClass2, -2);
		assertTrue(statistic.get(testClass2) == -2);

	}

	/**
	 * Test {@link Statistic#clone()}.
	 */
	@Test
	public void testClone() {

		setUp();

		// Two variables to hold the repeatedly user Class
		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;

		// Adds 4 to the test classes value.
		statistic.update(testClass1, 4);

		// Clone the statistic.
		Statistic<RoadUser> statisticClone = statistic.clone();

		// Check the clone is identical to the original statistic.
		assertTrue(statisticClone.get(testClass1) == 4);
		assertTrue(statisticClone.sum() == 4);

	}

}
