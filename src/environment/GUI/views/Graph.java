package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import environment.Simulator;
import environment.model.Station;
import environment.model.Statistic;
import environment.model.locations.ShoppingArea;
import environment.model.roadusers.FamilySedan_RoadUser;
import environment.model.roadusers.Motorbike_RoadUser;
import environment.model.roadusers.RoadUser;
import environment.model.roadusers.SmallCar_RoadUser;
import environment.model.roadusers.Truck_RoadUser;

public class Graph implements SimulatorView {

	// Public Fields ----------------------------------------------------------

	public volatile boolean isClosed;

	// Private Fields --------------------------------------------------------

	private GraphPanel graphPanel;
	private JFrame window;
	private JButton closeButton;
	private JComboBox<StatisticType> statisticTypes;
	private JComboBox<VehicleType> vehicleTypes;
	private Thread drawer;
	private LinkedTransferQueue<Station> buffer;
	private int currentTick;
	private int height;
	private int width;

	// Member Classes ---------------------------------------------------------

	/**
	 * All the values and encapsulated functionality of the elements that will
	 * be added to the {@link Graph#vehicleTypes}.
	 * 
	 * @author Joshua_Eddy
	 * @version 09/04/17
	 * 
	 * @see environment.GUI.views.Graph
	 *
	 */
	private enum VehicleType {

		// The values
		ALL("All", null), SMALLCAR("Small Car", SmallCar_RoadUser.class), TRUCK("Truck",
				FamilySedan_RoadUser.class), FAMILYSEDAN("Family Sedan",
						Motorbike_RoadUser.class), MOTORBIKE("Motorbike", Truck_RoadUser.class);

		/**
		 * The text representation of <code>this</code> {@link VehicleType}.
		 */
		private final String text;

		/**
		 * The {@link RoadUser} type that <code>this<code> {@link VehicleType}
		 * is assigned to.
		 */
		public final Class<? extends RoadUser> type;

		/**
		 * Constructs a new {@link VehicleType}.
		 * 
		 * @param text
		 *            The text representation of <code>this</code>
		 *            {@link VehicleType}.
		 * @param typeClass
		 *            The {@link RoadUser} type that <code>this<code>
		 *            {@link VehicleType} is assigned to.
		 * @see environment.GUI.views.Graph
		 */
		private VehicleType(String text, Class<? extends RoadUser> typeClass) {

			// Initialise instance fields.
			this.text = text;
			this.type = typeClass;
		}

		/**
		 * Retrieve the text representation of <code>this</code>
		 * {@link VehicleType}.
		 */
		@Override
		public final String toString() {
			return text;
		}

	}

	/**
	 * All the values and encapsulated functionality of the elements that will
	 * be added to the {@link Graph#statisticTypes}.
	 * 
	 * @author Joshua_Eddy
	 * @version 09/04/17
	 * @see environment.GUI.views.Graph
	 *
	 */

	private enum StatisticType {

		// The values
		PROCESSED("Processed", false), REJECTED("Rejected", false), FUELPROFIT("Fuel Profit", true), LOSTFUELPROFIT(
				"Lost Fuel Profit", true), SALESPROFIT("Sales Profit ", true), LOSTSALESPROFIT("Lost Sales Profit",
						true), PROFIT("Total Profit", true), LOSTPROFIT("Total Lost Profit", true);

		/**
		 * The text representation of <code>this</code> {@link VehicleType}.
		 */
		private final String text;

		/**
		 * Whether or not <code>this</code> {@link StatisticType} is displayed
		 * in the form of money or not.
		 */
		public final boolean isMoney;

		/**
		 * Constructs a new {@link StatisticType}.
		 * 
		 * @param text
		 *            The text representation of <code>this</code>
		 *            {@link VehicleType}.
		 * @param isMoney
		 *            Whether or not <code>this</code> {@link StatisticType} is
		 *            displayed in the form of money or not
		 */
		private StatisticType(String text, boolean isMoney) {

			// Initialise the instance fields.
			this.text = text;
			this.isMoney = isMoney;
		}

		@Override
		public final String toString() {
			return text;
		}

	}

	/**
	 * 
	 * This {@link GraphPanel} denotes a graph on screen. This is a member of
	 * {@link Graph} and requires the elements of it to exist.
	 * 
	 * @author Joshua_Eddy
	 * @version 09/04/17
	 * 
	 * @see environment.GUI.views.Graph
	 * @see javax.swing.JPanel
	 *
	 */
	private class GraphPanel extends JPanel {

