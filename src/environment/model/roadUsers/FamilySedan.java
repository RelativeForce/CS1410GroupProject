package environment.model.roadUsers;

import environment.model.roadUsers.vehicles.VehicleTypes;

public class FamilySedan extends RoadUser {

	private static final double PROB_TO_SHOP = 0.4;
	private static final double SPENDING_RANGE = 8;
	private static final double MINIMUM_SPENDING_MONEY = 8;
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
		// Hey mom i am amazing
	}

}
