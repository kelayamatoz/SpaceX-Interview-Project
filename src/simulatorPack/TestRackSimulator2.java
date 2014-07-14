package simulatorPack;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import abstractModulePack.AbstractSignalModule;

/**
 * TestRackSimulator1 simulates a testrack that: 
 * 
 * 1. outputs data to one port out of four at one time.
 * 
 * 2. starts output data at -10dbm.
 * 
 * 3. after every measurement, the output power is decremented by 2dbm.
 * 
 * 4. stops outputting power at -30dbm.
 * 
 * 5. is connected to a switch box that will randomly change its output port after the measurement device
 * 	takes one measurement.
 * 
 * Assumptions: 
 * 
 * 1. Assume the signal goes through an 1 Ohm resistor.
 * 
 * 2. There is a random noise that ranges from 0 to 0.001.
 * @author tianzhao
 *
 */

public class TestRackSimulator2 extends AbstractSignalModule{
	private final double maxEnergy = -10,
						 minEnergy = -30,
						 step = 2,
						 frequencyInGHz = 2.4,
						 timeStepInNanosec = 0.025,
						 noiseRange = 0.001;
	private double lastEnergy,
				   lastTimeInNanosec,
				   omegaInGRad; 
	private int switchBoxIndex = 1; // emulates a switch box
	private ArrayList<ArrayList<Point2D.Double>> pointsArray;
	
	public TestRackSimulator2(String signalGeneratorName, String xAxisName,	String yAxisName, int numOfPorts) {
		super(signalGeneratorName, xAxisName, yAxisName, numOfPorts);
		super.disableReadingFromAllPorts();
		lastEnergy = maxEnergy;
		lastTimeInNanosec = 0;
		omegaInGRad = 2 * Math.PI * frequencyInGHz;
		
		/* Sets up the structure for reading data */
		pointsArray = new ArrayList<ArrayList<Point2D.Double>>();
		for (int i = 0; i < 4; i ++) {
			pointsArray.add(new ArrayList<Point2D.Double>());
		}
	}
	
	public TestRackSimulator2() {
		super("Simulator 2", "Time in nanosecond", "Amplitude in volts", 4);
		lastEnergy = maxEnergy;
		lastTimeInNanosec = 0;
		omegaInGRad = 2 * Math.PI * frequencyInGHz;
		
		/* Sets up the structure for reading data */
		pointsArray = new ArrayList<ArrayList<Point2D.Double>>();
		for (int i = 0; i < 4; i ++) {
			pointsArray.add(new ArrayList<Point2D.Double>());
		}
	}

	/** 
	 * This function simulates the process of configuring data connection
	 * between the testrack and the software
	 */
	@Override
	public boolean configureConnection() {
		/* 
		 * Simply let the current thread sleep for 300 ms to simulate the 
		 * time needed for establishing connection
		 */
		try {
			Thread.currentThread();
			Thread.sleep(300);
		} catch (InterruptedException e) {
			System.out.println("Error: coule not put current thread to sleep");
			e.printStackTrace();
		}
		
		return true;
	}

	/**
	 * This function simulates the procedure of getting data from 
	 * a real signal generator. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean getDataFromDevice() {
		/* 
		 * Simply let the current thread sleep for 300 ms to simulate the 
		 * time needed for getting data from the signal generator
		 */
		if ((!this.canGenerateData() || lastEnergy < minEnergy)) {
			this.disableReadingFromAllPorts();
			return false;
		}
		
		// switch to a random port every time the signal generator generates data
		Random r = new Random();
		this.switchBoxIndex = r.nextInt(4);
				
		
		try {
			Thread.currentThread();
			Thread.sleep(300);
		} catch (InterruptedException e) {
			System.out.println("Error: could not put current thread to sleep");
			e.printStackTrace();
		}
		
		double sineWaveEnergy = lastEnergy;
		System.out.print("Energy = ");
		System.out.println(sineWaveEnergy);
		double amplitude = Math.sqrt(2 * Math.pow(10, ((sineWaveEnergy - 30) / 10)));
		/*
		 *  Generates a CW tone at 2.4GHz. The average power of the 
		 * CW tone is lastEnergy. 
		 * I plan to generate 20 points per one period. 
		 */
		for	(int i = 0; i < this.getDataBufferSize(); i ++) {
			double voltage = amplitude * Math.sin(omegaInGRad * lastTimeInNanosec) + Math.random() * noiseRange;
			pointsArray.get(switchBoxIndex).add(new Point2D.Double(lastTimeInNanosec, voltage));
			lastTimeInNanosec += timeStepInNanosec;
			/* simulating as if it takes 1 mils to read a data point from the signal generator */
			try {
				Thread.currentThread();
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Error: coule not put current thread to sleep");
				e.printStackTrace();
			}
		}
		
		this.prepareDataForReading(switchBoxIndex, (ArrayList<Point2D.Double>) pointsArray.get(switchBoxIndex).clone());
		this.disableReadingFromAllPorts();
		this.enableReadingFromPort(switchBoxIndex);
		
		// reset for reading new points
		for (int k = 0; k < 4; k ++) {
			pointsArray.get(k).clear();
		}
		
		lastEnergy -= step;
		return true;
	}

	@Override
	public boolean reset() {
		this.lastEnergy = this.maxEnergy;
		this.lastTimeInNanosec = 0;
		
		return true;
	}
}
