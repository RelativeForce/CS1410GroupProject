package environment.model.roadUsers.vehicles;

import java.util.Random;

/**
 * The <code>Vehicle</code> class models a <code>Vehicle</code>
 * which is being refilled with fuel at a fuel station.
 * 
 * <p>
 * The <code>Vehicle</code> class is an abstract class which
 * cannot be used as an anonymous class, and must be extended by
 * another class to be usable.
 * </p>
 * 
 * <p>
 * The <code>Vehicle</code> class provides a number of method to
 * modify and describe the status of the <code>Vehicle</code>.
 * The <code>Vehicle</code> class also contains the shared behaviour
 * of every type of <code>Vehicle</code> i.e. every <code>Vehicle
 * </code> is full when it's fuel level is equal to the size of it's
 * tank.
 * </p>
 * 
 * <p>
 * Subclasses of the <code>Vehicle</code> class should
 * 
 * <ui type="circle">
 * 		<li>Have a fixed physical size</li>
 * 		<li>Have a fixed fuel tank size</li>
 * 		<li>Have a fuel level</li>
 * </ui>
 * 
 * Examples of such could be, cars, motorcycles and lorries; as each
 * of these vehicles have, a fixed fuel tank size, a physical size and
 * a fuel level which increases as the vehicle is being refilled.
 * </p>
 * 
 * @author 	John Berg
 * @version 09/09/2017
 * @since 	01/03/2017
 */
public abstract class Vehicle {
	
	/**
	 * The default price of the <code>Vehicle</code> fuel, represented as
	 * <code>double</code>.
	 */
	private static final double DEFAULT_FUEL_PRICE = 1.20;
	/**
	 * The initial seed for the {@link #RNG}.
	 */
	private static final long SEED = 9000;
	/**
	 * The <code>Random</code> object used to generate properties for the
	 * <code>Vehicle</code>.
	 */
	protected static final Random RNG = new Random(SEED);
	/**
	 * The amount of fuel currently inside the tank of the <code>
	 * Vehicle</code>, represented as an <code>int</code>.
	 */
	private int fuelLevel;
	/**
	 * The size of the <code>Vehicle</code>, the <code>double</code>
	 * representation of the physical space occupied.
	 * 
	 * <p>
	 * <strong>Cannot be modified.</strong>
	 * </p>
	 */
	public final double size;
	/**
	 * The tank size of the <code>Vehicle</code> represented as an
	 * <code>int</code>.
	 * 
	 * <p>
	 * <strong>Cannot be modified.</strong>
	 * </p>
	 */
	public final int tankSize;
	
	/**
	 * Create a new <code>Vehicle</code> object.
	 * 
	 * <p>
	 * Create a new <code>Vehicle</code> by defining a physical size to
	 * for the <code>Vehicle</code> object's {@link #size}, and a fuel
	 * tank size as the <code>Vehicle</code> object's {@link #tankSize};
	 * after the initialisation the <code>Vehicle</code> object cannot
	 * have the {@link #size} and {@link tankSize} changed.
	 * </p>
	 * 
	 * @param size The physical size of the <code>Vehicle</code>.
	 * @param tankSize The size of the tank of the <code>Vehicle</code>.
	 */
	protected Vehicle(final double size, final int tankSize){
		
		 /*
		  * Initialise the fuelLebel as 0 (Assumed fuel level).
		  */
		fuelLevel = 0;
		
		/*
		 * Initialise the size of the vehicle as the provided constructor 
		 * size argument.
		 */
		this.size = size;
		
		/*
		 * Initialise the tankSize of the vehicle as the provided
		 * constructor tankSize argument.
		 */
		this.tankSize = tankSize;
	}
	
