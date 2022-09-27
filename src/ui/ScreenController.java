package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScreenController {
	
	private MooreMachineController mooreController;
	
	public ScreenController() {
		mooreController = new MooreMachineController();
	}
	
	@FXML
    public void selectMealyMachine(ActionEvent event) {
		
    }

    @FXML
    public void selectMooreMachine(ActionEvent event) {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MooreMachineStatesScreen.fxml"));
		
		fxmlLoader.setController(mooreController);
		
		try{
			Parent root = fxmlLoader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Moore Machine");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
