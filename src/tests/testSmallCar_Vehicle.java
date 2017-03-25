package tests;

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
public final class testSmallCar_Vehicle {
	
	/**
	 * The list containing {@link SmallCar_Vehicle} objects to be tested.
	 */
	private List<SmallCar_Vehicle> smallCars;
	
	/**
	 * Before a <code>@Test</code> method is run, renew the {@link #smallCars}
	 * to contain a list of unmodified {@link SmallCar_Vehicle} objects.
	 */
	@Before
	public void setUp() {
		
		smallCars = Stream.generate(SmallCar_Vehicle::new)
				.limit(300)
				.collect(Collectors.toList());
	}
	/**
	 * Test if all {@link SmallCar_Vehicle} objects have the correct size
	 */
	@Test
	public void testSize(){
		
		assertTrue(smallCars.stream().allMatch(sc -> sc.size == 1.0));
	}
	/**
	 * Test to see if all {@link SmallCar_Vehicle} objects have the correct
	 * {@link Vehicle#tankSize}, from the {@link #smallCars} sample space, all entries
	 * should be between 7 and 9, and every tankSize from 7 to 9 must appear at least
	 * once.
	 */
	@Test
	public void testTankSize(){
		
		//At least one car should have a tank size of 7.
		assertTrue(smallCars.stream().anyMatch(sc -> sc.tankSize == 7));
		
		//At least one car should have a tank size of 8.
		assertTrue(smallCars.stream().anyMatch(sc -> sc.tankSize == 8));
		
		//At least one car should have a tank size of 9.
		assertTrue(smallCars.stream().anyMatch(sc -> sc.tankSize == 9));
		
		//No car is allowed to have a tank size less than 7 or greater than 9.
		assertTrue(smallCars.stream().allMatch(
				sc-> 7 <= sc.tankSize && sc.tankSize <= 9));
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
		assertTrue(smallCars.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars until full.
		assertTrue(smallCars.stream().map(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
			
			return sc;
			
		}).allMatch(Vehicle::isFull));
		
		//Filling the cars further still results in the cars being full.
		assertTrue(smallCars.stream().map(sc -> {
			
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
		assertTrue(smallCars.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars
		smallCars.forEach(SmallCar_Vehicle::fill);
		
		//No car has been filled up fully
		assertTrue(smallCars.stream().noneMatch(Vehicle::isFull));
		
		//Fill all cars.
		smallCars.forEach(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
		});
		
		//All cars are full.
		assertTrue(smallCars.stream().allMatch(Vehicle::isFull));
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
		assertTrue(smallCars.stream().allMatch(sc -> 0 == sc.getCurrentWorth()));
		
		//Fill the cars and check that the current worth is greater than 0.
		smallCars.stream().map(sc -> {
			
			sc.fill();
			return sc;
			
		}).forEach(sc -> assertTrue(0.0 < sc.getCurrentWorth()));
		
		//The worth after filling the car is not the same as its previous worth.
		assertTrue(smallCars.stream()
				.noneMatch(sc -> {
					
					double temp = sc.getCurrentWorth();
					
					sc.fill();
					
					return temp == sc.getCurrentWorth();
				}));
		
		//Fill all the cars
		smallCars.forEach(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
		});
		
		//All cars have now reached their max worth.
		assertTrue(smallCars.stream()
				.allMatch(sc -> sc.getCurrentWorth() == sc.getMaxWorth()));
		
		//Fill the cars again will not change the current worth.
		smallCars.forEach(Vehicle::fill);
		assertTrue(smallCars.stream()
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
		
		smallCars.forEach(sc1 -> {
			
			smallCars.forEach(sc2 -> {
				
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
		assertTrue(smallCars.stream().anyMatch(sc -> sc.equals(testSc)
				&& sc.size == testSc.size
				&& sc.tankSize == testSc.tankSize));
		
		//Small cars cannot be equal to other Vehicles.
		assertTrue(smallCars.stream()
				.noneMatch(sc -> sc.equals(new Motorbike_Vehicle())));
		assertTrue(smallCars.stream()
				.noneMatch(sc -> sc.equals(new FamilySedan_Vehicle())));
	}
	/**
	 * Test the {@link Vehicle#toString()} method in the {@link SmallCar_Vehicle}
	 * class.
	 * 
	 * <p>
	 * Check that the {@link String} produced by the {@link SmallCar_Vehicle} is
	 * produced correctly.
	 * </p>
	 */
	@Test
	public void testToString(){
		
		//All cars follow the same String format.
		assertTrue(smallCars.stream()
				.allMatch(sc -> new StringBuilder()
						.append("Small car. Size: ")
						.append(sc.size)
						.append(" Tank (Gallons): ")
						.append(sc.tankSize)
						.toString()
						.equals(sc.toString())));
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
		assertTrue(smallCars.stream().noneMatch(sc -> sc == sc.clone()));
		
		//Clones must equal the original object.
		assertTrue(smallCars.stream().allMatch(sc -> sc.equals(sc.clone())));
		
		/*
		 * Changing the state of the clone does not change the state of the
		 * original object
		 */
		assertTrue(smallCars.stream().noneMatch(sc -> {
			
			SmallCar_Vehicle scClone = sc.clone();
			
			while(!scClone.isFull()){
				
				scClone.fill();
			}
			
			return sc.isFull() == scClone.isFull();
		}));
		
		//Clones must share the same state as the original object.
		assertTrue(smallCars.stream().map(sc -> {
			
			while(!sc.isFull()){
				
				sc.fill();
			}
			
			return sc;
		}).allMatch(sc -> sc.isFull() == sc.clone().isFull()));
	}
}