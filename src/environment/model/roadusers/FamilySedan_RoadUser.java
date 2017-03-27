package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.FamilySedan_Vehicle;

public class FamilySedan_RoadUser extends RoadUser implements Cloneable {

	private static final double PROB_TO_SHOP = 0.4;

	private static final int SPENDING_RANGE = 8;
	private static final int MINIMUM_SPENDING_MONEY = 8;

	private static final int SHOPPING_TIME_RANGE = 18;
	private static final int MINIMUM_SHOPPING_TIME = 12;

	private static final int MAXIMUM_TIME_TO_SHOP = 60;
	
	private static final Random GEN = new Random();	

	public FamilySedan_RoadUser() {

		super(new FamilySedan_Vehicle(), GEN.nextInt(SHOPPING_TIME_RANGE) + MINIMUM_SHOPPING_TIME,
				GEN.nextInt(SPENDING_RANGE) + MINIMUM_SPENDING_MONEY);

	}

	public static boolean exists(double p, double q, double value) {
		return (value > (2 * p)) && (value <= q);
	}

	@Override
	public boolean willShop() {

		double prob = GEN.nextDouble();

		return (timeSpent < MAXIMUM_TIME_TO_SHOP) && (prob <= PROB_TO_SHOP);
	}

	public FamilySedan_RoadUser clone() {

		FamilySedan_RoadUser clone = (FamilySedan_RoadUser) super.createClone(new FamilySedan_RoadUser());

		return clone;
	}

}
