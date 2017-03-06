package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * 
 * @author 	John
 * @version	02/03/2017
 * @since 	02/03/2017
 * @see 	Random
 * @see		Vehicle
 */
public final class FamilySedan extends Vehicle {
	
	/**
	 * The physical size representation of the <code>FamilySedan
	 * </code>.
	 */
	private static final double UNIT_SIZE = 1.5;
	/**
	 * The smallest possible tank size of the <code>FamilySedan</code>.
	 */
	private static final int MIN_TANK_SIZE = 12;
	/**
	 * The range of possible tank sizes of the <code>FamilySedan</code>.
	 */
	private static final int TANK_SIZE_RANGE = 6;
	/**
	 * The initial seed used for the {@link #RNG}.
	 */
	private static final long SEED = 99;
	/**
	 * The <code>Random</code> object used to generate properties of
	 * the <code>FamilySedan</code>.
	 */
	private static final Random RNG = new Random(SEED);
	
	/**
	 * 
	 */
	public FamilySedan() {
		
		/*
		 * 
		 */
		
		super(UNIT_SIZE, RNG.nextInt(TANK_SIZE_RANGE) + MIN_TANK_SIZE);
	}
	
}