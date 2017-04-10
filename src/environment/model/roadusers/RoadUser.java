package environment.model.roadusers;

import environment.model.roadusers.vehicles.Vehicle;

/**
 * 
 * The <code>RoadUser</code> class models a <code>RoadUser</code> which is
 * driving a vehicle.
 * 
 * <p>
 * The <code>RoadUser</code> class is an abstract class which cannot be used as
 * an anonymous class, and must be extended by another class to be usable.
 * </p>
 * 
 * <p>
 * The <code>RoadUser</code> class provides a number of method to modify and
 * describe the status of the <code>RoadUser</code>. The <code>RoadUser</code>
 * class also contains the shared behaviour of every type of
 * <code>RoadUser</code> i.e. every <code>RoadUser
 * </code> will shop when it's probability is equal to the probability to it's
 * vehicle's given shopping probability
 * </p>
 * 
 * <p>
 * Subclasses of the <code>RoadUser</code> class should
 * 
 * <ul>
 * <li>Have a number for time spent</li>
 * <li>Have boolean whether it will shop or not</li>
 * <li>Have boolean for has it paid or not</li>
 * <li>Have a vehicle type</li>
 * <li>Have a shopping time</li>
 * <li>Have the amount spent</li>
 * </ul>
 * 
 * @author Adrian_Wong
 * @author Joshua_Eddy
 * @version 10/04/2017
 * @since 20/03/2017
 *
 */
public abstract class RoadUser implements Cloneable {

	// Fields --------------------------------------------------------------

	/**
	 * The amount of time that <code>this</code> {@link RoadUser} has spent
	 * queueing and then filling up.
	 */
	protected int timeSpent;

	/**
	 * The value of this Boolean object as a boolean primitive.
	 */
	protected boolean willShop;

	/**
	 * The value of this Boolean object as a boolean primitive.
	 */
	private boolean hasPaid;

	private Vehicle vehicle;

	/**
	 * The value of this Boolean object as a boolean primitive.
	 */
	protected boolean finishedShopping;

	/**
	 * The amount of time <code>this</code> {@link RoadUser} will spend shopping
	 */
	private int shoppingTime;

	/**
	 * The amount of money <code>this</code> {@link RoadUser} has spent
	 */
	private double worth;

	/**
	 * The amount of time <code>this</code> {@link RoadUser} has spent shopping
	 */
	private int timeSpentShopping;

	/**
	 * Whether <code>this</code> {@link RoadUser} is currently shopping or not.
	 */
	private boolean isShopping;

	// Public Methods ------------------------------------------------------

	/**
	 * Constructor for objects of class RoadUser.
	 * 
	 * @param vehicle
	 *            The vehicle type
	 * @param shoppingTime
	 *            The amount of time will spend shopping
	 * @param worth
	 *            The amount of money spent
	 */
	public RoadUser(Vehicle vehicle, int shoppingTime, double worth) {

		this.timeSpent = 0;
		this.hasPaid = false;
		this.willShop = true;
		this.isShopping = false;
		this.vehicle = vehicle;
		this.shoppingTime = shoppingTime;
		this.worth = worth;
		this.timeSpentShopping = 0;

	}

	/**
	 * @return the vehicle type
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * Increments the time spent
	 */
	public void spendTime() {
		timeSpent++;
	}

	/**
	 * The value of this Boolean object as a boolean primitive.
	 */
	public abstract boolean willShop();

	/**
	 * The {@link RoadUser} will have finished shopping if the time spent
	 * shopping is less than the shopping time range
	 */
	public void shop() {

		timeSpentShopping++;
		isShopping = true;

		if (timeSpentShopping > shoppingTime) {

			finishedShopping = true;
			isShopping = false;

		}

	}

	/**
	 * @return the amount of money spent by the <code>this</code>
	 *         {@link RoadUser}
	 * 
	 */
	public double getWorth() {
		return worth;
	}

	/**
	 * The {@link RoadUser} will have to pay for fuel and shopping
	 */
	public void pay() {
		hasPaid = true;
	}

	/**
	 * @return the value of this Boolean object as a boolean primitive.
	 */
	public boolean hasPaid() {
		return hasPaid;
	}

	/**
	 * @return the value of this Boolean object as a boolean primitive.
	 */
	public boolean doneShopping() {
		return finishedShopping;
	}

	/**
	 * For use by concrete sub-classes of <code>this</code>. Will take a road
	 * user as parameter and return it with its {@link RoadUser} fields
	 * identical to <code>this</code>.
	 * 
	 * @param clone
	 *            The {@link RoadUser} to be made identical to
	 *            <code>this</code>.
	 * @return The parameter {@link RoadUser} with instance fields identical to
	 *         <code>this</code>.
	 */
	protected RoadUser createClone(RoadUser clone) {

		clone.vehicle = this.vehicle.clone();

		clone.finishedShopping = this.finishedShopping;
		clone.hasPaid = this.hasPaid;
		clone.timeSpent = this.timeSpent;
		clone.willShop = this.willShop;
		clone.timeSpentShopping = this.shoppingTime;
		clone.shoppingTime = this.shoppingTime;
		clone.worth = this.worth;

		return clone;
	}

	@Override
	public abstract RoadUser clone();

	/**
	 * Retrieves whether <code>this</code> is currently shopping or not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isShopping() {
		return isShopping;
	}
}
