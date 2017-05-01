package environment.model.roadusers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.Truck_RoadUser;

public class TestTruck_RoadUser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetWorth() {
		Truck_RoadUser test = new Truck_RoadUser();
		double actual = test.getWorth();
		double expected = 10.0;
		assertTrue(expected == actual);
	}

	@Test
	public void testWillShop() {
		Truck_RoadUser test = new Truck_RoadUser();
		assertTrue(test.willShop());
		assertFalse(test.willShop());
	}

	@Test
	public void testHasPaid() {
		Truck_RoadUser test = new Truck_RoadUser();
		assertTrue(test.hasPaid());
		assertFalse(test.hasPaid());
	}

	@Test
	public void testDoneShopping() {
		Truck_RoadUser test = new Truck_RoadUser();
		assertTrue(test.doneShopping());
		assertFalse(test.doneShopping());
	}
}