	/**
	 * Increment the <code>Vehicle</code> objects {@link #fuelLevel}
	 * by one.
	 * 
	 * <p>
	 * If the <code>Vehicle</code> object's {@link #fuelLevel} is less than
	 * the {@link #tankSize}, increment the {@link #fuelLevel} by one;
	 * otherwise the tank is full and cannot be filled further, which means
	 * that the {@link #fuelLevel} will not be incremented as a <code>Vehicle
	 * </code> cannot fill it's fuel tank once it is full.
	 * </p>
	 */
	public final void fill(){
		
		//Only increment the fuelLevel if it is less than the tankSize.
		if(fuelLevel < tankSize) ++fuelLevel;
	}
	/**
	 * Check if the <code>Vehicle</code> has completely filled it's fuel tank.
	 * 
	 * <p>
	 * Check if the <code>Vehicle</code> the <code>Vehicle</code> has completely
	 * filled the fuel tank. <strong>Cannot be overridden in subclasses</strong> as
	 * every instance of <code>Vehicle</code> is full when the {@link #fuelLevel}
	 * is equal to the {@link #tankSize}.
	 * </p>
	 * 
	 * @return <code>true</code> if the {@link #fuelLevel} is equal to the
	 * 			{@link #tankSize}, otherwise return <code>false</code>.
	 */
	public final boolean isFull(){
		
		/*
		 * Considered using tankSize <= fuelLevel, however, as there is no way
		 * of modifying the tankSize and the only place where the fuelLevel is
		 * be modified is in the fill() method which prevents the fuelLevel from
		 * incrementing once the tankSize has been reached, there should be no
		 * way for the subclasses to modify the fuelLevel as the fuelLevel has
		 * private visibility.
		 */
		
		return fuelLevel == tankSize;
	}
	/**
	 * Get the current worth of the <code>Vehicle</code>.
	 * 
	 * <p>
	 * The current worth of the <code>Vehicle</code> is the current cost of filling
	 * the <code>Vehicle</code>. The current worth of the <code>Vehicle</code>
	 * cannot exceed the {@link #getMaxWorth()}. When the {@link #isFull()} method
	 * returns <code>true</code>, the current worth is equal to the
	 * {@link #getMaxWorth()}.
	 * </p>
	 * 
	 * <p>
	 * The current worth of the <code>Vehicle</code> is calculated as
	 * <code>{@value #DEFAULT_FUEL_PRICE}*{@link #fuelLevel}</code>.
	 * </p>
	 * 
	 * @return The current cost of filling the <code>Vehicle</code>.
	 */
	public final double getCurrentWorth(){
		
		//Only using DEFAULT_FUEL_LEVEL whilst no fuel types exists.
		return fuelLevel*DEFAULT_FUEL_PRICE;
	}
	/**
	 * Get the total worth of refilling the <code>Vehicle</code>.
	 * 
	 * <p>
	 * The max worth of a <code>Vehicle</code> is the cost of filling the fuel tank
	 * until it is full.
	 * </p>
	 * 
	 * <p>
	 * The total worth of the <code>Vehicle</code> is calculated as
	 * <code>{@value #DEFAULT_FUEL_PRICE}*{@link #tankSize}</code>.
	 * </p>
	 * 
	 * @return The total cost of filling the <code>Vehicle</code>.
	 */
	public final double getMaxWorth(){
		
		//Only using DEFAULT_FUEL_LEVEL whilst no fuel types exists.
		return tankSize*DEFAULT_FUEL_PRICE;
	}
	/**
	 * Check if two <code>Vehicle</code> objects are equal.
	 * 
	 * <p>
	 * For an object to equal a <code>Vehicle</code> object, the object must
	 * 
	 * <ui type="cirlce">
	 * 		<li>Be an instance of the {@link Vehicle} class</li>
	 * 		<li>Have the same {@link #size}</li>
	 * 		<li>Have the same {@link #tankSize}</li>
	 * </ui>
	 * </p>
	 * 
	 * @param o The object to be tested for equality against.
	 * @return <code>true</code> if the tested object is an instance of the
	 * 		<code>Vehicle</code> class, and the {@link #size} and {@link #tankSize}
	 * 		attributes is equal to the <code>Vehicle</code> object; otherwise,
	 * 		returns <code>false</code>.
	 */
	@Override
	public boolean equals(Object o){
		
		//Check if the object is an instance of the Vehicle class.
		if(o instanceof Vehicle){
			
			//Label the Object as a Vehicle (since o is an instance of Vehicle).
			Vehicle v = (Vehicle) o;
			
			//Check if the size and the tankSize are equal.
			return this.size == v.size && this.tankSize == v.tankSize;
		}
		else{
			
			/*
			 * The Object is not an instance of Vehicle and hence cannot be
			 * logically equal any Vehicle.
			 */
			
			return false;
		}
	}
	/**
	 * Get the <code>String</code> representation of the <code>Vehicle</code> object.
	 * 
	 * <p>
	 * Get the <code>String</code> containing the <code>Vehicle</code> objects
	 * {@link #size} and {@link #tankSize}.
	 * </p>
	 * 
	 * <p>
	 * Format:
	 * </p>
	 * 
	 * <p>
	 * <code>Size: 0.85 Tank (Gallons): 5</code>
	 * </p>
	 * 
	 * @return The <code>String</code> representation of the <code>Vehicle</code>
	 * 		object.
	 */
	@Override
	public String toString(){
		
		return new StringBuilder()
				.append("Size: ")
				.append(size)
				.append(" Tank (Gallons): ")
				.append(tankSize)
				.toString();
	}
}