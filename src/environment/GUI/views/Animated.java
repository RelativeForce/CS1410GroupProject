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
import sun.util.resources.th.CurrencyNames_th_TH;

/**
 * 
 * @author John Berg
 * @author David Wightman
 * @version 20/03/2017
 * @since 20/03/2017
 * @See JFrame
 * @see SimulatorView
 */
public final class Animated extends JFrame implements SimulatorView {
	
	private static final long TEN_SECOND = 10_000_000_000L;

	/**
	 * As of right now, accessing the entries in the queue will cause an error.
	 * 
	 * <p>
	 * The information must be accessed before and copied.
	 * </p>
	 * 
	 */
	private final LinkedTransferQueue<TimeStamp> buffer;
	private final Thread animator;
	private final AnimationPanel mainPanel;
	private final JButton controlButton;
	private final JButton killSwitch;;
	private volatile JSlider speedSlider;

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
		super.setLayout(new BorderLayout());

		mainPanel = new AnimationPanel(500, 500);
		controlButton = new JButton("Pause");
		killSwitch = new JButton("Exit");

		JPanel buttonPanel = new JPanel();
		JPanel sliderPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		speedSlider = new JSlider(0, 1000);
		JLabel SpeedLabel = new JLabel("Speed of simulation");
	

		speedSlider.setMajorTickSpacing(speedSlider.getMaximum()/5);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);

		buttonPanel.setLayout(new FlowLayout());
		sliderPanel.setLayout(new FlowLayout());
		controlPanel.setLayout(new BorderLayout());

		buttonPanel.setPreferredSize(new Dimension(200, 100));
		sliderPanel.setPreferredSize(new Dimension(300, 100));
		mainPanel.setPreferredSize(new Dimension(500,500));

		
		buttonPanel.add(controlButton);
		buttonPanel.add(killSwitch);
		sliderPanel.add(SpeedLabel);
		sliderPanel.add(speedSlider);
		controlPanel.add(sliderPanel,BorderLayout.WEST);
		controlPanel.add(buttonPanel,BorderLayout.EAST);
		
		
		super.add(mainPanel, BorderLayout.CENTER);
		super.add(controlPanel, BorderLayout.SOUTH);
		super.pack();
		super.setVisible(true);
		controlButton.addActionListener(e -> control());
		killSwitch.addActionListener(e -> {
			
			dispose();
			System.exit(0);
		});
		
		buffer = new LinkedTransferQueue<TimeStamp>();
		
		animator = new Thread(() -> {
			
			long previousTime = System.nanoTime(); //Starting time.
			long updateRate; //The number of updates per ten seconds.
			
			//Loop endlessly.
			while(true){
				
				//Check if the thread's interupt flag is active.
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
		animator.setPriority(Thread.MIN_PRIORITY);
		animator.start();
	}
	private void control(){
		
		if (!animator.isInterrupted()){
			
			controlButton.setText("Continue");
			animator.interrupt(); //Set the interrupt flat of the animator.
		}
		else {
			
			controlButton.setText("Pause");
			animator.resume();
		}
	}
	/**
	 * 
	 */
	@Override
	public final synchronized void show(int time, Station station) {

		buffer.add(new TimeStamp(time, station));
	}

	@Override
	public void setEnd() {
		
		killSwitch.setText("Results");
	}
	/**
	 * 
	 * @author John
	 *
	 */
	private final class TimeStamp {

		public final int time;
		public final Station station;

		public TimeStamp(final int time, final Station station) {

			this.time = time;
			this.station = station;
		}
	}
}