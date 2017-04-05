package environment.model.roadusers.vehicles;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.vehicles.FamilySedan_Vehicle;
import environment.model.roadusers.vehicles.Motorbike_Vehicle;
import environment.model.roadusers.vehicles.SmallCar_Vehicle;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * Test the functionality of the {@link SmallCar_Vehicle} class.
 * 
 * <p>
 * Check the behaviour of the {@link SmallCar_Vehicle} class in the {@link Test}
 * methods. Each {@link Test} corresponds to one method fron the {@link SmallCar_Vehicle}
 * class which will be tested.
 * </p>
 * 
 * @author 	John Berg
 * @version 23/03/2017
 * @since 	20/03/2017
 * @see		Collectors
 * @see		List
 * @see 	Stream
 * @see		SmallCar_Vehicle
 */
public final class TestSmallCar_Vehicle extends TestVehicle {
	
	public TestSmallCar_Vehicle(){
		
		super(SmallCar_Vehicle.class);
	}
	
	/**
	 * Before a <code>@Test</code> method is run, renew the {@link #smallCars}
	 * to contain a list of unmodified {@link SmallCar_Vehicle} objects.
	 */
	@Before
	public void setUp() {
		
		vehicles = Stream.generate(SmallCar_Vehicle::new)
				.limit(300)
				.collect(Collectors.toList());
	}
	/**
	 * Test to see if all {@link SmallCar_Vehicle} objects' {@link Vehicle#isFull()}
	 * method behaves as expected.
	 * 
	 * <ui>
	 * 		<li>All {@link SmallCar_Vehicle} objects must not be full at the start</li>
	 * 		<li>When a car is full, further filling the car will still result in the
	 * 		car being full</li>
	 * </ui>
	 */
	@Test
	public void testIsFull(){
		
		//No car is full from the start.
		assertTrue(vehicles.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars until full.
		assertTrue(vehicles.stream().map(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
			
			return sc;
			
		}).allMatch(Vehicle::isFull));
		
		//Filling the cars further still results in the cars being full.
		assertTrue(vehicles.stream().map(sc -> {
			
			sc.fill();
			return sc;
		}).allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#fill()} method for the expected behaviour.
	 * <ui>
	 * 		<li>No car is full at the begining</li>
	 * 		<li>Using the fill method once should not make the car full</li>
	 * </ui>
	 * 
	 * <p>
	 * Related to {@link #testIsFull()}.
	 * </p>
	 */
	@Test
	public void testFill(){
		
		//No car is filled up yet.
		assertTrue(vehicles.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars
		vehicles.forEach(Vehicle::fill);
		
		//No car has been filled up fully
		assertTrue(vehicles.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars.
		vehicles.forEach(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
		});
		
		//All cars are full.
		assertTrue(vehicles.stream().allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#getCurrentWorth()} method.
	 * 
	 * <p>
	 * When a {@link SmallCar_Vehicle} is created, the current worth will
	 * always be <code>0.0</code>. When the {@link Vehicle#fill()} method
	 * is used, the current wirth of the car will increase (unless the car
	 * is already) full.
	 * </p>
	 */
	@Test
	public void testGetCurrentWorth(){
		
		//All cars have an initial worth of 0.0.
		assertTrue(vehicles.stream().allMatch(sc -> 0 == sc.getCurrentWorth()));
		
		//Fill the cars and check that the current worth is greater than 0.
		vehicles.stream().map(sc -> {
			
			sc.fill();
			return sc;
			
		}).forEach(sc -> assertTrue(0.0 < sc.getCurrentWorth()));
		
		//The worth after filling the car is not the same as its previous worth.
		assertTrue(vehicles.stream()
				.noneMatch(sc -> {
					
					double temp = sc.getCurrentWorth();
					
					sc.fill();
					
					return temp == sc.getCurrentWorth();
				}));
		
		//Fill all the cars
		vehicles.forEach(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
		});
		
		//All cars have now reached their max worth.
		assertTrue(vehicles.stream()
				.allMatch(sc -> sc.getCurrentWorth() == sc.getMaxWorth()));
		
		//Fill the cars again will not change the current worth.
		vehicles.forEach(Vehicle::fill);
		assertTrue(vehicles.stream()
				.allMatch(sc -> sc.getCurrentWorth() == sc.getMaxWorth()));
	}
	/**
	 * Test the {@link Vehicle#getMaxWorth()} method of the cars.
	 * 
	 * <p>
	 * The max worth is is determined by the {@link Vehicle#tankSize} which
	 * means that cars with smaller tanks have a lesser max worth.
	 * </p>
	 */
	@Test
	public void testGetMaxWorth(){
		
		vehicles.forEach(sc1 -> {
			
			vehicles.forEach(sc2 -> {
				
				//If a car has a smaller tank size it also has a lesser max worth.
				assertEquals(sc1.tankSize <= sc2.tankSize,
						sc1.getMaxWorth() <= sc2.getMaxWorth());
			});
		});
	}
	/**
	 * Test the logical equality of the {@link SmallCar_Vehicle} objects.
	 * 
	 * <p>
	 * Two cars are considered equal if they have an identical {@link Vehicle#size}
	 * and {@link Vehicle#tankSize}.
	 * <ol>
	 * 		<li>Test if there exists at least two cars that equal</li>
	 * 		<li>Cars that equal must also have equal size and tanks</li>
	 * 		<li>Test if other {@link Vehicle} subclasses do not equal any car</li>
	 * </ol>
	 * </p>
	 */
	@Test
	public void testEquals(){
		
		//Car for testing equality.
		SmallCar_Vehicle testSc = new SmallCar_Vehicle();
		
		//At least one cat should equal.
		assertTrue(vehicles.stream().anyMatch(sc -> sc.equals(testSc)
				&& sc.size == testSc.size
				&& sc.tankSize == testSc.tankSize));
		
		//Small cars cannot be equal to other Vehicles.
		assertTrue(vehicles.stream()
				.noneMatch(sc -> sc.equals(new Motorbike_Vehicle())));
		assertTrue(vehicles.stream()
				.noneMatch(sc -> sc.equals(new FamilySedan_Vehicle())));
	}
	/**
	 * Test the {@link Vehicle#clone()} method from the {@link SmallCar_Vehicle}
	 * class.
	 * 
	 * <p>
	 * The created clones must:
	 * <ol>
	 * 		<li><Not share memory location with the original object</li>
	 * 		<li>Be equal to the original object</li>
	 * 		<li>Not change the state of the original object</li>
	 * 		<li>Have the same state as the original object</li>
	 * </ol>
	 * </p>
	 */
	@Test
	public void testClone(){
		
		
		//Clones must not share the same memory location.
		assertTrue(vehicles.stream().noneMatch(sc -> sc == sc.clone()));
		
		//Clones must equal the original object.
		assertTrue(vehicles.stream().allMatch(sc -> sc.equals(sc.clone())));
		
		/*
		 * Changing the state of the clone does not change the state of the
		 * original object
		 */
		assertTrue(vehicles.stream().noneMatch(sc -> {
			
			Vehicle scClone = sc.clone();
			
			while(!scClone.isFull()){
				
				scClone.fill();
			}
			
			return sc.isFull() == scClone.isFull();
		}));
		
		//Clones must share the same state as the original object.
		assertTrue(vehicles.stream().map(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
			
			return sc;
		}).allMatch(sc -> sc.isFull() == sc.clone().isFull()));
	}
}