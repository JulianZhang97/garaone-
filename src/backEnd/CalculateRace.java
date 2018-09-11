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
	private double carTime;
	
	//Car progress in meters (up to 400m)
	private double progress;
	
	//Arrays detailing car progress at 100ms intervals
	private List<Double> carRace;


	
	public CalculateRace(Car car){
		this.progress = 0.0;
		this.carTime = car.getQuarterTime();
		
		carRace = new ArrayList<>();
	}
	
	public void startRace(){
		
		while(progress != 400){
			double newProgress = 400/(carTime * 10);
			if(progress + newProgress > 400){
				progress = 400;
			}
			else{
				progress += newProgress;
			}	
			carRace.add(progress);
		}
	}

	public List<Double> getCarRace(){
		return carRace;
	}
}