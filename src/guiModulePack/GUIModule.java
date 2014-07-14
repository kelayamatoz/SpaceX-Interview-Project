package guiModulePack;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import interfacePack.GUIActionHandlerInterface;

import javax.swing.*;

import controlModulePackage.DevicesInfo;
import abstractModulePack.AbstractDeviceModule;
import abstractModulePack.GUIAbstractModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This is a GUI example that utilized a chart library (JChart2D) to visualize 
 * the waveform. 
 * @author tianzhao
 */

public class GUIModule extends GUIAbstractModule {
	// set traces property at this point. will need to change later. 
	
	private static final String defaultChartName = "Default Chart",
								defaultXAxisName = "Time",
								defaultYAxisName = "Energy",
								defaultAllPortsDisplayName = "Display All Ports";
	private static final int defaultChartWidth = 800,
							 defaultChartHeight = 250,
							 maxTraceLength = 200;
	private Chart2D chart;
	private HashMap<String, ITrace2D> traceMap;
	private HashMap<String, Color> colorMap;
	private HashMap<String, Boolean> displayableTraceMap;

	private ArrayList<GUIActionHandlerInterface> actionHandlerList;

	private JLabel deviceLabel, displayLabel;
	private JFrame mainFrame;
	private JPanel controlPanel, buttonSetPanel, connectedDevicePanel, displayOptionPanel;
	
	/* control components */
	private JButton startButton, pauseButton, resetButton;
	private JComboBox connectedInstCombo, connectedSigenCombo, displayDeviceCombo, traceCombo;
	
