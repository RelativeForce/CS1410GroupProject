package environment.model.roadusers.vehicles;

import org.junit.Test;

import environment.model.roadusers.vehicles.Truck_Vehicle;

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
 * @version 12/04/2017
 * @since 	20/03/2017
 */
public final class TestTruck_Vehicle extends TestVehicle {
	
	public TestTruck_Vehicle(){
		
		super(Truck_Vehicle.class);
	}
}