package environment.model;

import java.util.HashMap;
import java.util.LinkedList;
import environment.model.locations.Location;
import environment.model.roadUsers.RoadUser;

public class Station {
	
	private LinkedList<Location> locations;
	private HashMap<Class<? extends Location>, RoadUser> toMove;
	private int roadUsersRejected;
	
	public Station(){
		
		toMove = new HashMap<Class<? extends Location>, RoadUser>();
		locations = new LinkedList<Location>();
		roadUsersRejected = 0;
		
	}
	
	public void processLocations(){
		
	}
	
	@Override
	public String toString(){
		
		
		return "";
	}
	
	public void enterStation (RoadUser roadUser){
		
		
	}
	
	private boolean isSpaceInStation(){
		
		
		return false;
	}
	
	private void relocateRoadUsers(){
		
	}
}