		/**
		 * Uniquely identifies this subclass of JPanel, it is Unused.
		 */
		private static final long serialVersionUID = -496349067427599752L;

		// Private Fields -----------------------------------------------------

		/**
		 * 
		 * Stores snapshots of the {@link Simulator}'s {@link Station} at each
		 * tick to allow a retrospective view of the simulation.
		 * 
		 * @see java.util.List
		 */
		private volatile List<Entry> stages;

		/**
		 * The width of the {@link GraphPanel} inside the {@link Graph}.
		 */
		private int canvasWidth;

		/**
		 * The height of the {@link GraphPanel} inside the {@link Graph}.
		 */
		private int canvasHeight;

		/**
		 * The percentage amount of space between the axis of the graph and the
		 * {@link GraphPanel}'s borders.
		 * 
		 */
		private int padding;

		/**
		 * The width of the actual graph that will be displayed with in the
		 * {@link GraphPanel}.
		 */
		private int paddedCanvasWidth;

		/**
		 * The height of the actual graph that will be displayed with in the
		 * {@link GraphPanel}.
		 */
		private int paddedCanvasHeight;

		/**
		 * The pixel equivalent of the padding in the horizontal direction.
		 * 
		 * @see #padding
		 * @see #paddedCanvasWidth
		 */
		private int xOffset;

		/**
		 * The pixel equivalent of the padding in the vertical direction.
		 * 
		 * @see #padding
		 * @see #paddedCanvasHeight
		 */
		private int yOffset;

		// Constructor --------------------------------------------------------

		/**
		 * Constructs a new {@link GraphPanel}.
		 * 
		 * @see environment.GUI.views.Graph
		 */
		public GraphPanel() {

			super();

			// Initialise the graph panel fields.
			canvasWidth = width - 100;
			canvasHeight = height - 100;
			padding = 5;
			paddedCanvasWidth = canvasWidth - ((canvasWidth * padding * 2) / 100);
			paddedCanvasHeight = canvasHeight - ((canvasHeight * padding * 2) / 100);
			stages = new ArrayList<Entry>();
			xOffset = (canvasWidth - paddedCanvasWidth) / 2;
			yOffset = (canvasHeight - paddedCanvasHeight) / 2;

			// Initialise the size of the panel.
			setSize(canvasWidth, canvasHeight);
			setPreferredSize(new Dimension(canvasWidth, canvasHeight));
			setMinimumSize(new Dimension(canvasWidth, canvasHeight));

		}

		// Public Methods -----------------------------------------------------

		/**
		 * Render the {@linkplain GraphPanel} on screen.
		 */
		@Override
		public void paint(Graphics g) {

			// Convert the panel graphics to 2D.
			Graphics2D graphics2D = (Graphics2D) g;

			graphics2D.setColor(Color.WHITE);

			drawBackground(graphics2D);

			graphics2D.setColor(Color.BLACK);

			constructGraph(graphics2D);

		}

		/**
		 * Add a Station to the {@link GraphPanel} to use when drawing the
		 * graph.
		 * 
		 * @param station
		 *            {@link Station} to be added.
		 */
		public void add(Station station) {
			stages.add(new Entry(station));
		}

		// Private Methods ----------------------------------------------------

		/**
		 * Draws the background of the {@link GraphPanel} the current colour of
		 * the parameter {@link Graphics2D}.
		 * 
		 * @param graphics2D
		 *            {@link Graphics2D} that denotes the graphics of the
		 *            canvas.
		 */
		private void drawBackground(Graphics2D graphics2D) {

			// Create a new Rectangle with the same dimensions as the graph
			// panel and draw it on screen.
			Rectangle background = new Rectangle();
			background.setBounds(0, 0, canvasWidth, canvasHeight);
			graphics2D.fill(background);

		}

