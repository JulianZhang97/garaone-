package frontEnd;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import backEnd.CalculateRace;
import backEnd.Car;
import backEnd.CarDatabase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class MainDisplayController extends Control{
	
	@FXML 
	ComboBox<String> car1Make;
	@FXML 
	ComboBox<String> car1Model;
	@FXML 
	ComboBox<String> car2Make;
	@FXML 
	ComboBox<String> car2Model;
	@FXML
	Button startButton;
	@FXML
	Button resetButton;
	@FXML 
	Label winnerStatus;
	@FXML
	Label car1;
	@FXML
	Label car1Trans;
	@FXML
	Label car1Drive;
	@FXML
	Canvas car1Status;
	@FXML 
	Label car2;
	@FXML
	Label car2Trans;
	@FXML
	Label car2Drive;
	@FXML
	Canvas car2Status;
	@FXML
	Label car1StatusInfo;
	@FXML
	Label car2StatusInfo;
	
	private Car vehicle1;
	private Car vehicle2;
	
	private CarDatabase db;
	
	private Car winner;
	
	public void initialize() {
		db = null;
		try {
			db = new CarDatabase();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		db.connectDB();
		
		vehicle1 = null;
		vehicle2 = null;
		
		startButton.setOnMousePressed(e2 -> startRace());
		resetButton.setOnMousePressed(e1 -> resetRace());
		
		car1Make.setOnMousePressed((MouseEvent e) -> loadMakes(car1Make, car1Model));
		car2Make.setOnMousePressed((MouseEvent e) -> loadMakes(car2Make, car2Model));
		car1Model.setOnMousePressed((MouseEvent e) -> loadModels(car1Make, car1Model));
		car2Model.setOnMousePressed((MouseEvent e) -> loadModels(car2Make, car2Model));
		
		car1Model.valueProperty().addListener((arg0, arg1, arg2) -> {
		    if(car1Make.getValue() != null && car1Model.getValue() != null){
                vehicle1 = db.getCar(car1Make.getValue(), car1Model.getValue());
                setCar(vehicle1, car1Make.getValue(), car1Model.getValue(), car1, car1Trans, car1Drive);
            }
		});
		car2Model.valueProperty().addListener((arg0, arg1, arg2) -> {
            if(car2Make.getValue() != null && car2Model.getValue() != null){
                vehicle2 = db.getCar(car2Make.getValue(), car2Model.getValue());
                setCar(vehicle2, car2Make.getValue(), car2Model.getValue(), car2, car2Trans, car2Drive);
            }
        });
		drawInitialState(car1Status);
		drawInitialState(car2Status);
	}

	private void setCar(Car curVehicle, String carBrand, String carName, Label carNameLabel, Label carTrans, Label carDrive){
        carNameLabel.setText(carNameLabel.getText() + curVehicle.getYear() + " " + carBrand + " " + carName);
        carTrans.setText(carTrans.getText() + curVehicle.getTrans());
        carDrive.setText(carDrive.getText() + curVehicle.getDrive());
    }
	
	
	private void loadMakes(ComboBox<String> carMake, ComboBox<String> carModel) {
			carModel.getItems().clear();
			carMake.getItems().addAll(db.displayBrands());
	}

	private void loadModels(ComboBox<String> carMake, ComboBox<String> carModel) {
			carModel.getItems().clear();
			carModel.getItems().addAll(db.displayModels(carMake.getValue()));
	}
	
	private void drawInitialState(Canvas canvas){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.clearRect(0, 0, 800, 50);
		gc.strokeRoundRect(0, 0, 800, 50, 10, 10);
	}

	private void startRace() {
		if(vehicle1 == null || vehicle2 == null){
			try {
				throw new Exception("Cars cannot be null!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		GraphicsContext gc = car1Status.getGraphicsContext2D();
		GraphicsContext gc2 = car2Status.getGraphicsContext2D();
		
		CalculateRace car1Race = new CalculateRace(vehicle1);
		car1Race.startRace();
		List<Double> car1Results = car1Race.getCarRace();
		
		CalculateRace car2Race = new CalculateRace(vehicle2);
		car2Race.startRace();
		List<Double> car2Results = car2Race.getCarRace();
		
		Iterator<Double> car1Check = car1Results.listIterator();
		Iterator<Double> car2Check = car2Results.listIterator(); 
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			public void run(){
                if(car1Check.hasNext()){ carCheck(car1Check, gc, car1StatusInfo);}
                if(car2Check.hasNext()){ carCheck(car2Check, gc2, car2StatusInfo);}
				
				//If car 1 wins, set its winner status
				if(!car1Check.hasNext() && car2Check.hasNext()){winner = vehicle1; }
				//Else if car 2 wins, set its winner status
				if(car1Check.hasNext() && !car2Check.hasNext()){ winner = vehicle2; }
				
				if(!car1Check.hasNext() && !car2Check.hasNext()){
					t.cancel();
					Platform.runLater(() -> winnerStatus.setText(winnerStatus.getText() + " " + winner.getMake() + " " + winner.getModel() + " @ " + winner.getQuarterTime() + " seconds"));
				}
				}}, 0, 100);
	}

	private void carCheck(Iterator<Double> carCheck, GraphicsContext gc, Label carStatusInfo){
	    Double nextPos = carCheck.next();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, nextPos * 2, 50);
        Platform.runLater(() -> carStatusInfo.setText("Race Progress: " + nextPos.intValue() + "/400m"));
    }
	
	private void resetRace() {
		
		drawInitialState(car1Status);
		drawInitialState(car2Status);
		
		car1Make.getItems().clear();
		car1Model.getItems().clear();
		car2Make.getItems().clear();
		car2Model.getItems().clear();
		vehicle1 = null;
		vehicle2 = null;
		initialSetup();
	}
	
	private void initialSetup(){
		car1.setText(car1.getText().substring(0, 7));
		car1Trans.setText(car1Trans.getText().substring(0, 14));
		car1Drive.setText(car1Drive.getText().substring(0, 7));
		
		car2.setText(car2.getText().substring(0, 7));
		car2Trans.setText(car2Trans.getText().substring(0, 14));
		car2Drive.setText(car2Drive.getText().substring(0, 7));
		
		winnerStatus.setText("Winner is: ");
		car1StatusInfo.setText("Race Progress: 0/400m");
		car2StatusInfo.setText("Race Progress: 0/400m");
	}
}