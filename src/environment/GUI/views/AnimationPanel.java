package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import environment.model.Station;
import environment.model.locations.Location;
import environment.model.roadusers.RoadUser;

public class AnimationPanel extends JPanel {
	
	/**
	 * The scaling factor of the {@link JPanel} which determens the size
	 * of the ...
	 */
	private static final int SCALING_FACTOR = 4;
	private static final int TOP_MARGIN = 50;
	public static final int BLOCK_SIZE = 75;
	private static final Font TEXT_FONT = new Font("calibri", 0, 12);
	private final Dimension size;
	
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
		super.setMinimumSize(new Dimension(width, height));
		super.setPreferredSize(new Dimension(width, height));
		super.setMaximumSize(new Dimension(width, height));
		size = new Dimension(width, height);
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
		if(img == null || getWidth() != size.width || getHeight() != size.height){
			
			img = super.createImage(getWidth(), getHeight()); //Initialise the img.
			g = img.getGraphics(); //Get the graphics of the image.
			size.setSize(getWidth(), getHeight());
		}
		
		g.setColor(Color.GREEN); //Set the background to green.
		g.fillRect(0, 0, size.width, size.height); //Draw the rectangle as the background.
		
		//Reset the font.
		g.setFont(TEXT_FONT);
	}
	/**
	 * Draw a table of ...
	 */
	private void drawColorTable(final int positionY){
		
		//Set the color to black, then draw a tag to indicate where colors will be displayed.
		g.setColor(Color.BLACK);
		g.drawString("Colors:", 0, positionY);
		
		final int colorNameMargin = 10; //Spacing between the color and the name.
		final int entryMargin = 20; //Spacing between entries.
		int positionX = 0; //x draw position.
		int offsetY = 20; //y draw offset.
		
		//Loop through all the defined colors to add them to the table.
		for(Entry<Class<? extends RoadUser>, Color> ruc:
			Visualisation.getRoadUserColors()){
			
			//Remove the "_RoadUser" part of the name.
			final String text = ruc.getKey().getSimpleName().replaceAll("_RoadUser", "");
			
			/*
			 * If the x position exceeds the width of the image, or if the string will
			 * be drawn outside the image, then reset x to 0 and go to the next y.
			 */
			if(size.width <= positionX 
					|| size.width <= positionX + g.getFontMetrics().stringWidth(text)){
				
				positionX = 0; //Reset x.
				offsetY += g.getFontMetrics().getHeight(); //Move down.
			}
			
			//Set the color to the color of the roaduser, then draw an 8 x 8 rectangle.
			g.setColor(ruc.getValue());
			g.fillRect(positionX, positionY + offsetY - 8, 8, colorNameMargin);
			
			/*
			 * Set the color to black and then draw the name of the roaduser next to the
			 * color.
			 */
			g.setColor(Color.black);
			g.drawString(text, positionX += colorNameMargin, positionY + offsetY);
			
			//Move to the next x location.
			positionX += g.getFontMetrics().stringWidth(text) + entryMargin;
			
		}
	}
	private int getEffectiveHeight(){
		
		return size.height - TOP_MARGIN;
	}
	/**
	 * 
	 * @param station
	 */
	public void draw(final Station station){
		
		prepareDraw(); //Prepare the img before drawing.
		
		//Group locations which exist in parallel.
		List<List<Location>> locationGroups = groupLocations(station.getLocations());
		
		//Calculate the horizontal spacing between locations. 
		final int spacingX = (size.width - BLOCK_SIZE*locationGroups.size())
				/(locationGroups.size() + 1);
		int positionX = spacingX; // x start position.
		
		//Loop through the groups of locations.
		for(List<Location> group: locationGroups){
			
			//Calculate the vertical spacing between locations.
			final int spacingY = (getEffectiveHeight() - BLOCK_SIZE*group.size())/(group.size() + 1);
			int positionY = spacingY; //y start position.
			
			//Loop through the parallel locations.
			for(Location loc: group){
				
				//Set the color to black and then draw the name of the location.
				g.setColor(Color.BLACK);
				g.drawString(loc.getClass().getSimpleName(), positionX, positionY);
				
				//Get the visual representation of the location and draw it.
				Visualisation.getVisual(loc.getClass()).visual
					.visiulise(g, loc, positionX, positionY);
				
				//Move to the next y-position.
				positionY += spacingY + BLOCK_SIZE;
			}
			
			//Move to the next x-position.
			positionX += spacingX + BLOCK_SIZE;
		}
		
		drawColorTable(getEffectiveHeight());
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