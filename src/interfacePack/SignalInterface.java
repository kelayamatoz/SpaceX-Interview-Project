package interfacePack;

import abstractModulePack.AbstractDeviceModule;

/**
 * This interface defines the basic functionalities of a signal generator module
 * @author tianzhao
 *
 */

public interface SignalInterface {
	/**
	 * This function enables a signal generator device to 
	 * start generating data 
	 */
	public void enableDataGeneration();
	
	/**
	 * This function lets a signal generator device stop generating data 
	 */
	public void disableDataGeneration();
	
	/**
	 * This function checks if a signal generator is allowed to generate data
	 * @return boolean
	 */
	public boolean canGenerateData();
	
	/**
	 * This function adds a device as a listener to the signal generator
	 * @param listener
	 */
	public void addListener(ModuleListener listener);

	/**
	 * This function opens port at portIndex for reading 
	 * @param portIndex
	 * @return boolean
	 */
	public boolean enableReadingFromPort(int portIndex);
	
	/**
	 * This function opens all the ports for reading
	 */
	public void enableReadingFromAllPorts();
	
	/**
	 * This function closes port at portIndex and prevents other devices to 
	 * read from this port
	 * @param portIndex
	 * @return boolean
	 */
	public boolean disableReadingFromPort(int portIndex);
	
	/**
	 * This function closes all the ports 
	 */
	public void disableReadingFromAllPorts();
	
	/**
	 * This function transmit data to all the ports of all the listener devices
	 * @return boolean
	 */
	public boolean transmitDataToAllListenersAtAllPorts();
	
	/**
	 * This function transmit data to port at portIndex of all the listener devices
	 * @param portIndex
	 * @return boolean
	 */
	public boolean transmitDataToAllListenersAtPort(int portIndex);
	
	/**
	 * This function disconnects a device from the current signal generator
	 * @param device
	 */
	public void disconnectDevice(AbstractDeviceModule device);


}
