package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * The <code>Motorbike</code> class is a subclass of the {@link Vehicle}
 * class, which models a motorbike.
 * 
 * <p>
 * The <code>Motorbike</code> uses the {@link Vehicle#size} and the
 * {@link Vehicle#tankSize} attributes from the {@link Vehicle} class.
 * </p>
 * 
 * <p>
 * <strong>The <code>Motorbike</code> class cannot be extended.</strong>
 * </p>
 * 
 * @author 	John Berg
 * @version 13/03/2017
 * @since	01/03/2017
 * @see		Random
 * @see 	Vehicle
 */
public final class Motorbike extends Vehicle {
	
	/**
	 * The physical size representation of the <code>Motorbike</code>.
	 */
	private static final double UNIT_SIZE = 0.75;
	/**
	 * The tank size of the <code>Motorbike</code>.
	 */
	private static final int TANK_SIZE = 5;
	
	/**
	 * Create a <code>Motorbike</code> object.
	 * 
	 * <p>
	 * Initialise a new <code>Motorbike</code> object where the
	 * {@link Vehicle} superclass is initialised with the {@link Vehicle#size}
	 * of {@link #UNIT_SIZE} ({@value #UNIT_SIZE}), and the
	 * {@link Vehicle#tankSize} of {@link #TANK_SIZE} ({@value #TANK_SIZE}).
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
	/**
	 * Check if an object is equal to the <code>Motorbike</code> object.
	 * 
	 * <p>
	 * Compare an object agains the <code>Motorbike</code> object to check of they
	 * are logically equal. First check if the object is an instance of the <code>
	 * Motorbike</code> class, if so, then use the {@link Vehicle#equals(Object)}
	 * method to test the objects {@link Vehicle#size} and {@link Vehicle#tankSize}
	 * attributes; if the object is not an instance of <code>Motorbike</code> then
	 * the object cannot be equal to any <code>Motorbike</code>.
	 * </p>
	 * 
	 * @param o The object to be tested for equality against.
	 * @return <code>true</code> if object is an instance of <code>Motorbike
	 * 		</code> and has the same {@link Vehicle#size} and
	 * 		{@link Vehicle#tankSize} as the <code>Motorbike</code> object;
	 * 		otherwise returns <code>false</code>.
	 */
	@Override
	public boolean equals(Object o){
		
		/*
		 * Check if the object an instance of the Motorbike class, if it
		 * is then check the equality in the Vehicle equals method; otherwise
		 * the result is false.
		 */
		
		return o instanceof Motorbike? super.equals(o): false;
	}
	/**
	 * Get the <code>String</code> representation of the <code>Motorbike</code>.
	 * 
	 * <p>
	 * Represent the <code>Motorbike</code> as a <code>String</code> containing
	 * the name of the <code>Motorbike</code> and, the {@link Vehicle#size} and
	 * {@link Vehicle#tankSize} from the {@link Vehicle} class using the
	 * {@link Vehicle#toString()} method.
	 * </p>
	 * 
	 * <p>
	 * Format:
	 * </p>
	 * 
	 * <p>
	 * <code>Motorbike. Size: 0.75 Tank (Gallons): 5</code>.
	 * </p>
	 * 
	 * @return The <code>String</code> representation of the
	 * 		<code>Motorbike</code>.
	 */
	@Override
	public String toString(){
		
		/*
		 * Build a String with the name of the class (Motorbike), then get the
		 * size and tank size of the Motorbike from the Vehicle class' toString()
		 * method.
		 */
		
		return new StringBuilder()
				.append("Motorbike. ")
				.append(super.toString())
				.toString();
	}
}