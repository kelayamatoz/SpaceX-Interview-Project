package abstractModulePack;
import interfacePack.MeasurementInstrumentInterface;
import interfacePack.ModuleListener;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class implements some basic functionalities of a measurement instrument module.
 * Users can choose to extend this class to get more functionalities.
 * @author tianzhao
 *
 */
public abstract class AbstractInstrumentModule 
					  extends AbstractDeviceModule
					  implements MeasurementInstrumentInterface,
					  			 ModuleListener {
	private static final int maxBufferSize = 256;
	private int sigGenBufferSize;
	private String signalGenName;
	private ArrayList<AbstractSignalModule> connectedSignalModules;
	private HashMap<Integer, ArrayList<Point2D.Double>> measureDataPointsMap;
	private HashMap<Integer, ArrayList<Point2D.Double>> processDataPointsMap;
	
	private void initializer() {
		super.setDataBufferSize(maxBufferSize);
		this.connectedSignalModules = new ArrayList<AbstractSignalModule>();
		measureDataPointsMap = new HashMap<Integer, ArrayList<Point2D.Double>>();
		processDataPointsMap = new HashMap<Integer, ArrayList<Point2D.Double>>();
	}
	
	public AbstractInstrumentModule(String measurementName) {
		super(measurementName);
		initializer();
	}
	
	/* Overloaded version of constructor */
	public AbstractInstrumentModule(String measurementName, AbstractSignalModule absModule) {
		super(measurementName, absModule.getXAxisName(), absModule.getYAxisName(), absModule.getNumOfPorts());
		initializer();
		connectToSignalGenModule(absModule);
	}
	
	/* Overloaded version of constructor */
	public AbstractInstrumentModule(String measurementName, String xAxisName, String yAxisName, int numOfPorts) {
		super(measurementName, xAxisName, yAxisName, numOfPorts);
		initializer();
	}
	
	/*
	 *  Assuming that we only connect the measurement module to 
	 *  signal generators that inherits AbstractSignalModule
	 */
	public void connectToSignalGenModule(AbstractSignalModule absModule) {
		this.connectedSignalModules.add(absModule);
		absModule.addListener(this);
		signalGenName = absModule.getDeviceName();
		sigGenBufferSize = absModule.getDataBufferSize();
		for (int i = 0; i < absModule.getNumOfPorts(); i ++) {
			measureDataPointsMap.put((Integer)i, new ArrayList<Point2D.Double>());
			processDataPointsMap.put((Integer)i, new ArrayList<Point2D.Double>());
			if (absModule.isAnActivePort(i)) super.enablePort(i);
			else super.disablePort(i);
		}
	}
	
	public void disconnectSignalModule(AbstractSignalModule absModule) {
		if (this.connectedSignalModules.contains(absModule)) this.connectedSignalModules.remove(absModule);
		absModule.disconnectDevice(this);
		signalGenName = "";
		sigGenBufferSize = 0;
		for (int i = 0; i < absModule.getNumOfPorts(); i ++) {
			measureDataPointsMap.put((Integer)i, new ArrayList<Point2D.Double>());
			processDataPointsMap.put((Integer)i, new ArrayList<Point2D.Double>());
			super.disablePort(i);
		}
	}
	
	public String getConnectedSignalModuleName() {
		return signalGenName;
	}
	
	public int getSignalModuleDataBufferSize() {
		return sigGenBufferSize;
	}
	
	public ArrayList<Point2D.Double> getRawDataFromPort(Integer portIndex) {
		if (!super.isAnActivePort(portIndex)) {
			System.out.println("Error: "+ super.getDeviceName() + " has not finished reading data yet");
			return null;
		}
		
		super.disablePort(portIndex);
		if (processDataPointsMap.containsKey(portIndex)) return processDataPointsMap.get(portIndex);
		System.out.println("Error: have not received data for processing at port " + portIndex);
		return null;
	}
	
	public ArrayList<Point2D.Double> getDataFromPort(int portIndex) {
		if (!super.isAnActivePort(portIndex)) {
			System.out.println("Error: port " + portIndex + " of " + super.getDeviceName() + " is closed.");
			return null;
		}
		
		if (measureDataPointsMap.containsKey(portIndex)) return measureDataPointsMap.get(portIndex);
		System.out.println("Error: data at port " + " is still being processed.");
		return null;
	}
	
	public boolean prepareDataForReading(int portIndex, ArrayList<Point2D.Double> dataPointsArray) {
		if (!measureDataPointsMap.containsKey(portIndex)) {
			System.out.println("Error: port does not exist");
			return false;
		}
		
		if (dataPointsArray.size() > super.getDataBufferSize()) {
			System.out.print("Error: data buffer overflow. Program can only transfer at most " + super.getDataBufferSize());
			return false;
		}
		
		measureDataPointsMap.put(portIndex, dataPointsArray);
		super.enablePort(portIndex);
		return true;
	}


	public void startReadingPortData(int portIndex, ArrayList<Point2D.Double> portData) {
		/* When the measurement instrument is reading data, disables
		 * other modules from reading at this port of measurement instrument*/
		super.disablePort(portIndex);
		processDataPointsMap.put(portIndex, portData);
	}

	public void endReadingPortData(int portIndex) {
		super.enablePort(portIndex);
	}
	
	public void closePort(int portIndex) {
		super.disablePort(portIndex);
	}
	
	public void openPort(int portIndex) {
		super.enablePort(portIndex);
	}
}
