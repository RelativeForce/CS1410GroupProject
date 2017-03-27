package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.SmallCar_Vehicle;

public class SmallCar_RoadUser extends RoadUser implements Cloneable{

	private static final double PROB_TO_SHOP = 0.3;
	
	private static final int SPENDING_RANGE = 5;
	private static final int MINIMUM_SPENDING_MONEY = 5;
	
	private static final int SHOPPING_TIME_RANGE = 12;
	private static final int MINIMUM_SHOPPING_TIME = 12;
	
	private static final int MAXIMUM_TIME_TO_SHOP = 24;

	private static final Random GEN = new Random();

	public SmallCar_RoadUser() {
		super(new SmallCar_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY);

	}
	
	public static boolean exists(double p, double q, double value) {
		return value <= p;
	}
	
	public SmallCar_RoadUser clone() {
		SmallCar_RoadUser clone = (SmallCar_RoadUser)super.createClone(new SmallCar_RoadUser());
		return clone;
	}

	@Override
	public boolean willShop() {
		
		double prob = GEN.nextDouble();
		return (timeSpent < MAXIMUM_TIME_TO_SHOP) && (prob <= PROB_TO_SHOP);
	}
}
