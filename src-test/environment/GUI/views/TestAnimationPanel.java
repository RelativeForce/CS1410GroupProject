package environment.GUI.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import environment.model.Station;
import environment.model.locations.Pump;

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
		station.addLocation(new Pump(null));
		
		try{
			
			ap.draw(station);
			Thread.sleep(10000);
		}
		catch(InterruptedException e){
			
			
		}
	}
}