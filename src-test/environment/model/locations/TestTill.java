package environment.model.locations;

import static org.junit.Assert.*;

import org.junit.Test;

import environment.model.locations.TestLocation;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;

public class TestTill extends TestLocation {

	private static Location testTill = new Till(ShoppingArea.class);

	public TestTill() {
		super(testTill);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
