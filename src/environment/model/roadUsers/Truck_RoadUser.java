package environment.model.roadUsers;

import java.util.Random;

import environment.model.roadUsers.vehicles.Truck_Vehicle;

public class Truck_RoadUser extends RoadUser {

	private static final double PROB_TO_SHOP = 1.0;
	private static final double SPENDING_RANGE = 5;
	private static final double MINIMUM_SPENDING_MONEY = 15;
	private static final double MAXIMUM_SPENDING_MONEY = 20;
	private static final int SHOPPING_TIME_RANGE = 18;
	private static final int MINIMUM_SHOPPING_TIME = 24;
	private static final int MAXIMUM_TIME_TO_SHOP = 36;
	private static double t = 0.02;

	public Truck_RoadUser() {
		super(new Truck_Vehicle());
	}

	public static boolean exists(double p, double q, double value) {
		return (value > ((2 * p) + q)) && (value <= ((2 * p) + q + t));
	}

	public void shop() {
		if (timeSpent < 48) {
			Random rn = new Random();
			double d = rn.nextDouble();
			if (d <= PROB_TO_SHOP) {
				// shop();
			}
		}
	}

	@Override
	public double getWorth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
