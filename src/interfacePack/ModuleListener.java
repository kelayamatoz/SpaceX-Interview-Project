package interfacePack;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import abstractModulePack.AbstractSignalModule;

/**
 * This interface helps the signal generator modules and the other modules in the 
 * program to form an observer pattern.
 * Essentially, in order for a device to get data from a signal generator, the device
 * needs to connect itself to the generator following the same pattern as connectToSignalGenModule
 * in AbstractInstrumentModule. At the same time the generator will keep a internal list of 
 * connected device. When the generator has data to send, the generator will notify each
 * connected device and use connected_device.startReadingPortData to transmit port data to 
 * a specific port. When transmission is completed the generator will call connected_device.end-
 * readingPortData to close the transmission port. 
 * @author tianzhao
 */
public interface ModuleListener {
	/**
	 * This is a call-back function. A signal module will invoke startReadingPortData
	 * to start transferring portData to a specific port of this module listener
	 */
	public void startReadingPortData(int portIndex, ArrayList<Point2D.Double> portData);
	
	/**
	 * This is a call-back function. A signal module will invoke endReadingPortData 
	 * to close data transmission at an existing port of this module listener
	 */
	public void endReadingPortData(int portIndex);
	
	/**
	 * This is a call-back function. A signal module will call closePort to close 
	 * the specific port of all the listener modules that are registered with this signal modules
	 * @param portIndex
	 */
	public void closePort(int portIndex);
	
	/**
	 * This is a call-back function. A signal module will call openPort to open 
	 * the specific port of all the listener modules that are registered with this signal module
	 * @param portIndex
	 */
	public void openPort(int portIndex);
	
	/**
	 * This function connects the current listener from a signal generator module
	 */
	public void connectToSignalGenModule(AbstractSignalModule absModule);
	
	/**
	 * This function disconnects the current listener from a signal generator module
	 * @param absModule
	 */
	public void disconnectSignalModule(AbstractSignalModule absModule);
}

