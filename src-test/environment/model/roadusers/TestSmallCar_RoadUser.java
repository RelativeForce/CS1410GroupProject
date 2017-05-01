package environment.model.roadusers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.SmallCar_RoadUser;

public class TestSmallCar_RoadUser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetWorth() {
		SmallCar_RoadUser test = new SmallCar_RoadUser();
		double actual = test.getWorth();
		double expected = 10.0;
		assertTrue(expected == actual);
	}

	@Test
	public void testWillShop() {
		SmallCar_RoadUser test = new SmallCar_RoadUser();
		assertTrue(test.willShop());
		assertFalse(test.willShop());
	}

	@Test
	public void testHasPaid() {
		SmallCar_RoadUser test = new SmallCar_RoadUser();
		assertTrue(test.hasPaid());
		assertFalse(test.hasPaid());
	}

	@Test
	public void testDoneShopping() {
		SmallCar_RoadUser test = new SmallCar_RoadUser();
		assertTrue(test.doneShopping());
		assertFalse(test.doneShopping());
	}
}
