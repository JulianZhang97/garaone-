package frontEnd;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import backEnd.Car;
import backEnd.CarDatabase;
import backEnd.calculateRace;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	Car vehicle1;
	Car vehicle2;
	
	CarDatabase db;
	
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
		
		startButton.setOnMousePressed((MouseEvent e) -> startRace(e));
		resetButton.setOnMousePressed((MouseEvent e) -> resetRace(e));
		
		car1Make.setOnMousePressed((MouseEvent e) -> loadMakes(e, 1));
		car2Make.setOnMousePressed((MouseEvent e) -> loadMakes(e, 2));
		
		car1Model.setOnMousePressed((MouseEvent e) -> loadModels(e, 1));
		car2Model.setOnMousePressed((MouseEvent e) -> loadModels(e, 2));
		
		car1Model.valueProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(car1Make.getValue() != null && car1Model.getValue() != null){
					
					String car1Brand = car1Make.getValue();
					String car1Name = car1Model.getValue();
				
					vehicle1 = db.getCar(car1Brand, car1Name);
		
					car1.setText(vehicle1.getYear() + " " + vehicle1.getMake() + " " + vehicle1.getModel());
					car1Trans.setText(vehicle1.getTrans());
					car1Drive.setText(vehicle1.getDrive());
				}
			}
		});
		
		car2Model.valueProperty().addListener(new ChangeListener<String>(){
			
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				
				if(car2Make.getValue() != null && car2Model.getValue() != null){
				
					String car2Brand = car2Make.getValue();
					String car2Name = car2Model.getValue();
				
					vehicle2 = db.getCar(car2Brand, car2Name);
				
					car2.setText(vehicle2.getMake() + " " + vehicle2.getModel());
					car2Trans.setText(vehicle2.getTrans());
					car2Drive.setText(vehicle2.getDrive());
				}
			}
		});
		
		drawInitialState(car1Status);
		drawInitialState(car2Status);
	}
	
	private void loadMakes(MouseEvent e, Integer select) {

		if(select == 2){
			car2Model.getItems().clear();
			car2Make.getItems().addAll(db.displayBrands());
		}
		else{
			car1Model.getItems().clear();
			car1Make.getItems().addAll(db.displayBrands());
		}
	}

	private void loadModels(MouseEvent e, Integer select) {

		if(select == 2){
			car2Model.getItems().clear();
			car2Model.getItems().addAll(db.displayModels(car2Make.getValue()));	
			}
		else{
			car1Model.getItems().clear();
			car1Model.getItems().addAll(db.displayModels(car1Make.getValue()));
		}
	}
	
	private void drawInitialState(Canvas canvas){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.strokeRoundRect(0, 0, 800, 50, 10, 10);
	}

	private void startRace(MouseEvent e) {
		if(vehicle1 == null || vehicle2 == null){
			try {
				throw new Exception("Cars cannot be null!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		GraphicsContext gc = car1Status.getGraphicsContext2D();
		GraphicsContext gc2 = car2Status.getGraphicsContext2D();
		
		
		
		calculateRace car1Race = new calculateRace(vehicle1);
		car1Race.startRace();
		List<Double> car1Results = car1Race.getCarRace();
		
		calculateRace car2Race = new calculateRace(vehicle2);
		car2Race.startRace();
		List<Double> car2Results = car2Race.getCarRace();
		
		Iterator<Double> car1Check = car1Results.listIterator();
		Iterator<Double> car2Check = car2Results.listIterator(); 
		
		Timer t = new Timer(); 
		
		t.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				
				if(!car1Check.hasNext() && !car2Check.hasNext()){
					t.cancel();
				}
				
				if(car1Check.hasNext()){
					Double nextPos = car1Check.next();
					gc.setStroke(Color.BLACK);
					gc.strokeRoundRect(0, 0, 800, 50, 10, 10);
					gc.setFill(Color.GREEN);
					gc.fillRect(0, 0, nextPos * 2, 50);
				}
				
				if(car2Check.hasNext()){
					Double nextPos2 = car2Check.next();
					gc2.setStroke(Color.BLACK);
					gc2.strokeRoundRect(0, 0, 800, 50, 10, 10);
					gc2.setFill(Color.GREEN);
					gc2.fillRect(0, 0, nextPos2 * 2, 50);
					}
				}
			}, 0, 100);	
	}
	
	private void resetRace(MouseEvent e) {
	}
}