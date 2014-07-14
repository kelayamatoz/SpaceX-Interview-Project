package controlModulePackage;

import interfacePack.ModuleListener;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;

import abstractModulePack.AbstractSignalModule;

/**
 * Demonstration of how to directly connect a module a signal module that utilizes observer pattern
 * @author tianzhao
 *
 */
class ConnectorToSigGen implements ModuleListener {
	private HashMap<Integer, ArrayList<Point2D.Double>> dataPoints;
	public ConnectorToSigGen() {
		dataPoints = new HashMap<Integer, ArrayList<Point2D.Double>>();
	}
	
	@Override
	public void startReadingPortData(int portIndex, ArrayList<Double> portData) {
		dataPoints.put(portIndex, portData);
	}

	@Override
	public void endReadingPortData(int portIndex) {}
	
	public ArrayList<Point2D.Double> getDataFromPort(Integer port) {
		return dataPoints.get(port);
	}

	@Override
	public void closePort(int portIndex) {
		
	}

	@Override
	public void openPort(int portIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectToSignalGenModule(AbstractSignalModule absModule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectSignalModule(AbstractSignalModule absModule) {
		// TODO Auto-generated method stub
		
	}
}