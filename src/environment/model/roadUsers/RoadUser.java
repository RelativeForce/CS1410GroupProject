package environment.model.roadUsers;

import environment.model.roadUsers.vehicles.Vehicle;

public abstract class RoadUser {

	protected int timeSpent;
	protected boolean willShop;
	
	private Vehicle vehicle;
	private boolean finishedShopping;
	
	public RoadUser(Vehicle vehicle){
		timeSpent = 0;
	}
	
	public Vehicle getVehicle(){
		return vehicle;
	}
	
	public void spendTime(){
		timeSpent++;
	}
	
	public abstract void shop();
	
	public boolean doneShopping(){
		return finishedShopping;
	}
	
}
