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
 * 
 * @author 	John Berg
 * @version 09/04/2017
 * @since	08/04/2017
 */
public enum LocationVisual {
	
	/**
	 * The visual representation of the {@link Pump} class.
	 * 
	 * <p>
	 * The procedure to produce the visual representation of the {@link Pump}
	 * class.
	 * </p>
	 * 
	 * @see Visual
	 * @see #visual
	 */
	PUMP(Pump.class, (g, l, x, y) -> {
		
		/*
		 * Set the background color to gray, then draw a rectangle with dimensions
		 * BLOCK_SIZE x BLOCK_SIZE/2.
		 * 
		 * Then draw an 8 x 8 black rectangle at the end of the to represent the
		 * pump.
		 */
		g.setColor(Color.GRAY);
		g.fillRect(x, y + AnimationPanel.BLOCK_SIZE/4, AnimationPanel.BLOCK_SIZE,
				AnimationPanel.BLOCK_SIZE/2);
		g.setColor(Color.BLACK);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE - 10,
				y + AnimationPanel.BLOCK_SIZE/2 - 10, 8, 8);
		
		
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
		
		g.setColor(Color.GRAY);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE/2, y,
				AnimationPanel.BLOCK_SIZE/2, AnimationPanel.BLOCK_SIZE);
	}),
	/**
	 * The visual representation of any {@link Location} not included in the list.
	 * 
	 * <p>
	 * 
	 * </p>
	 */
	DEFAULT(null, (g, l, x, y) -> {
		
		g.setColor(Color.GRAY);
		g.fillRect(x, y, AnimationPanel.BLOCK_SIZE, AnimationPanel.BLOCK_SIZE);
		List<? extends RoadUser> roadUsers = l.getQueue();
		RoadUser[][] grid = new RoadUser[AnimationPanel.BLOCK_SIZE][AnimationPanel.BLOCK_SIZE];
		Random rng = new Random();
		
		roadUsers.forEach(ru -> {
			
			int position = rng.nextInt(AnimationPanel.BLOCK_SIZE*AnimationPanel.BLOCK_SIZE);
			
			while(true){
				
				if(grid[position%AnimationPanel.BLOCK_SIZE][position/AnimationPanel.BLOCK_SIZE]
						== null){
					
					g.setColor(Color.RED);
					g.fillRect(x + (position%AnimationPanel.BLOCK_SIZE),
							y + (position/AnimationPanel.BLOCK_SIZE), 4, 4);
					break;
				}
				else
					++position;
			}
		});
	});
	/**
	 * 
	 */
	private static int COLOR_INDEX = 0;
	/**
	 * 
	 */
	private static final Color[] USABLE_COLORS = new Color[]{
			
			Color.RED, Color.BLUE, Color.YELLOW, Color.ORANGE
	};
	/**
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
	 * 
	 * @param locationClass
	 * @param visual
	 */
	private LocationVisual(final Class<? extends Location> locationClass,
			final Visual visual){
		
		this.locationClass = locationClass;
		this.visual = visual;
	}
	private static final Color getColorOf(final Class<? extends RoadUser> roadUserClass){ 
		
		if(!ROAD_USER_COLOR_MAP.containsKey(roadUserClass)){
			
			ROAD_USER_COLOR_MAP.put(roadUserClass,
					USABLE_COLORS[COLOR_INDEX++%USABLE_COLORS.length]);
		}
		
		return ROAD_USER_COLOR_MAP.get(roadUserClass);
	}
	/**
	 * Get the {@link LocationVisual} for a specified {@link Location} class.
	 * 
	 * <p>
	 * Search for the {@link LocationVisual} where the {@link #locationClass} matches
	 * the specified {@link Location} class. No match will result in the {@link #DEFAULT}
	 * {@link LocationVisual} being selected.
	 * </p>
	 * 
	 * @param targetClass The {@link Location} class to be used to search for the
	 * 		correct {@link LocationVisual}.
	 * @return The {@link LocationVisual} associated with the {@link Location} class,
	 * 		returns {@link #DEFAULT} if the {@link Location} class does not match
	 * 		any {@link #locationClass}.
	 */
	public static final LocationVisual getVisual(final Class<? extends Location> targetClass){
		
		//Loop through all LocationVisual enumerations.
		for(LocationVisual lv: values()){
			
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
	 * This interface is used to allow the {@link LocationVisual} to create visual
	 * representations of a {@link Location}, by using the interface as a
	 * {@link FunctionalInterface} to use in the {@link LocationVisual#visual}; which
	 * allows the procedure of drawing a {@link Location} to be used in the
	 * {@link LocationVisual}.
	 * </p>
	 * 
	 * @author 	John Berg
	 * @version 08/04/2017
	 * @since	08/04/2017
	 */
	@FunctionalInterface
	public interface Visual {
		
		/**
		 * Create the visual representation of the {@link Location}
		 * 
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