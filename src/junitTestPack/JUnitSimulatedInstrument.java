package junitTestPack;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Test;

import simulatorPack.SimulatedInstrumentModule;
import simulatorPack.TestRackSimulator1;


public class JUnitSimulatedInstrument {
	@Test
	public void main() {
		TestRackSimulator1 sim1 = new TestRackSimulator1("Sim1", "Time in nanosec","Energy in mdb", 4);
		SimulatedInstrumentModule ins1 = new SimulatedInstrumentModule("Ins1", sim1);
		ins1.configureConnection();
		sim1.configureConnection();
		sim1.enableDataGeneration();
		sim1.enableReadingFromAllPorts();
		sim1.getDataFromDevice();
		sim1.transmitDataToAllListenersAtAllPorts();
		sim1.enableReadingFromAllPorts();
		ins1.getDataFromDevice();
		
		ArrayList<Point2D.Double> dataPoint = ins1.getDataFromPort(0);
		for (Point2D.Double dp : dataPoint) {
			System.out.print("x is: ");
			System.out.print(dp.getX());
			System.out.print(" y is: ");
			System.out.print(dp.getY());
			System.out.println("");
		}
		
		sim1.disableDataGeneration();
		sim1.disableReadingFromAllPorts();
		
		sim1.enableDataGeneration();
	//	sim1.enableReadingFromAllPorts();
		sim1.getDataFromDevice();
	//	ins1.pullDataFromAllSignalModulePort(sim1);
		ins1.getDataFromDevice();
		
		ArrayList<Point2D.Double> dataPoint1 = ins1.getDataFromPort(0);
		for (Point2D.Double dp : dataPoint1) {
			System.out.print("x is: ");
			System.out.print(dp.getX());
			System.out.print(" y is: ");
			System.out.print(dp.getY());
			System.out.println("");
		}
		
		
	}
	

}
