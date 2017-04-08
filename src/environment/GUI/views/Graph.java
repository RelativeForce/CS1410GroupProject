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

import environment.model.Station;
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
	private JComboBox<StatisticTypes> statisticTypes;
	private JComboBox<VehicleTypes> vehicleTypes;
	private Thread drawer;
	private LinkedTransferQueue<Station> buffer;
	private int currentTick;
	private int height;
	private int width;

	// Member Class(es) -------------------------------------------------------

	private enum VehicleTypes {

		ALL("All", null), SMALLCAR("Small Car", SmallCar_RoadUser.class), TRUCK("Truck",
				FamilySedan_RoadUser.class), FAMILYSEDAN("Family Sedan",
						Motorbike_RoadUser.class), MOTORBIKE("Motorbike", Truck_RoadUser.class);

		private final String name;
		public final Class<? extends RoadUser> type;

		VehicleTypes(String name, Class<? extends RoadUser> typeClass) {
			this.name = name;
			this.type = typeClass;
		}

		@Override
		public final String toString() {
			return name;
		}

	}

	private enum StatisticTypes {

		PROCESSED("Processed", false), REJECTED("Rejected", false), FUELPROFIT("Fuel Profit", true), LOSTFUELPROFIT(
				"Lost Fuel Profit", true), SALESPROFIT("Sales Profit ", true), LOSTSALESPROFIT("Lost Sales Profit",
						true), PROFIT("Total Profit", true), LOSTPROFIT("Total Lost Profit", true);

		private final String name;
		public final boolean isMoney;

		StatisticTypes(String name, boolean isMoney) {
			this.name = name;
			this.isMoney = isMoney;
		}

		@Override
		public final String toString() {
			return name;
		}

	}

	/**
	 * 
	 * This {@link GraphPanel} denotes a graph on screen. This is a member of
	 * {@link Graph} and requires the elements of it to exist. 
	 * 
	 * @author Joshua_Eddy
	 * @version 08/04/17
	 * 
	 * @see environment.GUI.views.Graph
	 * @see javax.swing.JPanel
	 *
	 */
	private class GraphPanel extends JPanel {

		/**
		 * Unused.
		 */
		private static final long serialVersionUID = -496349067427599752L;

		// Private Fields -----------------------------------------------------

		private volatile List<Station> stages;
		private int canvasWidth;
		private int canvasHeight;
		private int padding;
		private int paddedCanvasWidth;
		private int paddedCanvasHeight;
		private int xOffset;
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
			stages = new ArrayList<Station>();
			xOffset = (canvasWidth - paddedCanvasWidth) / 2;
			yOffset = (canvasHeight - paddedCanvasHeight) / 2;

			// Initialise the size of the panel.
			setSize(canvasWidth, canvasHeight);
			setPreferredSize(new Dimension(canvasWidth, canvasHeight));
			setMinimumSize(new Dimension(canvasWidth, canvasHeight));

		}

		// Public Methods -----------------------------------------------------

		@Override
		public void paint(Graphics g) {

			Graphics2D graphics2D = (Graphics2D) g;

			graphics2D.setColor(Color.WHITE);

			drawBackground(graphics2D);

			graphics2D.setColor(Color.BLACK);

			constructGraph(graphics2D);

		}

		public void add(Station station) {

			stages.add(station);

		}

		// Private Methods ----------------------------------------------------

		private void drawBackground(Graphics2D graphics2D) {

			Rectangle background = new Rectangle();
			background.setBounds(0, 0, canvasWidth, canvasHeight);
			graphics2D.fill(background);

		}

		private void drawAxis(double max, Graphics2D graphics2D) {

			// Draw the Y axis
			graphics2D.drawLine(xOffset, yOffset, xOffset, yOffset + paddedCanvasHeight);
			graphics2D.drawLine(xOffset, yOffset, xOffset - 5, yOffset);

			// Draw the X axis
			graphics2D.drawLine(xOffset, yOffset + paddedCanvasHeight, xOffset + paddedCanvasWidth,
					yOffset + paddedCanvasHeight);
			graphics2D.drawLine(xOffset + paddedCanvasWidth, yOffset + paddedCanvasHeight, xOffset + paddedCanvasWidth,
					yOffset + paddedCanvasHeight + 5);

			if (max > 0) {

				DecimalFormat money = new DecimalFormat("#.##");
				money.setRoundingMode(RoundingMode.CEILING);

				DecimalFormat integer = new DecimalFormat("#");
				integer.setRoundingMode(RoundingMode.CEILING);

				boolean isMoney = ((StatisticTypes) statisticTypes.getSelectedItem()).isMoney;

				String maxString = "" + (isMoney ? "£" + money.format(max) : integer.format(max));

				String tickString = "" + currentTick;

				String zero = (isMoney ? "£0" : "0");

				graphics2D.drawString(maxString, xOffset + 5, yOffset + 5);

				graphics2D.drawString(tickString, xOffset + paddedCanvasWidth - (tickString.length() * 5),
						yOffset + paddedCanvasHeight + 17);

				graphics2D.drawString(zero, xOffset - 7, yOffset + paddedCanvasHeight + 12);

			} else {

				String type = ((VehicleTypes) vehicleTypes.getSelectedItem()).toString();
				String stat = ((StatisticTypes) statisticTypes.getSelectedItem()).toString();

				String alert = "No " + stat + " for " + type;

				graphics2D.drawString(alert, (xOffset + paddedCanvasWidth) / 2, (yOffset + paddedCanvasHeight) / 2);

			}

		}

		private void constructGraph(Graphics2D graphics2D) {

			Map<Integer, Double> values = new HashMap<Integer, Double>();

			double max = 0.0;

			for (int currentTick = 0; currentTick < stages.size(); currentTick++) {

				Station station = stages.get(currentTick);

				if (station != null) {

					double value = getStatisticValue(station, selectedVehicleType());
					max = (value > max) ? value : max;
					values.put(currentTick, value);

				}

			}

			drawLine(values, max, graphics2D);

			drawAxis(max, graphics2D);

		}

		private void drawLine(Map<Integer, Double> values, double max, Graphics2D graphics2D) {

			int x = xOffset;
			int y = paddedCanvasHeight + yOffset;
			int lastX = xOffset;
			int lastY = paddedCanvasHeight + yOffset;

			if (max > 0) {

				for (Integer tick : values.keySet()) {

					double value = values.get(tick);

					x = (int) ((tick * paddedCanvasWidth) / currentTick) + xOffset;
					y = (int) (paddedCanvasHeight - ((value * paddedCanvasHeight) / max)) + yOffset;

					if (!(x < 0 || x > paddedCanvasWidth + xOffset)) {

						if (!(y < 0 || y > paddedCanvasHeight + yOffset)) {

							if (y != lastY && x != lastX) {

								graphics2D.drawLine(lastX, lastY, x, y);

								lastX = x;
								lastY = y;

							}
						}
					}
				}
			} else {
				x = paddedCanvasWidth + xOffset;
			}

			graphics2D.drawLine(lastX, lastY, x, y);

		}

		private Class<? extends RoadUser> selectedVehicleType() {

			return ((VehicleTypes) vehicleTypes.getSelectedItem()).type;

		}

		private double getStatisticValue(Station station, Class<? extends RoadUser> type) {

			switch ((StatisticTypes) statisticTypes.getSelectedItem()) {
			case PROCESSED:
				return station.getRoadUsersProcessed().get(type);
			case REJECTED:
				return station.getRoadUsersRejected().get(type);
			case PROFIT:
				return (station.getFuelProfit().get(type) + station.getSalesProfit().get(type));
			case LOSTPROFIT:
				return (station.getLostFuelProfit().get(type) + station.getLostSalesProfit().get(type));
			case FUELPROFIT:
				return station.getFuelProfit().get(type);
			case LOSTFUELPROFIT:
				return station.getLostFuelProfit().get(type);
			case SALESPROFIT:
				return station.getSalesProfit().get(type);
			case LOSTSALESPROFIT:
				return station.getLostSalesProfit().get(type);
			default:
				return 0.0;

			}
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

		vehicleTypes = new JComboBox<VehicleTypes>(VehicleTypes.values());
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

		statisticTypes = new JComboBox<StatisticTypes>(StatisticTypes.values());
		statisticTypes.addActionListener(e -> graphPanel.repaint());

		statisticPanel.add(statisticLabel);
		statisticPanel.add(statisticTypes);

		return statisticPanel;
	}

}
