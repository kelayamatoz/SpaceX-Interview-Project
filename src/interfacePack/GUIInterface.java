package interfacePack;
import java.awt.Dimension;
/**
 * This interface defines some basic functionalities that a GUI needs to provide
 * @author tianzhao
 *
 */
public interface GUIInterface {
	/**
	 * This function sets the title of a GUI
	 * @param name
	 */
	public void setGUITitle(String name);
	
	/**
	 * This function sets the size of a GUI
	 * @param d
	 */
	public void setGUISize(Dimension d);
	
	/**
	 * THis function displays the GUI
	 */
	public void displayGUI();
	
	/**
	 * This function hides the GUI
	 */
	public void hideGUI();
	
	/**
	 * This function sets the name of the xAxis
	 * @param xName
	 */
	public void setXAxisTitle(String xName);
	
	/**
	 * This function sets the name of the yAxis
	 * @param yName
	 */
	public void setYAxisTitle(String yName);
}


