package abstractModulePack;
import interfacePack.GUIInterface;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This module implements some basic functionalities of a GUI module. A GUI with more 
 * functionalities can be implemented by extending this class.
 * @author tianzhao
 *
 */

public abstract class GUIAbstractModule implements GUIInterface {
	private JFrame mainFrame;
	private Set<Color> colorSet;
	private static final int defaultWidth = 800, defaultHeight = 450; 
	private static final String defaultFrameName = "Default GUI";
	
	private void addWindowController() {
		mainFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
			    	System.exit(0);
			    }
			}
		);
	}
	
	public GUIAbstractModule() {
		mainFrame = new JFrame(defaultFrameName);
		mainFrame.setSize(defaultWidth, defaultHeight);
		colorSet = new HashSet<Color>();
		addWindowController();
	}

	public void setGUITitle(String name) {
		mainFrame.setTitle(name);
	}
	
	public void setGUISize(Dimension d) {
		mainFrame.setSize(d);
	}

	public void displayGUI() {
		mainFrame.setVisible(true);
	}
	
	public void hideGUI() {
		mainFrame.setVisible(false);
	} 
	
	/**
	 * This function returns the main frame of the GUI
	 * @return JFrame
	 */
	public JFrame getFrame() {
		return mainFrame;
	}
	
	/**
	 * This function returns a random color 
	 * @return Color
	 */
	public Color getRandomColor() {
		Random random = new Random();
		final float hue = random.nextFloat();
		final float saturation = 0.5f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 0.6f; //1.0 for brighter, 0.0 for black
		Color color = Color.getHSBColor(hue, saturation, luminance);
		return colorSet.contains(color) ? getRandomColor() : color; 
	}
	
	/**
	 * This function adds a trace to the GUI
	 * @param traceName
	 * @param color
	 * @return boolean
	 */
	public abstract boolean addNewTrace(String traceName, Color color);
	
	/**
	 * This function add a point to a trace
	 * @param traceName
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public abstract boolean addOnePointToATrace(String traceName, double x, double y);
	
	/**
	 * This function checks if a trace exist
	 * @param traceName
	 * @return boolean
	 */
	public abstract boolean containsTrace(String traceName);
}
