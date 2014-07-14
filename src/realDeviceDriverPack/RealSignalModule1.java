package realDeviceDriverPack;
import abstractModulePack.AbstractSignalModule;

import java.awt.geom.Point2D;
import java.io.*; 
import java.net.*; 
import java.util.ArrayList;

/**
 * This is an example that connects the software to a real signal generator that can be communicated
 * via SCPI commands through LAN. Assume This signal generator will output to port 
 * 101, 105, 112, 113. 
 * @author tianzhao
 */

public class RealSignalModule1 extends AbstractSignalModule {
	private static final String defaultSignalGeneratorName = "xxx.xxx.xxx.xxx", // Put instrument hostname here
								defaultOperationFrequency = "freq 2020MHz\n";
	private static final int defaultSocketPort = 5025;
	private long measurementStartTime;
	private BufferedWriter out;
	private BufferedReader in;
	private Socket skt;
	private boolean setUpSuccess;
	
	public RealSignalModule1(String signalGeneratorName, String xAxisName, String yAxisName, int numOfPorts) {
		super(signalGeneratorName, xAxisName, yAxisName, numOfPorts);
		measurementStartTime = System.currentTimeMillis();
		setUpSuccess = false;
		try {
			skt = new Socket(defaultSignalGeneratorName, defaultSocketPort); // This is an example that connects 
																			 // the software to port 5025 
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error: failed to connect to " + RealSignalModule1.defaultSignalGeneratorName + " at port " + RealSignalModule1.defaultSocketPort);
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
		} catch (IOException e) {
			System.out.println("Error: failed to wait for the signal generator to settle.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean getDataFromDevice() {
		if (!setUpSuccess) return false;
		ArrayList<ArrayList<Point2D.Double>> pointsArray = new ArrayList<ArrayList<Point2D.Double>>();
		for (int i = 0; i < 4; i ++) {
			pointsArray.add(new ArrayList<Point2D.Double>());
		}
		try {
			sendCommand("ROUT:MON:STAT ON"); // start monitoring the default channel
			sendCommand("ROUT:SCAN (@101,105,112,113)"); // create a scan list
			sendCommand("TRIG:COUN " + this.getDataBufferSize()); // set the scan list to run this.getDataBufferSize() times to get enough data points
			sendCommand("INIT"); // run the scan list
			sendCommand("WAIT 2"); // wait 2 seconds for the scan to finish
			for (int i = 0; i < this.getDataBufferSize(); i ++) {
				double currentTime = (double)(System.currentTimeMillis() - this.measurementStartTime);
				String value = null;
				for (int j = 0; j < 4; j ++) {
					value = in.readLine();
					if (value == null) break;
					pointsArray.get(j).add(new Point2D.Double(currentTime, Double.parseDouble(value)));
				}
				
				if (value == null) break;
			}
			
			sendCommand("WAIT 2"); // ask the device to wait for 2 seconds so that the GUI can 
								   // process and display the data.
			
		} catch (IOException e) {
			System.out.println("Error: failed to read data from socket");
			return false;
		}
		
		for (int i = 0; i < 4; i ++) {
			this.prepareDataForReading(i, pointsArray.get(i));
		}
		
		this.enableReadingFromAllPorts();
		return true;
	}

}
