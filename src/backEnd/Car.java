package backEnd;

/**
 * The class representing a specific vehicle, containing all of its
 * specifications and performance data for both display and
 * race calculating purposes
 *
 * NOTE: The static class carBuilder appears here due to this
 * class utilization of the Builder pattern to minimize data
 * entry errors during initialization
 *
 */
public class Car {
	
	public static class carBuilder{
		private Integer id;
		private String make;
		private String model;
		private Integer year;
		private String drive;
		private String transmission;
		private double acceleration;
		private double quarterMileTime;
		private double quarterMileSpeed;
		
		carBuilder id(Integer id){
			this.id = id;
			return this;
		}
		carBuilder carMake(String make){
			this.make = make;
			return this;
		}
		carBuilder carModel(String model){
			this.model = model;
			return this;
		}
		carBuilder carYear(Integer year){
			this.year = year;
			return this;
		}
		carBuilder carDrive(String drive){
			this.drive = drive;
			return this;
		}
		carBuilder carTrans(String transmission){
			this.transmission = transmission;
			return this;
		}
		carBuilder carAccel(double accel){
			this.acceleration = accel;
			return this;
		}
		carBuilder carqMileTime(double quarterMileTime){
			this.quarterMileTime = quarterMileTime;
			return this;
		}
		carBuilder carqMileSpeed(double quarterMileSpeed){
			this.quarterMileSpeed = quarterMileSpeed;
			return this;
		}
		Car build(){
			return new Car(this);
		}
	}
	
	private Integer id;
	private String make;
	private String model;
	private Integer year;
	private String drive;
	private String transmission;
	private double acceleration;
	private double quarterMileTime;
	private double quarterMileSpeed;
	
	private Car(carBuilder builder){
		this.id = builder.id;
		this.make = builder.make;
		this.model = builder.model;
		this.year = builder.year;
		this.drive = builder.drive;
		this.transmission = builder.transmission;
		this.acceleration = builder.acceleration;
		this.quarterMileTime = builder.quarterMileTime;
		this.quarterMileSpeed = builder.quarterMileSpeed;
	}

	public Integer getId(){
		return this.id;
	}
	public String getMake(){
		return this.make;
	}
	public String getModel(){
		return this.model;
	}
	public Integer getYear(){
		return this.year;
	}
	public String getDrive(){
		return this.drive;
	}
	public String getTrans(){
		return this.transmission;
	}
	public double getAcceleration(){
		return this.acceleration;
	}
	public double getQuarterTime(){
		return this.quarterMileTime;
	}
	public double getQuarterSpeed(){
		return this.quarterMileSpeed;
	}
}