	/**
	 * This function adds Java Swing action listeners to all the JComponents on the GUI
	 */
	private void addListeners() {
		startButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (GUIActionHandlerInterface actHandler : actionHandlerList) {
						actHandler.setStart();
					}
				}
			}
		);
		
		pauseButton.addActionListener (
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (GUIActionHandlerInterface actHandler : actionHandlerList) {
						actHandler.setPause();
					}
				}
			}
		);
		
		resetButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (GUIActionHandlerInterface actHandler : actionHandlerList) {
						actHandler.resetAllDevices();
					}
				}
			}
		);
		
		displayDeviceCombo.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					String deviceName = (String)cb.getSelectedItem();
					for (GUIActionHandlerInterface actHandler : actionHandlerList) {
						actHandler.setDisplayDevice(deviceName);
					}
				}
			}
		);
	
		traceCombo.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					String portOption = (String)cb.getSelectedItem();
					chart.removeAllTraces();
					traceMap = new HashMap<String, ITrace2D>();
					if (portOption.equals(defaultAllPortsDisplayName)) {
						for (String traceName : colorMap.keySet()) {
							displayableTraceMap.put(traceName, true);
							addNewTrace(traceName, colorMap.get(traceName));
						}
					} else {
						for (String traceName : colorMap.keySet()) {
							displayableTraceMap.put(traceName, false);
						}
						
						displayableTraceMap.put(portOption, true);
						addNewTrace(portOption, colorMap.get(portOption));
					}
				}
			}
		);
		
		connectedInstCombo.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					String connectedInstrument = (String)cb.getSelectedItem();
					for (GUIActionHandlerInterface actHandler : actionHandlerList) {
						actHandler.setConnectedInstrumentName(connectedInstrument);
					}
				}
			}
		);
		
		connectedSigenCombo.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox cb = (JComboBox)e.getSource();
						String connectedSigGen = (String)cb.getSelectedItem();
						for (GUIActionHandlerInterface actHandler : actionHandlerList) {
							actHandler.setConnectedSigGenName(connectedSigGen);
						}
					}
				}
			);		
	}
	
	/**
	 * This function initializes all the JComponents on the GUI
	 * @param devInfo
	 */
	private void initializer(DevicesInfo devInfo) {
		mainFrame = super.getFrame();
		mainFrame.setLayout(new BorderLayout());
		actionHandlerList = new ArrayList<GUIActionHandlerInterface>();
		
		/* Sets up the control panel */
		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		buttonSetPanel = new JPanel();
		buttonSetPanel.setLayout(new FlowLayout());
		startButton = new JButton("Start");
		pauseButton = new JButton("Pause");
		resetButton = new JButton("Reset");
		buttonSetPanel.add(startButton);
		buttonSetPanel.add(pauseButton);
		buttonSetPanel.add(resetButton);
		this.controlPanel.add(buttonSetPanel, BorderLayout.NORTH);
		
		/* Sets up connectedDevicePanel */
		this.deviceLabel = new JLabel("Connected Devices: ");
		connectedDevicePanel = new JPanel();
		connectedDevicePanel.setLayout(new FlowLayout());
		ArrayList<String> instNames = devInfo.getInstrumentDevicesNames();
		instNames.add(0, "-- Choose Measurement Instrument --");
		connectedInstCombo = new JComboBox(instNames.toArray());
		ArrayList<String> sigNames = devInfo.getSigGenDevicesNames();
		sigNames.add(0, "-- Choose Signal Generator --");
		connectedSigenCombo = new JComboBox(sigNames.toArray());
		connectedDevicePanel.add(deviceLabel);
		connectedDevicePanel.add(connectedInstCombo);
		connectedDevicePanel.add(connectedSigenCombo);
		this.controlPanel.add(connectedDevicePanel, BorderLayout.CENTER);
		
		/* Sets up display option panel */
		this.displayLabel = new JLabel("Displays Data From: ");
		displayOptionPanel = new JPanel();
		displayOptionPanel.setLayout(new FlowLayout());
		displayDeviceCombo = new JComboBox(devInfo.getAllDevicesNames().toArray());
		ArrayList<String> displayComp = devInfo.getAllPortsNames();
		displayComp.add(0, defaultAllPortsDisplayName);
		traceCombo = new JComboBox(displayComp.toArray());
		displayOptionPanel.add(displayLabel);
		displayOptionPanel.add(displayDeviceCombo);
		displayOptionPanel.add(traceCombo);
		this.controlPanel.add(displayOptionPanel, BorderLayout.SOUTH);
		
		chart = new Chart2D() {
			private static final long serialVersionUID = 1L;
			public Dimension getPreferredSize() {
				return new Dimension(defaultChartWidth, defaultChartHeight);
			}
		};
		
		chart.setName(defaultChartName);
		mainFrame.getContentPane().add(chart, BorderLayout.NORTH);
		mainFrame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		addListeners();
		
		traceMap = new HashMap<String, ITrace2D>();
		colorMap = new HashMap<String, Color>();
		
		displayableTraceMap = new HashMap<String, Boolean>();
		for (String portName : devInfo.getAllPortsNames()) {
			this.displayableTraceMap.put(portName, true);
		}
		
	}
	
	public GUIModule(String name, DevicesInfo devInfo) {
		super();
		initializer(devInfo);
		super.setGUITitle(name);
	}
	
	public GUIModule(String name, int width, int height, DevicesInfo devInfo) {
		super();
		initializer(devInfo);
		super.setGUITitle(name);
		super.setGUISize(new Dimension(width, height));
	}
	
	public GUIModule(DevicesInfo devInfo) {
		super();
		initializer(devInfo);
	}
	
	/**
	 * This function adds an actHandler to the GUI
	 * @param actHandler
	 */
	public void addGUIActionHandler(GUIActionHandlerInterface actHandler) {
		actionHandlerList.add(actHandler);
	}
	
	/**
	 * This function sets the size of a GUI
	 * @param width
	 * @param height
	 */
	public void setGUISize(int width, int height) {
		super.setGUISize(new Dimension(width, height));
	}
	
	public boolean addNewTrace(String traceName, Color color) {
		if(traceMap.containsKey(traceName)) return false;
		ITrace2D trace = new Trace2DLtd(maxTraceLength);
		trace.setName(traceName);
		trace.setColor(color);
		chart.addTrace(trace);
		traceMap.put(traceName, trace);
		colorMap.put(traceName, color);
		return true;
	}
	
	/**
	 *  Overload: add a new trace to the chart using default values of color and traceName 
	 */
	public boolean addNewTrace(String traceName) {
		Color color;
		if(traceMap.containsKey(traceName)) return false;
		if(colorMap.containsKey(traceName)) color = colorMap.get(traceName);
		else {
			color = this.getRandomColor();
		}
		
		return addNewTrace(traceName, color);
	}
	
	/**
	 * This function finds the trace named traceName and adds a new point
	 * to the trace. It also throws ObjectNotFoundException when the specified
	 * trace is not found.
	 * @param traceName
	 * @param x
	 * @param y
	 */
	public boolean addOnePointToATrace(String traceName, double x, double y) {
		if (!traceMap.containsKey(traceName)) return false;
		traceMap.get(traceName).addPoint(x, y);
		return true;
	}
	
	/**
	 * This function checks if the trace named traceName is already present in the GUI.
	 * @param traceName
	 */
	public boolean containsTrace(String traceName) {
		return traceMap.containsKey(traceName);
	}
	
	/**
	 * This function resets all the traces
	 */
	public void resetAllTraces() {
		for (String traceName : traceMap.keySet()) {
			traceMap.get(traceName).removeAllPoints();
		}
	}
	
	/**
	 * This function sets the properties of a graph using an abstractDeviceModule
	 * @param absModule
	 */
	public void setGraphPropertyUsingDevice(AbstractDeviceModule absModule) {
		mainFrame.setTitle(absModule.getDeviceName());
		chart.setName(absModule.getDeviceName());
		chart.getAxisX().setAxisTitle(new AxisTitle(absModule.getXAxisName()));
		chart.getAxisY().setAxisTitle(new AxisTitle(absModule.getYAxisName()));
	}
	
	/**
	 * This function removes a trace from the GUI
	 * @param traceName
	 */
	public void removeTrace(String traceName) {
		if (!traceMap.containsKey(traceName)) return;
		chart.removeTrace(traceMap.get(traceName));
		traceMap.remove(traceName);
	}
	
	/**
	 * This function remove all J2DChart traces from the GUI
	 */
	public void removeAllTraces() {
		traceMap = new HashMap<String, ITrace2D>();
		chart.removeAllTraces();
	}
	
	/**
	 * This function checks if a trace should be displayed on the GUI
	 * @param portName
	 * @return boolean
	 */
	public boolean isDisplayableTrace(String portName) {
		return this.displayableTraceMap.get(portName);
	}
	
	/**
	 * This function sets the title of x axis. 
	 */
	@Override
	public void setXAxisTitle(String xTitle) {
		chart.getAxisX().setAxisTitle(new AxisTitle(defaultXAxisName));
	}
	
	@Override
	public void setYAxisTitle(String yTitle) {
		chart.getAxisY().setAxisTitle(new AxisTitle(defaultYAxisName));
	}
}
