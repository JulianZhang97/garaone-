package backEnd;

import java.util.ArrayList;
import java.util.List;


/**
 * Calculates the race positions of this car at 100ms intervals. Returns an array of each position (in meters)
 * @author zhang
 *
 */
public class CalculateRace {
	//The quarter mile time for the car
	private double quarterTime;
	private double accelTime;
	private double quarterSpeed;

	private double distanceTo60;
	
	//Arrays detailing car progress at 100ms intervals
	private List<Double> carRace;


	public CalculateRace(Car car){
		//Time to complete quarter mile (400m)
	    this.quarterTime = car.getQuarterTime();

	    //Time to get to 60mph/96kph/~26.8m/s
		this.accelTime = car.getAccel();

		//Speed at quarter mile (400m) in mph
		this.quarterSpeed = car.getQuarterSpeed();
		
		carRace = new ArrayList<>();
	}

	
	public void calculateRaceResults(){
        //Distance vehicle travels before getting to 60mph in meters
        distanceTo60 = 13.4 * accelTime;
        double averageAccelTo60 = 26.8/ accelTime;

        //double distanceToQuarterFrom60 = 400 - distanceTo60;
        double timeToQuarterFrom60 = quarterTime - accelTime;

		double averageAccelAfter60 = ((quarterSpeed * 0.44704) - 26.8) / timeToQuarterFrom60;

		double curVelocity = 0.0;
		double curDistance = 0.0;
        //Separate into 100ms intervals
		while(curDistance < 400){
            if(curDistance < distanceTo60){ curVelocity += averageAccelTo60 * 0.01; }
            else{ curVelocity += averageAccelAfter60 * 0.01; }

            if(curDistance + curVelocity > 400){ curDistance = 400; }
			else{
			    curDistance += curVelocity; }
			carRace.add(curDistance);
		}
	}


	public List<Double> getRaceResults(){
		return carRace;
	}


	public Double getDistanceTo60(){return distanceTo60; }
}