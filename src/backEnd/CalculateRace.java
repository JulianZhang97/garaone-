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
	
	boolean complete;
	
//	public final Lock lock;
//	public final Condition done;
	
	public CalculateRace(Car car){
		this.progress = 0.0;
		this.carTime = car.getQuarterTime();
		
		carRace = new ArrayList<Double>();
		
		//this.lock = new ReentrantLock();
		//this.done = lock.newCondition();
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
/*
	public static void main(String[] args) throws InterruptedException {
		Car testCar = new Car.carBuilder().id(1).carMake("Audi").carModel("A4")
		.carYear(2001).carDrive("AWD").carTrans("5M").carAccel(7.8)
		.carqMileTime(15.3).carqMileSpeed(85).build();
		
		calculateRace race = new calculateRace(testCar);
		race.startRace();
		race.lock.lock();
		
		try{
			//System.out.println("Waiting on timer");
			race.done.await();
			
			//System.out.println("Signaled here!");
		
			List<Double> results = race.getCarRace();
			System.out.println("Array size was " + results.size());
			for(int i = 0; i < results.size(); i ++){
				System.out.println(results.get(i));
			}
		}
		finally{
			race.lock.unlock();
		}
	}
	*/
}