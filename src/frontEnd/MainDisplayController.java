package frontEnd;

import backEnd.CalculateRace;
import backEnd.Car;
import backEnd.CarDatabase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The controller for the main GaraOne program display, responsible for all dynamic labels, as well as the live display
 * and update of race progress and acceleration time.
 *
 */
public class MainDisplayController extends Control{

    /** The selector for the first vehicle make */
	@FXML 
	ComboBox<String> car1MakeSelector;
    /** The selector for the first vehicle model */
	@FXML 
	ComboBox<String> car1ModelSelector;
    /** The selector for the second vehicle make */
	@FXML 
	ComboBox<String> car2MakeSelector;
	/** The selector for the second vehicle model */
	@FXML 
	ComboBox<String> car2ModelSelector;
	@FXML
	Button startButton;
	@FXML
	Button resetButton;
	/** The label displaying the time for the winning vehicle */
	@FXML 
	Label winnerStatus;
    /** Label for the first vehicle make and model */
	@FXML
	Label car1Name;
	/** Label for the first vehicle transmission type (# = number of speeds in transmission, M = manual, A = auto) */
	@FXML
	Label car1GearBox;
	/** Label for the first vehicle drivetrain type */
	@FXML
	Label car1DriveTrain;
	/** The progress bar of the first vehicle displaying its race progress */
	@FXML
	Canvas car1Status;
    /** Label for the second vehicle make and model */
	@FXML 
	Label car2Name;
    /** Label for the second vehicle transmission type (# = number of speeds in transmission, M = manual, A = auto) */
	@FXML
	Label car2Trans;
    /** Label for the second vehicle drivetrain type */
	@FXML
	Label car2Drive;
    /** The progress bar of the first vehicle displaying its race progress */
	@FXML
	Canvas car2Status;

	/** Label representing the first vehicle's progress during the race */
	@FXML
	Label car1RaceProgress;
    /** Label representing the second vehicle's progress during the race */
	@FXML
	Label car2RaceProgress;
	/** Label representing the first vehicle's acceleration time (to 60mph/96km/h) during the race */
	@FXML
	Label car1AccelerationTime;
    /** Label representing the second vehicle's acceleration time (to 60mph/96km/h) during the race */
	@FXML
	Label car2AccelerationTime;

	/** The first vehicle for the race */
	private Car vehicle1;
    /** The second vehicle for the race */
	private Car vehicle2;
	/** The database to get vehicle information and data from */
	private CarDatabase db;
	/** The winning vehicle, set when one vehicle crosses the finish line */
	private Car winner;
	/** The timer to update race progress and acceleration timing in real time*/
	private Timer t;

    private final String defaultGearBox = "Transmission: ";
    private final String defaultDriveTrain = "Drivetrain: ";
    private final String defaultRaceProgress = "Race Progress: 0/400m";
    private final String defaultAcceleration = "0-96km/h Time: 0.0s";

    private double elapsedTime;


