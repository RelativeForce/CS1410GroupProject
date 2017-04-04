package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.Truck_Vehicle;

/**
 * The <code>Truck</code> class is a subclass of the {@link RoadUser}
 * class, which models a Truck User.
 * 
 * @author Adrian_Wong, Josh_eddy
 * @version 04/04/2017
 * @since	21/03/2017
 * @see		roadusers
 * @see 	random
 *
 */
public class Truck_RoadUser extends RoadUser implements Cloneable {

	/**
	 * The probabilty that the {@link RoadUser} will shop
	 */
	private static final double PROB_TO_SHOP = 1.0;
	
	/**
	 * The maximum amount of money minus the minimum amount of money the {@link RoadUser} will spend
	 */
	private static final int SPENDING_RANGE = 5;
	/**
	 * The minimum amount of money that the {@link RoadUser} will spend
	 */
	private static final int MINIMUM_SPENDING_MONEY = 15;
	
	/**
	 * The maximum amount of time minus the minimum amount of time the {@link RoadUser} will spend shopping
	 */
	private static final int SHOPPING_TIME_RANGE = 18;
	/**
	 * The minimum amount of time the {@link RoadUser} will spend shopping
	 */
	private static final int MINIMUM_SHOPPING_TIME = 24;
	
	/**
	 * The maximum time that the {@link RoadUser} will spend at the {@link Pump} before the {@link RoadUser} will become unhappy and decide not to shop
	 */
	private static final int MAXIMUM_TIME_TO_SHOP = 48;
	
	/**
	 * The initial value for T
	 */
	private static final double INITIAL_T = 0.02;
	
	private static double t = INITIAL_T;

	/**
	 * Generates a random number
	 */
	private static final Random GEN = new Random();

	/**
	 * Generates a new {@link Truck_RoadUser}
	 */
	public Truck_RoadUser() {
		super(new Truck_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY);
	}

	public static boolean exists(double p, double q, double value) {
		return (value > ((2 * p) + q)) && (value <= ((2 * p) + q + t));
	}

	@Override
	public boolean willShop() {

		double prob = GEN.nextDouble();
		boolean belowMaxTime = (timeSpent < MAXIMUM_TIME_TO_SHOP);

		if (!belowMaxTime) {
			t = t * 0.8;
		} else if(belowMaxTime && t < INITIAL_T){
			t = t * 1.05;
		}

		return belowMaxTime && (prob <= PROB_TO_SHOP);
	}

	@Override
	/**
	 * @returm the amount of money spent by the {@link Truck_RoadUser}
	 */
	public double getWorth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Generates a clone of {@link Truck_RoadUser}
	 */
	public Truck_RoadUser clone(){
		Truck_RoadUser copy = (Truck_RoadUser) super.createClone(new Truck_RoadUser());
		return copy;
	}

}
