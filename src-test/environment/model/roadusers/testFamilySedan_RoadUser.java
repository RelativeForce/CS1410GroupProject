package environment.model.roadusers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.FamilySedan_RoadUser;

public class testFamilySedan_RoadUser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetWorth() {
		FamilySedan_RoadUser test = new FamilySedan_RoadUser();
		double actual = test.getWorth();
		double expected = 10.0;
		assertTrue(expected == actual);	
	}
	
	@Test
	public void testWillShop(){
		FamilySedan_RoadUser test = new FamilySedan_RoadUser();
		assertTrue (test.willShop());
		assertFalse(test.willShop());
	}
	
	@Test
	public void testHasPaid(){
		FamilySedan_RoadUser test = new FamilySedan_RoadUser();
		assertTrue (test.hasPaid());
		assertFalse(test.hasPaid());
	}
	
	@Test
	public void testDoneShopping(){
		FamilySedan_RoadUser test = new FamilySedan_RoadUser();
		assertTrue (test.doneShopping());
		assertFalse(test.doneShopping());
	}
}
