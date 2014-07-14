package realDeviceDriverPack;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.api.NObject;

import abstractModulePack.AbstractInstrumentModule;
/**
 * This is an example that connects the software to a real power meter. 
 * Requirements: 
 * 1. The testing computer needs to have .NET 3.5.
 * 
 * 2. The testing project needs to load javonet_1.3_net3.5.jar, which is a Java-to-.NET translation format. 
 * 
 * P.S. The included javonet_1.3_net3.5.jar is downloaded from http://www.javonet.com/download/. 
 * 
 * I am using the free-for-30-day trial version. The license key is: Rt46-i7X8-Rs3o-Cc4f-Mb5g and the username is tianzhao@stanford.edu.
 * 
 * 3. User needs to have mcl_pm64.dll under the project directory. If user is using a windows machine, 
 * please change the defaultLookupString parameter to \\mcl_pm64.dll. If the user is using a 32-bit machine, please
 * change the defaultLookupString to \\mcl_pm32.dll. 
 * The dlls can be found at http://www.minicircuits.com/support/software_download.html.
 * @author tianzhao
 */

public class RealInstrumentModule extends AbstractInstrumentModule {
	private long startTime; // in milliseconds
	private boolean configureSuccess;
	private static final String defaultLookupString = "\\mcl_pm64.dll";
	private NObject powerMeterObj;
	public RealInstrumentModule(String measurementName, String xAxis, String yAxis, int portNum) {
		super(measurementName, xAxis, yAxis, portNum);
		startTime = System.currentTimeMillis();
		configureSuccess = false;
		powerMeterObj = null;
	}

	private void setUpJavonet() throws JavonetException {
		Javonet.activate("tianzhao@stanford.edu", "Rt46-i7X8-Rs3o-Cc4f-Mb5g");
	}
	
	@Override
	public boolean configureConnection() {
		try {
			this.setUpJavonet();
		} catch(JavonetException e) {
			System.out.println("Error occurred while trying to initialize Javonet."
								+ " Please make sure if you have all dependencies installed. "
								+ "To check list of mandatory prerequisites please visit http://www.javonet.com/download/. ");
			configureSuccess = false;
			return false;
		}
		
		// The dll used for connection is put under ~\TestRackMeasurementGUI\mcl_pm64.dll
		String workingDir = System.getProperty("user.dir");
		String dllDir = workingDir + defaultLookupString;
		try {
			Javonet.reference(dllDir);
		} catch (JavonetException e) {
			System.out.println("Error: failed to load " + dllDir);
			configureSuccess = false;
			return false;
		}
	
		String pm_SN = "";
		powerMeterObj = null;
		try {
			powerMeterObj = Javonet.New("mcl_pm64.usb_pm");
		} catch (JavonetException e) {
			System.out.println("Error: failed to initialize the usb_pm object");
			return false;
		}	
			
		try {
			short Status = powerMeterObj.invoke("Open_Sensor", pm_SN);
			switch(Status) {
				case 0: 
					System.out.println("Error: Failed to open sensor");
					break;
						
				case 1: 
					break;
				
				case 2: 
					System.out.println("Error: Object is already open");
					break;
				
				case 3: 
					System.out.println("Error: Power Sensor is not available");
					break;
				
				default:
					break;
			}
		} catch (JavonetException e) {
			System.out.println("Error: failed to open Mini-circuits sensor");
			return false;
		}
		
		configureSuccess = true;
		return true;
	}

	@Override
	public boolean getDataFromDevice() {
		if (!this.configureSuccess) return false;
		ArrayList<Point2D.Double> dataPoints = new ArrayList<Point2D.Double>();
		try {
			powerMeterObj.set("Freq", 2020); // Reading data at 2020 MHz
			for (int i = 0; i < this.getDataBufferSize(); i ++) {
				double readResult = powerMeterObj.invoke("ReadPower");
				Point2D.Double pt = new Point2D.Double();
				pt.x = (double)(System.currentTimeMillis() - startTime);
				pt.y = readResult;
				dataPoints.add(pt);
			}
			
			this.prepareDataForReading(0, dataPoints);
			this.enablePort(0);
		} catch (JavonetException e) {
			System.out.println("Error: Failed to get power data from the power meter. Please check if the device is properly connected.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean reset() {
		return true;
	}
}
