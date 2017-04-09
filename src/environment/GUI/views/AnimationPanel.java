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
	public static final int BLOCK_SIZE = 100;
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
	}
	/**
	 * Prepare the {@link AnimationPanel} before drawing.
	 * 
	 * <p>
	 * Preparations involve:
	 * 
	 * <ol>
	 * 		<li>Initialise the {@link #img} if <code>null</code></li>
	 * 		<li>Get the {@link Graphics} of the {@link #img}</li>
	 * 		<li>Draw a green rectangle as a background</li>
	 * </ol>
	 * </p>
	 * 
	 */
	private void prepareDraw(){
		
		//Check if the img is null.
		if(img == null){
			
			img = super.createImage(width, height); //Initialise the img.
			g = img.getGraphics(); //Get the graphics of the image.
		}
		
		g.setColor(Color.GREEN); //Set the background to green.
		g.fillRect(0, 0, width, height); //Draw the rectangle as the background.
	}
	/**
	 * 
	 * @param station
	 */
	public void draw(final Station station){
		
		prepareDraw(); //Prepare the img before drawing.
		List<List<Location>> locationGroups = groupLocations(station.getLocations());
		
		final int startPositionX = width/(locationGroups.size() + 1) - BLOCK_SIZE/2;
		int positionX = startPositionX;
		
		for(List<Location> group: locationGroups){
			
			final int startPositionY = height/(group.size() + 1) - BLOCK_SIZE/2;
			int positionY = startPositionY;
			
			for(Location loc: group){
				
				g.setColor(Color.BLACK);
				g.drawString(loc.getClass().getSimpleName(), positionX, positionY);
				LocationVisual.getVisual(loc.getClass()).visual
					.visiulise(g, loc, positionX, positionY);
				positionY += height/(group.size() + 1);
			}
			
			positionX += width/(locationGroups.size() + 1);
		}
		
		repaint();
	}
	/**
	 * 
	 * 
	 * @param locations A {@link List} of {@link Location} objects.
	 * @return A {@link List} of {@link List} objects containing locations which exist
	 * 		in parallel.
	 */
	private List<List<Location>> groupLocations(List<Location> locations){
		
		LinkedList<List<Location>> locationGroups = new LinkedList<List<Location>>();
		locationGroups.add(new LinkedList<Location>());
		
		Location next;
		Class<? extends Location> nextLoc = null;
		
		for(Location loc: locations){
			
			next = loc;
			
			if(locationGroups.peekLast().isEmpty()){
				
				nextLoc = next.getNextLocation();
				locationGroups.peekLast().add(next);
			}
			else if(nextLoc == next.getNextLocation()){
				
				locationGroups.peekLast().add(next);
			}
			else{
				
				nextLoc = next.getNextLocation();
				locationGroups.add(new LinkedList<Location>());
				locationGroups.peekLast().add(next);
			}
		}
		
		return locationGroups;
	}
	@Override
	protected final void paintComponent(final Graphics g){
		
		if(img != null){
			
			g.drawImage(img, 0, 0, null);
		}
	}
}