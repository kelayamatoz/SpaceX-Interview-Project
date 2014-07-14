package interfacePack;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This interface defines the basic functionalities of a measurement instrument
 * @author tianzhao
 *
 */
public interface MeasurementInstrumentInterface {
	/**
	 * This function returns the name of the connected signal generator module to the 
	 * current instrument module
	 * @return String
	 */
	public String getConnectedSignalModuleName();

	/**
	 * This function returns the size of the buffer that the signal module uses to transmit data 
	 * @return int
	 */
	public int getSignalModuleDataBufferSize();

	/**
	 * This function returns the data that the measurement instrument reads from the 
	 * connected signal generator module
	 * @param portIndex
	 * @return ArrayList<Point2D.Double>
	 */
	public ArrayList<Point2D.Double> getRawDataFromPort(Integer portIndex);
}
