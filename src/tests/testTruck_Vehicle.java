package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import environment.model.roadusers.vehicles.Truck_Vehicle;
import environment.model.roadusers.vehicles.Motorbike_Vehicle;
import environment.model.roadusers.vehicles.SmallCar_Vehicle;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * Test the functionality of the {@link Truck_Vehicle} class.
 * 
 * <p>
 * Check the behaviour of the {@link Truck_Vehicle} class in the {@link Test}
 * methods. Each {@link Test} corresponds to one method from the {@link Truck_Vehicle}
 * class which will be tested.
 * </p>
 * 
 * @author 	John Berg
 * @version 25/03/2017
 * @since 	20/03/2017
 * @see		Collectors
 * @see		List
 * @see 	Stream
 * @see		Truck_Vehicle
 */
public final class testTruck_Vehicle {
	
	/**
	 * The list containing {@link Truck_Vehicle} objects to be tested.
	 */
	private List<Truck_Vehicle> trucks;
	
	/**
	 * Before a <code>@Test</code> method is run, renew the {@link #trucks}
	 * to contain a list of unmodified {@link Truck_Vehicle} objects.
	 */
	@Before
	public void setUp() {
		
		trucks = Stream.generate(Truck_Vehicle::new)
				.limit(300)
				.collect(Collectors.toList());
	}
	/**
	 * Test if all {@link Truck_Vehicle} objects have the correct size
	 */
	@Test
	public void testSize(){
		
		assertTrue(trucks.stream().allMatch(t -> t.size == 2.0));
	}
	/**
	 * Test to see if all {@link Truck_Vehicle} objects have the correct
	 * {@link Vehicle#tankSize}, from the {@link #trucks} sample space, all entries
	 * should be 5.
	 */
	@Test
	public void testTankSize(){
		
		//At least one truck should have one of the possible tank sizes.
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 30));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 31));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 32));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 33));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 34));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 35));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 36));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 37));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 38));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 39));
		assertTrue(trucks.stream().anyMatch(t -> t.tankSize == 40));
		
		//All trucks have a tank between of 30 and 40.
		assertTrue(trucks.stream().allMatch(
				fs -> 30 <= fs.tankSize && fs.tankSize <= 40));
	}
	/**
	 * Test to see if all {@link Truck_Vehicle} objects' {@link Vehicle#isFull()}
	 * method behaves as expected.
	 * 
	 * <ui>
	 * 		<li>All {@link Truck_Vehicle} objects must not be full at the start</li>
	 * 		<li>When a {@link Truck_Vehicle} is full, further filling still result in the
	 * 		{@link Truck_Vehicle} being full</li>
	 * </ui>
	 */
	@Test
	public void testIsFull(){
		
		//No truck is full from the start.
		assertTrue(trucks.stream().noneMatch(Vehicle::isFull));
		
		//Fill all trucks until full.
		assertTrue(trucks.stream().map(t -> {
			
			while(!t.isFull()){
				
				t.fill();
			}
			
			return t;
			
		}).allMatch(Vehicle::isFull));
		
		//Filling the trucks further still results in the cars being full.
		assertTrue(trucks.stream().map(t -> {
			
			t.fill();
			return t;
		}).allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#fill()} method for the expected behaviour.
	 * <ui>
	 * 		<li>No {@link Truck_Vehicle} is full at the begining</li>
	 * 		<li>Using the fill method once should not make the
	 * 		{@link Truck_Vehicle} full</li>
	 * </ui>
	 * 
	 * @see  #testIsFull()
	 */
	@Test
	public void testFill(){
		
		//No truck is filled up yet.
		assertTrue(trucks.stream().noneMatch(Vehicle::isFull));
		
		//Fill all trucks
		trucks.forEach(Truck_Vehicle::fill);
		
		//No truck has been filled up fully
		assertTrue(trucks.stream().noneMatch(Vehicle::isFull));
		
		//Fill all trucks fully.
		trucks.forEach(t -> {
			
			while(!t.isFull()){
				
				t.fill();
			}
		});
		
		//All truck are full.
		assertTrue(trucks.stream().allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#getCurrentWorth()} method.
	 * 
	 * <p>
	 * When a {@link Truck_Vehicle} is created, the current worth will
	 * always be <code>0.0</code>. When the {@link Vehicle#fill()} method
	 * is used, the current worth of the {@link Truck_Vehicle} will
	 * increase (unless the {@link Truck_Vehicle} is already) full.
	 * </p>
	 */
	@Test
	public void testGetCurrentWorth(){
		
		//All truck have an initial worth of 0.0.
		assertTrue(trucks.stream().allMatch(t -> 0 == t.getCurrentWorth()));
		
		//Fill the truck and check that the current worth is greater than 0.
		trucks.stream().map(t -> {
			
			t.fill();
			return t;
			
		}).forEach(t -> assertTrue(0.0 < t.getCurrentWorth()));
		
		//The worth after filling the trucks is not the same as its previous worth.
		assertTrue(trucks.stream()
				.noneMatch(t -> {
					
					double temp = t.getCurrentWorth();
					
					t.fill();
					
					return temp == t.getCurrentWorth();
				}));
		
		//Fill all the trucks
		trucks.forEach(t -> {
			
			while(!t.isFull()){
				
				t.fill();
			}
		});
		
		//All truck have now reached their max worth.
		assertTrue(trucks.stream()
				.allMatch(t -> t.getCurrentWorth() == t.getMaxWorth()));
		
		//Fill the trucks again will not change the current worth.
		trucks.forEach(Vehicle::fill);
		assertTrue(trucks.stream()
				.allMatch(t -> t.getCurrentWorth() == t.getMaxWorth()));
	}
	/**
	 * Test the {@link Vehicle#getMaxWorth()} method of the {@link Truck_Vehicle}.
	 * 
	 * <p>
	 * The max worth is is determined by the {@link Vehicle#tankSize} which means
	 * that {@link Truck_Vehicle} with smaller tanks have a lesser max worth.
	 * </p>
	 */
	@Test
	public void testGetMaxWorth(){
		
		trucks.forEach(t1 -> {
			
			trucks.forEach(t2 -> {
				
				//If a truck has a smaller tank size it also has a lesser max worth.
				assertEquals(t1.tankSize <= t2.tankSize,
						t1.getMaxWorth() <= t2.getMaxWorth());
			});
		});
	}
	/**
	 * Test the logical equality of the {@link Truck_Vehicle} objects.
	 * 
	 * <p>
	 * Two {@link Truck_Vehicle} objects are considered equal if they
	 * have an identical {@link Vehicle#size} and {@link Vehicle#tankSize}.
	 * <ol>
	 * 		<li>Test if there exists at least two {@link Truck_Vehicle}
	 * 		that equal</li>
	 * 		<li>{@link Truck_Vehicle} objects that equal must also have equal
	 * 		size and tanks</li>
	 * 		<li>Test if other {@link Vehicle} subclasses do not equal any
	 * 		{@link Truck_Vehicle}</li>
	 * </ol>
	 * </p>
	 */
	@Test
	public void testEquals(){
		
		//Truck for testing equality.
		Truck_Vehicle testT = new Truck_Vehicle();
		
		//At least one truck should equal.
		assertTrue(trucks.stream().anyMatch(t -> t.equals(testT)
				&& t.size == testT.size
				&& t.tankSize == testT.tankSize));
		
		//Trucks cannot be equal to other Vehicles.
		assertTrue(trucks.stream()
				.noneMatch(fs -> fs.equals(new SmallCar_Vehicle())));
		assertTrue(trucks.stream()
				.noneMatch(fs -> fs.equals(new Motorbike_Vehicle())));
	}
	/**
	 * Test the {@link Vehicle#toString()} method in the {@link Truck_Vehicle}
	 * class.
	 * 
	 * <p>
	 * Check that the {@link String} produced by the {@link Truck_Vehicle} is
	 * produced correctly.
	 * </p>
	 */
	@Test
	public void testToString(){
		
		//All trucks follow the same String format.
		assertTrue(trucks.stream()
				.allMatch(t -> new StringBuilder()
						.append("Truck. Size: ")
						.append(t.size)
						.append(" Tank (Gallons): ")
						.append(t.tankSize)
						.toString()
						.equals(t.toString())));
	}
	/**
	 * Test the {@link Vehicle#clone()} method from the {@link Truck_Vehicle}
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
		assertTrue(trucks.stream().noneMatch(t -> t == t.clone()));
		
		//Clones must equal the original object.
		assertTrue(trucks.stream().allMatch(t -> t.equals(t.clone())));
		
		/*
		 * Changing the state of the clone does not change the state of the
		 * original object
		 */
		assertTrue(trucks.stream().noneMatch(t -> {
			
			Truck_Vehicle tClone = t.clone();
			
			while(!tClone.isFull()){
				
				tClone.fill();
			}
			
			return t.isFull() == tClone.isFull();
		}));
		
		//Clones must share the same state as the original object.
		assertTrue(trucks.stream().map(t -> {
			
			while(!t.isFull()){
				
				t.fill();
			}
			
			return t;
		}).allMatch(t -> t.isFull() == t.clone().isFull()));
	}
}