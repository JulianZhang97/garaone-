package frontEnd;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RaceDisplay extends Application{

	@Override
	public void start(Stage primaryStage){
		 primaryStage.setTitle("GaraOne");
		 
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("MainDisplay.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
	
	        
	     primaryStage.setScene(scene);
	     primaryStage.setMaximized(true);
	     primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
