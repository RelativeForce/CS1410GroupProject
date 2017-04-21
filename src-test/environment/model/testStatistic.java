package environment.model;

import static org.junit.Assert.*;

import org.junit.Test;

import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

public class testStatistic {

	Statistic statistic;

	private void setUp() {
		statistic = new Statistic();
	}

	@Test
	public void testSum() {

		setUp();

		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;

		assertTrue(statistic.sum() == 0.0);

		statistic.update(testClass1, 1);
		statistic.update(testClass1, 3);
		statistic.update(testClass1, 1);

		assertTrue(statistic.sum() == 5.0);

		statistic.update(Truck_RoadUser.class, 2);
		statistic.update(Motorbike_RoadUser.class, 3);

		assertTrue(statistic.sum() == 10.0);

		statistic.update(testClass1, -3);

		assertTrue(statistic.sum() == 7.0);

		statistic.update(testClass1, -3.6);

		assertTrue(statistic.sum() == 3.4);

	}

	@Test
	public void testGet() {

		setUp();

		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;
		Class<? extends RoadUser> testClass2 = Truck_RoadUser.class;

		assertTrue(statistic.get(testClass1) == 0.0);

		statistic.update(testClass1, 0);

		assertTrue(statistic.get(testClass1) == 0.0);

		statistic.update(testClass1, 2);

		assertTrue(statistic.get(testClass1) == 2);

		statistic.update(testClass2, -2);

		assertTrue(statistic.get(testClass2) == -2);

	}

	@Test
	public void testClone() {

		setUp();

		Class<? extends RoadUser> testClass1 = SmallCar_RoadUser.class;


		statistic.update(testClass1, 4);
		
		Statistic statisticClone = statistic.clone();
		
		assertTrue(statisticClone.get(testClass1) == 4);
		
		assertTrue(statisticClone.sum() == 4);
		
	}

}
