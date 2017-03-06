package environment.model.vehicles;

import java.util.function.Supplier;

/**
 * The <code>VehicleTypes</code> is an enumeration of the ...
 * 
 * @author 	John Berg
 * @version 01/03/2017
 * @since 	01/03/2017
 * @see 	Supplier
 * @see 	Vehicle
 */
public enum VehicleTypes {
	
	/**
	 * 
	 */
	SMALL_CAR(0.33, SmallCar::new),
	/**
	 * 
	 */
	MOTORBIKE(0.33, Motorbike::new),
	/**
	 * 
	 */
	FAMILY_SEDAN(0.33, FamilySedan::new);
	
	/**
	 * The probability of creating a <code>Vehicle</code>.
	 */
	public final double probability;
	/**
	 * 
	 */
	private final Supplier<Vehicle> vehicle;
	
	/**
	 * 
	 * @param Probability The ...
	 * @param vehicle The constructor for the <code>Vehicle</code>.
	 */
	private VehicleTypes(final double Probability, final Supplier<Vehicle> vehicle){
		
		this.probability = Probability;
		this.vehicle = vehicle;
	}
	/**
	 * Create an instance of ...
	 * 
	 * @return 
	 */
	public final Vehicle create(){
		
		return vehicle.get();
	}
}