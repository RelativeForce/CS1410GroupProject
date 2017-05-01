package environment.GUI.views;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import environment.model.Station;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;
import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;

public class TestGraph {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		Graph testGraphView = Graph.getInstance();

		Station testStation = generateStation();
		
		double q = 0.03;
		double p = 0.02;
		
		for(int tick = 0; tick < 100000; tick++){
			
			addRoadUser(testStation, p, q);
			
			testStation.processLocations();
			
			testGraphView.show(tick, testStation.clone());
		}
		
		

		while (!testGraphView.isClosed) {

		}

	}
	private void addRoadUser(Station testStation, double p, double q){
		
		double value = (new Random()).nextDouble();

		// If value is lower than or equal to p then add a new small car to the
		// station.
		if (SmallCar_RoadUser.exists(p, q, value)) {
			testStation.enter(new SmallCar_RoadUser());
		}

		// If value is between p and 2p then add a new motor bike to the
		// station.
		if (Motorbike_RoadUser.exists(p, q, value)) {
			testStation.enter(new Motorbike_RoadUser());
		}

		// If exists is true then add a new family sedan to the station.
		if (FamilySedan_RoadUser.exists(p, q, value)) {
			testStation.enter(new FamilySedan_RoadUser());
		}
	}

	private Station generateStation() {

		Station testStation = new Station(Pump.class);

		testStation.addLocation(new Pump(ShoppingArea.class));
		testStation.addLocation(new ShoppingArea(Till.class));
		testStation.addLocation(new Till(null));
		
		return testStation;

	}

}
