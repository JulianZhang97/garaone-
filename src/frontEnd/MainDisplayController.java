package frontEnd;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class MainDisplayController {
	
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
	public void initialize() {
		startButton.setOnMousePressed((MouseEvent e) -> startRace(e));
		resetButton.setOnMousePressed((MouseEvent e) -> resetRace(e));
	}

	private void resetRace(MouseEvent e) {
		
	}

	private void startRace(MouseEvent e) {
		
	}

}
