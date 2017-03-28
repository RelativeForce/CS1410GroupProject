package environment.GUI.views;

import java.awt.Dimension;
import java.util.concurrent.LinkedTransferQueue;

import javax.swing.JFrame;

import environment.model.Station;

/**
 * 
 * @author 	John Berg
 * @version 20/03/2017
 * @since	20/03/2017
 * @See 	JFrame
 * @see 	SimulatorView
 */
public final class Animated extends JFrame implements SimulatorView {
	
	/**
	 * As of right now, accessing the entries in the queue
	 * will cause an error.
	 * 
	 * <p>
	 * The information must be accessed before and copied.
	 * </p>
	 * 
	 */
	private final LinkedTransferQueue<Entry> buffer;
	private final Thread animator;
	
	/**
	 * 
	 */
	public Animated(){
		
		//Create the JFrame
		super("blah blah blah");
		super.setLayout(null);
		
		//Set the JFrame size
		super.setMinimumSize(new Dimension(500, 500));
		super.setPreferredSize(new Dimension(500, 500));
		super.setMaximumSize(new Dimension(500, 500));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Create JPanel
		
		super.pack();
		super.setVisible(true);
		
		buffer = new LinkedTransferQueue<Entry>();
		
		animator = new Thread(() -> {
			
			
			if(buffer.isEmpty()){
				
				Thread.currentThread().suspend();
			}
		});
		
		animator.start();
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
		
		public Entry(final int time, final Station station){
			
			this.time = time;
			this.station = station;
		}
	}
}