package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.Truck_Vehicle;

public class Truck_RoadUser extends RoadUser implements Cloneable {

	private static final double PROB_TO_SHOP = 1.0;
	private static final int SPENDING_RANGE = 5;
	private static final int MINIMUM_SPENDING_MONEY = 15;
	private static final int SHOPPING_TIME_RANGE = 18;
	private static final int MINIMUM_SHOPPING_TIME = 24;
	private static final int MAXIMUM_TIME_TO_SHOP = 36;
	private static final double INITIAL_T = 0.02;
	
	private static double t = INITIAL_T;

	private static final Random GEN = new Random();

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
			t = t * 0.05;
		}

		return belowMaxTime && (prob <= PROB_TO_SHOP);
	}

	@Override
	public double getWorth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Truck_RoadUser clone(){
		Truck_RoadUser copy = (Truck_RoadUser) super.createClone(new Truck_RoadUser());
		return copy;
	}

}
