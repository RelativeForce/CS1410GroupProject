package environment.GUI.views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.locations.Till;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * The {@link Visualisation} enumeration contains visual representations of
 * objects to be displayed in the {@link AnimationPanel}.
 * 
 * <p>
 * The {@link Visualisation} enum has the responsibility of having details
 * regarding how to produce visual representations of the {@link Location}
 * classes.
 * </p>
 * 
 * @author 	John Berg
 * @version 09/04/2017
 * @since	08/04/2017
 */
public enum Visualisation {
	
	/**
	 * The visual representation of the {@link Pump} class.
	 * 
	 * <p>
	 * The procedure to produce the visual representation of the {@link Pump}
	 * class.
	 * </p>
	 * 
	 * <p>
	 * Drawing the {@link Pump} involves:
	 * <ol>
	 * 		<li>Drawing a rectangle representing of the {@link Pump} area</li>
	 * 		<li>Drawing a black square near the right most side of the rectangle
	 * 		which represents the actual {@link Pump}</li>
	 * 		<li>Drawing the {@link RoadUser} objects currently queueing for the
	 * 		{@link Pump}</li>
	 * </ol>
	 * </p>
	 * 
	 * @see Visual
	 * @see #visual
	 * @see Pump
	 */
	PUMP(Pump.class, (g, l, x, y) -> {
		
		//The size of the pump representation.
		final int pumpSize = 8;
		
		/*
		 * Set the background color to gray, then draw a rectangle with dimensions
		 * BLOCK_SIZE x BLOCK_SIZE/2.
		 */
		g.setColor(Color.GRAY);
		g.fillRect(x, y + AnimationPanel.BLOCK_SIZE/4, AnimationPanel.BLOCK_SIZE,
				AnimationPanel.BLOCK_SIZE/2);
		
		/* 
		 * Draw an pumpSize x pumpSize black rectangle at the end of the to represent
		 * the pump.
		 */
		g.setColor(Color.BLACK);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE - 10,
				y + AnimationPanel.BLOCK_SIZE/2 - 10, pumpSize, pumpSize);
		
		//The x starting position for vehicles queueing for the pump.
		int position = x + AnimationPanel.BLOCK_SIZE;
		
		//Loop through all road users that are currently in the queue.
		for(RoadUser ru: l.getQueue()){
			
			/*
			 * Get the road user's vehicle, then get the size of the vehicle and scale it
			 * by 3 x 4.
			 * 
			 * Set the color to ...
			 * then draw a rectangle representing the vehicle.
			 */
			Vehicle v = ru.getVehicle();
			int vehicleSize = (int) Math.floor(v.size*3*4);
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(position - vehicleSize, y + AnimationPanel.BLOCK_SIZE/2, vehicleSize, 4);
			position -= vehicleSize + 4; //Move to the position of the next vehicle.
		}
	}),
	/**
	 * 
	 */
	TILL(Till.class, (g, l, x, y) -> {
		
		//INCOMPLETE
		
		g.setColor(Color.GRAY);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE/2, y,
				AnimationPanel.BLOCK_SIZE/2, AnimationPanel.BLOCK_SIZE);
		
		int position = x + AnimationPanel.BLOCK_SIZE - 4;
		
		for(RoadUser ru: l.getQueue()){
			
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(position, y + AnimationPanel.BLOCK_SIZE/2, 4, 4);
			position -= 4;
		}
	}),
	/**
	 * The visual representation of any {@link Location} not included in the list.
	 * 
	 * <p>
	 * This {@link LocationVisual} is used for the {@link Location} classes which do
	 * not have a specific representation.
	 * </p>
	 */
	DEFAULT(null, (g, l, x, y) -> {
		
		g.setColor(Color.GRAY);
		g.fillRect(x, y, AnimationPanel.BLOCK_SIZE, AnimationPanel.BLOCK_SIZE);
		List<? extends RoadUser> roadUsers = l.getQueue();
		Random rng = new Random();
		
		roadUsers.forEach(ru -> {
			
			int position = rng.nextInt(AnimationPanel.BLOCK_SIZE*AnimationPanel.BLOCK_SIZE);
			
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(x + (position%AnimationPanel.BLOCK_SIZE),
				y + (position/AnimationPanel.BLOCK_SIZE), 4, 4);
		});
	});
	
	/**
	 * The index of the next {@link Color} from the {@link #USABLE_COLORS}.
	 * 
	 * @see #USABLE_COLORS
	 */
	private static int COLOR_INDEX = 0;
	
	/**
	 * The default colors which are to be used to represent the
	 * {@link RoadUser} class, unless the {@link RoadUser} class's
	 * {@link Color} is set manually via the {@link #setColorOf(Class, Color)}
	 * method.
	 * 
	 * @see #setColorOf(Class, Color)
	 * @see Color
	 */
	private static final Color[] USABLE_COLORS = new Color[]{
			
			Color.RED, Color.BLUE, Color.YELLOW, Color.ORANGE
	};
	
	/**
	 * The {@link Map} which contains the visual representation a
	 * {@link RoadUser} class.
	 * 
	 * @see Map
	 * @see HashMap
	 * @see Color
	 * @see RoadUser
	 * 
	 */
	private static final Map<Class<? extends RoadUser>, Color> ROAD_USER_COLOR_MAP =
			new HashMap<>();
	/**
	 * The {@link Location} subclass for which the visual representation
	 * is intended for.
	 * 
	 * @see Location
	 */
	public final Class<? extends Location> locationClass;
	/**
	 * The implementation of drawing the {@link Location}.
	 * 
	 * @see Visual
	 */
	public final Visual visual;
	
	/**
	 * Initialise a {@link Visualisation} object.
	 * 
	 * <p>
	 * Create the {@link Visualisation} object by providing a {@link Location}
	 * class for which the {@link Visualisation} is associated with, and a
	 * {@link Visual} which contains the detaild implementation of producing the
	 * visual representation of the {@link Location}.
	 * </p>
	 * 
	 * @param locationClass The {@link Location} class for which the {@link Visualisation}
	 * 		is associated with.
	 * @param visual The implementation for creating the visual representation
	 * 		of the {@link Location} class.
	 * @see #locationClass
	 * @see #visual
	 * @see Location
	 * @see Visual
	 */
	private Visualisation(final Class<? extends Location> locationClass,
			final Visual visual){
		
		this.locationClass = locationClass;
		this.visual = visual;
	}
	/**
	 * Set the color of a {@link RoadUser} class to a specified {@link Color}.
	 * 
	 * <p>
	 * Set the color of a {@link RoadUser} subclass to a specific {@link Color},
	 * where neither the {@link RoadUser} or {@link Color} are null.
	 * </p>
	 * 
	 * @param roadUser The {@link RoadUser} to set the color of.
	 * @param color The {@link Color} to represent the {@link RoadUser} type.
	 * @throws IllegalArgumentException If either argument is <code>null</code>.
	 * @see #ROAD_USER_COLOR_MAP
	 * @see RoadUser
	 * @see Color
	 * @see IllegalArgumentException
	 */
	public final void setColorOf(final Class<? extends RoadUser> roadUser, final Color color)
	throws IllegalArgumentException {
		
		//Check if the arguments are not null.
		if(roadUser == null) throw new IllegalArgumentException("null is not a valid Roaduser class");
		if(color == null) throw new IllegalArgumentException("null is not a valid Color");
		
		//Set the specified RoadUser's color to the specified color.
		ROAD_USER_COLOR_MAP.put(roadUser, color);
	}
	/**
	 * Get the {@link Color} of the {@link RoadUser} class.
	 * 
	 * <p>
	 * Get the {@link Color} representation of the {@link RoadUser} class,
	 * where the specified {@link RoadUser} must not be <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * If the {@link RoadUser} class has not yet been assigned a {@link Color}, then
	 * get the {@link Color} at the current {@link #COLOR_INDEX} and increment the index
	 * by 1.
	 * </p>
	 * 
	 * @param roadUserClass The {@link RoadUser} class to get the {@link Color}
	 * 		representation of.
	 * @return The {@link Color} of the {@link RoadUser} class.
	 * @throws IllegalArgumentException If the {@link RoadUser} class is null.
	 * @see #COLOR_INDEX
	 * @see #ROAD_USER_COLOR_MAP
	 * @see RoadUser
	 * @see Color
	 * @see IllegalArgumentException
	 */
	public static final Color getColorOf(final Class<? extends RoadUser> roadUserClass)
	throws IllegalArgumentException {
		
		//Check if the roadUserClass is not null (a valid argument).
		if(roadUserClass == null) throw
			new IllegalArgumentException("null is not a valid RoadUser class");
		
		//Check if the roudUser has a color.
		if(!ROAD_USER_COLOR_MAP.containsKey(roadUserClass)){
			
			//Select the next color from the USABLE_COLORS.
			ROAD_USER_COLOR_MAP.put(roadUserClass,
					USABLE_COLORS[COLOR_INDEX++%USABLE_COLORS.length]);
		}
		
		//Return the Color of the roadUser.
		return ROAD_USER_COLOR_MAP.get(roadUserClass);
	}
	/**
	 * Reset the colors used in the {@link Visualisation}.
	 * 
	 * <p>
	 * Reset all colors so that no {@link RoadUser} class has a {@link Color}
	 * representation. Reset the {@link #COLOR_INDEX} to <code>0</code>.
	 * </p>
	 * 
	 * @see #COLOR_INDEX
	 * @see #ROAD_USER_COLOR_MAP
	 */
	public final void resetColors(){
		
		ROAD_USER_COLOR_MAP.clear(); //Clear all existing entries.
		COLOR_INDEX = 0; //Reset the COLOR_INDEX.
	}
	/**
	 * Get the {@link Visualisation} for a specified {@link Location} class.
	 * 
	 * <p>
	 * Search for the {@link Visualisation} where the {@link #locationClass} matches
	 * the specified {@link Location} class. No match will result in the {@link #DEFAULT}
	 * {@link Visualisation} being selected.
	 * </p>
	 * 
	 * @param targetClass The {@link Location} class to be used to search for the
	 * 		correct {@link Visualisation}.
	 * @return The {@link Visualisation} associated with the {@link Location} class,
	 * 		returns {@link #DEFAULT} if the {@link Location} class does not match
	 * 		any {@link #locationClass}.
	 */
	public static final Visualisation getVisual(final Class<? extends Location> targetClass){
		
		//Loop through all LocationVisual enumerations.
		for(Visualisation lv: values()){
			
			/*
			 * If the targetClass matches the locationClass, then return that LocationVisual
			 * for which the target class matched.
			 */
			if(targetClass == lv.locationClass && lv.locationClass != null)
				return lv;
		}
		
		//If no matching LocationVisual was found, then return the default.
		return DEFAULT;
	}
	/**
	 * The interface used to produce the visual representation of the {@link Location}.
	 * 
	 * <p>
	 * This interface is used to allow the {@link Visualisation} to create visual
	 * representations of a {@link Location}, by using the interface as a
	 * {@link FunctionalInterface} to use in the {@link Visualisation#visual}; which
	 * allows the procedure of drawing a {@link Location} to be used in the
	 * {@link Visualisation}.
	 * </p>
	 * 
	 * @author 	John Berg
	 * @version 08/04/2017
	 * @since	08/04/2017
	 */
	@FunctionalInterface
	public interface Visual {
		
		/**
		 * Create the visual representation of the {@link Location}.
		 * 
		 * <p>
		 * Provide a {@link Graphics} and {@link Location} object which are the graphics to draw
		 * to and the {@link Location} to be drawn, along with an <code>int</code> x and y
		 * coordinate which specify the target location of where to draw. 
		 * </p>
		 * 
		 * @param g The {@link Graphics} add the visual representation to.
		 * @param loc The {@link Location} to be visually represented.
		 * @param x The x position of the {@link Visual}.
		 * @param y The y position of the {@link Visual}.
		 * @see Graphics
		 * @see Location
		 */
		public void visiulise(final Graphics g, final Location loc,
				final int x, final int y);
	}
}