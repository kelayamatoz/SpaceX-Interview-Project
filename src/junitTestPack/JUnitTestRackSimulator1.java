package junitTestPack;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;

import simulatorPack.TestRackSimulator1;

import java.awt.geom.Point2D;

public class JUnitTestRackSimulator1 {

	@Test
	public void main(){
		TestRackSimulator1 sim1 = new TestRackSimulator1("Sim1", "Time in nanosec","Energy in mdb", 4);
		Set<Integer> portSet = sim1.getIndicesOfAllPorts();
		sim1.configureConnection();
		sim1.enableDataGeneration();
		sim1.enableReadingFromAllPorts();
		sim1.getDataFromDevice();
		ArrayList<Point2D.Double> dataPoint = sim1.getDataFromPort((Integer)portSet.toArray()[0]);
		for (Point2D.Double dp : dataPoint) {
			System.out.print("x is: ");
			System.out.print(dp.getX());
			System.out.print(" y is: ");
			System.out.print(dp.getY());
			System.out.println("");
		}
		
		sim1.getDataFromDevice();
		dataPoint = sim1.getDataFromPort((Integer)portSet.toArray()[0]);
		
		for (Point2D.Double dp : dataPoint) {
			System.out.print("x is: ");
			System.out.print(dp.getX());
			System.out.print(" y is: ");
			System.out.print(dp.getY());
			System.out.println("");
		}
	}
}
