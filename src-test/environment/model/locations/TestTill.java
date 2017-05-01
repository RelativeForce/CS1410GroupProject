package environment.model.locations;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import environment.model.locations.TestLocation;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;

public class TestTill extends TestLocation {

	@Before
	public void setUp() throws Exception {
		
		TestLocation testTill = new Till(ShoppingArea.class);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
