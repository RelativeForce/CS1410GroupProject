package environment.model.roadusers;

import environment.model.roadusers.vehicles.Motorbike_Vehicle;

public class Motorbike_RoadUser extends RoadUser implements Cloneable {

	public Motorbike_RoadUser() {
		super(new Motorbike_Vehicle(), 0, 0);
	}

	public static boolean exists(double p, double q, double value) {
		return (value > p) && (value <= (2 * p));
	}

	@Override
	public void shop() {
		// Doesn't Shop
	}

	@Override
	public double getWorth() {
		return 0;
	}

	@Override
	public boolean willShop() {
		return false;
	}

	public Motorbike_RoadUser clone() {

		Motorbike_RoadUser clone = (Motorbike_RoadUser) super.createClone(new Motorbike_RoadUser());

		return clone;
	}
}
