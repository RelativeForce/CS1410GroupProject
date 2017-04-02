package environment.GUI.views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import environment.model.Station;

public class AnimationPanel extends JPanel {
	
	/**
	 * The scaling factor of the {@link JPanel} which determens the size
	 * of the ...
	 */
	private static final int SCALING_FACTOR = 3;
	/**
	 * 
	 */
	private final Image img;
	
	/**
	 * 
	 * @param width
	 * @param height
	 */
	public AnimationPanel(final int width, final int height){
		
		//Set the size of the JPanel.
		super();
		super.setMinimumSize(new Dimension(width*SCALING_FACTOR,
				height*SCALING_FACTOR));
		super.setPreferredSize(new Dimension(width*SCALING_FACTOR,
				height*SCALING_FACTOR));
		super.setMaximumSize(new Dimension(width*SCALING_FACTOR,
				height*SCALING_FACTOR));
		
		
		img = super.createImage(width*SCALING_FACTOR, height*SCALING_FACTOR);
	}
	/**
	 * 
	 * @param station
	 */
	public void draw(final Station station){
		
		
		
		//Incomplete
		Graphics g = null;
		
		if(img != null){
			
			g.drawImage(img, 0, 0, null);
		}
	}
}
