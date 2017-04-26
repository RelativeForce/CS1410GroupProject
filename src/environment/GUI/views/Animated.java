package environment.GUI.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import environment.model.Station;
import environment.model.locations.Location;
import environment.model.locations.Pump;
import environment.model.locations.Till;
import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;
import environment.model.roadusers.vehicles.Vehicle;

/**
 * The {@link Animated} class is a subclass of the {@link JFrame} which implements the
 * {@link SimulatorView} interface.
 * 
 * <p>
 * The {@link Animated} class is responsible for creating and displaying visual representations
 * of the {@link Station} class. The {@link Animation} also works independently as the {@link #animator}
 * asynchronously displays the {@link Station} objects.
 * </p>
 * 
 * <p>
 * The {@link Animated} also allows the simulation to be paused and the speed of which the {@link #animator}
 * displays the simulation can also be specified.
 * </p>
 * 
 * <p>
 * The {@link Animated} class uses the singleton pattern to allow only one instance of the {@link Animated}
 * to exist.
 * </p>
 * 
 * @author 	John Berg
 * @author 	David Wightman
 * @version 25/04/2017
 * @since 	20/03/2017
 * @See JFrame
 * @see SimulatorView
 */
public final class Animated extends JFrame implements SimulatorView {
	
	/**
	 * The serial ID of the {@link Animation}.
	 */
	private static final long serialVersionUID = 3900124928746616035L;
	/**
	 * The constant denoting 10 seconds in nanoseconds.
	 * 
	 * @see System#nanoTime()
	 */
	private static final long TEN_SECOND = 10_000_000_000L;
	/**
	 * The {@link RoadUser} classes which will be listed in the results table.
	 * 
	 * @see RoadUser
	 * @see Stream
	 * @see List
	 * @see Collectors
	 */
	private static final List<Class<? extends RoadUser>> roadUsers =Stream.of(
			SmallCar_RoadUser.class, 
			Motorbike_RoadUser.class,
			FamilySedan_RoadUser.class,
			Truck_RoadUser.class).collect(Collectors.toList());
	/**
	 * The single instance of the {@link Animated}.
	 */
	private static Animated animated = null;
	/**
	 * The final {@link Station} of the simulation.
	 */
	private Station end;
	/**
	 * As of right now, accessing the entries in the queue will cause an error.
	 * 
	 * <p>
	 * The information must be accessed before and copied.
	 * </p>
	 * 
	 * @see LinkedTransferQueue
	 * @see TimeStamp
	 */
	private final LinkedTransferQueue<TimeStamp> buffer;
	/**
	 * The {@link Thread} object which is timing the updates of the view to
	 * run at a specific interval of time.
	 * 
	 * @see Thread
	 */
	private final Thread animator;
	/**
	 * {@link AnimationPanel} representing the main panel of the frame.
	 * 
	 * @see AnimationPanel
	 */
	private final AnimationPanel mainPanel;
	/**
	 * {@link JButton} responsible for controlling the {@link Thread} object's state.
	 * 
	 * @see JButton
	 */
	private final JButton controlButton;
	/**
	 * {@link JButton} responsible for controlling the {@link JFrame#dispose}.
	 * 
	 * @see JFramme
	 * @see JButton
	 */
	private final JButton killSwitch;;
	/**
	 * {@link JSlider} used to represent animation speed.
	 * 
	 * @see JSlider
	 */
	private volatile JSlider speedSlider;

