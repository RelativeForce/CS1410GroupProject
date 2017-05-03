package environment.model.roadusers;

import java.util.Random;

import environment.model.Station;
import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * 
 * The {@link RoadUser} encapsulates the behaviour of the driver of a
 * {@link Vehicle}.
 * 
 * 
 * <p>
 * {@link RoadUser}s may {@link #shop()}, this models the driver browsing the
 * items in a {@link ShoppingArea}. They may also {@link #pay()} which causes
 * this {@link RoadUser} to pay for the fuel in their {@link Vehicle} and
 * results in {@link #hasPaid()} returning <code>true</code>. Also
 * {@link #getVehicle()} retrieves the {@link Vehicle} assigned to this
 * {@link RoadUser}. The number of ticks spent waiting at the {@link Pump}
 * effects whether a {@link RoadUser} will shop or not. The time spent in the
 * {@link Station} can be incremented using {@link #spendTime()} and
 * {@link #willShop()} method will return whether the criteria for this
 * {@link RoadUser} to shop have been met or not. The amount of money a
 * {@link RoadUser} will spend in the {@link ShoppingArea} can be retrieved
 * using {@link #getWorth()}. When this {@link RoadUser} has finished shopping
 * {@link #doneShopping()} will return <code>true</code>.
 * </p>
 * 
 * 
 * @author Adrian_Wong
 * @author Joshua_Eddy
 * 
 * @version 01/05/2017
 * 
 * @see java.util.Random
 *
 */
public abstract class RoadUser implements Cloneable {

	// Protected Fields -------------------------------------------------------

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
	protected boolean finishedShopping;

	/**
	 * The {@link Random} generator that should be user by the sub-classes of
	 * {@link RoadUser}.
	 * 
	 * @see java.util.Random
	 */
	protected static final Random GEN = new Random();

	// Private Fields ---------------------------------------------------------

	/**
	 * The value of this Boolean object as a boolean primitive.
	 */
	private boolean hasPaid;

	/**
	 * Holds the {@link Vehicle} assigned to <code>this</code> {@link RoadUser}.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 */
	private Vehicle vehicle;

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

	/**
	 * The <code>double</code> probability that <code>this</code>
	 * {@link RoadUser} will shop.
	 */
	private double probabilityToShop;

	/**
	 * The <code>int</code> maximum time that <code>this</code> {@link RoadUser}
	 * will spent in the {@link Station} and still shop.
	 */
	private int maximumTimeToShop;

	// Constructor ------------------------------------------------------------

	/**
	 * Constructs a new {@link RoadUser}.
	 * 
	 * @param vehicle
	 *            The {@link Vehicle} assigned to <code>this</code>
	 *            {@link RoadUser}.
	 * @param shoppingTime
	 *            The <code>int</code> amount of time will spend shopping.
	 * @param worth
	 *            The <code>double</code> amount of money <code>this</code>
	 *            {@link RoadUser} will spent.
	 * @param probabilityToShop
	 *            The <code>double</code> probability that <code>this</code>
	 *            {@link RoadUser} will shop.
	 * 
	 * @param maximumTimeToShop
	 *            The <code>int</code> maximum time that <code>this</code>
	 *            {@link RoadUser} will spent in the {@link Station} and still
	 *            shop.
	 */
	public RoadUser(Vehicle vehicle, int shoppingTime, double worth, double probabilityToShop, int maximumTimeToShop) {

		this.timeSpent = 0;
		this.hasPaid = false;
		this.willShop = true;
		this.isShopping = false;
		this.vehicle = vehicle;
		this.shoppingTime = shoppingTime;
		this.worth = worth;
		this.timeSpentShopping = 0;
		this.probabilityToShop = probabilityToShop;
		this.maximumTimeToShop = maximumTimeToShop;

	}

	// Public Methods ------------------------------------------------------

	/**
	 * Retrieves the {@link Vehicle} assigned to <code>this</code>
	 * {@link RoadUser}.
	 * 
	 * @return {@link Vehicle}
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * Increments the time spent in the {@link Station}.
	 */
	public void spendTime() {
		timeSpent++;
	}

	/**
	 * If the {@link RoadUser} spends less time than {@link #maximumTimeToShop}
	 * and has the required probability then it will shop
	 */
	public boolean willShop() {

		double prob = GEN.nextDouble();

		if (timeSpent < maximumTimeToShop && prob <= probabilityToShop) {
			return true;
		}

		return false;

	}

	/**
	 * Causes the {@link RoadUser} to browse in the shop.
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
	 * Retrieves the amount of money that <code>this</code> {@link RoadUser}
	 * will spent at the shop.
	 * 
	 * @return <code>double</code>/
	 */
	public double getWorth() {
		return worth;
	}

	/**
	 * Changes the {@link RoadUser} state to denote that they have payed for
	 * their fuel.
	 */
	public void pay() {
		hasPaid = true;
	}

	/**
	 * Returns whether <code>this</code> {@link RoadUser} has paid for their
	 * fuel.
	 * 
	 * @return <code>double</code>
	 */
	public boolean hasPaid() {
		return hasPaid;
	}

	/**
	 * Returns whether <code>this</code> {@link RoadUser} is finished shopping.
	 * 
	 * @return <code>double</code>
	 */
	public boolean doneShopping() {
		return finishedShopping;
	}

	/**
	 * Clones <code>this</code> {@link RoadUser}. </br>
	 * The method {@link #createClone(RoadUser)} should be used to create the
	 * initial clone then that clone should be casted to the specific sub-class
	 * of {@link RoadUser}. Then then that clone should be returned.
	 */
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

	/**
	 * Retrieves the number of ticks spent in the {@link Station} according to
	 * this {@link RoadUser}. {@link Location}s must increment this to cause the
	 * passage of time.
	 * 
	 * @return <code>int</code> number of ticks spent in the station.
	 * 
	 * @see #timeSpent
	 */
	public int getTimeSpent() {
		return timeSpent;
	}

	// Protected Methods -------------------------------------------------------

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
}
