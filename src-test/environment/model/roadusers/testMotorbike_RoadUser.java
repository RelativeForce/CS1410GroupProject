package environment.model.roadusers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.Motorbike_RoadUser;

public class testMotorbike_RoadUser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetWorth() {
		Motorbike_RoadUser test = new Motorbike_RoadUser();
		double actual = test.getWorth();
		double expected = 10.0;
		assertTrue(expected == actual);
	}

	@Test
	public void testWillShop() {
		Motorbike_RoadUser test = new Motorbike_RoadUser();
		assertTrue(test.willShop());
		assertFalse(test.willShop());
	}
}
