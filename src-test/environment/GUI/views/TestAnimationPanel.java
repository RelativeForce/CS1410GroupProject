package environment.GUI.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import environment.model.Station;
import environment.model.locations.Pump;
import environment.model.locations.ShoppingArea;
import environment.model.locations.Till;

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
		station.addLocation(new Pump(Till.class));
		
		try{
			
			ap.draw(station);
			Thread.sleep(2000);
			Station s = new Station(ShoppingArea.class);
			s.addLocation(new ShoppingArea(null));
			ap.draw(s);
			Thread.sleep(2000);
			station.addLocation(new Pump(Till.class));
			station.addLocation(new Till(null));
			ap.draw(station);
			Thread.sleep(2000);
		}
		catch(InterruptedException e){
			
			
		}
	}
}