		/**
		 * Draw the axis of the graph on the {@link GraphPanel} including the
		 * labels of type and maximum value.
		 * 
		 * @param max
		 *            <code>double<code> maximum value that the graph will reach
		 *            on the Y axis.
		 * @param graphics2D
		 *            {@link Graphics2D} that denotes the graphics of the
		 *            canvas.
		 */
		private void drawAxis(double max, Graphics2D graphics2D) {

			// Draw the Y axis
			graphics2D.drawLine(xOffset, yOffset, xOffset, yOffset + paddedCanvasHeight);
			graphics2D.drawLine(xOffset, yOffset, xOffset - 5, yOffset);

			// Draw the X axis
			graphics2D.drawLine(xOffset, yOffset + paddedCanvasHeight, xOffset + paddedCanvasWidth,
					yOffset + paddedCanvasHeight);
			graphics2D.drawLine(xOffset + paddedCanvasWidth, yOffset + paddedCanvasHeight, xOffset + paddedCanvasWidth,
					yOffset + paddedCanvasHeight + 5);

			// If the max value is not zero display a the max value labels.
			// Otherwise display an alert message.
			if (max > 0) {

				// If the statistic type that is currently selected is displayed
				// as money of not.
				boolean isMoney = ((StatisticType) statisticTypes.getSelectedItem()).isMoney;

				// Initialise the number string formatter. If the selected
				// statistic is money then the formatter will format the strings
				// as money otherwise it will just be an integer format.
				DecimalFormat formatter = new DecimalFormat(isMoney ? "#.##" : "#");
				formatter.setRoundingMode(RoundingMode.CEILING);

				// The label of the Y axis max value.
				String maxString = (isMoney ? "£" : "") + formatter.format(max);

				// The label of the X axis max value.
				String tickString = "" + currentTick;

				// The origin of the y axis.
				String zero = (isMoney ? "£0" : "0");

				// Display the maxString label at the top of the Y axis.
				graphics2D.drawString(maxString, xOffset + 5, yOffset + 5);

				// Display the tickString label at the end of the X axis.
				graphics2D.drawString(tickString, xOffset + paddedCanvasWidth - (tickString.length() * 5),
						yOffset + paddedCanvasHeight + 17);

				// Display the zero label at the bottom of the Y axis.
				graphics2D.drawString(zero, xOffset - 7, yOffset + paddedCanvasHeight + 12);

			} else {

				// Initialise an alert message.
				String type = ((VehicleType) vehicleTypes.getSelectedItem()).toString();
				String stat = ((StatisticType) statisticTypes.getSelectedItem()).toString();
				String alert = "No " + stat + " for " + type;

				// Display that alert message in the centre of the graph.
				graphics2D.drawString(alert, (xOffset + paddedCanvasWidth) / 2, (yOffset + paddedCanvasHeight) / 2);

			}

		}

		/**
		 * Build and display the graph in the {@link GraphPanel}.
		 * 
		 * @param graphics2D
		 *            {@link Graphics2D} that denotes the graphics of the
		 *            canvas.
		 */
		private void constructGraph(Graphics2D graphics2D) {

			// Holds the Y axis values.
			Map<Integer, Double> values = new HashMap<Integer, Double>();

			// The maximum value the y axis reaches.
			double max = 0.0;

			// Iterate through every element in stages.
			for (int currentTick = 0; currentTick < stages.size(); currentTick++) {

				Entry currentEntry = stages.get(currentTick);

				if (currentEntry != null) {

					// Retrieve the value of the selected statistic by vehicle
					// type.
					double value = getStatisticValue(currentEntry, ((VehicleType) vehicleTypes.getSelectedItem()).type);

					// If the value retrieved from the station is larger than
					// the current max then assign it as the new max.
					max = (value > max) ? value : max;

					// Add the value to the map of values.
					values.put(currentTick, value);

				}

			}

			drawLine(values, max, graphics2D);

			drawAxis(max, graphics2D);

		}

		/**
		 * Draws the graphs line in the {@link GraphPanel}.
		 * 
		 * @param values
		 *            <code>Map<Integer, Double></code> that denotes the y
		 *            values of the graph.
		 * @param max
		 *            <code>double</code> maximum value of the y axis.
		 * @param graphics2D
		 *            {@link Graphics2D} that denotes the graphics of the
		 *            canvas.
		 */
		private void drawLine(Map<Integer, Double> values, double max, Graphics2D graphics2D) {

			// Holds the initial x and y coordinates of the origin of the graph.
			int x = xOffset;
			int y = paddedCanvasHeight + yOffset;

			// Holds the coordinates of the last point on the graph. As this
			// persists to the next iteration of the following loop.
			int lastX = xOffset;
			int lastY = paddedCanvasHeight + yOffset;

			// If the maximum value of the y axis is larger than zero.
			if (max > 0) {

				// Iterate thought all the y axis values.
				for (Integer tick : values.keySet()) {

					// Holds the current y value.
					double value = values.get(tick);

					// Calculate the position of the value on the graph panel
					// based on the percentage of the canvas size and padding.
					x = (int) ((tick * paddedCanvasWidth) / currentTick) + xOffset;
					y = (int) (paddedCanvasHeight - ((value * paddedCanvasHeight) / max)) + yOffset;

					// If the x value is valid.
					if (!(x < 0 || x > paddedCanvasWidth + xOffset)) {

						// If the y value is valid.
						if (!(y < 0 || y > paddedCanvasHeight + yOffset)) {

							// If the position is not the same as the previous
							// values position. This results in a 'smooth' line
							// instead of a staircase.
							if (y != lastY && x != lastX) {

								// Draw the link from the previous value's
								// position to the current value's position.
								graphics2D.drawLine(lastX, lastY, x, y);

								// Set the current value to be the new previous
								// value for he next iteration of the loop.s
								lastX = x;
								lastY = y;

							}
						}
					}
				}
			} else {
				// set the x of the final line to be the length of the graph.
				// This results in a horizontal line along the x axis.
				x = paddedCanvasWidth + xOffset;
			}

			// Draw a final line which acts as a plateau for the graph.
			graphics2D.drawLine(lastX, lastY, x, y);

		}

