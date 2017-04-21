package environment.GUI.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.LinkedTransferQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


import environment.model.Station;

/**
 * 
 * @author John Berg
 * @version 20/03/2017
 * @since 20/03/2017
 * @See JFrame
 * @see SimulatorView
 */
public final class Animated extends JFrame implements SimulatorView {

	/**
	 * As of right now, accessing the entries in the queue will cause an error.
	 * 
	 * <p>
	 * The information must be accessed before and copied.
	 * </p>
	 * 
	 */
	private final LinkedTransferQueue<Entry> buffer;
	private final Thread animator;
	private AnimationPanel MainPanel;
	private boolean paused;
	private JButton ControlButton;
	private int CurrentSpeed;
	private JSlider SpeedSlider;

	/**
	 * 
	 */
	public Animated() {

		// Create the JFrame
		super("Animated View");
		super.setLayout(null);

		// Set the JFrame size
		super.setMinimumSize(new Dimension(500, 500));
		super.setPreferredSize(new Dimension(500, 600));
		super.setMaximumSize(new Dimension(500, 500));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(new FlowLayout());

		paused = false;
		MainPanel = new AnimationPanel(500, 500);
		ControlButton = new JButton("Pause");
		JButton KillSwitch = new JButton("Exit");

		JPanel ButtonPanel = new JPanel();
		JPanel SliderPanel = new JPanel();
		JPanel ControlPanel = new JPanel();
		SpeedSlider = new JSlider(0, 20);
		JLabel SpeedLabel = new JLabel("Speed of simulation");
	

		SpeedSlider.setMajorTickSpacing(4);
		SpeedSlider.setPaintTicks(true);

		ButtonPanel.setLayout(new FlowLayout());
		SliderPanel.setLayout(new FlowLayout());
		ControlPanel.setLayout(new BorderLayout());

		ButtonPanel.setPreferredSize(new Dimension(200, 100));
		SliderPanel.setPreferredSize(new Dimension(300, 100));
		MainPanel.setPreferredSize(new Dimension(500,500));

		
		ButtonPanel.add(ControlButton);
		ButtonPanel.add(KillSwitch);
		SliderPanel.add(SpeedLabel);
		SliderPanel.add(SpeedSlider);
		ControlPanel.add(SliderPanel,BorderLayout.WEST);
		ControlPanel.add(ButtonPanel,BorderLayout.EAST);
		
		
		super.add(MainPanel);
		super.add(ControlPanel);
		super.pack();
		super.setVisible(true);
		MainPanel.draw(new Station(null));
		ControlButton.addActionListener(e -> {
			try {
				Control();
			} catch (InterruptedException e1) {
				
			}
		});
		KillSwitch.addActionListener(e -> dispose());
		SpeedSlider.addChangeListener(e -> UpdateSpeed());
		
		buffer = new LinkedTransferQueue<Entry>();

		animator = new Thread(() -> {

			if (buffer.isEmpty()) {

				Thread.currentThread().suspend();
			}
		});

		animator.start();
	}

	private void Control() throws InterruptedException{
		if (paused == true){
			ControlButton.setText("Continue");
			paused = false;
			Thread.currentThread().suspend();
		}
		else {
			ControlButton.setText("Pause");
			paused = true;
			Thread.currentThread().resume();
		}
	}
	private void UpdateSpeed(){
		CurrentSpeed = SpeedSlider.getValue();
	}
	/**
	 * 
	 */
	@Override
	public final synchronized void show(int time, Station station) {

		buffer.add(new Entry(time, null));
	}

	/**
	 * 
	 * @author John
	 *
	 */
	private final class Entry {

		public final int time;
		public final Station station;

		public Entry(final int time, final Station station) {

			this.time = time;
			this.station = station;
		}
	}

	@Override
	public void setEnd() {
		// TODO Auto-generated method stub

	}
}