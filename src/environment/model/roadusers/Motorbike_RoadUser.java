package environment.model.roadusers;

import environment.model.roadusers.vehicles.Motorbike_Vehicle;
import environment.model.roadusers.RoadUser;

/**
 * The <code>Motorbike</code> class is a subclass of the {@link RoadUser}
 * class, which models a Motorbike User.
 * 
 * @author Adrian_Wong
 * @version 04/04/2017
 * @since	21/03/2017
 * @see		roadusers
 * @see 	random
 *
 */
public class Motorbike_RoadUser extends RoadUser implements Cloneable {

	/**
	 * Generates a new {@link Motorbike_RoadUser}
	 */
	public Motorbike_RoadUser() {
		super(new Motorbike_Vehicle(), 0, 0);
	}

	public static boolean exists(double p, double q, double value) {
		return (value > p) && (value <= (2 * p));
	}

	@Override
	/**
	 * The Motorbike Road User does not shop
	 */
	public void shop() {
		// Doesn't Shop
	}

	@Override
	/**
	 * @returns The worth of a {@link Motornike_RoadUser}
	 */
	public double getWorth() {
		return 0;
	}

	@Override
	/**
	 * @returns false as {@link Motornike_RoadUser} does not shop
	 */
	public boolean willShop() {
		return false;
	}

	/**
	 * Clones a {@link Motornike_RoadUser}
	 */
	public Motorbike_RoadUser clone() {

		Motorbike_RoadUser clone = (Motorbike_RoadUser) super.createClone(new Motorbike_RoadUser());

		return clone;
	}
}
