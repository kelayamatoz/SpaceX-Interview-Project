package abstractModulePack;

import interfacePack.DeviceInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements some basic functionalities of a device module. 
 * Users can choose to extend this class to get more functionalities.
 * @author tianzhao
 *
 */
public abstract class AbstractDeviceModule implements DeviceInterface{
	private String deviceName, 
				   xAxisName,
				   yAxisName;
	private int numOfPorts,
              	maxDataBufferSize;
	public HashMap<Integer, Boolean> activePortMap;

	public AbstractDeviceModule(String deviceName, String xName, String yName, int numOfPorts) {
		this.deviceName = deviceName;
		xAxisName = xName;
		yAxisName = yName;
		this.numOfPorts = numOfPorts;
		activePortMap = new HashMap<Integer, Boolean>();
	}
  
	/* Overload version of the constructor */
	public AbstractDeviceModule(String deviceName) {
		this.deviceName = deviceName;
	}
  
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public void setXAxisName(String xName) {
		xAxisName = xName;
	}
  
	public void setYAxisName(String yName) {
		yAxisName = yName;
	}
  
	public void setNumOfPorts(int numOfPorts) {
		this.numOfPorts = numOfPorts;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public String getXAxisName() {
		return xAxisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public int getNumOfPorts() {
		return numOfPorts;
	}

	public void setDataBufferSize(int bufferSize) {
		maxDataBufferSize = bufferSize;
	}
  
	public int getDataBufferSize() {
		return maxDataBufferSize;
	}
	
	public boolean isAnActivePort(int portIndex) {
		if (!activePortMap.containsKey(portIndex)) {
			System.out.println("Error: port " + portIndex + " does not exist");
			return false;
		}
		return activePortMap.get(portIndex);
	}
	
	public void enablePort(int portIndex) {
		activePortMap.put(portIndex, true);
	}
	
	public void disablePort(int portIndex) {
		activePortMap.put(portIndex, false);
	}
	
	public boolean containsPort(int portIndex) {
		return activePortMap.containsKey(portIndex);
	}
	
	public void initializePortUsability(int maxPortNum, boolean f) {
		for (int i = 0; i < maxPortNum; i ++) {
			activePortMap.put(i, f);
		}
	}
	
	public void initializePortUsability(HashMap<Integer, Boolean> activePortMap) {
		this.activePortMap = activePortMap;
	}
	
	public Set<Integer> getIndicesOfAllActivePorts() {
		Set<Integer> activePortIndSet = new HashSet<Integer>();
		for (Integer keyPort : activePortMap.keySet()) {
			if (activePortMap.get(keyPort)) activePortIndSet.add(keyPort);
		}
		
		return activePortIndSet;
	}
	
	public boolean allPortsClosed() {
		for (Integer key : this.activePortMap.keySet()) {
			if (this.activePortMap.get(key)) return false;
		}
		
		return true;
	}
	
	public Set<Integer> getIndicesOfAllPorts() {
		return this.activePortMap.keySet();
	}
}