		/**
		 * Retrieves the value of a {@link Statistic} filed in the specified
		 * {@link Station} based on the {@link StatisticType} from
		 * {@link Graph#statisticTypes} and the
		 * <code>Class&lt;? extends {@link RoadUser}&gt;</code> that the user
		 * specified in {@link Graph#vehicleTypes}.
		 * 
		 * @param entry
		 *            {@link Station}
		 * @param type
		 *            <code>Class&lt;? extends {@link RoadUser}&gt;</code>
		 * @return <code>double<code> value of that {@link Statistic}.
		 */
		private double getStatisticValue(Entry entry, Class<? extends RoadUser> type) {

			switch ((StatisticType) statisticTypes.getSelectedItem()) {
			case PROCESSED:
				return entry.getRoadUsersProcessed().get(type);
			case REJECTED:
				return entry.getRoadUsersRejected().get(type);
			case PROFIT:
				return (entry.getFuelProfit().get(type) + entry.getSalesProfit().get(type));
			case LOSTPROFIT:
				return (entry.getLostFuelProfit().get(type) + entry.getLostSalesProfit().get(type));
			case FUELPROFIT:
				return entry.getFuelProfit().get(type);
			case LOSTFUELPROFIT:
				return entry.getLostFuelProfit().get(type);
			case SALESPROFIT:
				return entry.getSalesProfit().get(type);
			case LOSTSALESPROFIT:
				return entry.getLostSalesProfit().get(type);
			default:
				return 0.0;

			}
		}
	}

	/**
	 * Stores all the {@link Statistic}s from the {@link Station} that is passed
	 * to the {@link Graph} each tick of the {@link Simulator}.
	 * 
	 * @author Joshua_Eddy
	 * @version 09/04/17
	 *
	 */
	private class Entry {

		/**
		 * The {@link Statistic} that denotes the amount of {@link RoadUser}s
		 * that <code>this</code> {@link Station} could not accommodate and
		 * therefore must have been rejected.
		 * 
		 */
		private Statistic roadUsersRejected;

		/**
		 * Stores the {@link Double} value of fuels profit that was lost by each
		 * {@link RoadUser} type being rejected by the station.
		 * 
		 * @see environment.model.roadusers.vehicles.Vehicle
		 * @see environment.model.roadusers.RoadUser
		 */
		private Statistic lostFuelprofit;

		/**
		 * Stores the {@link Double} value of sales profit that was lost by each
		 * {@link RoadUser} type being rejected by the station.
		 * 
		 * @see environment.model.roadusers.RoadUser
		 */
		private Statistic lostSalesProfit;

		/**
		 * Stores the {@link Double} number of each type of {@link RoadUser}s
		 * that have been processed by the station.
		 * 
		 * @see environment.model.roadusers.vehicles.Vehicle
		 * @see environment.model.roadusers.RoadUser
		 */
		private Statistic roadUsersProcessed;

		/**
		 * Stores the {@link Double} value of fuels profit that was gained by
		 * each {@link RoadUser} type paying for their fuel.
		 * 
		 * @see environment.model.roadusers.vehicles.Vehicle
		 * @see environment.model.roadusers.RoadUser
		 */
		private Statistic fuelProfit;

		/**
		 * The <code>double</code> sales profit that <code>this</code>
		 * {@link Station} has made.
		 */
		private Statistic salesProfit;

		public Entry(Station station) {

			roadUsersRejected = station.getRoadUsersRejected();
			roadUsersProcessed = station.getRoadUsersProcessed();
			lostFuelprofit = station.getLostFuelProfit();
			lostSalesProfit = station.getLostSalesProfit();
			fuelProfit = station.getFuelProfit();
			salesProfit = station.getSalesProfit();

		}

