package environment.GUI.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import environment.model.Station;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;
import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

public class TestAnimationPanel {
	
	private AnimationPanel ap;
	private JFrame frame;
	
	
	@Before
	public void setUp(){
		
		ap = new AnimationPanel(500, 500);
		
		frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ap);
		frame.setPreferredSize(new Dimension(500, 500));
		frame.pack();
		frame.setVisible(true);
	}
	@Test
	public void testDraw(){
		
		Station station = new Station(Pump.class);
		station.addLocation(new Pump(ShoppingArea.class));
		
		try{
			
			ap.draw(station);
			Thread.sleep(2000);
			Station s = new Station(ShoppingArea.class);
			s.addLocation(new ShoppingArea(null));
			ap.draw(s);
			Thread.sleep(2000);
			station.addLocation(new Pump(ShoppingArea.class));
			station.addLocation(new Pump(ShoppingArea.class));
			station.addLocation(new Pump(ShoppingArea.class));
			station.addLocation(new ShoppingArea(Till.class));
			station.addLocation(new Till(null));
			station.addLocation(new Till(null));
			

			station.enter(new Motorbike_RoadUser());
			station.processLocations();
			station.enter(new Motorbike_RoadUser());
			station.processLocations();
			station.enter(new Motorbike_RoadUser());
			station.processLocations();
			station.enter(new Motorbike_RoadUser());
			station.processLocations();
			
			for(int i = 0; i < 100; ++i){
				
				//station.
				station.processLocations();
				station.enter(new SmallCar_RoadUser());
				ap.draw(station);
				Thread.sleep(1000);
			}
		}
		catch(InterruptedException e){
			
			
		}
	}
}