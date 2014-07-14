package controlModulePackage;
import java.util.ArrayList;
import java.util.HashMap;

import abstractModulePack.AbstractDeviceModule;
import abstractModulePack.AbstractInstrumentModule;
import abstractModulePack.AbstractSignalModule;

/**
 * This class is a supportive class for the control module. The control module 
 * uses this class to store information of every devices that are instantiated and 
 * to track the status of the devices being connected and being displayed.
 * @author tianzhao
 */

public class DevicesInfo {
	private String connectedSigGenName, connectedInstName;
	private HashMap<Integer, String> portNamesMap;
	private HashMap<String, AbstractDeviceModule> deviceMap;
	private ArrayList<AbstractDeviceModule> devices;
	private ArrayList<AbstractSignalModule> sigDevices;
	private ArrayList<AbstractInstrumentModule> insDevices;
	public DevicesInfo() {
		insDevices = new ArrayList<AbstractInstrumentModule>();
		sigDevices = new ArrayList<AbstractSignalModule>();
		devices = new ArrayList<AbstractDeviceModule>();
		portNamesMap = new HashMap<Integer, String>();
		deviceMap = new HashMap<String, AbstractDeviceModule>();
	}
	
	/**
	 * This function sets the signal generator module connected to the software
	 * @param sig
	 */
	public void setConnectedSigGenName(String sig) {
		this.connectedSigGenName = sig;
	}
	
	/**
	 * This function returns the name of the signal generator that is connected
	 * @return String
	 */
	public String getConnectedSigGenName() {
		return this.connectedSigGenName;
	}
	
	/**
	 * This function sets the connected measurement instrument module
	 * @param ins
	 */
	public void setConnectedInstrumentName(String ins) {
		this.connectedInstName = ins;
	}
	
	/**
	 * This function returns the name of the connected measurement instrument module
	 * @return String
	 */
	public String getConnectedInstrumentName() {
		return this.connectedInstName;
	}
	
	/**
	 * This function gets the name of ports from the device and stores the names 
	 * into a DeviceInfo object
	 * @param dev
	 */
	public void getPortNamesFromDevice(AbstractDeviceModule dev) {
		for (Integer key : dev.getIndicesOfAllPorts()) {
			portNamesMap.put(key, "port " + key);
		}
	}
	
	/**
	 * This function adds a port 
	 * @param portIndex
	 * @param portName
	 */
	public void addPortName(int portIndex, String portName) {
		portNamesMap.put(portIndex, portName);
	}
	
	/**
	 * This function returns the name of a port
	 * @param portIndex
	 * @return String
	 */
	public String getPortName(int portIndex) {
		return portNamesMap.get(portIndex);
	}
	
	/**
	 * This function adds a measurement instrument device to the list of instrument devices 
	 * that is stored in the DeviceInfo class
	 * @param device
	 */
	public void addDeviceAsInstrument(AbstractInstrumentModule device) {
		this.insDevices.add(device);
		this.devices.add(device);
		this.deviceMap.put(device.getDeviceName(), device);
	}
	
	/**
	 * This function adds a signal generator device to the list of signal generator devices that
	 * is stored in the DeviceInfo class
	 * @param device
	 */
	public void addDeviceAsSigGenerator(AbstractSignalModule device) {
		this.sigDevices.add(device);
		this.devices.add(device);
		this.deviceMap.put(device.getDeviceName(), device);
	}
	
	/**
	 * This is a helper function that takes in a list of devices, returns a list of 
	 * the names of the devices
	 * @param arrlist
	 * @return  ArrayList<String>
	 */
	private ArrayList<String> toStringArray(ArrayList<AbstractDeviceModule> arrlist) {
		ArrayList<String> names = new ArrayList<String>();
		for (AbstractDeviceModule dev : arrlist) {
			names.add(dev.getDeviceName());
		}
		
		return names;
	}
	
