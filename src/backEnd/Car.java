package backEnd;


public class Car {
	
	public static class carBuilder{
		private Integer id;
		private String make;
		private String model;
		private Integer year;
		private String drive;
		private String transmission;
		private double accel;
		private double quarterMileTime;
		private double quarterMileSpeed;
		
		public carBuilder id(Integer id){
			this.id = id;
			return this;
		}
		public carBuilder carMake(String make){
			this.make = make;
			return this;
		}
		public carBuilder carModel(String model){
			this.model = model;
			return this;
		}
		public carBuilder carYear(Integer year){
			this.year = year;
			return this;
		}
		
		public carBuilder carDrive(String drive){
			this.drive = drive;
			return this;
		}
		public carBuilder carTrans(String transmission){
			this.transmission = transmission;
			return this;
		}
		public carBuilder carAccel(double accel){
			this.accel = accel;
			return this;
		}
		public carBuilder carqMileTime(double quarterMileTime){
			this.quarterMileTime = quarterMileTime;
			return this;
		}
		public carBuilder carqMileSpeed(double quarterMileSpeed){
			this.quarterMileSpeed = quarterMileSpeed;
			return this;
		}
		public Car build(){
			return new Car(this);
		}
	}
	
	private Integer id;
	private String make;
	private String model;
	private Integer year;
	private String drive;
	private String transmission;
	private double accel;
	private double quarterMileTime;
	private double quarterMileSpeed;
	
	private Car(carBuilder builder){
		this.id = builder.id;
		this.make = builder.make;
		this.model = builder.model;
		this.year = builder.year;
		this.drive = builder.drive;
		this.transmission = builder.transmission;
		this.accel = builder.accel;
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
	public double getAccel(){
		return this.accel;
	}
	public double getQuarterTime(){
		return this.quarterMileTime;
	}
	public double getQuarterSpeed(){
		return this.quarterMileSpeed;
	}
}
