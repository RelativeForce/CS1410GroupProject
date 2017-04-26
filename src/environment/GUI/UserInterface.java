package environment.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import environment.GUI.views.Animated;
import environment.GUI.views.CommandLine;
import environment.GUI.views.Graph;
import environment.GUI.views.SimulatorView;

/**
 *
 * Provides the functionality and creates a User interface that takes inputs
 * from the user checks if they are viable for the solution and then sends them
 * to the simulator to accurately represent what the user wants.
 *
 * @author David_Wightman
 * @version 21/03/2017
 * 
 * @see #getNumberOfPumps()
 * @see #getNumberOfTills()
 * @see #getP()
 * @see #getQ()
 * @see #getTickCount()
 * @see #getView()
 * 
 */
public class UserInterface {
	// Instance variables -------------------------------------

	/**
	 * boolean dictating when the system is ready for submission.
	 */
	private volatile boolean isReady;
	/**
	 * Boolean dictating wether or not trucks are allowed
	 */
	private boolean trucks;

	/**
	 * Value representing the value of P
	 */
	private double p;
	/**
	 * Value representing the value of Q
	 */
	private double q;

	/**
	 * Value representing the number of tills
	 */
	private int tills;
	/**
	 * Value representing the number of pumps
	 */
	private int pumps;
	/**
	 * Value representing the number of ticks
	 */
	private int ticks;

	/**
	 * Text field to allow users to enter a tills number
	 */
	private JComboBox<pointType> tillsEntry;
	/**
	 * Text field to allow users to enter a pumps number
	 */
	private JComboBox<pointType> pumpsEntry;
	/**
	 * Text field to allow users to enter a tick count
	 */
	private JTextField tickCount;

	/**
	 * Jframe responsible for loading the JPanels onto
	 */
	private JFrame display;

	/**
	 * Drop down box containing options for Q
	 */
	private JComboBox<variableType> pDropDown;
	/**
	 * Drop down box containing options for Q
	 */
	private JComboBox<variableType> qDropDown;
	/**
	 * Drop down box containing options for selectable views
	 */
	private JComboBox<String> viewDropDown;

	/**
	 * Checkbox which allows the user to set if trucks is true or false
	 */
	private JCheckBox Trucks;

	/**
	 * Constructs a new {@link UserInterface} by initialising the private
	 * variables and then creating frames with which to store them on as well as
	 * formatting and organising these frames.Also creates action listeners
	 * allowing the window to detect when the user is ready to submit values to
	 * the {@link SimulatorView}.
	 */
	public UserInterface() {

		// Create new JPanels and assign them names relevant to the objects they
		// will be handling.

		JPanel pPanel = new JPanel();
		JPanel qPanel = new JPanel();
		JPanel tillPanel = new JPanel();
		JPanel pumpPanel = new JPanel();
		JPanel truckPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel tickPanel = new JPanel();
		JPanel viewPanel = new JPanel();

		// Assign negative values to the initialised variables so they will fail
		// a later check unless changed.
		isReady = false;
		p = -1.0;
		q = -1.0;
		tills = -1;
		pumps = -1;
		ticks = -1;
		trucks = false;

		// create a new JFrame for the initialised display
		display = new JFrame();
		// create string arrays for the options in the previously initialised
		// Drop down boxes

		String[] viewOptions = { "Command Line", "Graph", "Animated View" };

		// create new JComboBox<string> objects for the initialised ComboBoxes
		// and assign them the String arrays.
		pDropDown = new JComboBox<variableType>(variableType.values());
		qDropDown = new JComboBox<variableType>(variableType.values());
		viewDropDown = new JComboBox<String>(viewOptions);
		tillsEntry = new JComboBox<pointType>(pointType.values());
		pumpsEntry = new JComboBox<pointType>(pointType.values());
		// Initialise the JTextField with the set sizes for entry.

		tickCount = new JTextField(8);

		// Initialise the Jbuttons and set the text that will appear on them
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");

		// Initialise the JLabels and set the text that they will use
		JLabel pLabel = new JLabel("Enter value for P:");
		JLabel qLabel = new JLabel("Enter value for Q:");
		JLabel tillsLabel = new JLabel("Enter number of tills:");
		JLabel pumpLabel = new JLabel("Enter number of pumps:");
		JLabel tickLabel = new JLabel("How many ten second ticks?");
		JLabel viewLabel = new JLabel("Select simulation view:");

		// initialise the Jcheckbox and set the text that appear next to it
		Trucks = new JCheckBox("Allow trucks?");

		// Assign preferred sizes to the initialised JPanels
		display.setPreferredSize(new Dimension(250, 500));
		qPanel.setPreferredSize(new Dimension(200, 50));
		pPanel.setPreferredSize(new Dimension(200, 50));
		tillPanel.setPreferredSize(new Dimension(200, 50));
		pumpPanel.setPreferredSize(new Dimension(200, 50));
		truckPanel.setPreferredSize(new Dimension(200, 50));
		buttonPanel.setPreferredSize(new Dimension(200, 50));
		tickPanel.setPreferredSize(new Dimension(200, 50));
		viewPanel.setPreferredSize(new Dimension(200, 50));

		// Initialise new layouts and assign them to JPanels
		display.setLayout(new FlowLayout());
		qPanel.setLayout(new FlowLayout());
		pPanel.setLayout(new FlowLayout());
		tillPanel.setLayout(new FlowLayout());
		pumpPanel.setLayout(new FlowLayout());
		truckPanel.setLayout(new FlowLayout());
		buttonPanel.setLayout(new FlowLayout());
		tickPanel.setLayout(new FlowLayout());
		viewPanel.setLayout(new FlowLayout());

		// Assign the default close operation of the window to be nothing
		display.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Add the initialised objects to the respective panels
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
		viewPanel.add(viewLabel);
		viewPanel.add(viewDropDown);
		buttonPanel.add(submit);
		buttonPanel.add(cancel);

		// Add the constructed panels to the display panel
		display.add(pPanel);
		display.add(qPanel);
		display.add(tillPanel);
		display.add(pumpPanel);
		display.add(truckPanel);
		display.add(tickPanel);
		display.add(viewPanel);
		display.add(buttonPanel);

		// Pack the display into a singular object and then make it visible to
		// the user
		display.pack();
		display.setVisible(true);

		// Create an action listener for each button and link it to a function
		submit.addActionListener(e -> submit());
		cancel.addActionListener(e -> {dispose();
		System.exit(0);
		});

		// Create an action listener for the window and link it to a function
		display.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

	}

