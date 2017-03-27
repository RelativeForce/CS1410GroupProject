package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import environment.model.locations.*;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;

public class testShoppingArea {

	Location testShoppingArea;
	
	@Before
	public void setUp() throws Exception {
		
		testShoppingArea = new ShoppingArea(Till.class);
		
	}

	@Test
	public void testRoadUsersProcessed() {

			assertTrue(testShoppingArea.getRoadUsersProcessed() == 0);
			
			testShoppingArea.enter(new Motorbike_RoadUser());
			
			assertTrue(testShoppingArea.getRoadUsersProcessed() == 0);
			
			Map<RoadUser, Location> toMove = new HashMap<RoadUser, Location>();
			
			testShoppingArea.processQueue(toMove);
			
			assertTrue(testShoppingArea.getRoadUsersProcessed() == 1);
			

		}

}
