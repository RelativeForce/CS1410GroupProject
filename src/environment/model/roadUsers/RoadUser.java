package environment.model.roadUsers;

import environment.model.roadUsers.vehicles.Vehicle;

public abstract class RoadUser {

	protected int timeSpent;
	protected boolean willShop;
	private boolean hasPaid;
	private Vehicle vehicle;
	private boolean finishedShopping;

	public RoadUser(Vehicle vehicle) {
		timeSpent = 0;
		hasPaid = false;
		willShop = true;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void spendTime() {
		timeSpent++;
	}
	
	public boolean willShop(){
		return willShop;
	}

	public abstract void shop();

	public abstract double getWorth();

	public void pay() {
		hasPaid = true;
	}

	public boolean hasPaid() {
		return hasPaid;
	}

	public boolean doneShopping() {
		return finishedShopping;
	}

}
