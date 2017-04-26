package environment.model.roadusers;

import environment.Simulator;
import environment.model.roadusers.vehicles.SmallCar_Vehicle;

/**
 * This {@link SmallCar_RoadUser} encapsulates the behaviour of a family sedan
 * in the {@link Station}. This is a subclass of the {@link RoadUser}.
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
public class SmallCar_RoadUser extends RoadUser implements Cloneable {

	/**
	 * The probability that the {@link RoadUser} will shop
	 */
	private static final double PROB_TO_SHOP = 0.3;

	/**
	 * The maximum amount of money minus the minimum amount of money the
	 * {@link RoadUser} will spend
	 */
	private static final int SPENDING_RANGE = 5;
	/**
	 * The minimum amount of money that the {@link RoadUser} will spend
	 */
	private static final int MINIMUM_SPENDING_MONEY = 5;

	/**
	 * The maximum amount of time minus the minimum amount of time the
	 * {@link RoadUser} will spend shopping
	 */
	private static final int SHOPPING_TIME_RANGE = 12;
	/**
	 * The minimum amount of time the {@link RoadUser} will spend shopping
	 */
	private static final int MINIMUM_SHOPPING_TIME = 12;

	/**
	 * The maximum time that the {@link RoadUser} will spend at the {@link Pump}
	 * before the {@link RoadUser} will become unhappy and decide not to shop
	 */
	private static final int MAXIMUM_TIME_TO_SHOP = 30;

	/**
	 * Generates a new {@link SmallCar_RoadUser}
	 */
	public SmallCar_RoadUser() {
		super(new SmallCar_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY, PROB_TO_SHOP, MAXIMUM_TIME_TO_SHOP);

	}

	/**
	 * Returns whether an instance of a {@link SmallCar_RoadUser} will appear in
	 * the station using a the values of <strong>p</strong>, <strong>q</strong>
	 * and <strong>value</strong> given by the {@link Simulator} and applying
	 * them to a predetermined formula.
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
		return value <= p;
	}

	/**
	 * Clones a {@link FamilySedan_RoadUser}
	 */
	@Override
	public SmallCar_RoadUser clone() {
		SmallCar_RoadUser clone = (SmallCar_RoadUser) super.createClone(new SmallCar_RoadUser());
		return clone;
	}

}
