package abstractModulePack;
import interfacePack.ModuleListener;
import interfacePack.SignalInterface;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements some basic functionalities of a signal generator module.
 * Users can choose to extend this class to include more functionalities.
 * @author tianzhao
 *
 */
public abstract class AbstractSignalModule 
					  extends AbstractDeviceModule
					  implements SignalInterface {
	/*
	 *  One data transfer between signal module and the measurement module contains 
	 * at most 256 data points
	 */
	private static final int maxBufferSize = 256;
	private boolean canGenerateData;
	private HashMap<Integer, ArrayList<Point2D.Double>> dataPointsMap;
	private ArrayList<ModuleListener> listenerList;
	
	public AbstractSignalModule(String signalGeneratorName, String xAxisName, String yAxisName, int numOfPorts) {
		super(signalGeneratorName, xAxisName, yAxisName, numOfPorts);
		super.setDataBufferSize(maxBufferSize);
		canGenerateData = false;
		listenerList = new ArrayList<ModuleListener>();
		dataPointsMap = new HashMap<Integer, ArrayList<Point2D.Double>>();
		
		super.initializePortUsability(numOfPorts, false);
		for (int i = 0; i < numOfPorts; i ++) {
			dataPointsMap.put((Integer)i, new ArrayList<Point2D.Double>());
			super.disablePort(i);
		}
	}
	
	public void enableDataGeneration() {
		canGenerateData = true;
	}
	
	public void disableDataGeneration() {
		canGenerateData = false;
	}
	
	public boolean canGenerateData() {
		return canGenerateData;
	}
	
	public boolean enableReadingFromPort(int portIndex) {
		if (!super.containsPort(portIndex)) return false;
		super.enablePort(portIndex);
		// notify all the listeners to open this port
		for (ModuleListener listener : this.listenerList) listener.openPort(portIndex);
		return true;
	}
	
	public void enableReadingFromAllPorts() {
		for (Integer key : this.getIndicesOfAllPorts()) {
			enableReadingFromPort(key);
		}
	}
	
	public boolean disableReadingFromPort(int portIndex) {
		if (!super.containsPort(portIndex)) return false;
		super.disablePort(portIndex);
		// notify all the listeners to close this port
		for (ModuleListener listener : this.listenerList) listener.closePort(portIndex);
		return true;
	}
	
	public void disableReadingFromAllPorts() {
		for (Integer key : this.getIndicesOfAllPorts()) {
			disableReadingFromPort(key);
		}
	}
	
	public void disconnectDevice(AbstractDeviceModule device) {
		if (!listenerList.contains((ModuleListener)device)) System.out.println("Device does not exist");
		this.listenerList.remove((ModuleListener)device);
	}

	public ArrayList<Point2D.Double> getDataFromPort(int portIndex) {
		if (!this.canGenerateData) {
			System.out.println("Error: signal generator does not have the permission to generate data yet");
			return null;
		}
		
		if (!super.isAnActivePort(portIndex)) {
			System.out.print("Error: port " + portIndex + " is not an active port");
			return null;
		}
		
		if (dataPointsMap.containsKey(portIndex)) return dataPointsMap.get(portIndex);
		return null;
	}
	
	public boolean prepareDataForReading(int portIndex, ArrayList<Point2D.Double> dataPointsArray){
		if (!dataPointsMap.containsKey(portIndex)) {
			System.out.println("Error: port does not exist");
			return false;
		}
		
		if (dataPointsArray.size() > maxBufferSize) {
			System.out.print("Error: data buffer overflow. Program can only transfer at most " +
							super.getDataBufferSize() +
							" from a generator module to a measurement module");
			return false;
		}
		
		dataPointsMap.put((Integer)portIndex, dataPointsArray);
		return true;
	}
	
	public void addListener(ModuleListener listener) {
		listenerList.add(listener);
	}
	
	
	public boolean transmitDataToAllListenersAtAllPorts() {
		for (Integer portNum : this.getIndicesOfAllActivePorts()) {
			boolean success = transmitDataToAllListenersAtPort(portNum);
			if (!success) return false;
		}
		
		return true;
	}
	
	public boolean transmitDataToAllListenersAtPort(int portIndex) {
		for (ModuleListener listener : listenerList) {
			listener.startReadingPortData(portIndex, dataPointsMap.get(portIndex));
			listener.endReadingPortData(portIndex);
		}
		
		return true;
	}	
}