	/**
	 * Constructs a {@link JFrame} that holds a newly constructed {@link AnimationPanel} 
	 * and provides a panel underneath with buttons and sliders to control the speed and 
	 * state of the animation,It then uses methods from {@link SimulatorView} to allow 
	 * the simulation to be represented with the user chosen values from {@link UserInterface}
	 * 
	 * <p>
	 * <strong>Not to be used by external classes.</strong>
	 * </p>
	 */
	private Animated() {

		// Create the JFrame
		super("Animated View");
		super.setLayout(null);

		// Set the JFrame size
		super.setMinimumSize(new Dimension(500, 500));
		super.setPreferredSize(new Dimension(500, 600));
		super.setMaximumSize(new Dimension(500, 500));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(new BorderLayout());
		//set values for the mainPanel JPanel
		mainPanel = new AnimationPanel(500, 500);
		//Creates a new button and sets the aprpriate text for both Control and Kill buttons.
		controlButton = new JButton("Pause");
		killSwitch = new JButton("Exit");
		//Creates 3 new JPanels to store the Previously created swing elements
		JPanel buttonPanel = new JPanel();
		JPanel sliderPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		//Creates a new Jslider in Speedslider and sets the min and max values
		speedSlider = new JSlider(0, 1000);
		//creates a new label and assigns its starting text
		JLabel SpeedLabel = new JLabel("Speed of simulation");
	
		//format tyhe speed sliders tick spacing and then paints the ticks and labels.
		speedSlider.setMajorTickSpacing(speedSlider.getMaximum()/5);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);

		//Set the layout for the three previously created panels
		buttonPanel.setLayout(new FlowLayout());
		sliderPanel.setLayout(new FlowLayout());
		controlPanel.setLayout(new BorderLayout());
		//Set the preferred size for the panels 
		buttonPanel.setPreferredSize(new Dimension(200, 100));
		sliderPanel.setPreferredSize(new Dimension(300, 100));
		mainPanel.setPreferredSize(new Dimension(500,500));

		//Add the buttons and labels to the corresponding JPanels.
		buttonPanel.add(controlButton);
		buttonPanel.add(killSwitch);
		sliderPanel.add(SpeedLabel);
		sliderPanel.add(speedSlider);
		controlPanel.add(sliderPanel,BorderLayout.WEST);
		controlPanel.add(buttonPanel,BorderLayout.EAST);
		
		//Add the panels to the Jframe in the order they are needed to appear.
		super.add(mainPanel, BorderLayout.CENTER);
		super.add(controlPanel, BorderLayout.SOUTH);
		//Construct the JFrame and then make it visible to the user.
		super.pack();
		super.setVisible(true);
		//Create two action listeners and then set them to the two buttons.
		controlButton.addActionListener(e -> control());
		killSwitch.addActionListener(e -> {
			//When this action listener is called call the dispose function and then system exit.
			dispose();
			System.exit(0);
		});
		
		//Initialise the buffer of the LinkedTransferQueue of the TimeStamps.
		buffer = new LinkedTransferQueue<TimeStamp>();
		
		/*
		 * Initialise the animator Thread with a Runnable object which holds the routine
		 * that the Thread will follow to update the mainPanel.
		 */
		animator = new Thread(() -> {
			
			long previousTime = System.nanoTime(); //Starting time.
			long updateRate; //The number of updates per ten seconds.
			
			//Loop endlessly.
			while(true){
				
				//Check if the thread's interrupted flag is active.
				if(!Thread.currentThread().isInterrupted()){
					
					try{
						
						//Get the value of the speedSlider and calculate the update rate.
						updateRate = TEN_SECOND/speedSlider.getValue();
					}
					catch(ArithmeticException e){
						
						/*
						 * If calculating the updateRate results in division by 0, then
						 * set the updateRate to TEN_SECOND.
						 */
						updateRate = TEN_SECOND;
					}
					
					/*
					 * Calculate the difference (delta) in time between the current time and the
					 * previousTime, while the delta is greater than or equal to the updateRate
					 * then enter the loop; upon loop completion, subtract the updtateRate from
					 * delta and then repeat if the condition is still true.
					 * 
					 * This loop allows the thread to update the AnimationPanel a set number of times
					 * per second, where if the thread completed the task quickly, the thread must
					 * wait until enough time has elapsed to update again; and if the thread is not
					 * keeping up with the number of updates per second, then the thread will catch up
					 * update the amount of times possible, since the number of times the thread will
					 * repeat the loop increases.
					 */
					for(long delta = System.nanoTime() - previousTime;
							updateRate < delta; delta -= updateRate){
						
						//Check if there are any TimeStamps to draw.
						if (!buffer.isEmpty()){
							
							//Get the timestamp and draw it.
							TimeStamp t = buffer.poll();
							mainPanel.draw(t.time, t.station);
						}
						else
							//No point in looping whilst the buffer is empty.
							break;
						
						previousTime = System.nanoTime(); //Update the previous time.
					}
				}
				else{
					
					/*
					 * Suspend the thread, then when the thread is resumed, remove the interrupt
					 * flag and set the previousTime to the current nanoTime.
					 */
					Thread.currentThread().suspend();
					Thread.currentThread().interrupted();
					previousTime = System.nanoTime();
				}
			}
		});
		
