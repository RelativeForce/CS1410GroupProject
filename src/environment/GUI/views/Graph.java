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

	private GraphHandler graphHandler;
	private JFrame window;
	private JButton closeButton;
	private JComboBox<String> statistics;
	private JComboBox<String> types;
	private int currentTick;
	public volatile boolean isClosed;
	private Thread drawer;
	private LinkedTransferQueue<Station> buffer;

	private int height;
	private int width;

	private enum CarNames {

		SMALLCAR("Small Car"), TRUCK("Truck"), FAMILYSEDAN("Family Sedan"), MOTORBIKE("Motorbike");

		public final String name;

		CarNames(String name) {
			this.name = name;
		}

	}

	private enum StatisticNames {

		PROCESSED("Processed"), REJECTED("Rejected"), FUELPROFIT("Fuel Profit"), LOSTFUELPROFIT(
				"Lost Fuel Profit"), SALESPROFIT("Sales Profit "), LOSTSALESPROFIT(
						"Lost Sales Profit"), PROFIT("Total Profit"), LOSTPROFIT("Total Lost Profit");

		public final String name;

		StatisticNames(String name) {
			this.name = name;
		}

	}

	private class GraphHandler extends JPanel {

		/**
		 * Unused.
		 */
		private static final long serialVersionUID = -496349067427599752L;

		private volatile List<Station> stages;

		public GraphHandler() {

			super();
			setSize(600, 400);
			setPreferredSize(new Dimension(600, 400));
			setMinimumSize(new Dimension(600, 400));
			stages = new ArrayList<Station>();

		}

		@Override
		public synchronized void paint(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(Color.WHITE);

			Rectangle background = new Rectangle();
			background.setBounds(0, 0, width, height);
			g2.fill(background);

			Map<Integer, Double> values = new HashMap<Integer, Double>();

			double max = 0.0;

			for (int currentTick = 0; currentTick < stages.size(); currentTick++) {

				Station station = stages.get(currentTick);
				double value = getStatisticValue(station, selectType());
				max = (value > max) ? value : max;
				values.put(currentTick, value);

			}

			g2.setColor(Color.BLACK);

			int lastX = 0;
			int lastY = height;

			for (Integer tick : values.keySet()) {

				double value = values.get(tick);

				int x = (int) ((tick * width) / currentTick);
				int y = (int) (height - ((value * height) / max));



				g2.drawLine(lastX, lastY, x, y);
				
				lastX = x;
				lastY = y;

			}

		}

		public void add(Station station) {
			stages.add(station);

		}

		private Class<? extends RoadUser> selectType() {

			Class<? extends RoadUser> currentType = null;

			if (types.getSelectedItem().equals(CarNames.SMALLCAR.name)) {
				currentType = SmallCar_RoadUser.class;
			} else if (types.getSelectedItem().equals(CarNames.FAMILYSEDAN.name)) {
				currentType = FamilySedan_RoadUser.class;
			} else if (types.getSelectedItem().equals(CarNames.MOTORBIKE.name)) {
				currentType = Motorbike_RoadUser.class;
			} else if (types.getSelectedItem().equals(CarNames.TRUCK.name)) {
				currentType = Truck_RoadUser.class;
			}
			return currentType;
		}

		private double getStatisticValue(Station station, Class<? extends RoadUser> type) {

			if (statistics.getSelectedItem().equals(StatisticNames.PROCESSED.name)) {
				return station.getRoadUsersProcessed().get(type);
			} else if (statistics.getSelectedItem().equals(StatisticNames.REJECTED.name)) {
				return station.getRoadUsersRejected().get(type);
			} else if (statistics.getSelectedItem().equals(StatisticNames.PROFIT.name)) {
				return (station.getFuelProfit().get(type) + station.getSalesProfit().get(type));
			} else if (statistics.getSelectedItem().equals(StatisticNames.LOSTPROFIT.name)) {
				return (station.getLostFuelProfit().get(type) + station.getLostSalesProfit().get(type));
			} else if (statistics.getSelectedItem().equals(StatisticNames.FUELPROFIT.name)) {
				return station.getFuelProfit().get(type);
			} else if (statistics.getSelectedItem().equals(StatisticNames.LOSTFUELPROFIT.name)) {
				return station.getLostFuelProfit().get(type);
			} else if (statistics.getSelectedItem().equals(StatisticNames.SALESPROFIT.name)) {
				return station.getSalesProfit().get(type);
			} else if (statistics.getSelectedItem().equals(StatisticNames.LOSTSALESPROFIT.name)) {
				return station.getLostSalesProfit().get(type);
			}
			return 0.0;
		}
	}

	public Graph() {

		buffer = new LinkedTransferQueue<Station>();
		isClosed = false;
		currentTick = 0;
		width = 600;
		height = 500;

		graphHandler = new GraphHandler();
		window = new JFrame("Graph View");
		window.setSize(width, height);
		window.setMaximumSize(new Dimension(width, height));
		window.setLayout(new FlowLayout());

		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> {
			window.dispose();
			isClosed = true;
			System.exit(0);
		});

		JPanel statisticPanel = new JPanel();
		statisticPanel.setPreferredSize(new Dimension(200, 35));
		statisticPanel.setLayout(new FlowLayout());
		JLabel statisticLabel = new JLabel("Statistic:");

		String[] statTypes = { StatisticNames.PROCESSED.name, StatisticNames.REJECTED.name, StatisticNames.PROFIT.name,
				StatisticNames.LOSTPROFIT.name, StatisticNames.SALESPROFIT.name, StatisticNames.LOSTSALESPROFIT.name,
				StatisticNames.FUELPROFIT.name, StatisticNames.LOSTFUELPROFIT.name };

		statistics = new JComboBox<String>(statTypes);
		statistics.addActionListener(e -> graphHandler.repaint());

		statisticPanel.add(statisticLabel);
		statisticPanel.add(statistics);

		JPanel typePanel = new JPanel();
		typePanel.setPreferredSize(new Dimension(200, 35));
		typePanel.setLayout(new FlowLayout());
		JLabel typeLabel = new JLabel("Vehicle Type:");

		String[] vehicleTypes = { "All", CarNames.SMALLCAR.name, CarNames.FAMILYSEDAN.name, CarNames.MOTORBIKE.name,
				CarNames.TRUCK.name };

		types = new JComboBox<String>(vehicleTypes);
		types.addActionListener(e -> graphHandler.repaint());

		typePanel.add(typeLabel);
		typePanel.add(types);

		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(width, 35));
		controlPanel.setLayout(new FlowLayout());

		controlPanel.add(typePanel);
		controlPanel.add(statisticPanel);
		controlPanel.add(closeButton);

		window.add(graphHandler);
		window.add(controlPanel);

		window.setVisible(true);

		drawer = new Thread("Drawer") {

			@Override
			public void run() {

				while (!isClosed) {

					while (buffer.isEmpty()) {

					}

					graphHandler.add(buffer.poll());
					graphHandler.repaint();

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

}
