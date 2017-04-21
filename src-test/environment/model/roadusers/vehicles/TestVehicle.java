package environment.model.roadusers.vehicles;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;


/**
 * Test the {@link Vehicle} class for expected behaviour.
 * 
 * <p>
 * All tests should pass, and the tests are designed to check if the {@link Vehicle}
 * objects have the correct attributes and that the methods modify the state of the
 * {@link Vehicle} correctly or return correct values.
 * </p>
 * 
 * <p>
 * <strong>This class must be extended by a test class for each {@link Vehicle}</strong>.
 * </p>
 * 
 * @author 	John Berg
 * @version 12/04/2017
 * @since	04/04/2017
 * @see List
 * @see Stream
 * @see Collectors
 * @see Vehicle
 */
public abstract class TestVehicle {
	
	/**
	 * The {@link List} of {@link Vehicle} objects to be tested.
	 * 
	 * @see List
	 * @see Vehicle
	 */
	protected List<Vehicle> vehicles;
	
	/**
	 * The {@link Vehicle} class to be used in the test.
	 */
	protected Class<? extends Vehicle> vehicleType;
	
	/**
	 * Initialise the test object with the type of {@link Vehicle} to be tested.
	 * 
	 * @param vehicleType The {@link Vehicle} class to be tested.
	 * @see Vehicle
	 * @see #vehicleType
	 */
	protected TestVehicle(Class<? extends Vehicle> vehicleType){
		
		this.vehicleType = vehicleType;
	}
	/**
	 * Before running a test, fill the {@link #vehicles} with {@link Vehicle} objects.
	 * 
	 * @see TestVehicle#vehicles
	 */
	@Before
	public final void setup(){
		
		//Fill the list with vehicles of the using the vehicleType constructor.
		vehicles = Stream
				.generate(() -> {
					try {
						
						return vehicleType.newInstance();
						
					} catch (InstantiationException | IllegalAccessException e) {
						
						e.printStackTrace();
						fail("Unable to initiate new instance");
					}
					
					/*
					 * This code should never be triggered as the exception is thrown
					 * if an exception occurs.
					 */
					
					return null;
				})
				.limit(100)
				.collect(Collectors.toList());
	}
	/**
	 * Test the {@link Vehicle#size} field.
	 * 
	 * <p>
	 * It is expected that the {@link Vehicle} has a <code>static</code>
	 * field called "UNIT_SIZE" which contains the {@link Vehicle#size}
	 * of that {@link Vehicle} type, which all instances of that {@link Vehicle}
	 * must have the same {@link Vehicle#size}.
	 * </p>
	 * 
	 * @see Vehicle#size
	 */
	@Test
	public final void testSize(){
		
		try{
			
			//Get the size constant of the Vehicle subclass.
			Field sizeField = vehicleType.getDeclaredField("UNIT_SIZE");
			sizeField.setAccessible(true);
			
			//Get the double value from the field.
			double size = sizeField.getDouble(null);
			
			//Check to see if ALL vehicles of this type has the same size as the constant.
			vehicles.stream().allMatch(v -> v.size == size);
			
		}
		catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){
			
			//To be added
			e.printStackTrace();
			fail("Unable to access the UNIT_SIZE field;");
		}
	}
	/**
	 * Test the {@link Vehicle#tankSize} field of the {@link Vehicle} class.
	 * 
	 * <p>
	 * The {@link Vehicle} class should have either:
	 * <ul>
	 * 		<li>A MIN_TANK_SIZE and a TANK_SIZE_RANGE constant</li>
	 * 		<li>A TANK_SIZE constant</li> 
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * All {@link Vehicle} objects must be in the range of <code>Min_TANK_SIZE</code>
	 * and <code>MIN_TANK_SIZE + TANK_SIZE_RANGE - 1</code>, or have the exact
	 * {@link Vehicle#tankSize} of TANK_SIZE.
	 * </p>
	 * 
	 * @see Vehicle#tankSize
	 */
	@Test
	public final void testTankSize(){
		
		//The expected field names.
		final String min = "MIN_TANK_SIZE";
		final String range = "TANK_SIZE_RANGE";
		final String tank = "TANK_SIZE"; //Only if min and range does not exist.
		
		//Get a list of all declared fields in the Vehicle subclass.
		List<Field> fields = Stream.of(vehicleType.getDeclaredFields())
				.collect(Collectors.toList());
		try{
			
			//Check if min and range exists.
			if(fields.stream().anyMatch(s -> s.getName().equals(min))
					&& fields.stream().anyMatch(s -> s.getName().equals(range))){
				
				//Get the values of the Fields which should be ints
				final Field minSize = vehicleType.getDeclaredField(min);
				minSize.setAccessible(true);
				final int minTankSize = minSize.getInt(null);
				final Field rangeSize = vehicleType.getDeclaredField(range);
				rangeSize.setAccessible(true);
				final int rangeTankSize = rangeSize.getInt(null);
				
				//Loop through all possible sizes.
				for(int i = 0; i < rangeTankSize; ++i){
					
					final int expectedSize = i + minTankSize; //Current testing size.
					
					//All sizes should appear at least once.
					assertTrue("size: " + expectedSize + " was not found",
							vehicles.stream().anyMatch(v -> v.tankSize == expectedSize));
				}
				
				/*
				 * All vehicles should be greater than or equal to the minimum tank size, and
				 * less than the minimum size + the range of sizes.
				 */
				assertTrue(vehicles.stream()
						.allMatch(v -> minTankSize <= v.tankSize
						&& v.tankSize < minTankSize + rangeTankSize));
			}
			else if(fields.stream().anyMatch(f -> f.getName().equals(tank))){
				
				//Get the values of the Fields which should be ints
				final Field tankSize = vehicleType.getDeclaredField(tank);
				tankSize.setAccessible(true);
				final int size = tankSize.getInt(null);
				
				//All vehicles should have the same size.
				assertTrue("All " + vehicleType.getSimpleName() + " were expected to"
						+ "match the tank size of " + size,
						vehicles.stream().allMatch(v -> v.tankSize == size));
			}
			else{
				
				//The expected fields was not found.
				fail("No such field as " + min +", " + range + "or " + tank);
			}
		}
		catch(NoSuchFieldException | IllegalArgumentException
				| IllegalAccessException | SecurityException e){
			
			//This should not need to be triggered.
			e.printStackTrace();
			fail("Expected Field might not exist");
		}
	}
	/**
	 * 
	 * 
	 * <p>
	 * Test {@link Vehicle#fill()} and {@link Vehicle#isFull()} by:
	 * <ol>
	 * 		<li>Checking if all {@link Vehicle} object are not full upon creation</li>
	 * 		<li>Check if filling the {@link Vehicle} objects <code>{@link Vehicle#tankSize} - 1
	 * 		</code> results in the {@link Vehicle} still being not full</li>
	 * 		<li>Check if all {@link Vehicle} objects are full after filling them one more time</li>
	 * 		<li>Check if further filling the {@link Vehicle} objects will change the result of the
	 * 		{@link Vehicle#isFull()} method</li>
	 * </ol>
	 * </p>
	 * 
	 * @see Vehicle#fill()
	 * @see Vehicle#isFull()
	 */
	@Test
	public final void testFillAndIsFull(){
		
		//No vehicle should start with a full tank.
		assertTrue("All vheicles should be empety at the start",
				vehicles.stream().noneMatch(Vehicle::isFull));
		
		//Fill all vehicles so that one more fill is needed to make them full.
		vehicles.forEach(v -> {
			
			//Fill the vehicles until only one more filling is needed.
			for(int i = 0; i < v.tankSize - 1; ++i){
				
				v.fill(); //Fill the vehicle.
				assertFalse(v.isFull()); //The vehicle should not be full.
			}
		});
		
		//Fill the vehicles so they are full.
		vehicles.forEach(v -> {
			
			//Fill the vehicles one more time so that all vehicles are full.
			v.fill();
			assertTrue(v.isFull());
		});
		
		//Further fill the vehicles and check i they remain full.
		vehicles.forEach(v -> {
			
			for(int i = 0; i < 4; ++i){
				
				//Further filling the vehicle does not change the stare.
				v.fill();
				assertTrue(v.isFull());
			}
		});
	}
	/**
	 * Test the {@link Vehicle#getCurrentWorth()} and {@link Vehicle#getMaxWorth()}
	 * method.
	 * 
	 * <p>
	 * When a {@link Vehicle} is created, the current worth will
	 * always be <code>0.0</code>. When the {@link Vehicle#fill()} method
	 * is used, the current worth of the car will increase (unless the car
	 * is already) full.
	 * When a {@link Vehicle} is full, the current worth should equal the max worth.
	 * </p>
	 * 
	 * @see Vehicle#getCurrentWorth()
	 * @see Vehicle#getMaxWorth()
	 */
	@Test
	public final void testGetCurrentWorthAndGetMaxWorth(){
		
		//No vehicle has any worth in the simulation yet.
		assertTrue(vehicles.stream().allMatch(v -> v.getCurrentWorth() == 0.0));
		
		//After filling the vehicle, the current worth is no longer 0.0.
		assertTrue(vehicles.stream().allMatch(v -> {
			
			v.fill();
			return v.getCurrentWorth() != 0.0;
		}));
		
		//The current worth has increased after filling the tank.
		assertTrue(vehicles.stream().allMatch(v -> {
			
			double previous = v.getCurrentWorth();
			v.fill();
			return v.getCurrentWorth() != previous;
		}));
		
		//When the vehicles are full, the current worth equals the max worth.
		assertTrue(vehicles.stream().allMatch(v -> {
			
			//Fill the vehicle until full.
			while(!v.isFull()){
				
				v.fill();
			}
			
			//The vehicle has now reached the max worth.
			return v.getCurrentWorth() == v.getMaxWorth();
		}));
	}
	/**
	 * Test the {@link Vehicle#clone()} method from the {@link Vehicle}
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
	 * 
	 * @see Vehicle#clone()
	 */
	@Test
	public final void testClone(){
		
		//All clones should equal their original object.
		assertTrue(vehicles.stream().allMatch(v -> v.clone().equals(v)));
		
		//No clone should have the same reference as their original object.
		assertTrue(vehicles.stream().noneMatch(v -> v.clone() == v));
		
		//Changing the clones state does not affect the original object.
		assertTrue(vehicles.stream().allMatch(v -> {
			
			Vehicle clone = v.clone();
			clone.fill(); //Should not affect v.
			
			//v's fuelLevel remains unchanged.
			return v.getFuelLevel() != clone.getFuelLevel();
		}));
		
		//The clone should reflect the state of the original object upon creation.
		assertTrue(vehicles.stream().allMatch(v -> {
			
			//Make the vehicle full.
			while(!v.isFull()){
				
				v.fill();
			}
			
			//The vehicle clone should also be full.
			return v.isFull() == v.clone().isFull();
		}));
	}
	/**
	 * Test the {@link Vehicle#equals(Object)} method.
	 * 
	 * <p>
	 * Check if two {@link Vehicle} objects will be equal if:
	 * 
	 * <ol>
	 * 		<li>They have identical {@link Vehicle#size}</li>
	 * 		<li>They have identical {@link Vehicle#tankSize}</li>
	 * 		<li>They have a matching {@link FuelType}</li>
	 * </ol>
	 * </p>
	 * 
	 * @see Vehicle#equals(Object)
	 */
	@Test
	public final void testEquals(){
		
		try {
			
			//Get the clone constructor from the vehicle subclass.
			Constructor<? extends Vehicle> cloneConstructor = vehicleType
					.getDeclaredConstructor(double.class,
							int.class,
							int.class,
							FuelType.class);
			cloneConstructor.setAccessible(true);
			
			//Create a new vehicle.
			Vehicle v1 = vehicleType.newInstance();
			
			//No vehicle should have 0 as size or tank size so there is no equality.
			assertNotEquals(v1, cloneConstructor.newInstance(0, 0, 0, null));
			
			//same size, tankSize and fuelType results in equality.
			assertEquals(v1, cloneConstructor.newInstance(v1.size,
					v1.tankSize,
					v1.getFuelLevel(),
					v1.fuelType));
			
			//Non-equal size results in no equality.
			assertNotEquals(v1, cloneConstructor.newInstance(0,
					v1.tankSize,
					v1.getFuelLevel(),
					v1.fuelType));
			//Non-equal tankSize results in no equality.
			assertNotEquals(v1, cloneConstructor.newInstance(v1.size,
					0,
					v1.getFuelLevel(),
					v1.fuelType));
			
			//fuelLevel does not affect equality.
			assertEquals(v1, cloneConstructor.newInstance(v1.size,
					v1.tankSize,
					v1.getFuelLevel() + 1,
					v1.fuelType));
			
			//Non-matching fuelType results in no equality. 
			assertEquals(v1, cloneConstructor.newInstance(v1.size,
					v1.tankSize,
					v1.getFuelLevel(),
					v1.fuelType));
			
		}
		catch (NoSuchMethodException | SecurityException
				| IllegalAccessException e) {
			
			e.printStackTrace();
			fail("Could not find the clone constructor.");
		}
		catch (InstantiationException e) {
			
			e.printStackTrace();
			fail("Faild to initialise a " + vehicleType.getSimpleName());
		}
		catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			fail("Wrong arguments provided to the clone constructor.");
		}
		catch (InvocationTargetException e) {
			
			e.printStackTrace();
			fail("Could not invoke the clone constructor.");
		}
	}
	/**
	 * Test to see if the {@link Vehicle#toString()} method produces the a string
	 * with the correct information.
	 * 
	 * @see Vehicle#toString()
	 */
	@Test
	public void testToString(){
		
		//All vehicles should contain a string which represent the size and tank size.
		assertTrue(vehicles.stream().allMatch(v -> v.toString().contains("Size: " + v.size)));
		assertTrue(vehicles.stream().allMatch(v -> v.toString().contains("Tank (Gallons): " + v.tankSize)));
	}
}