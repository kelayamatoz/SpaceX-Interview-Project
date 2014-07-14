package controlModulePackage;
import guiModulePack.GUIModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.geom.Point2D;

import com.javonet.JavonetException;

import abstractModulePack.*;
import realDeviceDriverPack.*;
import simulatorPack.*;

/**
 * This module implements the core control module that connects the GUI with all the devices. 
 * This module is running as a state-machine, where its state is modified by a private function called
 * updateStateOfControl().
 * @author tianzhao
 *
 */
public class ControlModule {
	public static void main(String[]args) throws JavonetException{
		/* Sets up back-end info logger */
		final DevicesInfo devInfo = new DevicesInfo();
		final GUIActionHandler actHandler = new GUIActionHandler();
		
		/* Sets up Signal generator modules */
		final TestRackSimulator1 sim1 = new TestRackSimulator1("TestRackSimulator1", 
															   "Time (nanoseconds)", 
															   "Amplitude (volts)", 
															   4);
		
		final TestRackSimulator2 sim2 = new TestRackSimulator2("TestRackSimulator2",
															   "Time (nanoseconds)",
															   "Amplitude (volts)",
															   4);
		
		final RealSignalModule1 realRack1 = new RealSignalModule1("RealTestRack(no switchbox)",
																	  "Time (nanoseconds)",
																	  "Amplitude (volts)",
																	  4);
		
		final RealSignalModule2 realRack2 = new RealSignalModule2("RealTestRack(with switchbox)",
																	  "Time (nanoseconds)",
																	  "Amplitude (volts)",
																	  4);
	
		sim1.configureConnection();
		sim2.configureConnection();
		realRack1.configureConnection();
		realRack2.configureConnection();
		
		devInfo.addDeviceAsSigGenerator(sim1);
		devInfo.addDeviceAsSigGenerator(sim2);
		devInfo.addDeviceAsSigGenerator(realRack1);
		devInfo.addDeviceAsSigGenerator(realRack2);
		
		devInfo.getPortNamesFromDevice(sim1);
		
		/* Sets up measurement instrument modules and connects to a signal generator module */
		final SimulatedInstrumentModule ins1 = new SimulatedInstrumentModule("MeasurementInstrument1",
																			 "Time (nanoseconds)",
																			 "Energy (dBm)",
																			 4);
		final RealInstrumentModule realIns1 = new RealInstrumentModule("Power Meter 1",
																	   "Time (nanosecons)",
																	   "Energy (dBm)",
																	   4);
		ins1.configureConnection();
		realIns1.configureConnection();
		devInfo.addDeviceAsInstrument(ins1);
		devInfo.addDeviceAsInstrument(realIns1);
		
		/* Initializes the GUI */
		final GUIModule defaultGUI = new GUIModule(devInfo);
		for (String portName : devInfo.getAllPortsNames()) {
			if (!defaultGUI.addNewTrace(portName, defaultGUI.getRandomColor()));
		}
		
		defaultGUI.setGraphPropertyUsingDevice(sim1);
		defaultGUI.addGUIActionHandler(actHandler);
		defaultGUI.displayGUI();
	    
		devInfo.startAllSignalDevices();
		
		// Set up the default connected devices 
		/* The 0th signal generator devices recorded by the devInfo class will be the 
		 * default generator shown on the GUI. Same for the measurement instrument */
		devInfo.setConnectedSigGenName(devInfo.getSigGenDevicesNames().get(0));
		devInfo.setConnectedInstrumentName(devInfo.getInstrumentDevicesNames().get(0));
		AbstractInstrumentModule defaultInstrument = devInfo.getConnectedInstrumentModule();
		defaultInstrument.connectToSignalGenModule(devInfo.getConnectedSignalModule());
		
		/* Wait for the user to choose the connected devices and to click on the start button */
		while(!actHandler.needStart() || 
			  !actHandler.connectedSigGenIsUpdated() || 
			  !actHandler.connectedInstrumentIsUpdated()) 
			System.out.print("");  
		
		/* 
	     * In order not to harm the performance of the GUI thread, a new thread is used to 
	     * add points to the GUI
	     */
		Timer timer = new Timer(true);
	    TimerTask task = new TimerTask(){
	    	String defaultDeviceName = devInfo.getAllDevicesNames().get(0); // initialize devDisplay to be the default 
	    																	// device showned on the combobox
	    	AbstractDeviceModule devDisplay = devInfo.getDeviceByName(defaultDeviceName); 
	    	
	    	/**
	    	 * This is a inner-class helper function. 
	    	 * This function checks the state of the actionHandler to see if the user has clicked on any buttons
	    	 * or has chosen a different device from the combo-box. If this function finds an update, it will 
	    	 * update the DevicesInfo object to update the devices based on user's actions. 
	    	 */
	    	private void updateStateOfControl() {
	    		// checks the behavior of the start button
    			if (actHandler.needStart()) {
    				devInfo.startAllSignalDevices();
    			} 
    			
    			// checks the behavior of the pause button
    			if (actHandler.needPause()) {
    				devInfo.pauseAllSignalDevices();
    			} 
    			
    			// checks the behavior of the reset button
    			if (actHandler.needResetAllDevices()) {
    				devInfo.resetAllSignalDevices();
    				defaultGUI.resetAllTraces();
    				actHandler.finishResetAllDevices();
    			}
    			
    			// checks the behavior of the combo box for devices being displayed 
    			if (actHandler.displayDeviceIsUpdated()) {
    				devDisplay =  devInfo.getDeviceByName(actHandler.getDisplayDeviceName());
    				defaultGUI.resetAllTraces();
    				defaultGUI.setGraphPropertyUsingDevice(devDisplay);
    			}

    			AbstractSignalModule currentSig = devInfo.getConnectedSignalModule();
    			AbstractInstrumentModule currentInst = devInfo.getConnectedInstrumentModule();

    			// checks the combo box for connected signal generators
    			if (actHandler.connectedInstrumentIsUpdated()) {
    				currentInst.disconnectSignalModule(currentSig);
    				/*
    				 * If user selects the wrong item on the combo box this if-block will terminate 
    				 * the function. e.g. User clicks on the "-- Choose Measurement Instrument -- "
    				 */
    				if (!devInfo.containsDevice(actHandler.getConnectedInstrumentName())) return;
    				devInfo.setConnectedInstrumentName(actHandler.getConnectedInstrumentName());
    				currentInst = devInfo.getConnectedInstrumentModule();
    				currentInst.connectToSignalGenModule(currentSig);
    				defaultGUI.resetAllTraces();
    			}
    			
    			// checks the combo box for connected instrument devices
    			if (actHandler.connectedSigGenIsUpdated()) {
    				currentSig.disconnectDevice(currentInst);
    				/*
    				 * If user selects the wrong item on the combo box this if-block will terminate 
    				 * the function. e.g. User clicks on the "-- Choose Signal Generator -- "
    				 */
    				if (!devInfo.containsDevice(actHandler.getConnectedSigGenName())) return;
    				devInfo.setConnectedSigGenName(actHandler.getConnectedSigGenName());
    				currentSig = devInfo.getConnectedSignalModule();
    				currentInst.connectToSignalGenModule(currentSig);
    				defaultGUI.resetAllTraces();
    			}
	    	}
	    	
	    	@Override
	    	public void run() {
	    		while(true) {
	    			updateStateOfControl();	
	    			
	    			/* Signal generator devices get data */
	    			devInfo.getDataFromDeviceForSigDevices();
	    			/* Signal generator devices transmit data to their listener devices */
	    			devInfo.transmitDataToAllPortsForSigDevices();
	    			/* Measurement devices output data for reading */
	    			devInfo.getDataFromDeviceForInstDevices();
	    			
	    			HashMap<Integer, ArrayList<Point2D.Double>> dataInfoMap = new HashMap<Integer, ArrayList<Point2D.Double>>();
	    			int dataSize = 0;
	    			/* 
	    			 * The GUI is not updated when: 
	    			 * 1. devDisplay.getIndicesOfAllPorts() == null:
	    			 * 	getIndicesOfAllPorts() will return null when the device that calls this function failed to initialize 
	    			 *  (a device can fail to be initialized in many ways, e.g. the user didn't connect a real device to the software). 
	    			 *  Therefore, we simply continue the execution of the program in hope that the faulted device can come back to execution
	    			 *  
	    			 * 2. devDisplay.allPortsClosed()
	    			 * 	allPortsClosed returns true when all ports of the device being displayed are closed. In this scenario there is 
	    			 *  no need to update the GUI. 
	    			 */
	    			if (devDisplay.getIndicesOfAllPorts() == null || devDisplay.allPortsClosed()) continue;  
	    			for (int portIndex : devDisplay.getIndicesOfAllPorts()) {
	    				if (!devDisplay.isAnActivePort(portIndex)) defaultGUI.removeTrace(devInfo.getPortName(portIndex));
	    				else {
	    					if (defaultGUI.isDisplayableTrace(devInfo.getPortName(portIndex))){
	    						defaultGUI.addNewTrace(devInfo.getPortName(portIndex));
	    						ArrayList<Point2D.Double> dataPoint = devDisplay.getDataFromPort(portIndex);
			    				dataInfoMap.put(portIndex, dataPoint);
			    				dataSize = dataPoint.size();
	    					}
	    				}
	    			}
	    			
	    			/* adds data to traces for display*/
	    			for (Integer portIndex : devDisplay.getIndicesOfAllActivePorts()) {
    					if (defaultGUI.isDisplayableTrace(devInfo.getPortName(portIndex))) {
    						for (int i = 0; i < dataSize; i ++) {
	    						double x = dataInfoMap.get(portIndex).get(i).x;
		    					double y = dataInfoMap.get(portIndex).get(i).y;
		    					defaultGUI.addOnePointToATrace(devInfo.getPortName(portIndex), x, y);
		    					
	    					}
	    				}
	    			}
	    		}
	    	}
	    };

	    // Every 20 milliseconds a new value is collected.
	    timer.schedule(task, 1000, 20);
	  }
}
