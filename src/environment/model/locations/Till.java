package environment.model.locations;

import java.util.HashMap;

import environment.model.roadUsers.RoadUser;

public class Till extends Location {

	public Till(Class<? extends Location> nextLocation) {
		super(nextLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processQueue(HashMap<Class<? extends Location>, RoadUser> toMove) {
		// TODO Auto-generated method stub

	}

}
