package environment.model.roadusers;

import environment.Simulator;
import environment.model.roadusers.vehicles.Motorbike_Vehicle;

/**
 * This {@link Motorbike_RoadUser} encapsulates the behaviour of a motor bike in
 * the {@link Station}. This is a subclass of the {@link RoadUser}.
 * 
 * @author Adrian_Wong
 * @author Joshua_Eddy
 * 
 * @version 26/04/2017
 * 
 * @see environment.model.roadusers.RoadUser
 * @see java.util.Random
 *
 */
public class Motorbike_RoadUser extends RoadUser implements Cloneable {

	/**
	 * Constructs a new {@link Motorbike_RoadUser}
	 */
	public Motorbike_RoadUser() {
		super(new Motorbike_Vehicle(), 0, 0, 0, 0);
	}

	/**
	 * Returns whether an instance of a {@link Motorbike_RoadUser} will appear
	 * in the station using a the values of <strong>p</strong>,
	 * <strong>q</strong> and <strong>value</strong> given by the
	 * {@link Simulator} and applying them to a predetermined formula.
	 * 
	 * @param p
	 *            <code>double</code>
	 * @param q
	 *            <code>double</code>
	 * @param value
	 *            <code>double</code>
	 * @return <code>boolean</code>
	 */
	public static boolean exists(double p, double q, double value) {
		return (value > p) && (value <= (2 * p));
	}

	/**
	 * The {@link Motorbike_RoadUser} does not shop. This method is unused as
	 * {@link #willShop()} will always return <code>false</code>.
	 */
	@Override
	public void shop() {
		// Doesn't Shop
	}

	/**
	 * @returns <code>false</code> as {@link Motornike_RoadUser} does not shop.
	 */
	@Override
	public boolean willShop() {
		return false;
	}

	/**
	 * Clones a <code>this</code> {@link Motornike_RoadUser}.
	 */
	@Override
	public Motorbike_RoadUser clone() {

		Motorbike_RoadUser clone = (Motorbike_RoadUser) super.createClone(new Motorbike_RoadUser());

		return clone;
	}
}