		/**
		 * Retrieve the number of rejected {@link RoadUser}s.
		 * 
		 * @return The number of {@link RoadUser}s that this {@link Station} has
		 *         rejected.
		 */
		public Statistic getRoadUsersRejected() {
			return roadUsersRejected;
		}

		/**
		 * Retrieves the amount of profit <code>this</code> {@link Station} has
		 * generated from fuel sales.
		 * 
		 * @return {@link Statistic} fuel profit.
		 * 
		 */
		public Statistic getFuelProfit() {
			return fuelProfit;
		}

		/**
		 * Retrieves the number of {@link RoadUser}s processed by the
		 * {@link Station}.
		 * 
		 * @return {@link Statistic} amount of {@link RoadUser}s processed by
		 *         the {@link Station}.
		 */
		public Statistic getRoadUsersProcessed() {
			return roadUsersProcessed;
		}

		/**
		 * Retrieves the sales profit <code>this</code> {@link Station} has
		 * generated from {@link RoadUser}s spending money in
		 * {@link ShoppingArea}.
		 * 
		 * @return {@link Statistic} sales profit.
		 * 
		 * @see #salesProfit
		 * @see #locations
		 * @see #fuelProfit
		 */
		public Statistic getSalesProfit() {
			return salesProfit;
		}

		/**
		 * The amount of profit that <code>this</code> {@link Station} has lost
		 * based on {@link RoadUser}s not being happy enough with their service
		 * to spend any money in the {@link ShoppingArea}.
		 * 
		 * @return {@link Statistic} lost profit.
		 */
		public Statistic getLostSalesProfit() {
			return lostSalesProfit;
		}

		/**
		 * The amount of profit that <code>this</code> {@link Station} has lost
		 * based on {@link Station} not being able to accommodate the new
		 * {@link RoadUser}.
		 * 
		 * @return {@link Statistic} profit lost.
		 */
		public Statistic getLostFuelProfit() {
			return lostFuelprofit;
		}

	}

	// Constructor ------------------------------------------------------------

	public Graph() {

		buffer = new LinkedTransferQueue<Station>();
		isClosed = false;
		currentTick = 0;
		width = 600;
		height = 500;

		graphPanel = new GraphPanel();

		buildWindow();

		drawer = new Thread("Drawer") {

			@Override
			public void run() {

				while (!isClosed) {

					while (buffer.isEmpty()) {
						// Wait for a new item to be added to the buffer.
					}

					graphPanel.add(buffer.poll());
					graphPanel.repaint();

				}
			}

		};

		drawer.start();

	}

	// Public Methods ---------------------------------------------------------

	@Override
	public void show(int time, Station station) {

		currentTick = time;
		buffer.add(station);

	}

	@Override
	public void setEnd() {
		// Does nothing as this method is used to tell the view that the
		// simulation has ended. This view will operate primarily once the
		// simulation has ended.
	}

	// Private Methods --------------------------------------------------------

	private void buildWindow() {

		window = new JFrame("Graph View");

		window.setSize(width, height);
		window.setMaximumSize(new Dimension(width, height));
		window.setLayout(new FlowLayout());

		window.add(graphPanel);
		window.add(newControlPanel());

		window.setVisible(true);

	}

	private JPanel newControlPanel() {

		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> {
			window.dispose();
			isClosed = true;
			System.exit(0);
		});

		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(width, 35));
		controlPanel.setLayout(new FlowLayout());

		controlPanel.add(newTypePanel());
		controlPanel.add(newStatisticPanel());
		controlPanel.add(closeButton);

		return controlPanel;

	}

	private JPanel newTypePanel() {

		JPanel typePanel = new JPanel();
		typePanel.setPreferredSize(new Dimension(200, 35));
		typePanel.setLayout(new FlowLayout());
		JLabel typeLabel = new JLabel("Vehicle Type:");

		vehicleTypes = new JComboBox<VehicleType>(VehicleType.values());
		vehicleTypes.addActionListener(e -> graphPanel.repaint());

		typePanel.add(typeLabel);
		typePanel.add(vehicleTypes);
		return typePanel;
	}

	private JPanel newStatisticPanel() {

		JPanel statisticPanel = new JPanel();
		statisticPanel.setPreferredSize(new Dimension(200, 35));
		statisticPanel.setLayout(new FlowLayout());
		JLabel statisticLabel = new JLabel("Statistic:");

		statisticTypes = new JComboBox<StatisticType>(StatisticType.values());
		statisticTypes.addActionListener(e -> graphPanel.repaint());

		statisticPanel.add(statisticLabel);
		statisticPanel.add(statisticTypes);

		return statisticPanel;
	}

}
