package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * The <code>SmallCar</code> class is a subclass of the {@link vehicle}
 * class, which models a car.
 * 
 * <p>
 * The <code>SmallCar</code> uses the {@link Vehicle#size} and
 * {@link Vehicle#tankSize} attributes from the {@link Vehicle} class.
 * </p>
 * 
 * <p>
 * <strong>The <code>SmallCar</code> class cannot be extended.</strong>
 * </p>
 * 
 * @author 	John Berg
 * @version 13/03/2017
 * @since 	01/02/2017
 * @see		Random
 * @see 	Vehicle
 */
public final class SmallCar_Vehicle extends Vehicle {
	
	/**
	 * The physical size representation of the <code>SmallCar
	 * </code>.
	 */
	private static final double UNIT_SIZE = 1.0;
	/**
	 * The smallest possible tank size of the <code>SmallCar</code>.
	 */
	private static final int MIN_TANK_SIZE = 7;
	/**
	 * The range of possible tank sizes of the <code>SmallCar</code>.
	 */
	private static final int TANK_SIZE_RANGE = 3;
	
	/**
	 * Create a <code>SmallCar</code> object.
	 * 
	 * <p>
	 * Initialise a new <code>SmallCar</code> where the {@link Vehicle#size}
	 * of the <code>SmallCar</code> is set to {@link #UNIT_SIZE}
	 * ({@value #UNIT_SIZE}), and the {@link Vehicle#tankSize} of the <code>
	 * SmallCar</code> is to a minimum of {@link #MIN_TANK_SIZE}
	 * ({@value #MIN_TANK_SIZE}) and up to {@link #TANK_SIZE_RANGE}
	 * {@value #TANK_SIZE_RANGE} in range.
	 * </p>
	 */
	public SmallCar_Vehicle(){
		
		/*
		 * Invoke the superclass constructor with the arguments for the
		 * SmallCar's physical size and tank size (generated).
		 */
		
		super(UNIT_SIZE, RNG.nextInt(TANK_SIZE_RANGE) + MIN_TANK_SIZE);
	}
	/**
	 * Check if an object is equal to the <code>SmallCar</code>.
	 * 
	 * <p>
	 * Compare an object against the <code>SmallCar</code> the check if the
	 * object is equal to the <code>SmallCar</code>. If the object is an
	 * instance of the {@link SmallCar_Vehicle} class, the {@link Vehicle#equals(Object)}
	 * method will determine if the object is equal to the <code>SmallCar</code>;
	 * if the object is not an instance of the <code>SmallCar</code> class then
	 * object cannot be equal to the <code>SmallCar</code> as the object is not a
	 * <code>SmallCar</code>.
	 * </p>
	 * 
	 * @param o The object to be tested for equality against.
	 * @return <code>true</code> if the tested object is an instance of the <code>
	 * 		SmallCar</code> class and the {@link Vehicle#equals(Object)} method
	 * 		returns <code>true</code>, otherwise returns <code>false</code>.
	 */
	@Override
	public final boolean equals(Object o){
		
		/*
		 * If o is an instance of SmallCar, then use the equals method in the
		 * Vehicle class to check for equality; otherwise, if o is not an
		 * instance of SmallCar the result is fault.
		 */
		
		return o instanceof SmallCar_Vehicle? super.equals(o): false;
	}
	/**
	 * Get the <code>String</code> representation of the <code>SmallCar</code>
	 * object.
	 * 
	 * <p>
	 * Get the <code>String</code> representing the <code>SmallCar</code> containing
	 * a description of the <code>SmallCar</code>. The {@link Vehicle#toString()} from
	 * the {@link Vehicle} is used to get the details of the {@link Vehicle#size} and
	 * {@link Vehicle#tankSize}.
	 * </p>
	 * 
	 * <p>
	 * Format:
	 * </p>
	 * 
	 * <p>
	 * <code>SmallCar. Size: 1 Tank (Gallons): 7</code>.
	 * </p>
	 * 
	 * @return The <code>String representation of the <code>SmallCar</code>.
	 */
	@Override
	public String toString(){
		
		/*
		 *  The name of the SmallCar, the size and tank size of the Vehicle,
		 *  get the size and the tank size from the Vehicle class.
		 */
		
		return new StringBuilder()
				.append("Small car. ")
				.append(super.toString())
				.toString();
	}
}