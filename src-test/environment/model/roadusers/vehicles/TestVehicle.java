package environment.model.roadusers.vehicles;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;


/**
 * 
 * 
 * 
 * @author 	John Berg
 * @version 05/04/2017
 * @since	04/04/2017
 */
public abstract class TestVehicle {
	
	
	protected List<Vehicle> vehicles;
	protected Class<? extends Vehicle> vehicleType;
	
	protected TestVehicle(Class<? extends Vehicle> vehicleType){
		
		this.vehicleType = vehicleType;
	}
	
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
			fail("Could not find ...");
		}
	}
	@Test
	public final void testTankSize(){
		
		
	}
	@Test
	public final void testEquals(){
			
		
	}
	@Test
	public void testToString(){
		
		
		assertTrue(vehicles.stream().allMatch(v -> v.toString().contains("Size: ")));
		assertTrue(vehicles.stream().allMatch(v -> v.toString().contains("Tank (Gallons): ")));
	}
}