		//Set the animator Thread priotiry to the lowest possible, then start the animator.
		animator.setPriority(Thread.MIN_PRIORITY);
		animator.start();
	}
	/**
	 * Control the execution of the {@link #animator} by interrupted and suspending the {@link Thread}
	 * or resuming the {@link Thread}.
	 * 
	 * <p>
	 * Activated when the {@link #controlButton} is clicked checks if the {@link animator} is interrupted
	 * and if so, then call the {@link Thread#resume()} method to allow the {@link #animator} to continiue
	 * execution, otherwise interrupt the {@link #animator}.
	 * </p>
	 * 
	 * <p>
	 * Set the title of the {@link #controlButton} to "Pause" or "Continue" depending on if the
	 * {@link #animator} is running or interrupted respectively.
	 * </p>
	 * 
	 * @see #animator
	 * @see #controlButton
	 * @see Thread
	 * @see JButton
	 */
	private void control(){
		
		//check if the animator thread is currently interrupted
		if (!animator.isInterrupted()){
			/*
			 * if the animator thread is not interrupted change the text on the control button and
			 * interrupt the thread. 
			 */
			controlButton.setText("Continue");
			animator.interrupt(); //Set the interrupt flat of the animator.
		}
		else {
			
			//else set the control button text to pause and resume the interrupted thread.
			controlButton.setText("Pause");
			animator.resume();
		}
	}
	/**
	 * Get the singleton instance of the {@link Animated} class.
	 * 
	 * <p>
	 * If the {@link #animated} is <code>null</code> then initialise a new instance
	 * of {@link Animated} before returning it.
	 * </p>
	 * 
	 * <p>
	 * <stong>Lazy initialisation</strong>.
	 * </p>
	 * 
	 * @return The {@link #animated} single instance of {@link Animated}.
	 */
	public static final Animated getInstance(){
		
		return animated != null? animated: (animated = new Animated());
	}
	/**
	 * Set a {@link Station} to be visually displayed.
	 * 
	 * @param time The time elapsed in the simulation.
	 * @param station The {@link Station} to be displayed.
	 */
	@Override
	public final void show(int time, Station station) {

		buffer.add(new TimeStamp(time, station));
		end = station;
	}
	/**
	 * Set the end of the simulation.
	 */
	@Override
	public void setEnd() {
		
		//If end is null, then exit the applications.
		if(end == null) System.exit(0);
		
		//The end station.
		final Station endStation = end;
		
		//Change the text of the killSwitch to Results
		killSwitch.setText("Results");
		
		//Remove all existing action listeners.
		for(ActionListener l :killSwitch.getActionListeners())
			killSwitch.removeActionListener(l);
		
		/*
		 * Add a new action listener to the killSwitch which will stop the animator and
		 * dispose of the Animation, then create a new frame with the results listed as
		 * a table. 
		 */
		killSwitch.addActionListener(e -> {
			
			//Stop the animator and dispose of the Animation frame.
			animator.stop();
			dispose();
			
			//Create the results frame and set the layout to BorderLayout.
			JFrame resultFrame = new JFrame("Results");
			resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			resultFrame.setLayout(new BorderLayout());
			
			/*
			 * Create the StringBuilders which will be used to append information about the
			 * simulation as a table, each StringBuilder represents a row.
			 */
			StringBuilder headers 			= new StringBuilder().append("\t\t");
			StringBuilder processed 		= new StringBuilder().append("Processed:\t\t");
			StringBuilder rejected			= new StringBuilder().append("Rejected:\t\t");
			StringBuilder fuelProfit 		= new StringBuilder().append("Fuel profit:\t\t");
			StringBuilder lostFuelProfit 	= new StringBuilder().append("Lost fuel profit\t\t");
			StringBuilder shopProfit 		= new StringBuilder().append("Shop profit:\t\t");
			StringBuilder lostShopProfit 	= new StringBuilder().append("Lost shop profit:\t");
			
			//Create the formating of the money and counters to not overflow the table columns.
			DecimalFormat money = new DecimalFormat("#.##");
			DecimalFormat counter = new DecimalFormat("####");
			
			//Iterate through the RoadUsers and append the information to the results table.
			roadUsers.forEach(r -> {
				
				//Add The RoadUser class name to the headers.
				headers.append(r.getSimpleName().replaceFirst("_RoadUser",
						""))
					.append("\t");
				//Add the number of processed vehicles of that type.
				processed.append(counter.format(endStation.getRoadUsersProcessed().get(r)))
					.append("\t");
				//Add the number oh rejected vehicles of that type.
				rejected.append(counter.format(endStation.getRoadUsersRejected().get(r)))
					.append("\t");
				//Add the fuel profit of that vehicle.
				fuelProfit.append(money.format(endStation.getFuelProfit().get(r)))
					.append("\t");
				//Add the lost fuel profit of the vehicle.
				lostFuelProfit.append(money.format(endStation.getLostFuelProfit().get(r)))
					.append("\t");
				//Add the shop profit of the road user.
				shopProfit.append(money.format(endStation.getSalesProfit().get(r)))
					.append("\t");
				//Add the lost shop profit of that road user.
				lostShopProfit.append(money.format(endStation.getLostSalesProfit().get(r)))
					.append("\t");
			});
			
			/*
			 * Create a JTextArea and add the Strings from the StringBuilders.
			 * Make the JTextArea not editable.
			 */
			JTextArea textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.append(headers.append("\n").toString());
			textArea.append(processed.append("\n").toString());
			textArea.append(rejected.append("\n").toString());
			textArea.append(fuelProfit.append("\n").toString());
			textArea.append(lostFuelProfit.append("\n").toString());
			textArea.append(shopProfit.append("\n").toString());
			textArea.append(lostShopProfit.append("\n").toString());
			
			/*
			 * Add the JTextArea to the centre of the Results frame, then pack the components
			 * and make the frame visible.
			 */
			resultFrame.add(textArea, BorderLayout.CENTER);
			resultFrame.pack();
			resultFrame.setVisible(true);
		});
	}
	/**
	 * The {@link TimeStamp} class is a container used for the {@link Animation} class
	 * to store {@link Station} objects and times.
	 * 
	 * @author 	John Berg
	 * @version 20/03/2017 
	 * @since	20/03/2017
	 * @see Station
	 */
	private final class TimeStamp {
		
		/**
		 * The time associated with the {@link #station} object.
		 */
		public final int time;
		/**
		 * The snapshot version of the {@link Station} objects
		 * current state.
		 */
		public final Station station;
		
		/**
		 * Create a {@link TImeStamp} of a {@link Station} object.
		 * 
		 * @param time The time of the snapshot.
		 * @param station The state of the snapshot.
		 */
		public TimeStamp(final int time, final Station station) {

			this.time = time;
			this.station = station;
		}
	}
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
	public static class AnimationPanel extends JPanel {
		
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
					nextLoc = loc.nextLocation;
					locationGroups.peekLast().add(loc);
				}
				else if(nextLoc == loc.nextLocation){
					
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
					nextLoc = loc.nextLocation;
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
	 * @version 13/04/2017
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
			
			//The size of all roadusers.
			final int roadUserSize = 4;
			
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
				g.fillRect(x + roadUserSize + rng.nextInt(AnimationPanel.BLOCK_SIZE - 2*roadUserSize),
					y + roadUserSize + rng.nextInt(AnimationPanel.BLOCK_SIZE - 2*roadUserSize),
					roadUserSize, roadUserSize);
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