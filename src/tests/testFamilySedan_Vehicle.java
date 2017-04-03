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
 * Test the functionality of the {@link FamilySedan_Vehicle} class.
 * 
 * <p>
 * Check the behaviour of the {@link FamilySedan_Vehicle} class in the {@link Test}
 * methods. Each {@link Test} corresponds to one method from the {@link FamilySedan_Vehicle}
 * class which will be tested.
 * </p>
 * 
 * @author 	John Berg
 * @version 25/03/2017
 * @since 	20/03/2017
 * @see		Collectors
 * @see		List
 * @see 	Stream
 * @see		FamilySedan_Vehicle
 */
public final class testFamilySedan_Vehicle {
	
	/**
	 * The list containing {@link FamilySedan_Vehicle} objects to be tested.
	 */
	private List<FamilySedan_Vehicle> familySedans;
	
	/**
	 * Before a <code>@Test</code> method is run, renew the {@link #familySedans}
	 * to contain a list of unmodified {@link FamilySedan_Vehicle} objects.
	 */
	@Before
	public void setUp() {
		
		familySedans = Stream.generate(FamilySedan_Vehicle::new)
				.limit(300)
				.collect(Collectors.toList());
	}
	/**
	 * Test if all {@link FamilySedan_Vehicle} objects have the correct size
	 */
	@Test
	public void testSize(){
		
		assertTrue(familySedans.stream().allMatch(fs -> fs.size == 1.5));
	}
	/**
	 * Test to see if all {@link FamilySedan_Vehicle} objects have the correct
	 * {@link Vehicle#tankSize}, from the {@link #familySedans} sample space, all entries
	 * should be 5.
	 */
	@Test
	public void testTankSize(){
		
		//At least one familysedan should have one of the possible tank sizes.
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 12));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 13));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 14));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 15));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 16));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 17));
		assertTrue(familySedans.stream().anyMatch(fs -> fs.tankSize == 18));
		
		//All familysedans have a tank between of 12 and 18.
		assertTrue(familySedans.stream().allMatch(
				fs -> 12 <= fs.tankSize && fs.tankSize <= 18));
	}
	/**
	 * Test to see if all {@link FamilySedan_Vehicle} objects' {@link Vehicle#isFull()}
	 * method behaves as expected.
	 * 
	 * <ui>
	 * 		<li>All {@link FamilySedan_Vehicle} objects must not be full at the start</li>
	 * 		<li>When a {@link FamilySedan_Vehicle} is full, further filling still result in the
	 * 		{@link FamilySedan_Vehicle} being full</li>
	 * </ui>
	 */
	@Test
	public void testIsFull(){
		
		//No familysedan is full from the start.
		assertTrue(familySedans.stream().noneMatch(Vehicle::isFull));
		
		//Fill all familysedan until full.
		assertTrue(familySedans.stream().map(fs -> {
			
			while(!fs.isFull()){
				
				fs.fill();
			}
			
			return fs;
			
		}).allMatch(Vehicle::isFull));
		
		//Filling the familysedan further still results in the cars being full.
		assertTrue(familySedans.stream().map(fs -> {
			
			fs.fill();
			return fs;
		}).allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#fill()} method for the expected behaviour.
	 * <ui>
	 * 		<li>No {@link FamilySedan_Vehicle} is full at the begining</li>
	 * 		<li>Using the fill method once should not make the
	 * 		{@link FamilySedan_Vehicle} full</li>
	 * </ui>
	 * 
	 * @see  #testIsFull()
	 */
	@Test
	public void testFill(){
		
		//No familysedan is filled up yet.
		assertTrue(familySedans.stream().noneMatch(Vehicle::isFull));
		
		//Fill all familysedans
		familySedans.forEach(FamilySedan_Vehicle::fill);
		
		//No familysedan has been filled up fully
		assertTrue(familySedans.stream().noneMatch(Vehicle::isFull));
		
		//Fill all familysedans fully.
		familySedans.forEach(fs -> {
			
			while(!fs.isFull()){
				
				fs.fill();
			}
		});
		
		//All familysedans are full.
		assertTrue(familySedans.stream().allMatch(Vehicle::isFull));
	}
	/**
	 * Test the {@link Vehicle#getCurrentWorth()} method.
	 * 
	 * <p>
	 * When a {@link FamilySedan_Vehicle} is created, the current worth will
	 * always be <code>0.0</code>. When the {@link Vehicle#fill()} method
	 * is used, the current worth of the {@link FamilySedan_Vehicle} will
	 * increase (unless the {@link FamilySedan_Vehicle} is already) full.
	 * </p>
	 */
	@Test
	public void testGetCurrentWorth(){
		
		//All familysedan have an initial worth of 0.0.
		assertTrue(familySedans.stream().allMatch(fs -> 0 == fs.getCurrentWorth()));
		
		//Fill the familysedan and check that the current worth is greater than 0.
		familySedans.stream().map(fs -> {
			
			fs.fill();
			return fs;
			
		}).forEach(fs -> assertTrue(0.0 < fs.getCurrentWorth()));
		
		//The worth after filling the familysedan is not the same as its previous worth.
		assertTrue(familySedans.stream()
				.noneMatch(fs -> {
					
					double temp = fs.getCurrentWorth();
					
					fs.fill();
					
					return temp == fs.getCurrentWorth();
				}));
		
		//Fill all the familysedan
		familySedans.forEach(fs -> {
			
			while(!fs.isFull()){
				
				fs.fill();
			}
		});
		
		//All familysedans have now reached their max worth.
		assertTrue(familySedans.stream()
				.allMatch(fs -> fs.getCurrentWorth() == fs.getMaxWorth()));
		
		//Fill the familysedans again will not change the current worth.
		familySedans.forEach(Vehicle::fill);
		assertTrue(familySedans.stream()
				.allMatch(fs -> fs.getCurrentWorth() == fs.getMaxWorth()));
	}
	/**
	 * Test the {@link Vehicle#getMaxWorth()} method of the {@link FamilySedan_Vehicle}.
	 * 
	 * <p>
	 * The max worth is is determined by the {@link Vehicle#tankSize} which means
	 * that {@link FamilySedan_Vehicle} with smaller tanks have a lesser max worth.
	 * </p>
	 */
	@Test
	public void testGetMaxWorth(){
		
		familySedans.forEach(fs1 -> {
			
			familySedans.forEach(fs2 -> {
				
				//If a familysedan has a smaller tank size it also has a lesser max worth.
				assertEquals(fs1.tankSize <= fs2.tankSize,
						fs1.getMaxWorth() <= fs2.getMaxWorth());
			});
		});
	}
	/**
	 * Test the logical equality of the {@link FamilySedan_Vehicle} objects.
	 * 
	 * <p>
	 * Two {@link FamilySedan_Vehicle} objects are considered equal if they
	 * have an identical {@link Vehicle#size} and {@link Vehicle#tankSize}.
	 * <ol>
	 * 		<li>Test if there exists at least two {@link FamilySedan_Vehicle}
	 * 		that equal</li>
	 * 		<li>{@link FamilySedan_Vehicle} objects that equal must also have equal
	 * 		size and tanks</li>
	 * 		<li>Test if other {@link Vehicle} subclasses do not equal any
	 * 		{@link FamilySedan_Vehicle}</li>
	 * </ol>
	 * </p>
	 */
	@Test
	public void testEquals(){
		
		//Familysedans for testing equality.
		FamilySedan_Vehicle testFs = new FamilySedan_Vehicle();
		
		//At least one familysedan should equal.
		assertTrue(familySedans.stream().anyMatch(fs -> fs.equals(testFs)
				&& fs.size == testFs.size
				&& fs.tankSize == testFs.tankSize));
		
		//Familysedan cannot be equal to other Vehicles.
		assertTrue(familySedans.stream()
				.noneMatch(fs -> fs.equals(new SmallCar_Vehicle())));
		assertTrue(familySedans.stream()
				.noneMatch(fs -> fs.equals(new Motorbike_Vehicle())));
	}
	/**
	 * Test the {@link Vehicle#toString()} method in the {@link FamilySedan_Vehicle}
	 * class.
	 * 
	 * <p>
	 * Check that the {@link String} produced by the {@link FamilySedan_Vehicle} is
	 * produced correctly.
	 * </p>
	 */
	@Test
	public void testToString(){
		
		//All familysedan follow the same String format.
		assertTrue(familySedans.stream()
				.allMatch(fs -> new StringBuilder()
						.append("FamilySedan. Size: ")
						.append(fs.size)
						.append(" Tank (Gallons): ")
						.append(fs.tankSize)
						.toString()
						.equals(fs.toString())));
	}
	/**
	 * Test the {@link Vehicle#clone()} method from the {@link FamilySedan_Vehicle}
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
		assertTrue(familySedans.stream().noneMatch(fs -> fs == fs.clone()));
		
		//Clones must equal the original object.
		assertTrue(familySedans.stream().allMatch(fs -> fs.equals(fs.clone())));
		
		/*
		 * Changing the state of the clone does not change the state of the
		 * original object
		 */
		assertTrue(familySedans.stream().noneMatch(fs -> {
			
			Vehicle fsClone = fs.clone();
			
			while(!fsClone.isFull()){
				
				fsClone.fill();
			}
			
			return fs.isFull() == fsClone.isFull();
		}));
		
		//Clones must share the same state as the original object.
		assertTrue(familySedans.stream().map(fs -> {
			
			while(!fs.isFull()){
				
				fs.fill();
			}
			
			return fs;
		}).allMatch(fs -> fs.isFull() == fs.clone().isFull()));
	}
}