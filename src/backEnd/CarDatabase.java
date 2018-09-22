package backEnd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The class for the database holding all the vehicle data
 *
 */
public class CarDatabase {

    /** The connection between the Java program client and the database server*/
	private Connection connection;


	public CarDatabase() throws ClassNotFoundException {
		Class.forName("org.postgresql.Driver");		
	}


    /**
     * The method called when first starting the program to initialize the client database connection
     *
     */
	public void connectDB() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to find the JDBC driver");
		}

		try {
			String url = "jdbc:postgresql://baasu.db.elephantsql.com:5432/djutedbs?currentSchema=carperf";
			String username = "djutedbs";
			String password = "rops3VUH3ObTAd3CGxjYBHSwVz1YGw1O";
			connection = DriverManager.getConnection(url, username, password);

		} catch (SQLException se) {
			System.err.println("SQL Exception." + "<Message>: " + se.getMessage());
		}

	}


    /**
     * Returns the makes representing all vehicles stored in the database
     *
     * @return A list of all vehicle makes stored on the database
     */
	public List<String> displayMakes(){
		List<String> carMakes = new ArrayList<String>();
		try{
			String allMakes = "select distinct make from cars order by make";
			PreparedStatement pStatement = connection.prepareStatement(allMakes);
			ResultSet rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				String curMakes = rs1.getString("make");
				carMakes.add(curMakes);
			}
			return carMakes;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return carMakes;
		}
	}


    /**
     * Returns all vehicle model names of a given vehicle make stored in the database
     *
     * @param brand The brand to list the vehicle models from
     * @return A list of all vehicle models of the brand in the database
     */
	public List<String> displayModels(String brand){
		List<String> brandModels = new ArrayList<String>();
		try{
			String allBrandModels = "select model from cars where make = ? order by model";
			PreparedStatement pStatement = connection.prepareStatement(allBrandModels);
			pStatement.setString(1, brand);
			
			ResultSet rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				String curModel = rs1.getString("model");
				brandModels.add(curModel);
			}
			return brandModels;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return brandModels;
		}
	}


    /**
     * Gets all the specifications and performance information for a specific vehicle, given a make and model,
     * initializes a Car object, and populates the Cae with all the information of this vehicle
     *
     * @param make The make of the vehicle requested
     * @param model The model of the vehicle requested
     * @return A Car with specifications and performance data, representing this specific make and model vehicle
     */
	public Car getCar(String make, String model){
		Car car;
		try{
			String searchCar = "select * from cars where make = ? and model = ?";
			PreparedStatement pStatement = connection.prepareStatement(searchCar);
			
			pStatement.setString(1, make);
			pStatement.setString(2, model);
			
			ResultSet rs1 = pStatement.executeQuery();
			
			Integer id, curYear;
			String curMake, curModel, drive, trans;
			Double accel, qMileTime, qMileSpeed;

			id = curYear = null;
			curMake = curModel = drive = trans = null;
			accel = qMileTime = qMileSpeed = null;
			
			while(rs1.next()){
		
			id = rs1.getInt("id");
			curMake = rs1.getString("make");
			curModel = rs1.getString("model");
			curYear = rs1.getInt("model_year");
			drive = rs1.getString("drive");
			trans = rs1.getString("transmission");
			accel = rs1.getDouble("accel");
			qMileTime = rs1.getDouble("quarter_mile_time");
			qMileSpeed = rs1.getDouble("quarter_mile_speed");
			}
			car = new Car.carBuilder().id(id).carMake(curMake).carModel(curModel)
					.carYear(curYear).carDrive(drive).carTrans(trans).carAccel(accel)
					.carqMileTime(qMileTime).carqMileSpeed(qMileSpeed).build();
			
			return car;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * Disconnects the client from the database upon the session concluding
     *
     * @return whether the disconnection was successful
     */
	public boolean disconnectDB() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}