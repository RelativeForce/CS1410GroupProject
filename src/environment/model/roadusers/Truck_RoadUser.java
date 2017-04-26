package environment.model.roadusers;

import environment.Simulator;
import environment.model.roadusers.vehicles.Truck_Vehicle;

/**
 * This {@link Truck_RoadUser} encapsulates the behaviour of a family sedan
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
public class Truck_RoadUser extends RoadUser implements Cloneable {

	/**
	 * The probability that the {@link RoadUser} will shop
	 */
	private static final double PROB_TO_SHOP = 1.0;

	/**
	 * The maximum amount of money minus the minimum amount of money the
	 * {@link RoadUser} will spend
	 */
	private static final int SPENDING_RANGE = 5;
	
	/**
	 * The minimum amount of money that the {@link RoadUser} will spend
	 */
	private static final int MINIMUM_SPENDING_MONEY = 15;

	/**
	 * The maximum amount of time minus the minimum amount of time the
	 * {@link RoadUser} will spend shopping
	 */
	private static final int SHOPPING_TIME_RANGE = 18;
	
	/**
	 * The minimum amount of time the {@link RoadUser} will spend shopping
	 */
	private static final int MINIMUM_SHOPPING_TIME = 24;

	/**
	 * The maximum time that the {@link RoadUser} will spend at the {@link Pump}
	 * before the {@link RoadUser} will become unhappy and decide not to shop
	 */
	private static final int MAXIMUM_TIME_TO_SHOP = 48;

	/**
	 * The initial value for {@link #t}.
	 */
	private static final double INITIAL_T = 0.02;

	/**
	 * Holds value for <strong>t</strong> which is a value used in the
	 * {@link #exists(double, double, double)} method to determining if a new
	 * {@link Truck_RoadUser} will appear in the {@link Simulator}.
	 */
	private static double t = INITIAL_T;

	/**
	 * Generates a new {@link Truck_RoadUser}
	 */
	public Truck_RoadUser() {
		super(new Truck_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY, PROB_TO_SHOP, MAXIMUM_TIME_TO_SHOP);
	}

	/**
	 * Returns whether an instance of a {@link Truck_RoadUser} will appear in
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
		return (value > ((2 * p) + q)) && (value <= ((2 * p) + q + t));
	}

	/**
	 * If the {@link Truck_RoadUser} spends less time than
	 * {@link #maximumTimeToShop}, has the required probability then it will
	 * shop. The evaluation of this method will effect the probability of more
	 * {@link Truck_RoadUser} appearing in the {@link Simulator}.
	 */
	@Override
	public boolean willShop() {

		double prob = GEN.nextDouble();
		boolean belowMaxTime = (timeSpent < MAXIMUM_TIME_TO_SHOP);

		if (!belowMaxTime) {
			t = t * 0.8;
		} else if (belowMaxTime && t < INITIAL_T) {
			t = t * 1.05;
		}

		return belowMaxTime && (prob <= PROB_TO_SHOP);
	}

	/**
	 * Generates a clone of {@link Truck_RoadUser}
	 */
	@Override
	public Truck_RoadUser clone() {
		Truck_RoadUser copy = (Truck_RoadUser) super.createClone(new Truck_RoadUser());
		return copy;
	}

}
