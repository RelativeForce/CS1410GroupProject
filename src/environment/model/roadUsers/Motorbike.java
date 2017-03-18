package environment.model.roadUsers;

import environment.model.roadUsers.vehicles.VehicleTypes;

public class Motorbike extends RoadUser {
	
	private static final double PROB_TO_SHOP = 0.0;
	
	public Motorbike() {
		super(VehicleTypes.MOTORBIKE.create());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void shop() {
		// TODO Auto-generated method stub
		System.out.print("\"Motorbike drivers in the area are thrifty and will never go to the shopping area.\"");
	}

}
