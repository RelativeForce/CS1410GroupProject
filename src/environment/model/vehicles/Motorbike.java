package environment.model.vehicles;

import java.util.Random;

/**
 * The <code>Motorbike</code> class is a subclass of the<code>
 * Vehicle</code> class.
 * 
 * <p>
 * 
 * </p>
 * 
 * <p>
 * The <code>Motorbike</code> class cannot be extended.
 * </p>
 * 
 * @author 	John Berg
 * @version 01/03/2017
 * @since	01/03/2017
 * @see		Random
 * @see 	Vehicle
 */
public final class Motorbike extends Vehicle {
	
	/**
	 * The physical size representation of the <code>Motorbike
	 * </code>.
	 */
	private static final double UNIT_SIZE = 0.75;
	/**
	 * The tank size of the <code>Motorbike</code>.
	 */
	private static final int TANK_SIZE = 5;
	/**
	 * The initial seed used for the {@link #RNG}.
	 */
	private static final long SEED = 13;
	/**
	 * The <code>Random</code> object used to generate properties of
	 * the <code>Motorbike</code>.
	 */
	private static final Random RNG = new Random(SEED);
	
	/**
	 * Create a <code>Motorbike</code> object.
	 * 
	 * <p>
	 * Initialise a new <code>Motorbike</code> ...
	 * </p>
	 */
	public Motorbike(){
		
		/*
		 * Invoke the superclass constructor with the physical
		 * size of the Motorbike and the tank size of the
		 * Motorbike.
		 */
		
		super(UNIT_SIZE, TANK_SIZE);
	}
}