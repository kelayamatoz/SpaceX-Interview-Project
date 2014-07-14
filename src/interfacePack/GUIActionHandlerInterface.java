package interfacePack;

import java.util.Set;
/**
 * This interface defines the basic functionalities of a GUI action handler
 * @author tianzhao
 *
 */

public interface GUIActionHandlerInterface {
	/**
	 * The GUI calls this function to indicate that the user pressed the start button
	 */
	public void setStart();
	
	/**
	 * The action handler calls this function to check if the control module should start running
	 * @return boolean
	 */
	public boolean needStart();
	
	/**
	 * The GUI calls this function to indicate that the user pressed the pause button
	 */
	public void setPause();
	
	/**
	 * The action handler calls this function to check if the control module should pauses
	 * @return boolean
	 */
	public boolean needPause();
	
	/**
	 * The GUI calls this function to set the port to be displayed
	 * @param displayPorts
	 */
	public void setDisplayPorts(Set<Integer> displayPorts);
	
	/**
	 * The action handler calls this function to get the ports to be displayed
	 * @return Set<Integer>
	 */
	public Set<Integer> getDisplayPorts();
	
	/**
	 * The GUI calls this function to indicate that the user pressed the reset button
	 */
	public void resetAllDevices();
	
	/**
	 * The action handler calls this function to indicate that the control module has finished
	 * reseting all the devices
	 */
	public void finishResetAllDevices();
	
	/**
	 * The action handler calls this function to check if the control module needs to reset 
	 * all devices
	 * @return boolean
	 */
	public boolean needResetAllDevices();
	
	/**
	 * The GUI calls this function to set the device to be displayed
	 * @param displayDevice
	 */
	public void setDisplayDevice(String displayDevice);
	
	/**
	 * The action handler calls this function to check if the user has updated the device
	 * to be displayed
	 * @return boolean
	 */
	public boolean displayDeviceIsUpdated();
	
	/**
	 * The action handler calls this function to give the control module the device that should 
	 * be displayed
	 * @return String 
	 */
	public String getDisplayDeviceName();
	
	/**
	 * The GUI calls this function to set the measurement instrument module that is connected to the software
	 * @param inst
	 */
	public void setConnectedInstrumentName(String inst);
	
	/**
	 * The action handler calls this function to get the instrument that is selected by the user
	 * @return String
	 */
	public String getConnectedInstrumentName();
	
	/**
	 * The action handler calls this function to check if the user has updated the connected
	 * instrument device
	 * @return boolean
	 */
	public boolean connectedInstrumentIsUpdated();
	
	/**
	 * The GUI calls this function to set the signal generator module that is connected to the software
	 * @param sig
	 */
	public void setConnectedSigGenName(String sig);
	
	/**
	 * The action handler calls this function to get the signal generator module connected 
	 * @return String
	 */
	public String getConnectedSigGenName();
	
	/**
	 * The action handler calls this function to check if the user has updated the signal generator module that is 
	 * connected to the software
	 * @return boolean
	 */
	public boolean connectedSigGenIsUpdated();
}
