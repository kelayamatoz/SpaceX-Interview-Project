package simulatorPack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import abstractModulePack.AbstractInstrumentModule;
import abstractModulePack.AbstractSignalModule;
/**
 * This is a simulated measurement instrument that: 
 * 
 * 1. Can be connected to a signal generator module (can be either a real or a simulated signal generator module).
 *
 * 2. Measures the RMS of the data coming in.
 * @author tianzhao
 *
 */

public class SimulatedInstrumentModule extends AbstractInstrumentModule {
	public SimulatedInstrumentModule(String measurementName) {
		super(measurementName);
	}
	
	public SimulatedInstrumentModule(String measurementName, AbstractSignalModule sigGenModule) {
		super(measurementName, sigGenModule);
	}
	
	public SimulatedInstrumentModule(String measurementName, String xAxisName, String yAxisName, int numOfPorts) {
		super(measurementName, xAxisName, yAxisName, numOfPorts);
	}

	/* Does not need to configure connection to a simulated hardware */
	@Override
	public boolean configureConnection() {
		return true;
	}

	private Point2D.Double calculateRMSIndBm(ArrayList<Point2D.Double> sampleArray) {
		Point2D.Double resultDataPoint = new Point2D.Double();
		double ms = 0;
		double averageTime = 0;
		for (int i = 0; i < sampleArray.size(); i ++) {
			ms += sampleArray.get(i).y * sampleArray.get(i).y;
			averageTime += sampleArray.get(i).x;
		}
		
		ms /= sampleArray.size();
	//	ms = Math.sqrt(ms);
		ms = 30 + 10 * Math.log10(ms);
		averageTime /= sampleArray.size();
		resultDataPoint.x = averageTime;
		resultDataPoint.y = ms;
		return resultDataPoint;
	}
	
	/**
	 * This function simulates the process of getting measured data from the instrument
	 */
	@Override
	public boolean getDataFromDevice() {
		for (Integer portIndex : this.getIndicesOfAllActivePorts()) {
			ArrayList<Point2D.Double> dataArrayList = this.getRawDataFromPort(portIndex);
			ArrayList<Point2D.Double> measurementDataArray = new ArrayList<Point2D.Double>();
			// calculate RMS every 1/8 of the data
			int lastIndex = 0;
			for (int j = 0; j < 8; j ++) {
				ArrayList<Double> sampleArray = new ArrayList<Double>();
				for (int i = lastIndex; i < lastIndex + dataArrayList.size()/4; i ++) {
					sampleArray.add(dataArrayList.get(i));
				}
				
				lastIndex ++;
				measurementDataArray.add(calculateRMSIndBm(sampleArray));
			}
			
			this.prepareDataForReading(portIndex, measurementDataArray);
		}
	
		return true;
	}
	
	/**
	 * No need to reset a measurement instruement
	 */
	@Override
	public boolean reset() {
		return true;
	}
}
