package environment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestSimulator {

	Simulator testSim;

	@Before
	public void setUp() throws Exception {

		testSim = new Simulator();

	}

	@Test
	public void testRun() {
		try{
			testSim.start();
		}catch(Exception e){
			fail("Simulation Exception.");
		}
		
	}

}

