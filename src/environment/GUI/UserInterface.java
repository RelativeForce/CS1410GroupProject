package environment.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import environment.GUI.views.CommandLine;
import environment.GUI.views.Graph;
import environment.GUI.views.SimulatorView;

import javax.swing.JComboBox;

public class UserInterface {
	
	private volatile boolean isReady;
	private double p;
	private double q;
	private int tills;
	private int pumps;
	private int ticks;
	private boolean trucks;

	private JTextField tillsEntry;
	private JTextField pumpsEntry;
	private JTextField tickCount;
	private JFrame display;
	private JComboBox<String> pDropDown;
	private JComboBox<String> qDropDown;
	private JComboBox<String> viewDropDown;
	private JCheckBox Trucks;

	public UserInterface() {

		JPanel pPanel = new JPanel();
		JPanel qPanel = new JPanel();
		JPanel tillPanel = new JPanel();
		JPanel pumpPanel = new JPanel();
		JPanel truckPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel tickPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		isReady = false;
		p = -1.0;
		q = -1.0;
		tills = -1;
		pumps = -1;
		ticks = -1;
		trucks = false;

		display = new JFrame();

		String[] pOptions = { "0.01", "0.02", "0.03", "0.04", "0.05" };
		String[] qOptions = { "0.01", "0.02", "0.03", "0.04", "0.05" };
		String[] viewOptions = { "Command Line", "Graph" };

		pDropDown = new JComboBox<String>(pOptions);
		qDropDown = new JComboBox<String>(qOptions);
		viewDropDown = new JComboBox<String>(viewOptions);

		tillsEntry = new JTextField(3);
		pumpsEntry = new JTextField(3);
		tickCount = new JTextField(8);

		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");

		JLabel pLabel = new JLabel("Enter value for P:");
		JLabel qLabel = new JLabel("Enter value for Q:");
		JLabel tillsLabel = new JLabel("Enter number of tills:");
		JLabel pumpLabel = new JLabel("Enter number of pumps;");
		JLabel tickLabel = new JLabel("How many ten second ticks?");
		JLabel viewLabel = new JLabel("Select simulation view:");

		Trucks = new JCheckBox("Allow trucks?");

		display.setMinimumSize(new Dimension(200, 700));
		qPanel.setMinimumSize(new Dimension(200, 100));
		pPanel.setMinimumSize(new Dimension(200, 100));
		tillPanel.setMinimumSize(new Dimension(200, 100));
		pumpPanel.setMinimumSize(new Dimension(200, 100));
		truckPanel.setMinimumSize(new Dimension(200, 100));
		buttonPanel.setMinimumSize(new Dimension(200, 100));
		tickPanel.setMinimumSize(new Dimension(200, 100));
		viewPanel.setMinimumSize(new Dimension(200, 200));

		display.setLayout(new GridLayout(7, 1));
		qPanel.setLayout(new FlowLayout());
		pPanel.setLayout(new FlowLayout());
		tillPanel.setLayout(new FlowLayout());
		pumpPanel.setLayout(new FlowLayout());
		truckPanel.setLayout(new FlowLayout());
		buttonPanel.setLayout(new FlowLayout());
		tickPanel.setLayout(new FlowLayout());
		viewPanel.setLayout(new FlowLayout());

		display.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		pPanel.add(pLabel);
		pPanel.add(pDropDown);
		qPanel.add(qLabel);
		qPanel.add(qDropDown);
		tillPanel.add(tillsLabel);
		tillPanel.add(tillsEntry);
		pumpPanel.add(pumpLabel);
		pumpPanel.add(pumpsEntry);
		truckPanel.add(Trucks);
		tickPanel.add(tickLabel);
		tickPanel.add(tickCount);
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		viewPanel.add(viewLabel);
		viewPanel.add(viewDropDown);

		display.add(pPanel);
		display.add(qPanel);
		display.add(tillPanel);
		display.add(pumpPanel);
		display.add(truckPanel);
		display.add(tickPanel);
		display.add(viewPanel);
		display.add(buttonPanel);

		display.pack();
		display.setVisible(true);

		submit.addActionListener(e -> submit());
		cancel.addActionListener(e -> dispose());

		display.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	private void submit() {
		try {
			pumps = Integer.parseInt(tillsEntry.getText());
			tills = Integer.parseInt(pumpsEntry.getText());
			p = Double.parseDouble((String) pDropDown.getSelectedItem());
			q = Double.parseDouble((String) qDropDown.getSelectedItem());
			trucks = Trucks.isSelected();
			ticks = Integer.parseInt(tickCount.getText());
		} catch (Exception e) {

		}
		if (pumps > 0 && tills > 0 && p > 0.00 && q > 0.00 && ticks > 0) {
			
			isReady = true;
		}
	}

	public void dispose() {
		display.dispose();
	}

	public boolean isReady() {
		return isReady;

	}

	public double getP() {
		return p;
	}

	public double getQ() {
		return q;
	}

	public boolean hasTrucks() {
		return trucks;
	}

	public int getNumberOfPumps() {
		return pumps;
	}

	public int getNumberOfTills() {
		return tills;
	}

	public int getTickCount() {
		return ticks;
	}

	public SimulatorView getView() {
		
		SimulatorView view = null;
		
		if (((String) viewDropDown.getSelectedItem()).equals("Command Line")) {
			view = new CommandLine();
		} else if (((String) viewDropDown.getSelectedItem()).equals("Graph")) {
			view = new Graph();
		}
		
		return view;
	}
}
