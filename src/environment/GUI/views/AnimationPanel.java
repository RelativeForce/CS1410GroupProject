package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JPanel;

import environment.model.Station;
import environment.model.locations.Location;
import sun.font.FontScaler;

public class AnimationPanel extends JPanel {
	
	/**
	 * The scaling factor of the {@link JPanel} which determens the size
	 * of the ...
	 */
	private static final int SCALING_FACTOR = 4;
	private final int width;
	private final int height;
	
	/**
	 * 
	 */
	private Image img;
	private Graphics g;
	/**
	 * 
	 * @param width
	 * @param height
	 */
	public AnimationPanel(final int width, final int height){
		
		//Set the size of the JPanel.
		super();
		this.width = width;
		this.height = height;
		
		//super.setBackground(Color.GREEN);
	}
	private void prepareDraw(){
		
		if(img == null)
			img = super.createImage(width, height);
		
		g = img.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, width, height);
	}
	/**
	 * 
	 * @param station
	 */
	public void draw(final Station station){
		
		int x = 100;
		
		prepareDraw();
		List<Location> locations = station.getLocations();
		
		locations.forEach(l -> {
			
			
			g.setColor(Color.BLACK);
			g.drawString(l.getClass().getSimpleName(), 10, 20);
			g.setColor(Color.GRAY);
			g.fillRect(10, 30, x, x);
		});
		
		repaint();
	}
	@Override
	public final void paintComponent(final Graphics g){
		
		if(img != null){
			
			g.drawImage(img, 0, 0, null);
			g.dispose();
		}
	}
}
