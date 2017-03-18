package environment.model.roadUsers;

import java.util.Random;

import environment.model.roadUsers.vehicles.SmallCar_Vehicle;

public class SmallCar_RoadUser extends RoadUser {

	private static final double PROB_TO_SHOP = 0.3;
	private static final double SPENDING_RANGE = 5;
	private static final double MINIMUM_SPENDING_MONEY = 5;
	private static final double MAXIMUM_SPENDING_MONEY = 10;
	private static final int SHOPPING_TIME_RANGE = 12;
	private static final int MINIMUM_SHOPPING_TIME = 12;
	private static final int MAXIMUM_TIME_TO_SHOP = 24;
	private static final int SEED = 11;
	private static final Random GEN = new Random(SEED);

	public SmallCar_RoadUser() {
		super(new SmallCar_Vehicle());

	}
	
	public static boolean exists(double p, double q, double value) {
		return value <= p;
	}

	@Override
	public void shop() {
		// TODO Auto-generated method stub
		if (timeSpent < 30) {
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
