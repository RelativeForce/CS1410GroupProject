package environment.model.roadusers;

import environment.Simulator;
import environment.model.roadusers.vehicles.FamilySedan_Vehicle;

/**
 * This {@link FamilySedan_RoadUser} encapsulates the behaviour of a family
 * sedan in the {@link Station}. This is a subclass of the {@link RoadUser}.
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
public class FamilySedan_RoadUser extends RoadUser implements Cloneable {

	/**
	 * The probability that the {@link RoadUser} will shop
	 */
	private static final double PROB_TO_SHOP = 0.4;

	/**
	 * The maximum amount of money minus the minimum amount of money the
	 * {@link RoadUser} will spend
	 */
	private static final int SPENDING_RANGE = 8;
	
	/**
	 * The minimum amount of money that the {@link RoadUser} will spend
	 */
	private static final int MINIMUM_SPENDING_MONEY = 8;

	/**
	 * The maximum amount of time minus the minimum amount of time the
	 * {@link RoadUser} will spend shopping
	 */
	private static final int SHOPPING_TIME_RANGE = 18;
	
	/**
	 * The minimum amount of time the {@link RoadUser} will spend shopping
	 */
	private static final int MINIMUM_SHOPPING_TIME = 12;

	/**
	 * The maximum time that the {@link RoadUser} will spend at the {@link Pump}
	 * before the {@link RoadUser} will become unhappy and decide not to shop
	 */
	private static final int MAXIMUM_TIME_TO_SHOP = 60;

	/**
	 * Constructs a new {@link FamilySedan_RoadUser}.
	 */
	public FamilySedan_RoadUser() {

		super(new FamilySedan_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY, PROB_TO_SHOP, MAXIMUM_TIME_TO_SHOP);

	}

	/**
	 * Returns whether an instance of a {@link FamilySedan_RoadUser} will appear
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
		return (value > (2 * p)) && (value <= q);
	}

	/**
	 * Clones a new {@link FamilySedan_RoadUser}
	 */
	@Override
	public FamilySedan_RoadUser clone() {

		FamilySedan_RoadUser clone = (FamilySedan_RoadUser) super.createClone(new FamilySedan_RoadUser());

		return clone;
	}

}
