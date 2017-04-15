package environment.GUI.views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
 * classes. Since the {@link Visualisation} enum is responsible for representing
 * the {@link Location} classes, it must also be responsible for {@link RoadUser}
 * classes, as the {@link Location} objects contain the {@link RoadUser} objects.
 * </p>
 * 
 * @author 	John Berg
 * @version 09/04/2017
 * @since	08/04/2017
 * @see Location
 * @see RoadUser
 * @see Graphics
 * @see Color
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
		
		//Pump dimensions.
		final int pumpWidth = AnimationPanel.BLOCK_SIZE;
		final int pumpHeight = AnimationPanel.BLOCK_SIZE/2;
		
		//Pump representation location.
		final int pumpLocationX = x + AnimationPanel.BLOCK_SIZE - 10;
		final int pumpLocationY = y + AnimationPanel.BLOCK_SIZE/2 - 10;
		final int pumpSize = 8; //The size of the pump representation.
		
		final int vehicleHeight = 4; //The width of all vehicles.
		final int vehicleQueuSize = 3; //The scaling factor of the vehicles.
		
		/*
		 * Set the background color to gray, then draw a rectangle with dimensions
		 * pumpWidth x pumpHeight.
		 */
		g.setColor(Color.GRAY);
		g.fillRect(x, y + AnimationPanel.BLOCK_SIZE/4, pumpWidth, pumpHeight);
		
		/* 
		 * Draw an pumpSize x pumpSize black rectangle at the end of the to represent
		 * the pump.
		 */
		g.setColor(Color.BLACK);
		g.fillRect(pumpLocationX, pumpLocationY, pumpSize, pumpSize);
		
		//The x starting position for vehicles queueing for the pump.
		int position = x + AnimationPanel.BLOCK_SIZE;
		
		//Loop through all road users that are currently in the queue.
		for(RoadUser ru: l.getQueue()){
			
			/*
			 * Get the road user's vehicle, then get the size of the vehicle and scale it
			 * by vehicleQueueSize x 4.
			 * 
			 * Set the color to the roaduser's color.
			 * then draw a rectangle representing the vehicle.
			 */
			Vehicle v = ru.getVehicle();
			int vehicleSize = (int) Math.floor(v.size*vehicleQueuSize*4);
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(position - vehicleSize, y + pumpHeight,
					vehicleSize, vehicleHeight);
			position -= vehicleSize + vehicleHeight; //Move to the position of the next vehicle.
		}
	}),
	/**
	 * The visual representation of the {@link Till} class.
	 * 
	 * <p>
	 * The procedure to produce the visual representation of th {@link Till} class.
	 * </p>
	 * 
	 * <p>
	 * Drawing the {@link Till} involves:
	 * <ol>
	 * 		<li>Drawing a small square to represent the {@link Till} area</li>
	 * 		<li>Drawing a black rectangle to represent the till</li>
	 * 		<li>Drawing an orange square to represent a shopkeeper</li>
	 * 		<li>Drawing the {@link RoadUser} objects currently queueing to check out</li>
	 * </ol>
	 * </p>
	 * 
	 * @see Visual
	 * @see #visual
	 * @see Till
	 */
	TILL(Till.class, (g, l, x, y) -> {
		
		//Till dimensions.
		final int tillWidth = AnimationPanel.BLOCK_SIZE/2;
		final int tillHeight = AnimationPanel.BLOCK_SIZE/2;
		
		//Till representation location.
		final int tillLocationX = x + AnimationPanel.BLOCK_SIZE - 22;
		final int tillLocationY = y + AnimationPanel.BLOCK_SIZE/2 - 5;
		final int tillSizeX = 20; //Width
		final int tillSizeY = 4; //Height
		
		//Shopkeeper representation location.
		final int shopkeeperLocationX = x + AnimationPanel.BLOCK_SIZE - 6;
		final int shopkeeperLocationY = y + AnimationPanel.BLOCK_SIZE/2 - 10;
		
		//The size of all roadusers.
		final int roadUserSize = 4;
		
		//Set the color to gray and then draw a square which represents the Till area.
		g.setColor(Color.GRAY);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE/2, y + AnimationPanel.BLOCK_SIZE/4,
				tillWidth, tillHeight);
		
		//Set the color to black and draw the till representation.
		g.setColor(Color.BLACK);
		g.fillRect(tillLocationX, tillLocationY, tillSizeX, tillSizeY);
		
		//Set the color to orange and draw a square to represent a shopkeeper.
		g.setColor(Color.ORANGE);
		g.fillRect(shopkeeperLocationX, shopkeeperLocationY, roadUserSize, roadUserSize);
		
		//Queue starting position.
		int position = x + AnimationPanel.BLOCK_SIZE - roadUserSize;
		
		//Loop through the queue of roadusers at the location.
		for(RoadUser ru: l.getQueue()){
			
			//Get the color of the roaduser and draw a square to represent the roaduser.
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(position - roadUserSize, y + AnimationPanel.BLOCK_SIZE/2,
					roadUserSize, roadUserSize);
			position -= roadUserSize + 2; //Move to the next position and add a margin of 2.
		}
	}),
	/**
	 * The visual representation of any {@link Location} not included in the list.
	 * 
	 * <p>
	 * This {@link LocationVisual} is used for the {@link Location} classes which do
	 * not have a specific representation.
	 * </p>
	 * 
	 * <p>
	 * The {@link #DEFAULT} {@link Visualisation} will draw a square and then place all the
	 * {@link RoadUsers} at a random position inside the square to simulate movement.
	 * </p>
	 * 
	 * @see Visual
	 * @see #visual
	 * @see Location
	 */
	DEFAULT(null, (g, l, x, y) -> {
		
		//Draw a gray square representing the Location.
		g.setColor(Color.GRAY);
		g.fillRect(x, y, AnimationPanel.BLOCK_SIZE, AnimationPanel.BLOCK_SIZE);
		
		//rng for placing RoadUsers.
		Random rng = new Random();
		
		//Loop through the queue of RoadUsers at the Location.
		l.getQueue().forEach(ru -> {
			
			/*
			 * Set the color the the color of the RoadUser, then draw the roaduser
			 * at a random position inside the square.
			 */
			g.setColor(getColorOf(ru.getClass()));
			g.fillRect(x + 4 + rng.nextInt(AnimationPanel.BLOCK_SIZE - 8),
				y + 4 + rng.nextInt(AnimationPanel.BLOCK_SIZE - 8), 4, 4);
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
			
			Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA
	};
	
	/**
	 * The {@link Map} which contains the visual representation a
	 * {@link RoadUser} class.
	 * 
	 * @see Map
	 * @see HashMap
	 * @see Color
	 * @see RoadUser
	 */
	private static final Map<Class<? extends RoadUser>, Color> ROAD_USER_COLOR_MAP =
			new HashMap<Class<? extends RoadUser>, Color>();
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
					USABLE_COLORS[COLOR_INDEX++]);
			
			/*
			 * If the COLOR_INDEX is out of bounds of the USABLE_COLORS the reset it
			 * to 0.
			 */
			if(!(COLOR_INDEX < USABLE_COLORS.length)) COLOR_INDEX = 0;
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
	public static final void resetColors(){
		
		ROAD_USER_COLOR_MAP.clear(); //Clear all existing entries.
		COLOR_INDEX = 0; //Reset the COLOR_INDEX.
	}
	/**
	 * Get the {@link RoadUser} and the {@link Color} used in the {@link Visualisation}.
	 * 
	 * @return The {@link RoadUser} and {@link Color} association.
	 * @see Color
	 * @see Entry
	 * @see Set
	 * @see RoadUser
	 */
	public static final Set<Entry<Class<? extends RoadUser>, Color>> getRoadUserColors(){
		
		return ROAD_USER_COLOR_MAP.entrySet();
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