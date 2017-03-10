package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * The <code>Truck</code> class is a subclass of the {@link Vehicle}
 * class, which models a Truck.
 * 
 * <p>
 * The <code>Truck</code> usese the {@link vehicle#size} and the
 * {@link Vehicle#tankSize} attributes from the {@link Vehicle}
 * superclass.
 * </p>
 * 
 * <p>
 * <strong>The <code>Truck</code> class cannot be extended.</strong>
 * </p>
 * 
 * @author 	John Berg
 * @version 10/03/2017
 * @since 	09/03/2017
 * @see Random
 * @see Vehicle
 */
public final class Truck extends Vehicle {
	
	/**
	 * The physical size representation of the <code>Truck</code>.
	 */
	private static final double UNIT_SIZE = 2;
	/**
	 * The smallest possible tank size for a <code>Truckk</code>.
	 */
	private static final int MIN_TANK_SIZE = 30;
	/**
	 * The range of possible tank sizes for a <code>Truck</code>.
	 */
	private static final int TANK_SIZE_RANGE = 11;
	/**
	 * The initial seed for the {@link #RNG}.
	 */
	private static final long SEED = 9000;
	/**
	 * The <code>Random</code> object used to generate properties for the
	 * <code>Truck</code>.
	 */
	private static final Random RNG = new Random(SEED);
	
	/**
	 * Create a new <code>Truck</code>.
	 * 
	 * <p>
	 * Initialise a <code>Truck</code> object by invoking the {@link Vehicle}
	 * constructor, set the {@link Veehicle#size} to be the {@link #UNIT_SIZE}
	 * ({@value #UNIT_SIZE}), and set the {@link Vehicle#tankSize} to a minimum
	 * of {@link #MIN_TANK_SIZE} ({@value #MIN_TANK_SIZE}), with a range of
	 * {@link #TANK_SIZE_RANGE} ({@value #TANK_SIZE_RANGE}).
	 * </p>
	 * 
	 * <p>
	 * <code>tankSize = RNG.nextInt({@value #TANK_SIZE_RANGE}) + {@value #MIN_TANK_SIZE}
	 * </code>
	 * </p>
	 */
	public Truck(){
		
		super(UNIT_SIZE, RNG.nextInt(TANK_SIZE_RANGE) + MIN_TANK_SIZE);
	}
	/**
	 * Check if an object is equal to a <code>Truck</code>. 
	 * 
	 * <p>
	 * Compare an object against a <code>Truck</code>. If the object is an
	 * instance of the <code>Truck</code> class, then test the for the equality
	 * in the {@link Vehicle#equals(Object)} method in the {@link Vehicle} class
	 * which will determine if the object and the <code>Truck</code> are equal; if
	 * the object is not an instance of the <code>Truck</code> class, then the
	 * object cannot equal any <code>Truck</code>.
	 * </p>
	 * 
	 * @param o The object to be tested for equality against.
	 * @return <code>true</code> if the tested  object is an instance of the
	 * 		<code>Truck</code> class and has the same {@link Vehicle#size} and
	 * 		{@link Vehicle#tankSize} as the <code>Truck</code>, otherwise
	 * 		<code>false</code>.
	 */
	@Override
	public boolean equals(Object o){
		
		/*
		 * If o is an instance of the Truck class, then proceed by testing
		 * it's attributes in the Vehicle.equals(o) method, otherwise it is
		 * not equal.
		 */
		
		return o instanceof Truck? super.equals(o): false;
	}
	/**
	 * Get the <code>String</code> representation of the <code>Truck</code>.
	 * 
	 * <p>
	 * Represent the <code>Truck</code> as a <code>String</code> containing the
	 * name of the <code>Truck</code>, and the details about it's properties
	 * ({@link Vehicle#size} and {@link Vehicle#tankSize}) from the
	 * {@link Vehicle#toString()} method.
	 * </p>
	 * 
	 * <p>
	 * Format:
	 * </p>
	 * 
	 * <p>
	 * <code>Truck. Size: 2.0 Tank (Gallons): 33</code>
	 * </p>
	 * 
	 * @return The <code>String</code> representation of the <code>Truck</code>.
	 */
	@Override
	public String toString(){
		
		/*
		 * Add the name of the Truck to the String, then followed by the
		 * Vehicle attributes from the Vehicle.toString() method.
		 */
		
		return new StringBuilder()
				.append("Truck. ")
				.append(super.toString())
				.toString();
	}
}