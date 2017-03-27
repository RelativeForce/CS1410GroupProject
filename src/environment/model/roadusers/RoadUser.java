package environment.model.roadusers;

import java.util.Random;

import environment.model.locations.Location;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * 
 * This <code>abstract</code> class encapsulates the behaviour of a road user in
 * a <code>Vehicle</code>. This should be extended with concrete subclasses.
 * 
 * @author Adrian_Wong
 * @version 20/03/2017
 *
 */

public abstract class RoadUser implements Cloneable {

	// Fields --------------------------------------------------------------

	/**
	 * The amount of time that <code>this</code> {@link RoadUser} has spent
	 * queueing and then filling up at <code>this</code> {@link Pump}.
	 * 
	 * @see
	 */
	protected int timeSpent;

	protected boolean willShop;

	private boolean hasPaid;

	private Vehicle vehicle;

	protected boolean finishedShopping;

	private int shoppingTime;

	private double worth;

	private int timeSpentShopping;

	// Public Methods ------------------------------------------------------

	public RoadUser(Vehicle vehicle, int shoppingTime, double worth) {

		this.timeSpent = 0;
		this.hasPaid = false;
		this.willShop = true;
		this.vehicle = vehicle;
		this.shoppingTime = shoppingTime;
		this.worth = worth;
		this.timeSpentShopping = 0;

	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void spendTime() {
		timeSpent++;
	}

	public abstract boolean willShop();

	public void shop() {

		timeSpentShopping++;

		if (timeSpentShopping > shoppingTime) {

			finishedShopping = true;

		}

	}

	public double getWorth() {
		return worth;
	}

	public void pay() {
		hasPaid = true;
	}

	public boolean hasPaid() {
		return hasPaid;
	}

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

}
