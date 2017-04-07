package environment.GUI.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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

	public volatile boolean isClosed;

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

	private enum VehicleTypes {

		SMALLCAR("Small Car", SmallCar_RoadUser.class), TRUCK("Truck", FamilySedan_RoadUser.class), FAMILYSEDAN(
				"Family Sedan",
				Motorbike_RoadUser.class), MOTORBIKE("Motorbike", Truck_RoadUser.class), ALL("All", null);

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

		PROCESSED("Processed"), REJECTED("Rejected"), FUELPROFIT("Fuel Profit"), LOSTFUELPROFIT(
				"Lost Fuel Profit"), SALESPROFIT("Sales Profit "), LOSTSALESPROFIT(
						"Lost Sales Profit"), PROFIT("Total Profit"), LOSTPROFIT("Total Lost Profit");

		private final String name;

		StatisticTypes(String name) {
			this.name = name;
		}

		@Override
		public final String toString() {
			return name;
		}

	}

	private class GraphPanel extends JPanel {

		/**
		 * Unused.
		 */
		private static final long serialVersionUID = -496349067427599752L;

		private volatile List<Station> stages;

		private int canvasWidth;
		private int canvasHeight;

		public GraphPanel() {

			super();

			canvasWidth = width;
			canvasHeight = height - 100;

			setSize(canvasWidth, canvasHeight);
			setPreferredSize(new Dimension(canvasWidth, canvasHeight));
			setMinimumSize(new Dimension(canvasWidth, canvasHeight));
			stages = new ArrayList<Station>();

		}

		@Override
		public synchronized void paint(Graphics g) {

			Graphics2D graphics2D = (Graphics2D) g;

			graphics2D.setColor(Color.CYAN);

			drawCanvas(graphics2D);

			graphics2D.setColor(Color.BLACK);

			drawLine(graphics2D);

		}

		public void add(Station station) {
			stages.add(station);

		}

		private void drawCanvas(Graphics2D graphics2D){
			
			Rectangle background = new Rectangle();
			background.setBounds(0, 0, canvasWidth, canvasHeight);
			graphics2D.fill(background);
			
		}
		
		private void drawLine(Graphics2D graphics2D) {

			Map<Integer, Double> values = new HashMap<Integer, Double>();

			double max = 0.0;

			for (int currentTick = 0; currentTick < stages.size(); currentTick++) {

				Station station = stages.get(currentTick);
				double value = getStatisticValue(station, selectedVehicleType());
				max = (value > max) ? value : max;
				values.put(currentTick, value);

			}
			
			int lastX = 0;
			int lastY = canvasHeight;

			for (Integer tick : values.keySet()) {

				double value = values.get(tick);

				int x = (int) ((tick * canvasWidth) / currentTick);
				int y = (int) (canvasHeight - ((value * canvasHeight) / max));

				if (!(x < 0 || x > canvasWidth || y < 0 || y > canvasHeight)) {
					graphics2D.drawLine(lastX, lastY, x, y);
				}

				lastX = x;
				lastY = y;

			}

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
