package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * The <code>FamilySedan</code> class is a subclass of the {@link vehicle}
 * class, which models a Family sedan.
 * 
 * <p>
 * The <code>FamilySedan</code> uses the {@link Vehicle#size} and
 * {@link Vehicle#tankSize} attributes from the {@link Vehicle} class, which
 * are initialised upon creation of the <code>FamilySedan</code>.
 * </p>
 * 
 * <p>
 * <strong>The <code>FamilySedan</code> class cannot be extended.</strong>
 * </p>
 * 
 * @author 	John
 * @version	13/03/2017
 * @since 	02/03/2017
 * @see 	Random
 * @see		Vehicle
 */
public final class FamilySedan_Vehicle extends Vehicle {
	
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
	private static final int TANK_SIZE_RANGE = 7;
	
	/**
	 * Create a new <code>FamilySedan</code>.
	 * 
	 * <p>
	 * Initialise a <code>FamilySedan</code> object by invoking the
	 * {@link Vehicle} constructor, where the {@link Vehicle#size} is
	 * set as {@link #UNIT_SIZE} ({@value #UNIT_SIZE}) and the
	 * {@link Vehicle#tankSize} is set to a minimum of {@link #MIN_TANK_SIZE}
	 * ({@value #MIN_TANK_SIZE}), with a range of {@link #TANK_SIZE_RANGE}
	 * ({@value #TANK_SIZE_RANGE}).
	 * </p>
	 * 
	 * <p>
	 * <code>size = RNG.nextInt({@value #TANK_SIZE_RANGE}) + {@value #MIN_TANK_SIZE}
	 * </code> 
	 * </p>
	 */
	public FamilySedan_Vehicle() {
		
		/*
		 * Set the size of the Vehicle to UNIT_SIZE and randomly generate the
		 * tankSize to be between 12 and 18. 
		 */
		
		super(UNIT_SIZE, RNG.nextInt(TANK_SIZE_RANGE) + MIN_TANK_SIZE);
	}
	/**
	 * Check if an object is equal to the <code>FamilySedan</code>.
	 * 
	 * <p>
	 * Compare an object against a <code>FamilySedan</code> if the object is an
	 * instance of the <code>FamilySedan</code> class, then the
	 * {@link Vehicle#equals(Object)} method will determine if the object is equal
	 * to the <code>FamilySedan</code>, otherwise, if the object is not an instance
	 * of the <code>FamilySedan</code> class, the object cannot be equal to any <code>
	 * FamilySedan</code>.
	 * </p>
	 * 
	 * @param o The object to be tested for equality against.
	 * @return <code>true</code> if the object is and instance of the
	 * 		<code>FamilySedan</code> class, and the {@link Vehicle#size} and
	 * 		{@link Vehicle#tankSize} of the object are equal to the <code>
	 * 		FamilySedan</code>; otherwise <code>false</code>.
	 */
	@Override
	public boolean equals(Object o){
		
		/*
		 * Check if o is a FamilySedan, if so, determine the equality in the superclass
		 * otherwise o cannot be equal to a FamilySedan.
		 */
		
		return o instanceof FamilySedan_Vehicle? super.equals(o): false;
	}
	/**
	 * Get the <code>String</code> representation of the <code>FamilySedan</code>.
	 * 
	 * <p>
	 * Get the <code>FamilySedan</code> represented as a <code>String</code>,
	 * containing the <code>FamilySedan</code> name, and the result from the
	 * {@link Vehicle#toString()}.
	 * </p>
	 * 
	 * <p>
	 * Format:
	 * </p>
	 * 
	 * <p>
	 * <code>FamilySedan. Size: 1.5 Tank (Gallons): 15</code>
	 * </p>
	 * 
	 * @return The <code>String</code> representation of the <code>FamilySedan</code>.
	 */
	@Override
	public String toString(){
		
		/*
		 * Make a String with the name of the class, and the attributes from the
		 * Vehicle superclass.
		 */
		
		return new StringBuilder()
				.append("FamilySedan. ")
				.append(super.toString())
				.toString();
	}
}