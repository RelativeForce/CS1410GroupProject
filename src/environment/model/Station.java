package environment.model;

import java.util.HashMap;
import java.util.LinkedList;
import environment.model.locations.Location;
import environment.model.roadUsers.RoadUser;

public class Station {

	private LinkedList<Location> locations;
	private HashMap<RoadUser, Class<? extends Location>> toMove;
	private int roadUsersRejected;
	private Class<? extends Location> startLoaction;

	public Station(Class<? extends Location> startLocation) {

		toMove = new HashMap<RoadUser, Class<? extends Location>>();
		locations = new LinkedList<Location>();
		roadUsersRejected = 0;
		this.startLoaction = startLocation;

	}

	public void processLocations() {

		relocateRoadUsers();
		
		for(Location location : locations){
			location.processQueue(toMove);
		}
		
	}

	@Override
	public String toString() {

		return "";
	}

	public void enterStation(RoadUser roadUser) {

		if (isSpaceInStation(roadUser)) {

			for (Location currentLocation : locations) {
				if (currentLocation.getClass() == startLoaction && currentLocation.isEnoughSpaceFor(roadUser)) {
					currentLocation.enter(roadUser);
					break;
				}
			}

		} else {
			roadUsersRejected++;
		}

	}
	
	public int getRoadUsersRejected(){
		return roadUsersRejected;
	}

	private boolean isSpaceInStation(RoadUser newRoadUser) {

		for (Location currentLocation : locations) {

			if (currentLocation.getClass() == startLoaction) {
				if (currentLocation.isEnoughSpaceFor(newRoadUser)) {
					return true;
				}
			}
		}

		return false;
	}

	private void relocateRoadUsers() {
		
		for(RoadUser roadUser: toMove.keySet()){
			
			Class<? extends Location> nextLocation = toMove.get(roadUser);

			for(Location location : locations){
				
				if(location.getClass() == nextLocation && location.isEnoughSpaceFor(roadUser)){
					
					location.enter(roadUser);
					toMove.remove(roadUser);
					break;
					
				}
			}
		}
		
		if(!toMove.isEmpty()){
			// Put it back where it came from! Or so help me!
		}

	}
}
