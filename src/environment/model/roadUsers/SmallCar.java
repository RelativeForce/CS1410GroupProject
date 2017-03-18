package environment.model.roadUsers;

import java.util.Random;

import environment.model.roadUsers.vehicles.VehicleTypes;

public class SmallCar extends RoadUser {

	private static final double PROB_TO_SHOP = 0.3;
	private static final double SPENDING_RANGE = 5;
	private static final double MINIMUM_SPENDING_MONEY = 5;
	private static final double MAXIMUM_SPENDING_MONEY = 10;
	private static final int SHOPPING_TIME_RANGE = 12;
	private static final int MINIMUM_SHOPPING_TIME = 12;
	private static final int MAXIMUM_TIME_TO_SHOP = 24;
	private static final int SEED = 11;
	private static final Random GEN = new Random(SEED);
	
	public SmallCar() {
		super(VehicleTypes.SMALL_CAR.create());

	}

	@Override
	public void shop() {
		// TODO Auto-generated method stub
		if (timeSpent < 30) {
			 Random rn = new Random();
			    double d = rn.nextDouble();   
			    if(d<=PROB_TO_SHOP){
			    //shop();
			
		}
	}

}
