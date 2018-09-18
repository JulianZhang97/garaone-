package backEnd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDatabase {
	
	public Connection connection;
	
	public CarDatabase() throws ClassNotFoundException {
		Class.forName("org.postgresql.Driver");		
	}
	
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
	
	public List<String> displayBrands(){
		List<String> carBrands = new ArrayList<String>();
		try{
			String allBrands = "select distinct make from cars order by make";
			PreparedStatement pStatement = connection.prepareStatement(allBrands);
			ResultSet rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				String curBrand = rs1.getString("make");
				carBrands.add(curBrand);
			}
			return carBrands;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return carBrands;
		}
	}
	
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
	
	public Car getCar(String make, String model){
		Car car;
		try{
			String searchCar = "select * from cars where make = ? and model = ?";
			PreparedStatement pStatement = connection.prepareStatement(searchCar);
			
			pStatement.setString(1, make);
			pStatement.setString(2, model);
			
			ResultSet rs1 = pStatement.executeQuery();
			
			Integer id = null;
			String curMake = null;
			String curModel = null;
			Integer curYear = null;
			String drive = null;
			String trans = null;
			Double accel = null;
			Double qMileTime = null;
			Double qMileSpeed = null;
			
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