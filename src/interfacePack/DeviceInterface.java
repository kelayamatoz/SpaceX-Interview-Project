package interfacePack;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This interface defines the basic behaviors of a device
 * @author tianzhao
 *
 */

public interface DeviceInterface {
	/**
	 * This function sets the name of the device
	 * @param deviceName
	 */
	public void setDeviceName(String deviceName);
	
	/**
	 * This function sets the name of the xAxis of data
	 * @param xName
	 */
	public void setXAxisName(String xName);
	
	/**
	 * This function sets the name of the yAxis of data
	 * @param yName
	 */
	public void setYAxisName(String yName);
	
	/**
	 * This function sets the number of output ports
	 * from this device
	 * @param numOfPorts
	 */
	public void setNumOfPorts(int numOfPorts);
	
	/**
	 * This function sets the size of the data buffer
	 * that will carry the output data from a device
	 * @param bufferSize
	 */
	public void setDataBufferSize(int bufferSize);
	
	/**
	 * This function gets the name of the device
	 * @return String
	 */
	public String getDeviceName();
	
	/**
	 * This function gets the name of xAxis
	 * @return String
	 */
	public String getXAxisName();
	
	/**
	 * This function gets the name of yAxis
	 * @return String
	 */
	public String getYAxisName();
	
	/**
	 * This function gets the number of output ports
	 * @return int
	 */
	public int getNumOfPorts();
	
	/**
	 * This function gets the size of the data buffer
	 * @return int
	 */
	public int getDataBufferSize();

	/**
	 * This function returns a set of port indices
	 * @return Set<Integer>
	 */
	public Set<Integer> getIndicesOfAllPorts();
	
	/**
	 * This function processes data read from the portIndex'th port of the device
	 * @param portIndex
	 * @param dataPointsArray
	 * @return boolean
	 */
	public boolean prepareDataForReading(int portIndex, ArrayList<Point2D.Double> dataPointsArray);
	
	/**
	 * This function configures the connection between the software module and the actual device
	 * The device can be either a simulated device or a real device.
	 * @return boolean
	 */
	public boolean configureConnection();
	
	/**
	 * This function checks if a port exists at the current device
	 * @param portIndex
	 * @return boolean
	 */
	public boolean containsPort(int portIndex);
	
	/**
	 * This function checks if port specified by portIndex is an open port
	 * @param portIndex
	 * @return boolean
	 */
	public boolean isAnActivePort(int portIndex);
	
	/**
	 * This function let a device open one of its ports
	 * @param portIndex
	 */
	public void enablePort(int portIndex);
	
	/**
	 * This function let a device close one of its ports
	 * @param portIndex
	 */
	public void disablePort(int portIndex);
	
	/**
	 * This function returns indices of all open ports
	 * @return Set<Integer>
	 */
	public Set<Integer> getIndicesOfAllActivePorts();
	
	/**
	 * This function is used by device constructor to set all ports to open / closed
	 * @param maxPortNum
	 * @param f
	 */
	public void initializePortUsability(int maxPortNum, boolean f);
	
	/**
	 * This function uses a map of active ports to initialize all the ports
	 * @param activePortMap
	 */
	public void initializePortUsability(HashMap<Integer, Boolean> activePortMap);

	/**
	 * Developers need to implement this function to
	 * either: establish connection to a real device, prepare data and open ports for other
	 * devices to read from 
	 * or: implement a simulated device, prepare data and open ports for other devices to 
	 * read from.
	 * @return boolean
	 */
	public boolean getDataFromDevice();
	
	/**
	 * This function is used to get output data from a single port of the current device. 
	 * @param portIndex
	 * @return ArrayList<Point2D.Double>
	 */
	public ArrayList<Point2D.Double> getDataFromPort(int portIndex);
	
	/**
	 * Developers need to implement this function to reset the device
	 * @return boolean
	 */
	public boolean reset();
	
	/**
	 * This function tells if all the ports of a device are currently closed
	 * @return boolean
	 */
	public boolean allPortsClosed();
}
