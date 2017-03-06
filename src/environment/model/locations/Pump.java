package environment.model.locations;

import java.util.HashMap;

import environment.model.roadUsers.RoadUser;

public class Pump extends Location {

	public Pump(Class<? extends Location> nextLocation) {
		super(nextLocation);

	}

	@Override
	public void processQueue(HashMap<Class<? extends Location>, RoadUser> toMove) {
		// TODO Auto-generated method stub

	}

}
