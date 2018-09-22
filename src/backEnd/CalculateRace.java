package backEnd;

import java.util.ArrayList;
import java.util.List;


/**
 * Calculates the race positions of this car at 100ms intervals. Returns an array of each position (in meters)
 * @author zhang
 *
 */
public class CalculateRace {
	private double quarterTime;
	private double accelTime;
	private double quarterSpeed;
	private double distanceTo96;
	
	//Arrays detailing car progress at 100ms intervals
	private List<Double> carRaceDistances;


    /**
     * Class for calculating the race progress of a vehicle given its acceleration information
     *
     * @param car The car to calculate race performance for
     */
	public CalculateRace(Car car){
		//Time to complete quarter mile (400m)
	    this.quarterTime = car.getQuarterTime();
	    //Time to get to 60mph/96kph/~26.8m/s
		this.accelTime = car.getAcceleration();
		//Speed at quarter mile (400m) in mph
		this.quarterSpeed = car.getQuarterSpeed();
		
		carRaceDistances = new ArrayList<>();
	}


    /**
     * Calculates average vehicle acceleration to 96km/h (60mph) and beyond 96km/h (60mph) to 400 meters and uses these
     * pieces of information to calculate estimated distances covered by the vehicle from race start to completion
     * in 100ms increments, and sets the list accordingly.
     *
     */
	public void calculateRaceResults(){
        //Distance vehicle travels before getting to 60mph in meters
        distanceTo96 = 13.4 * accelTime;

        double averageAccelTo60 = 26.8/ accelTime;
        double timeToQuarterFrom60 = quarterTime - accelTime;
		double averageAccelAfter60 = ((quarterSpeed * 0.44704) - 26.8) / timeToQuarterFrom60;

		double curVelocity = 0.0;
		double curDistance = 0.0;
        //Separate into 100ms intervals
		while(curDistance < 400){
            if(curDistance < distanceTo96){ curVelocity += averageAccelTo60 * 0.01; }
            else{ curVelocity += averageAccelAfter60 * 0.01; }

            if(curDistance + curVelocity > 400){ curDistance = 400; }
			else{
			    curDistance += curVelocity; }
			carRaceDistances.add(curDistance);
		}
	}


    /**
     * Gets the complete race results of a vehicle as list of distances in 100ms increments
     *
     * @return The race results as a list of meter distances
     */
	public List<Double> getRaceResults(){
		return carRaceDistances;
	}

	/**
	 * Getter for the distance (in meters) this vehicle takes to reach 96km/h (60mph)
	 *
	 * @return The distance (in meters) for this vehicle to get to speed
	 */
	public Double getDistanceTo96(){return distanceTo96; }
}