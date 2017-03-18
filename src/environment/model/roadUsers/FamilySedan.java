package environment.model.roadUsers;

import java.util.Random;

import environment.model.roadUsers.vehicles.VehicleTypes;

public class FamilySedan extends RoadUser {

	private static final double PROB_TO_SHOP = 0.4;
	private static final double SPENDING_RANGE = 8;
	private static final double MINIMUM_SPENDING_MONEY = 8;
	private static final double MAXIMUM_SPENDING_MONEY = 16;
	private static final int SHOPPING_TIME_RANGE = 18;
	private static final int MINIMUM_SHOPPING_TIME = 12;
	private static final int MAXIMUM_TIME_TO_SHOP = 60;
	
	public FamilySedan() {
		
		super(VehicleTypes.FAMILY_SEDAN.create());
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void shop() {
		// TODO Auto-generated method stub
		if (timeSpent < 60) {
			Random rn = new Random();
		    double d = rn.nextDouble();   
		    if(d<=PROB_TO_SHOP){
		    //shop();
			}
		}
	}

}
