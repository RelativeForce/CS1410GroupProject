package environment.model.roadusers;

import java.util.Random;

import environment.model.roadusers.vehicles.Motorbike_Vehicle;

public class Motorbike_RoadUser extends RoadUser {

	private static final double PROB_TO_SHOP = 0.0;

	public Motorbike_RoadUser() {
		super(new Motorbike_Vehicle());
		// TODO Auto-generated constructor stub
	}
	
	public static boolean exists(double p, double q, double value){
		return (value > p) && (value <= (2 * p));
	}

	@Override
	public void shop() {
		// TODO Auto-generated method stub

		Random rn = new Random();
		double d = rn.nextDouble();
		if (d <= PROB_TO_SHOP) {

			System.out.print("\"Motorbike drivers in the area are thrifty and will never go to the shopping area.\"");
		}

	}

	@Override
	public double getWorth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean willShop(){
		return false;
	}
}
