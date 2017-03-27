package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.locations.Location;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;

public class testTill {

	@Before
	public void setUp() throws Exception {
		
		Location testTill = new Till(ShoppingArea.class);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
