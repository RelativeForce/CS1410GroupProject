package environment.model.vehicles;

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
 * @version 01/03/2017
 * @since 	01/03/2017
 */
public abstract class Vehicle {
	
	/**
	 * The number of time units that the <code>Vehicle</code> has
	 * existed represented as an <code>int</code>.
	 */
	private int timeSpent;
	/**
	 * The amount of fuel currently inside the tank of the <code>
	 * Vehicle</code>, represented as an <code>int</code>. 
	 */
	private int fuelLevel;
	//private final Driver driver;
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
		 * Initialise the timeSpent as 0 since a vehicle has not had any
		 * time elapse (in the simulation) 
		 */
		timeSpent = 0;
		
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
	 * by one if the {@link #fuelLevel} is less than the {@link #tankSize}.
	 * 
	 * <p>
	 * Increment the {@link #fuelLevel} for the <code>Vehicle</code>when the
	 * {@link #fuelLevel} is less than the {@link tankSize}, otherwise no
	 * changes will be made to {@link #fuelLevel} as a <code>Vehicle</code>
	 * cannot fill it's fuel tank once it is full.
	 * </p>
	 */
	private void fill(){
		
		//Only increment the fuelLevel if it is less than the tankSize.
		if(fuelLevel < tankSize) ++fuelLevel;
	}
	/**
	 * Increment {@link #timeSpent} by one unit of time.
	 * <p>
	 * If the {@link #timeSpent} is not less than (is equals to) <code>
	 * Integer.MAX_VALUE</code> then {@link #timeSpent} will not be
	 * incremented as that would result in {@link #timeSpent} to become
	 * <code>Integer.MIN_VALUE</code> due to integer overflow.
	 * </p>
	 */
	public final void spendTime(){
		
		/*
		 * Increment the timeSpent.
		 * 
		 * As a safeguard against integer overflow, the timeSpent is only
		 * incremented if timeSpent cannot cause an integer overflow.
		 * 
		 * Integer overflow is unlikely.
		 */
		
		if(timeSpent < Integer.MAX_VALUE) ++timeSpent;
	}
	/**
	 * When the <code>Vehicle</code> is refilling it's fuel tank,
	 * the ... ... ... ((Merge with fill?))
	 */
	public final void act(){
		
		fill();
		//tbi
	}
	/**
	 * Check if the <code>Vehicle</code> has completely filled it's fuel tank.
	 * 
	 * <p>
	 * <strong>Cannot be overridden in subclasses</strong> as every <code>Vehicle
	 * </code> is full when the {@link #fuelLevel} is equal to the {@link #tankSize}.
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
	 * 
	 * @return
	 */
	//TBI
	//public abstract Driver getDriver();
	
	//Driver class here
}