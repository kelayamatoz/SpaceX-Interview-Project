package realDeviceDriverPack;
import abstractModulePack.AbstractSignalModule;

import java.awt.geom.Point2D;
import java.io.*; 
import java.net.*; 
import java.util.ArrayList;

/**
 * This is an example that connects software to a real signal generator that can be communicated
 * via SCPI commands through LAN. 
 * 
 * Assuming that this signal generator is connected to a switch box.
 * 
 * Please edit the default Strings to configure the driver with the correct name of the signal generator,
 * default channel and the frequency at which the device operates on.
 * @author tianzhao
 */

public class RealSignalModule2 extends AbstractSignalModule {
	private static final String defaultSignalGeneratorName = "xxx.xxx.xxx.xxx";
	private String defaultOperationFrequency = "freq 2020MHz\n", 
					defaultChannel = "101";
	private static final int defaultSocketPort = 5025;
	private long measurementStartTime;
	private BufferedWriter out;
	private BufferedReader in;
	private Socket skt;
	private boolean setUpSuccess;
	
	public RealSignalModule2(String signalGeneratorName, String xAxisName, String yAxisName, int numOfPorts) {
		super(signalGeneratorName, xAxisName, yAxisName, numOfPorts);
		measurementStartTime = System.currentTimeMillis();
		setUpSuccess = false;
		try {
			skt = new Socket(defaultSignalGeneratorName, defaultSocketPort); // This is an example that connects 
																			 // the software to port 5025 
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error: failed to connect to " + RealSignalModule2.defaultSignalGeneratorName + " at port " + RealSignalModule2.defaultSocketPort);
			return;
		}

		setUpSuccess = true;

	}

	private void sendCommand(String command) throws IOException {
		out.write(command + "\n");
		out.flush();
	}
	
	@Override
	public boolean reset() {
		if (!setUpSuccess) return false;
		try {
			sendCommand("*RST"); //reset
			sendCommand("*CLS"); //clear IEEE 488.2 status
			sendCommand("*SRE 0"); //clear SRQ enable 
			sendCommand("*ESE 0"); //clear std. event
			sendCommand("STATUS:PRESET\n"); //clean up other registers
		} catch (IOException e) {
			System.out.println("Error: unable to reset the device");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean configureConnection() {
		if (!setUpSuccess) return false;
		try {
			out.write(defaultOperationFrequency);
			out.flush();
			sendCommand("*OPC?"); //wait for the source to settle
			sendCommand("ROUT:MON:CHAN (@"+ defaultChannel +")"); //monitor default channel
		} catch (IOException e) {
			System.out.println("Error: failed to wait for the signal generator to settle.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean getDataFromDevice() {
		if (!setUpSuccess) return false;
		ArrayList<Point2D.Double> dataPoints = new ArrayList<Point2D.Double>();
		try {
			sendCommand("ROUT:MON:STAT ON"); // start monitoring the default channel
			for (int i = 0; i < this.getDataBufferSize(); i ++) {
				Point2D.Double currentDataPoint = new Point2D.Double();
				currentDataPoint.x = (double)(System.currentTimeMillis() - this.measurementStartTime);
				String value = in.readLine();
				if (value == null) break;
				currentDataPoint.y = Double.parseDouble(value);
				dataPoints.add(currentDataPoint);
			}
			
			sendCommand("WAIT 2"); // ask the device to wait for 2 seconds before the measurement is done
			
		} catch (IOException e) {
			System.out.println("Error: failed to read data from socket");
			return false;
		}
		
		this.prepareDataForReading(0, dataPoints);
		this.disableReadingFromAllPorts();
		this.enableReadingFromPort(0);
		return true;
	}

}
