package environment.model;

import static org.junit.Assert.*;

import org.junit.Test;

import environment.model.Station;
import environment.model.locations.*;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

/**
 * 
 * Tests for all the aspects of the {@link Station}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 21/04/17
 *
 * @see environment.model.Station
 */
public class TestStation {

	/**
	 * A global {@link Station} for use and reassignment by multiple tests.
	 */
	private Station station;

	/**
	 * Initialises {@link #station}.
	 */
	private void setUp(){
		station = new Station(Pump.class);
	}

	/**
	 * Tests {@link Station#addLocation(TestLocation)}.
	 */
	@Test
	public void testAddLocation() {

		setUp();
		
		// Assert that the number of location in the station is initially zero.
		assertTrue(station.getNumberOfLoactions() == 0);

		// Asses whether the station will allow null locations to be added.
		station.addLocation(null);

		// Assert the the station will not allow the station to add null
		// locations by ensuring that the number of locations is still zero.
		assertTrue(station.getNumberOfLoactions() == 0);

		// Asses whether a location can be added to the station successfully.
		Location testLocation = new Pump(ShoppingArea.class);
		station.addLocation(testLocation);

		// Assert that the number of locations in the station has increased by
		// one.
		assertTrue(station.getNumberOfLoactions() == 1);

		// Asses whether the same location can be added to the station more than
		// once.
		station.addLocation(testLocation);

		// Assert that the station will not allow the same location to be added
		// to the station more than once by ensuring that the number of
		// locations in the station has not changed.
		assertTrue(station.getNumberOfLoactions() == 1);

	}

	/**
	 * Test {@link Station#enter(RoadUser)}.
	 */
	@Test
	public void testEnter() {
		
		// Assert that the number of road users in the station is initially
		// zero.
		assertTrue(station.getNumberOfRoadUsers().sum() == 0);

		// Asses whether adding a null road user to the station is logged as a
		// road user entering the station.
		station.enter(null);

		// Assert that the number of road users has not changed and is still
		// zero.
		assertTrue(station.getNumberOfRoadUsers().sum() == 0);

		// Asses whether adding a road user can be added to the station.
		RoadUser testRoadUser = new SmallCar_RoadUser();

		// Add a test road user to the station.
		station.enter(testRoadUser);

		// Assert that the road user has been added and the number of road users
		// in the station has increased by one.
		assertTrue(station.getNumberOfRoadUsers().sum() == 1);

		// Asses whether the station will allow the same road user to the
		// station.
		station.enter(testRoadUser);

		// Assert that the station will not allow the same road user to enter
		// the station twice by ensuring that the amount of road users in the
		// station has not incremented.
		assertTrue(station.getNumberOfRoadUsers().sum() == 1);

	}

	/**
	 * Test {@link Station#toString()}
	 */
	@Test
	public void testToString() {

		setUp();

		// The correct format of the String that the toString() of Station.java
		// should return.
		String testStationString = "";

		testStationString += "Number of Vehicles:          " + 0 + "\n";
		testStationString += "Number of Rejected Vehicles: " + 0 + "\n";
		testStationString += "Petrol profit:              �" + 0.0 + "\n";
		testStationString += "Lost petrol profit:         �" + 0.0 + "\n";
		testStationString += "Shopping profit:            �" + 0.0 + "\n";
		testStationString += "Total profit:               �" + 0.0 + "\n";
		testStationString += "Total lost profit:          �" + 0.0 + "\n";

		// The actual toString() of the Station.
		String stationString = station.toString();

		// Assert that the toString() of that station is correct.
		assertTrue(testStationString.equals(stationString));

	}

	/**
	 * Test {@link Station#equals(Object)}.
	 */
	@Test
	public void testEquals() {

		assertTrue(station.equals(station));

		Station testStation1 = new Station(Pump.class);

		assertFalse(station.equals(testStation1));

		Station testStation2 = new Station(Pump.class);

		assertTrue(testStation1.equals(testStation2));

		Station testStation3 = new Station(ShoppingArea.class);

		assertFalse(testStation2.equals(testStation3));

		RoadUser testRoadUser = new SmallCar_RoadUser();
		testStation1.enter(testRoadUser);

		assertFalse(testStation1.equals(testStation2));

		Location testLocation = new Pump(ShoppingArea.class);
		testStation1.addLocation(testLocation);

		assertFalse(testStation1.equals(testStation2));

	}

	/**
	 * Test {@link Station#getNumberOfRoadUsers()}.
	 * 
	 * @see environment.model.Station
	 */
	@Test
	public void testGetNumberOfRoadUsers() {

		newBasicStation();

		assertTrue(station.getNumberOfRoadUsers().sum() == 0.0);

		assertTrue(station.getNumberOfRoadUsers().get(SmallCar_RoadUser.class) == 0.0);

		station.enter(new SmallCar_RoadUser());

		assertTrue(station.getNumberOfRoadUsers().get(SmallCar_RoadUser.class) == 1.0);

		for (int index = 0; index < 300; index++) {
			station.processLocations();
		}

		assertTrue(station.getNumberOfRoadUsers().get(SmallCar_RoadUser.class) == 0.0);

		station.enter(new Truck_RoadUser());

		assertTrue(station.getNumberOfRoadUsers().get(Truck_RoadUser.class) == 1.0);

		for (int index = 0; index < 300; index++) {
			station.processLocations();
		}

		assertTrue(station.getNumberOfRoadUsers().get(Truck_RoadUser.class) == 0.0);
		
		

	}

	/**
	 * Initialises and constructs a {@link Station} with 1 {@link Pump}, 1
	 * {@link ShoppingArea} and 1 {@link Till}.
	 * 
	 * @see environment.model.locations
	 */
	private void newBasicStation() {

		// Initialise the station
		setUp();

		// Add the locations
		station.addLocation(new Pump(ShoppingArea.class));
		station.addLocation(new ShoppingArea(Till.class));
		station.addLocation(new Till(null));

	}
}