	private enum pointType {
		TILL(1), TWOTILL(2), FOURTILL(4);

		public final int num;

		private pointType(int num) {
			this.num = num;
		}

		@Override
		public final String toString() {
			return "" + num;
		}

	}

	private enum variableType {
		POINT1(0.01), POINT2(0.02), POINT3(0.03), POINT4(0.04), POINT5(0.05);

		public final double doub;

		private variableType(double doub) {
			this.doub = doub;
		}

		@Override
		public final String toString() {
			return "" + doub;
		}

	}

	/**
	 * Handles values from the {@link UserInterface} and sets them to the
	 * private variables whilst also handling errors caused by user entries the
	 * system cannot handle. Also creates an error message to warn users that
	 * entered values may not be acceptable and then calls the {@link dispose}
	 * command.
	 */
	private void submit() {
		try {
			// Takes values from the user entry fields and assign them to
			// respective variables
			pumps = ((pointType) pumpsEntry.getSelectedItem()).num; 
			tills = ((pointType) tillsEntry.getSelectedItem()).num;
			p = ((variableType) pDropDown.getSelectedItem()).doub;
			q = ((variableType) qDropDown.getSelectedItem()).doub;
			trucks = Trucks.isSelected();
			ticks = Integer.parseInt(tickCount.getText());
			// catch any exception thrown by the recovered values and do nothing
		} catch (Exception e) {
			System.out.println("Made it to the catch");
		}
		// Test if all of the values are positive to ensure feasibility of the
		// simulation
		if (ticks < 100000) {
			if (ticks > 0) {
				// if ths if statement is met set isReady to true
				isReady = true;
			}
			// if conditions are not met
			else {
				// Create an alert message warning the user and provide a button
				// to
				// close the alert.
				Object[] options = { "OK" };
				JOptionPane.showOptionDialog(null, "Please make sure all the values enterred are positive and feasible",
						"Warning: Inputted value error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
						options, options[0]);
			}
		} else {
			Object[] options = { "OK" };
			JOptionPane.showOptionDialog(null,
					"Running the simulation for this long will max the CPU and cause other unforseen issues please choose a value below 100,000 ticks",
					"Warning:Excessvie value error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
					options, options[0]);
		}
	}

	/**
	 * Removes and destroys the visual element of the {@link UserInterface}
	 */
	public void dispose() {
		display.dispose();
		
	}

	/**
	 * Retrieves the value of <strong>isReady</strong> from the
	 * {@link UserInterface}.
	 * 
	 * @return <code>boolean</code> value of <strong>isReady</strong>.
	 */
	public boolean isReady() {
		return isReady;

	}

	/**
	 * Retrieves the value of <strong>p</strong> from the {@link UserInterface}.
	 * 
	 * @return <code>double</code> value of <strong>p</strong>.
	 */
	public double getP() {
		return p;
	}

	/**
	 * Retrieves the value of <strong>q</strong> from the {@link UserInterface}.
	 * 
	 * @return <code>double</code> value of <strong>q</strong>.
	 */
	public double getQ() {
		return q;
	}

	/**
	 * Retrieves the value of <strong>trucks</strong> from the
	 * {@link UserInterface}.
	 * 
	 * @return <code>boolean</code> value of <strong>trucks</strong>.
	 */
	public boolean hasTrucks() {
		return trucks;
	}

	/**
	 * Retrieves the value of <strong>pumps</strong> from the
	 * {@link UserInterface}.
	 * 
	 * @return <code>Int</code> value of <strong>pumps</strong>.
	 */
	public int getNumberOfPumps() {
		return pumps;
	}

	/**
	 * Retrieves the value of <strong>tills</strong> from the
	 * {@link UserInterface}.
	 * 
	 * @return <code>Int</code> value of <strong>tills</strong>.
	 */
	public int getNumberOfTills() {
		return tills;
	}

	/**
	 * Retrieves the value of <strong>ticks</strong> from the
	 * {@link UserInterface}.
	 * 
	 * @return <code>Int</code> value of <strong>ticks</strong>.
	 */
	public int getTickCount() {
		return ticks;
	}

	/**
	 * Handles the view setting chosen by the user sent from the
	 * {@link UserInterface} nulls the current view and then sets view to be in
	 * accordance with the new value.
	 * 
	 * @return <code>SimulatorView<code>
	 */
	public SimulatorView getView() {
		// Set the value in simulator view to null
		SimulatorView view = null;
		// If The string matches command line then set view to command line
		if (((String) viewDropDown.getSelectedItem()).equals("Command Line")) {
			view = CommandLine.getInstance();
			// if the string matches graph then set the view to graphical view
		} else if (((String) viewDropDown.getSelectedItem()).equals("Graph")) {
			view = Graph.getInstance();
		} else if (((String) viewDropDown.getSelectedItem()).equals("Animated View")) {
			view = Animated.getInstance();
		}

		// return the value in view
		return view;
	}
}