    /**
     * Connects the program to the database and initializes the GUI functions
     */
	public void initialize() {
		db = null;
		try {
			db = new CarDatabase();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		db.connectDB();
		initializeGUIFunctions();
	}


    /**
     * Loads the race start and reset button functions as well as the drop down selector functions
     *
     */
	private void initializeGUIFunctions(){
        vehicle1 = vehicle2 = null;

        startButton.setOnMousePressed(e2 -> startRace());
        resetButton.setOnMousePressed(e1 -> resetRace());

        car1MakeSelector.setOnMousePressed((MouseEvent e) -> loadMakes(car1MakeSelector, car1ModelSelector));
        car2MakeSelector.setOnMousePressed((MouseEvent e) -> loadMakes(car2MakeSelector, car2ModelSelector));
        car1ModelSelector.setOnMousePressed((MouseEvent e) -> loadModels(car1Name, car1GearBox, car1DriveTrain, car1MakeSelector, car1ModelSelector));
        car2ModelSelector.setOnMousePressed((MouseEvent e) -> loadModels(car2Name, car2Trans, car2Drive, car2MakeSelector, car2ModelSelector));

        car1ModelSelector.valueProperty().addListener((arg0, arg1, arg2) -> {
            if(car1MakeSelector.getValue() != null && car1ModelSelector.getValue() != null){
                vehicle1 = db.getCar(car1MakeSelector.getValue(), car1ModelSelector.getValue());
                setCarInfo(vehicle1, car1Name, car1GearBox, car1DriveTrain);
            }
        });
        car2ModelSelector.valueProperty().addListener((arg0, arg1, arg2) -> {
            if(car2MakeSelector.getValue() != null && car2ModelSelector.getValue() != null){
                vehicle2 = db.getCar(car2MakeSelector.getValue(), car2ModelSelector.getValue());
                setCarInfo(vehicle2, car2Name, car2Trans, car2Drive);
            }
        });
        drawInitialState(car1Status);
        drawInitialState(car2Status);
    }


    /**
     * Sets the vehicle name and information labels
     *
     * @param curVehicle The vehicle to set the labels for
     * @param carNameLabel The label displaying the vehicle make and model
     * @param carGearBoxLabel The label displaying the vehicle transmission
     * @param carDriveLabel The label displaying the vehicle drivetrain
     */
	private void setCarInfo(Car curVehicle, Label carNameLabel, Label carGearBoxLabel, Label carDriveLabel){
        carNameLabel.setText(carNameLabel.getText() + curVehicle.getYear() + " " +
                curVehicle.getMake() + " " + curVehicle.getModel());
        carGearBoxLabel.setText(carGearBoxLabel.getText() + curVehicle.getTrans());
        carDriveLabel.setText(carDriveLabel.getText() + curVehicle.getDrive());
    }


    /**
     * Populates a car make selector ComboBox with all database vehicle makes
     *
     * @param carMakeSelector The ComboBox selector to populate with makes
     * @param carModelSelector The ComboBox car model selector
     */
	private void loadMakes(ComboBox<String> carMakeSelector, ComboBox<String> carModelSelector) {
			carModelSelector.getItems().clear();
			carMakeSelector.getItems().addAll(db.displayMakes());
	}


    /**
     * Populates a car model selector ComboBox with all database vehicle models for a specific vehicle brand.
     *
     *
     * @param carNameLabel The label displaying the vehicle make and model
     * @param carGearBoxLabel The label displaying the car transmission information
     * @param carDriveLabel The label displaying the car drivetrain information
     * @param carMakeSelector The dropdown ComboBox for listing car makes
     * @param carModelSelector The  dropdown ComboBox for listing car models
     */
	private void loadModels(Label carNameLabel, Label carGearBoxLabel, Label carDriveLabel, ComboBox<String> carMakeSelector, ComboBox<String> carModelSelector) {
	        carNameLabel.setText(carNameLabel.getText().substring(0, 11));
	        carGearBoxLabel.setText(defaultGearBox);
            carDriveLabel.setText(defaultDriveTrain);

			carModelSelector.getItems().clear();
			carModelSelector.getItems().addAll(db.displayModels(carMakeSelector.getValue()));
	}


    /**
     * Initializes the visual race progress bar for a vehicle.
     *
     * @param canvas The canvas on which the race progress bar is drawn
     */
	private void drawInitialState(Canvas canvas){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.clearRect(0, 0, 800, 50);
		gc.strokeRoundRect(0, 0, 800, 50, 10, 10);
	}


    /**
     * Gets both vehicles' race progress in 100ms increments, and then runs through these races in real time,
     * displaying race progress and acceleration information, and then displaying the race winner information upon
     * both vehicles completing the race.
     *
     */
	private void startRace() {
		if(vehicle1 == null || vehicle2 == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Must select two cars to race!", ButtonType.OK);
            alert.setHeaderText("No car selected!");
            alert.showAndWait();
		}
		else{
            car1MakeSelector.setDisable(true);
            car1ModelSelector.setDisable(true);
            car2MakeSelector.setDisable(true);
            car2ModelSelector.setDisable(true);
            startButton.setDisable(true);

            CalculateRace car1RaceCalculator = new CalculateRace(vehicle1);
            CalculateRace car2RaceCalculator = new CalculateRace(vehicle2);
            GraphicsContext gc = car1Status.getGraphicsContext2D();
            GraphicsContext gc2 = car2Status.getGraphicsContext2D();

            car1RaceCalculator.calculateRaceResults();
            car2RaceCalculator.calculateRaceResults();
            Iterator<Double> car1Check = car1RaceCalculator.getRaceResults().listIterator();
            Iterator<Double> car2Check = car2RaceCalculator.getRaceResults().listIterator();

            t = new Timer();
            t.scheduleAtFixedRate(new TimerTask(){
                public void run(){
                    incrementTimer();

                    if(car1Check.hasNext()){
                        updateRaceProgress(car1RaceCalculator, car1Check, gc, car1RaceProgress, car1AccelerationTime);}
                    if(car2Check.hasNext()){ updateRaceProgress(car2RaceCalculator, car2Check, gc2, car2RaceProgress, car2AccelerationTime);}
                    //If car 1 wins, set its winner status
                    if(!car1Check.hasNext() && car2Check.hasNext()){winner = vehicle1; }
                    //Else if car 2 wins, set its winner status
                    if(car1Check.hasNext() && !car2Check.hasNext()){ winner = vehicle2; }
                    //If a car won
                    if(!car1Check.hasNext() && !car2Check.hasNext()){
                        t.cancel();
                        Platform.runLater(() -> showWinner()); }
                }}, 0, 100);
        }
	}


    /**
     * Updates the acceleration time and race progress displays during the race
     *
     * @param raceInfo The
     * @param carCheck The list of race progress figures
     * @param gc The graphics canvas for displaying race progress
     * @param raceStatusInfoLabel The label showing the vehicle race status
     * @param carAccelLabel The label showing vehicle acceleration
     */
	private void updateRaceProgress(CalculateRace raceInfo, Iterator<Double> carCheck, GraphicsContext gc, Label raceStatusInfoLabel, Label carAccelLabel){
	    Double nextPos = carCheck.next();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, nextPos * 2, 50);

        Platform.runLater(() -> raceStatusInfoLabel.setText("Race Progress: " + nextPos.intValue() + "/400m"));
        if(nextPos <= raceInfo.getDistanceTo96()){
            Platform.runLater(() -> carAccelLabel.setText("0-96km/h Time: " +
                    new DecimalFormat("#.0").format(elapsedTime) + "s"));
        }
    }


