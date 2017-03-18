package environment.model.roadUsers;

import environment.model.roadUsers.vehicles.VehicleTypes;

public class Truck extends RoadUser {
	
	private static final double PROB_TO_SHOP = 1.0;
	private static final double SPENDING_RANGE = 5;
	private static final double MINIMUM_SPENDING_MONEY = 15;
	private static final double MAXIMUM_SPENDING_MONEY = 20;
	private static final int SHOPPING_TIME_RANGE = 18;
	private static final int MINIMUM_SHOPPING_TIME = 24;
	private static final int MAXIMUM_TIME_TO_SHOP = 36;
	
	public Truck() {
		super(VehicleTypes.TRUCK.create());
		
	}
	
	public void shop() {
		if (timeSpent < 48 ) {
			
		}

}
