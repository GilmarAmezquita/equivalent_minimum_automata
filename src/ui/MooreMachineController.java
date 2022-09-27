package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MooreMachineController {
	
	@FXML
    private Label focusLabel;
	
	public MooreMachineController() {
		
	}
	
	private void initialize() {
		focusLabel.requestFocus();	
		System.out.println("yes");
	}
}
