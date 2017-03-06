package environment.model.roadUsers.vehicles;

import java.util.function.Supplier;

/**
 * The <code>VehicleTypes</code> is an enumeration of the ...
 * 
 * @author John_Berg
 * @version 01/03/2017
 * @since 01/03/2017
 * @see Supplier
 * @see Vehicle
 */
public enum VehicleTypes {

	/**
	 * 
	 */
	SMALL_CAR(SmallCar::new),
	/**
	 * 
	 */
	MOTORBIKE(Motorbike::new),
	/**
	 * 
	 */
	FAMILY_SEDAN(FamilySedan::new);

	/**
	 * 
	 */
	private final Supplier<Vehicle> vehicle;

	/**
	 * 
	 * @param Probability
	 *            The ...
	 * @param vehicle
	 *            The constructor for the <code>Vehicle</code>.
	 */
	private VehicleTypes(final Supplier<Vehicle> vehicle) {

		this.vehicle = vehicle;
	}

	/**
	 * Create an instance of ...
	 * 
	 * @return
	 */
	public final Vehicle create() {

		return vehicle.get();
	}
}