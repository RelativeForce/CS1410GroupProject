package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.SmallCar_Vehicle;

/**
 * The <code>SmallCar</code> class is a subclass of the {@link RoadUser} class,
 * which models a Small Car User.
 * 
 * @author Adrian_Wong, Josh_Eddy
 * @version 04/04/2017
 * @since 21/03/2017
 * @see roadusers
 * @see random
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
	 * Generates a random number
	 */
	private static final Random GEN = new Random();

	/**
	 * Generates a new {@link SmallCar_RoadUser}
	 */
	public SmallCar_RoadUser() {
		super(new SmallCar_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY);

	}

	public static boolean exists(double p, double q, double value) {
		return value <= p;
	}

	/**
	 * Clones a {@link FamilySedan_RoadUser}
	 */
	public SmallCar_RoadUser clone() {
		SmallCar_RoadUser clone = (SmallCar_RoadUser) super.createClone(new SmallCar_RoadUser());
		return clone;
	}

	@Override
	/**
	 * If the {@link SmallCar_RoadUser} spends less time than
	 * {@link MAXIMUM_TIME_TO_SHOP} and has the required probability then it
	 * will shop
	 */
	public boolean willShop() {

		double prob = GEN.nextDouble();

		if (timeSpent < MAXIMUM_TIME_TO_SHOP && prob <= PROB_TO_SHOP) {
			return true;
		}

		return false;
	}
}
