package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import environment.model.Station;
import environment.model.locations.Location;
import environment.model.roadusers.RoadUser;

/**
 * The {@link AnimationaPanel} class is a subclass of the {@link JPanel} class
 * which is designed for drawing visual representations of the {@link Station}
 * objects.
 * 
 * <p>
 * The {@link AnimationPanel} has an {@link Image} which is used to draw the
 * {@link Station}, and is responsible for mainly structuring and organising
 * the visual representation of the {@link Station} visual representation.
 * </p>
 * 
 * @author 	John Berg
 * @version	21/04/2017
 * @since	01/04/2017
 * @see JPanel
 * @see Image
 * @see Graphics
 */
public class AnimationPanel extends JPanel {
	
	/**
	 * The serial ID of the {@link AnimationPanel}.
	 */
	private static final long serialVersionUID = -1209329463678441603L;
	/**
	 * The amount of space between the main area of the {@link AnimationPanel}
	 * and the left side of {@link AnimationPanel}.
	 * 
	 * <p>
	 * The {@link #LEFT_MARGIN} reserves space for information about the simulation
	 * to be displayed.
	 * </p>
	 */
	private static final int LEFT_MARGIN = 170;
	/**
	 * The size of the area which is usable when drawing the {@link Location}
	 * objects.
	 * 
	 * <p>
	 * Each {@link Location} is assigned an area of equal size, where the
	 * size is also used to organise how the {@link Location} objects are placed
	 * on the screen.
	 * </p>
	 * 
	 * <p>
	 * The area usable by each {@link Location} is:
	 * <code>{@value #BLOCK_SIZE}*{@value #BLOCK_SIZE}</code>
	 * </p>
	 */
	public static final int BLOCK_SIZE = 80;
	/**
	 * The {@link Font} used by the {@link AnimationPanel}.
	 * 
	 * <p>
	 * The default {@link Font} which is used by the {@link AnimationPanel} and
	 * the size of the {@link Font}.
	 * </p>
	 * 
	 * @see Font
	 */
	private static final Font TEXT_FONT = new Font("calibri", 0, 12);
	/**
	 * The current width and height of the {@link AnimationPanel}.
	 * 
	 * <p>
	 * The {@link Dimension} containing the current width and height of this
	 * {@link AnimationPanel} which allows the {@link #img} to match the
	 * size of the {@link AnimationPanel}.
	 * </p>
	 * 
	 * @see Dimension
	 */
	private final Dimension size;
	/**
	 * The {@link Image} which will be drawn on.
	 * 
	 * <p>
	 * The {@link Image} object which the {@link AnimationPanel} uses
	 * to draw the simulation.
	 * </p>
	 * 
	 * @see Image
	 */
	private Image img;
	/**
	 * The graphics to be drawn to the {@link #img}.
	 * 
	 * <p>
	 * The {@link Graphics} object which id used to draw the {@link Station}
	 * object to the {@link #img}.
	 * </p>
	 * 
	 * @see Graphics
	 */
	private Graphics g;
	/**
	 * Create a new {@link AnimationPanel}.
	 * 
	 * <p>
	 * Initialise a {@link AnimationPanel} by providing the initial width and
	 * height of the panel.
	 * </p>
	 * 
	 * @param width The height of the {@link AnimationPanel}.
	 * @param height The width of the {@link AnimationPanel}.
	 */
	public AnimationPanel(final int width, final int height){
		
		//Set the size of the JPanel.
		super();
		super.setMinimumSize(new Dimension(width, height));
		super.setPreferredSize(new Dimension(width, height));
		super.setMaximumSize(new Dimension(width, height));
		size = new Dimension(width, height); //Initialise the dimensions.
	}
	/**
	 * Prepare the {@link AnimationPanel} before drawing.
	 * 
	 * <p>
	 * Preparations involve:
	 * 
	 * <ol>
	 * 		<li>Initialise the {@link #img} if <code>null</code> or if
	 * 		the this panel's size has been changed</li>
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
	 * Draw a table of {@link RoadUser} classes with their associated {@link Color}.
	 * 
	 * <p>
	 * Drawing the table involves:
	 * <ol>
	 * 		<li>Drawing a "Colors:" label.</li>
	 * 		<li>Drawing a colored square</li>
	 * 		<li>Drawing the name of the {@link RoadUser}</li>
	 * </ol>
	 * </p>
	 * 
	 * <p>
	 * The table is created at the bottom of the {@link AnimationPanel} where each entry is
	 * added as a row; if the entry exceeds the width of the {@link #img} then the entry will
	 * wrap to the next row of the table.
	 * </p>
	 * 
	 * @see Color
	 * @see RoadUser
	 */
	private final void drawInfo(final String[] lines){
		
		final int colorNameMargin = 10; //Spacing between the color and the name.
		int position = 20; //The starting y-position to draw at.
		
		//Set the color to black and draw the statistics header.
		g.setColor(Color.BLACK);
		g.drawString("Statistics:", getEffectiveWidth(), position);
		
		//Loop through each String.
		for(String line: lines){
			
			//Draw the String, and move to the next position.
			g.drawString(line, getEffectiveWidth(),
					position += g.getFontMetrics().getHeight());
		}
		
		//Draw The colors header and move to the next location.
		g.drawString("Colors:", getEffectiveWidth(), position += g.getFontMetrics().getHeight());
		
		//Loop through all the defined colors to add them to the table.
		for(Entry<Class<? extends RoadUser>, Color> ruc:
			Visualisation.getRoadUserColors()){
			
			//Remove the "_RoadUser" part of the name.
			final String text = ruc.getKey().getSimpleName().replaceAll("_RoadUser", "");
			
			 position += g.getFontMetrics().getHeight();
			
			//Set the color to the color of the roaduser, then draw an 8 x 8 rectangle.
			g.setColor(ruc.getValue());
			g.fillRect(getEffectiveWidth(),  position,8, 8);
			
			/*
			 * Set the color to black and then draw the name of the roaduser next to the
			 * color.
			 */
			g.setColor(Color.black);
			g.drawString(text, getEffectiveWidth() + colorNameMargin,
					position += g.getFontMetrics().getHeight()/2);
		}
	}
	/**
	 * Get the width which the {@link AnimationPanel} can use to draw the visual
	 * representation of the simulation.
	 * 
	 * @return The usable width of the {@link AnimationPanel} for visualising the
	 * 		simulation.
	 */
	private int getEffectiveWidth(){
		
		return size.width - LEFT_MARGIN;
	}
	/**
	 * Draw a {@link Station} object to this {@link AnimationPanel}.
	 * 
	 * <p>
	 * Draw a {@link Station} object by:
	 * <ol>
	 * 		<li>Calling the {@link #prepareDraw()} method</li>
	 * 		<li>Grouping the {@link Location} objects ({@link #groupLocations(List)})</li>
	 * 		<li>Calculating the spacing between {@link Location} objects</li>
	 * 		<li>Drawing each {@link Location} from the grouped {@link Location} objects
	 * 		at the next position.</li>
	 * </ol>
	 * </p>
	 * 
	 * <p>
	 * The specific visual representation for the {@link Location} object is stored in the
	 * {@link Visualisation} enum, which can be accessed by calling the
	 * {@link Visualisation#getVisual(Class)} method.
	 * </p>
	 * 
	 * @param station The {@link Station} to be drawn to the {@link AnimationPanel}.
	 * @see Station
	 * @see Visualisation
	 * @see Visualisation#getVisual(Class)
	 */
	public void draw(final int time,final Station station){
		
		prepareDraw(); //Prepare the img before drawing.
		
		//Group locations which exist in parallel.
		List<List<Location>> locationGroups = groupLocations(station.getLocations());
		
		//Calculate the horizontal spacing between locations. 
		final int spacingX = (getEffectiveWidth() - BLOCK_SIZE*locationGroups.size())
				/(locationGroups.size() + 1);
		int positionX = spacingX; // x start position.
		
		//Loop through the groups of locations.
		for(List<Location> group: locationGroups){
			
			//Calculate the vertical spacing between locations.
			final int spacingY = (size.height - BLOCK_SIZE*group.size())/(group.size() + 1);
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
		
		drawInfo(new StringBuilder().append("Time:   ")
				.append(time)
				.append("\n")
				.append(station)
				.toString().split("\n"));
		repaint();
	}
	/**
	 * Group a {@link List} of {@link Location} objects into a {@link List} objects
	 * of {@link Location} objects which exist in parallel.
	 * 
	 * <p>
	 * Convert a {@link List} of {@link Location} objects into a {@link List} of
	 * {@link List} of {@link Location} objects which represent the {@link Location}
	 * objects which exist in parallel during the simulation.
	 * </p>
	 * 
	 * <p>
	 * A groups of {@link Location} objects must share the same {@link Location#nextLocation},
	 * {@link Location} objects which meet this requierment will be placed in the same group as the
	 * other {@link Location} object with the same next {@link Location}; otherwise the {@link Location}
	 * is placed in the next group.
	 * </p>
	 * 
	 * @param locations A {@link List} of {@link Location} objects.
	 * @return A {@link List} of {@link List} objects containing locations which exist
	 * 		in parallel.
	 * @see List
	 * @see Location
	 */
	private List<List<Location>> groupLocations(List<Location> locations){
		
		/*
		 * Create a list which contains the a list of locations, representing the
		 * locations which exist in parallel.
		 */
		LinkedList<List<Location>> locationGroups = new LinkedList<List<Location>>();
		locationGroups.add(new LinkedList<Location>());
		
		//The class of the next location.
		Class<? extends Location> nextLoc = null;
		
		//Iterate through the locations of the station.
		for(Location loc: locations){
			
			if(locationGroups.peekLast().isEmpty()){
				
				/*
				 * If there is currently no location in the last group, then add the
				 * first location to the first group of locations.
				 * 
				 * Locations which exist in parallel must have the same next location.
				 */
				nextLoc = loc.nextLocation();
				locationGroups.peekLast().add(loc);
			}
			else if(nextLoc == loc.nextLocation()){
				
				/*
				 * The location is parallel with the locations in the same group and
				 * is therefore added to the group.
				 */
				locationGroups.peekLast().add(loc);
			}
			else{
				
				/*
				 * The location is not in the current group.
				 * 
				 * Get the next location of the current location, and add
				 * the current location to the next group.
				 */
				nextLoc = loc.nextLocation();
				locationGroups.add(new LinkedList<Location>());
				locationGroups.peekLast().add(loc);
			}
		}
		
		//Return the grouped locations.
		return locationGroups;
	}
	@Override
	protected final void paintComponent(final Graphics g){
		
		//Check so that the image is not null before painting.
		if(img != null){
			
			//Draw the image and dispose of the graphics.
			g.drawImage(img, 0, 0, null);
			g.dispose();
		}
	}
}