package environment.model.vehicles;

import java.util.Random;

/**
 * The <code>SmallCar</code> class is a subclass of the <code>Vehicle
 * </code> class.
 * 
 * <p>
 * The <code>SmallCar</code> class inherits the {@link Vehicle#size}
 * and {@link Vehicle#tankSize} attributes from the <code>Vehicle
 * </code> class.
 * </p>
 * 
 * <p>
 * The <code>SmallCar</code> class cannot be extended.
 * </p>
 * 
 * @author 	John Berg
 * @version 01/03/2017
 * @since 	01/02/2017
 * @see		Random
 * @see 	Vehicle
 */
public final class SmallCar extends Vehicle {
	
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
	private static final int TANK_SIZE_RANGE = 2;
	/**
	 * The initial seed used for the {@link #RNG}.
	 */
	private static final long SEED = 11;
	/**
	 * The <code>Random</code> object used to generate properties of
	 * the <code>SmallCar</code>.
	 */
	private static final Random RNG = new Random(SEED);
	
	/**
	 * Create a <code>SmallCar</code> object.
	 * 
	 * <p>
	 * Initialise a new <code>SmallCar</code> where the {@link Vehicle#size}
	 * of the <code>SmallCar</code> is set to {@value #UNIT_SIZE}, and the
	 * {@link Vehicle#tankSize} of the <code>SmallCar</code> is to a minimum of
	 * {@value #MIN_TANK_SIZE} and up to {@value #TANK_SIZE_RANGE} in range.
	 * </p>
	 */
	public SmallCar(){
		
		/*
		 * Invoke the superclass constructor with the arguments for the
		 * SmallCar's physical size and tank size (generated).
		 */
		
		super(UNIT_SIZE, RNG.nextInt(TANK_SIZE_RANGE) + MIN_TANK_SIZE);
	}
}