	/**
	 * This function returns the names of signal generator devices
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getSigGenDevicesNames() {
		ArrayList<String> sigGenNames = new ArrayList<String>();
		for (AbstractSignalModule sig : this.sigDevices) {
			sigGenNames.add(sig.getDeviceName());
		}
		
		return sigGenNames;
	}
	
	/**
	 * This function returns the names of measurement instrument devices
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getInstrumentDevicesNames() {
		ArrayList<String> instNames = new ArrayList<String>();
		for (AbstractInstrumentModule inst : this.insDevices) {
			instNames.add(inst.getDeviceName());
		}
		
		return instNames;
	}
	
	/**
	 * This function returns the names of all the devices
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getAllDevicesNames() {
		return this.toStringArray(this.devices);
	}
	
	/**
	 * This function returns the names of all the ports. 
	 * 
	 *  Assume that for every device we only have four ports. 
	 *  Though we make such assumption, this module can be easily 
	 *  extended to include more ports. 
	 *  @return ArrayList<String>
	 */
	public ArrayList<String> getAllPortsNames() {
		ArrayList<String> ports = new ArrayList<String>();
		for (Integer key : this.portNamesMap.keySet()) {
			ports.add(portNamesMap.get(key));
		}
		
		return ports;
	}
	
	/**
	 * This function resets all the devices that the DeviceInfo object tracks
	 */
	public void resetAllSignalDevices() {
		for (AbstractDeviceModule dev : this.devices) {
			dev.reset();
		}
	}
	
	/**
	 * This function starts all the signal generator devices. 
	 * We don't need a similar function for the measurement instrument devices 
	 * because measurement instrument devices cannot generate data on its own. They 
	 * must be connected to signal generators to get meaningful outputs. 
	 */
	public void startAllSignalDevices() {
		for (AbstractSignalModule sig : this.sigDevices) {
			sig.enableDataGeneration();
		}
	}
	
	/**
	 * This function stops all the signal generator devices from outputting data
	 */
	public void pauseAllSignalDevices() {
		for (AbstractSignalModule sig : this.sigDevices) {
			sig.disableDataGeneration();
		}
	}
	
	/**
	 * This function commands all the signal generator devices to get data from either
	 * a simulated signal generator or a real signal generator
	 * @return boolean
	 */
	public boolean getDataFromDeviceForSigDevices() {
		boolean noError = true; 
		for (AbstractSignalModule sig : this.sigDevices) {
			if (!sig.getDataFromDevice()) noError = false;
		}
		
		return noError;
	}
	
	/**
	 * This function commands all the signal generators to send data to listener devices
	 * that are connected to them.
	 * @return boolean
	 */
	public boolean transmitDataToAllPortsForSigDevices() {
		boolean noError = true;
		for (AbstractSignalModule sig : this.sigDevices) {
			if (!sig.transmitDataToAllListenersAtAllPorts()) noError = false;
		}
		
		return noError;
	}
	
	/**
	 * This function commands all the measurement instrument devices to get data for display
	 * @return boolean
	 */
	public boolean getDataFromDeviceForInstDevices() {
		boolean noError = true; 
		for (AbstractInstrumentModule inst : this.insDevices) {
			if (!inst.getDataFromDevice()) noError = false;
		}
		
		return noError;
	}
	
	/**
	 * This function finds a device using deviceName and return the device
	 * @param deviceName
	 * @return AbstractDeviceModule
	 */
	public AbstractDeviceModule getDeviceByName(String deviceName) {
		return this.deviceMap.get(deviceName);
	}
	
	/**
	 * This function returns the signal module that is currently connected to the software
	 * @return AbstractSignalModule
	 */
	public AbstractSignalModule getConnectedSignalModule() {
		return (AbstractSignalModule)this.getDeviceByName(this.getConnectedSigGenName());
	}
	
	/**
	 * This function returns the measurement instrument module that is currently connected 
	 * to the software
	 * @return AbstractInstrumentModule
	 */
	public AbstractInstrumentModule getConnectedInstrumentModule() {
		return (AbstractInstrumentModule)this.getDeviceByName(this.getConnectedInstrumentName());
	}
	
	/**
	 * This function checks if deviceName is a valid device
	 * @param deviceName
	 * @return boolean
	 */
	public boolean containsDevice(String deviceName) {
		return this.deviceMap.keySet().contains(deviceName);
	}
}
