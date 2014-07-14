package controlModulePackage;

import java.util.HashSet;
import java.util.Set;

import interfacePack.GUIActionHandlerInterface;

/**
 * This class is a supportive class. The control module uses a GUIActionHandler object 
 * to talk to the GUI. 
 * 
 * @author tianzhao
 *
 */

public class GUIActionHandler implements GUIActionHandlerInterface {
	private boolean start, pause, deviceUpdated, connectedInstrumentUpdated, connectedSigGenUpdated, needsReset;
	private String connectedInstrument, connectedSigGen, displayDevice;
	private Set<Integer> displayPortsSet;

	public GUIActionHandler() {
		this.needsReset = false;
		this.deviceUpdated = false;
		this.connectedInstrumentUpdated = false;
		this.connectedSigGenUpdated = false;
		this.start = false;
		this.pause = false;
		this.connectedInstrument = "";
		this.connectedSigGen = "";
		this.displayDevice = "";
		this.displayPortsSet = new HashSet<Integer>();
	}
	
	public void setStart() {
		this.start = true;
		this.pause = false;
	}

	public boolean needStart() {
		return this.start;
	}

	public void setPause() {
		this.pause = true;
	}

	public boolean needPause() {
		return this.pause;
	}

	public void setDisplayDevice(String displayDevice) {
		this.displayDevice = displayDevice;
		this.deviceUpdated = true;
	}

	public String getDisplayDeviceName() {
		this.deviceUpdated = false;
		return this.displayDevice;
	}
	
	public boolean displayDeviceIsUpdated() {
		return this.deviceUpdated;
	}
	
	public void setConnectedInstrumentName(String inst) {
		this.connectedInstrument = inst;
		this.connectedInstrumentUpdated = true;
	}
	
	public String getConnectedInstrumentName() {
		this.connectedInstrumentUpdated = false;
		return this.connectedInstrument;
	} 
	
	public boolean connectedInstrumentIsUpdated() {
		return this.connectedInstrumentUpdated;
	}

	public void setConnectedSigGenName(String sig) {
		this.connectedSigGen = sig;
		this.connectedSigGenUpdated = true;
	}
	
	public String getConnectedSigGenName() {
		this.connectedSigGenUpdated = false;
		return this.connectedSigGen;
	}
	
	public boolean connectedSigGenIsUpdated() {
		return this.connectedSigGenUpdated;
	}
	
	public void setDisplayPorts(Set<Integer> displayPorts) {
		this.displayPortsSet = displayPorts;
	}

	public Set<Integer> getDisplayPorts() {
		return this.displayPortsSet;
	}
	
	public void resetAllDevices() {
		this.needsReset = true;
	}
	
	public void finishResetAllDevices() {
		this.needsReset = false;
	}
	
	public boolean needResetAllDevices() {
		return this.needsReset;
	}
}
