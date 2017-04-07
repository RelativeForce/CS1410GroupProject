package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import environment.model.Station;
import environment.model.locations.Location;

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
		Iterator<? extends Location> locIter = locations.iterator();
		
		while(locIter.hasNext()){
			
			//Class<? extends Location> nextLocation = locIter.next();
			
			while(locIter.hasNext()){
				
				
			}
		}
		
		int positionX = width/(locations.size() + 1) - x/2;
		int positionY = height/(locations.size() + 1) - x/2;
		
		for(Location l: locations){
			
			g.setColor(Color.BLACK);
			g.drawString(l.getClass().getSimpleName(), positionX, positionY);
			g.setColor(Color.GRAY);
			g.fillRect(positionX, positionY, x, x);
			positionX += width/(locations.size() + 1);
			positionY += height/(locations.size() + 1);
		};
		
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