    /**
     * Displays the winner race information (race time and speed) and displays an alert for the user
     *
     */
    private void showWinner(){
        String winnerInfo =  winner.getMake() + " " + winner.getModel() + "\n" + winner.getQuarterTime() + " seconds @ " + winner.getQuarterSpeed() + " mph";
        winnerStatus.setText(winnerStatus.getText() + " " + winnerInfo);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, winnerInfo, ButtonType.OK);
        alert.setHeaderText(winner.getMake() + " " + winner.getModel() + " has won!");
        alert.showAndWait();
    }


    /**
     * Clears all vehicle and label information and stops a race if it is in progress
     *
     */
	private void resetRace() {
	    if(t != null){
	        t.cancel();
        }
        elapsedTime = 0.0;

        car1MakeSelector.setDisable(false);
        car1ModelSelector.setDisable(false);
        car2MakeSelector.setDisable(false);
        car2ModelSelector.setDisable(false);
        startButton.setDisable(false);
		
		drawInitialState(car1Status);
		drawInitialState(car2Status);
		
		car1MakeSelector.getItems().clear();
		car1ModelSelector.getItems().clear();
		car2MakeSelector.getItems().clear();
		car2ModelSelector.getItems().clear();

		vehicle1 = vehicle2 = null;
		resetLabels();
	}


    /**
     * Helper function for the race reset function for clearing vehicle information labels
     *
     */
	private void resetLabels(){
		car1Name.setText(car1Name.getText().substring(0, 11));
		car1GearBox.setText(defaultGearBox);
		car1DriveTrain.setText(defaultDriveTrain);
		car1AccelerationTime.setText(defaultAcceleration);

		car2Name.setText(car2Name.getText().substring(0, 11));
		car2Trans.setText(defaultGearBox);
		car2Drive.setText(defaultDriveTrain);
        car2AccelerationTime.setText(defaultAcceleration);

		winnerStatus.setText("Winner is: ");
		car1RaceProgress.setText(defaultRaceProgress);
		car2RaceProgress.setText(defaultRaceProgress);
	}


    private void incrementTimer(){ elapsedTime += 0.1; }
}