package environment.model.roadUsers;

import java.util.Random;

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
		
		Random rn = new Random();
	    double d = rn.nextDouble();   
	    	if(d<=PROB_TO_SHOP){
	    	
		System.out.print("\"Motorbike drivers in the area are thrifty and will never go to the shopping area.\"");
	}

}
