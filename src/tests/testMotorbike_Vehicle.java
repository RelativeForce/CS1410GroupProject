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
 * Test the functionality of the {@link Motorbike_Vehicle} class.
 * 
 * <p>
 * Check the behaviour of the {@link Motorbike_Vehicle} class in the {@link Test}
 * methods. Each {@link Test} corresponds to one method fron the {@link Motorbike_Vehicle}
 * class which will be tested.
 * </p>
 * 
 * @author 	John Berg
 * @version 24/03/2017
 * @since 	20/03/2017
 * @see		Collectors
 * @see		List
 * @see 	Stream
 * @see		Motorbike_Vehicle
 */
public final class testMotorbike_Vehicle {
	
	/**
	 * The list containing {@link Motorbike_Vehicle} objects to be tested.
	 */
	private List<Motorbike_Vehicle> motorbikes;
	
	/**
	 * Before a <code>@Test</code> method is run, renew the {@link #motorbikes}
	 * to contain a list of unmodified {@link Motorbike_Vehicle} objects.
	 */
	@Before
	public void setUp() {
		
		motorbikes = Stream.generate(Motorbike_Vehicle::new)
				.limit(300)
				.collect(Collectors.toList());
	}
	/**
	 * Test if all {@link Motorbike_Vehicle} objects have the correct size
	 */
	@Test
	public void testSize(){
		
		assertTrue(motorbikes.stream().allMatch(mb -> mb.size == 0.75));
	}
	/**
	 * Test to see if all {@link Motorbike_Vehicle} objects have the correct
	 * {@link Vehicle#tankSize}, from the {@link #motorbikes} sample space, all entries
	 * should be 5.
	 */
	@Test
	public void testTankSize(){
		
		//All motorbikes have a tank size of 5.
		assertTrue(motorbikes.stream().allMatch(
				mb-> 5 == mb.tankSize));
	}
	/**
	 * Test to see if all {@link Motorbike_Vehicle} objects' {@link Vehicle#isFull()}
	 * method behaves as expected.
	 * 
	 * <ui>
	 * 		<li>All {@link Motorbike_Vehicle} objects must not be full at the start</li>
	 * 		<li>When a {@link Motorbike_Vehicle} is full, further filling still result in the
	 * 		{@link Motorbike_Vehicle} being full</li>
	 * </ui>
	 */
	@Test
	public void testIsFull(){
		
		//No motorbike is full from the start.
		assertTrue(motorbikes.stream().noneMatch(Vehicle::isFull));
		
		//Fill all motorbikes until full.
		assertTrue(motorbikes.stream().map(mb -> {
			
			while(!mb.isFull()){
				
				mb.fill();
			}
			
			return mb;
			
		}).allMatch(Vehicle::isFull));
		
		//Filling the motorbikes further still results in the cars being full.
		assertTrue(motorbikes.stream().map(mb -> {
			
			mb.fill();
			return mb;
		}).allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#fill()} method for the expected behaviour.
	 * <ui>
	 * 		<li>No {@link Motorbike_Vehicle} is full at the begining</li>
	 * 		<li>Using the fill method once should not make the
	 * 		{@link Motorbike_Vehicle} full</li>
	 * </ui>
	 * 
	 * @see  #testIsFull()
	 */
	@Test
	public void testFill(){
		
		//No motorbike is filled up yet.
		assertTrue(motorbikes.stream().noneMatch(Vehicle::isFull));
		
		//Fill all motorbikes
		motorbikes.forEach(Motorbike_Vehicle::fill);
		
		//No motorbike has been filled up fully
		assertTrue(motorbikes.stream().noneMatch(Vehicle::isFull));
		
		//Fill all motorbikes fully.
		motorbikes.forEach(mb -> {
			
			while(!mb.isFull()){
				
				mb.fill();
			}
		});
		
		//All motorbikes are full.
		assertTrue(motorbikes.stream().allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#getCurrentWorth()} method.
	 * 
	 * <p>
	 * When a {@link Motorbike_Vehicle} is created, the current worth will
	 * always be <code>0.0</code>. When the {@link Vehicle#fill()} method
	 * is used, the current worth of the {@link Motorbike_Vehicle} will
	 * increase (unless the motorbike is already) full.
	 * </p>
	 */
	@Test
	public void testGetCurrentWorth(){
		
		//All motorbike have an initial worth of 0.0.
		assertTrue(motorbikes.stream().allMatch(mb -> 0 == mb.getCurrentWorth()));
		
		//Fill the motorbike and check that the current worth is greater than 0.
		motorbikes.stream().map(mb -> {
			
			mb.fill();
			return mb;
			
		}).forEach(mb -> assertTrue(0.0 < mb.getCurrentWorth()));
		
		//The worth after filling the motorbike is not the same as its previous worth.
		assertTrue(motorbikes.stream()
				.noneMatch(mb -> {
					
					double temp = mb.getCurrentWorth();
					
					mb.fill();
					
					return temp == mb.getCurrentWorth();
				}));
		
		//Fill all the motorbikes
		motorbikes.forEach(mb -> {
			
			while(!mb.isFull()){
				
				mb.fill();
			}
		});
		
		//All motorbikes have now reached their max worth.
		assertTrue(motorbikes.stream()
				.allMatch(mb -> mb.getCurrentWorth() == mb.getMaxWorth()));
		
		//Fill the motorbikes again will not change the current worth.
		motorbikes.forEach(Vehicle::fill);
		assertTrue(motorbikes.stream()
				.allMatch(mb -> mb.getCurrentWorth() == mb.getMaxWorth()));
	}
	/**
	 * Test the {@link Vehicle#getMaxWorth()} method of the {@link Motorbike_Vehicle}.
	 * 
	 * <p>
	 * The max worth is is determined by the {@link Vehicle#tankSize} which means
	 * that {@link Motorbike_Vehicle} with smaller tanks have a lesser max worth.
	 * </p>
	 */
	@Test
	public void testGetMaxWorth(){
		
		motorbikes.forEach(mb1 -> {
			
			motorbikes.forEach(mb2 -> {
				
				//If a motorbike has a smaller tank size it also has a lesser max worth.
				assertEquals(mb1.tankSize <= mb2.tankSize,
						mb1.getMaxWorth() <= mb2.getMaxWorth());
			});
		});
	}
	/**
	 * Test the logical equality of the {@link Motorbike_Vehicle} objects.
	 * 
	 * <p>
	 * Two {@link Motorbike_Vehicle} objects are considered equal if they
	 * have an identical {@link Vehicle#size} and {@link Vehicle#tankSize}.
	 * <ol>
	 * 		<li>Test if there exists at least two {@link Motorbike_Vehicle}
	 * 		that equal</li>
	 * 		<li>{@link Motorbike_Vehicle} objects that equal must also have equal
	 * 		size and tanks</li>
	 * 		<li>Test if other {@link Vehicle} subclasses do not equal any
	 * 		{@link Motorbike_Vehicle}</li>
	 * </ol>
	 * </p>
	 */
	@Test
	public void testEquals(){
		
		//Motorbike for testing equality.
		Motorbike_Vehicle testMb = new Motorbike_Vehicle();
		
		//At least one motorbike should equal.
		assertTrue(motorbikes.stream().anyMatch(mb -> mb.equals(testMb)
				&& mb.size == testMb.size
				&& mb.tankSize == testMb.tankSize));
		
		//Motorbikes cannot be equal to other Vehicles.
		assertTrue(motorbikes.stream()
				.noneMatch(mb -> mb.equals(new SmallCar_Vehicle())));
		assertTrue(motorbikes.stream()
				.noneMatch(mb -> mb.equals(new FamilySedan_Vehicle())));
	}
	/**
	 * Test the {@link Vehicle#toString()} method in the {@link Motorbike_Vehicle}
	 * class.
	 * 
	 * <p>
	 * Check that the {@link String} produced by the {@link Motorbike_Vehicle} is
	 * produced correctly.
	 * </p>
	 */
	@Test
	public void testToString(){
		
		//All motorbikes follow the same String format.
		assertTrue(motorbikes.stream()
				.allMatch(mb -> new StringBuilder()
						.append("Motorbike. Size: ")
						.append(mb.size)
						.append(" Tank (Gallons): ")
						.append(mb.tankSize)
						.toString()
						.equals(mb.toString())));
	}
	/**
	 * Test the {@link Vehicle#clone()} method from the {@link Motorbike_Vehicle}
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
		assertTrue(motorbikes.stream().noneMatch(mb -> mb == mb.clone()));
		
		//Clones must equal the original object.
		assertTrue(motorbikes.stream().allMatch(mb -> mb.equals(mb.clone())));
		
		/*
		 * Changing the state of the clone does not change the state of the
		 * original object
		 */
		assertTrue(motorbikes.stream().noneMatch(mb -> {
			
			Motorbike_Vehicle mbClone = mb.clone();
			
			while(!mbClone.isFull()){
				
				mbClone.fill();
			}
			
			return mb.isFull() == mbClone.isFull();
		}));
		
		//Clones must share the same state as the original object.
		assertTrue(motorbikes.stream().map(mb -> {
			
			while(!mb.isFull()){
				
				mb.fill();
			}
			
			return mb;
		}).allMatch(mb -> mb.isFull() == mb.clone().isFull()));
	}
}