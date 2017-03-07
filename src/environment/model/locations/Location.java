package environment.model.locations;

import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;

import environment.model.roadUsers.RoadUser;

public abstract class Location {

	protected LinkedTransferQueue<RoadUser> queue;
	protected Class<? extends Location> nextLocation;
	protected int roadUsersProcessed;
	protected double profit;
	protected int fuelSold;

	public Location(Class<? extends Location> nextLocation) {

		queue = new LinkedTransferQueue<RoadUser>();
		this.nextLocation = nextLocation;
		roadUsersProcessed = 0;
		profit = 0;
		fuelSold = 0;

	}

	public abstract void processQueue(HashMap<Class<? extends Location>, RoadUser> toMove);

	public Class<? extends Location> getNextLocation() {
		return nextLocation;
	}

	public double getProfit() {
		return profit;
	}

	public int getFuelSold() {
		return fuelSold;
	}

	public int getRoadUsersProcessed() {
		return roadUsersProcessed;
	}

	public void enter(RoadUser newRoadUser) {

	}

}
