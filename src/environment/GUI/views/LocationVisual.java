package environment.GUI.views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * 
 * @author 	John Berg
 * @version 08/04/2017
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
		g.fillRect(x, y + AnimationPanel.BLOCK_SIZE/4, AnimationPanel.BLOCK_SIZE, AnimationPanel.BLOCK_SIZE/2);
		g.setColor(Color.BLACK);
		g.fillRect(x + AnimationPanel.BLOCK_SIZE - 10, y + AnimationPanel.BLOCK_SIZE/2 - 10, 8, 8);
		
		//The x starting position for vehicles queueing for the pump.
		int position = x + AnimationPanel.BLOCK_SIZE;
		
		//Loop through all road users that are currently in the queue.
		for(RoadUser ru: l.getQueue()){
			
			/*
			 * Get the road user's vehicle, then get the size of the vehicle and scale it
			 * by 3 x 4.
			 * 
			 * Set the color to ...
			 * then draw 
			 */
			Vehicle v = ru.getVehicle();
			int vehicleSize = (int) Math.floor(v.size*3*4);
			g.setColor(Color.RED);
			g.fillRect(position - vehicleSize, y + AnimationPanel.BLOCK_SIZE/2, vehicleSize, 4);
			position -= vehicleSize + 4;
		}
	}),
	/**
	 * 
	 */
	DEFAULT(null, (g, l, x, y) -> {
		
		g.setColor(Color.GRAY);
		g.fillRect(x, y, AnimationPanel.BLOCK_SIZE, AnimationPanel.BLOCK_SIZE);
		List<? extends RoadUser> roadUsers = l.getQueue();
		RoadUser[][] grid = new RoadUser[AnimationPanel.BLOCK_SIZE][AnimationPanel.BLOCK_SIZE];
		Random rng = new Random(10);
		
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
	 * The {@link Location} subclass for which the visual representation
	 * is intended for.
	 * 
	 * @see Location
	 */
	public final Class<? extends Location> locationClass;
	/**
	 * 
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
	/**
	 * 
	 * @param targetClass
	 * @return
	 */
	public static final LocationVisual getVisual(final Class<? extends Location> targetClass){
		
		for(LocationVisual lv: values()){
			
			if(targetClass == lv.locationClass && lv.locationClass != null)
				return lv;
		}
		
		return DEFAULT;
	}
	/**
	 * 
	 * @author John
	 *
	 */
	@FunctionalInterface
	public interface Visual {
		
		/**
		 * 
		 * 
		 * 
		 * @param g The {@link Graphics} add the visual representation to.
		 * @param loc The {@link Location} to be visually represented.
		 * @param x The x position of the {@link Visual}.
		 * @param y The y position of the {@link Visual}.
		 */
		public void visiulise(final Graphics g, final Location loc,
				final int x, final int y);
	}
}