package ui;

import model.Mealy.*;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MealyMachineController {
	
	private final static String CHAR_SEPARATOR = ",";
	

    @FXML
    private TextArea txtReducedStateTable;

    @FXML
    private TextArea txtResultStateTable;
	
	@FXML
    private TextField alfabetField;

    @FXML
    private TextField statesField;
    
    @FXML
    private Button buttonOkTransition;

    @FXML
    private ComboBox<String> comboAlphabetInput;

    @FXML
    private ComboBox<String> comboAlphabetOutput;

    @FXML
    private ComboBox<String> comboStates;

    @FXML
    private ComboBox<String> comboTransition;

    @FXML
    private TextArea txtTransition;
    
    private List<String> states;
    
    private List<String> alphabet;
    
    private String[][] stateTable;
    
    private String[][] transitions;
	
    private Machine mealyMachine;
    
	public MealyMachineController() {
		
	}
	
	@FXML
    public void addMachineOk(ActionEvent event) {
		
		if(!alfabetField.getText().isEmpty() && !statesField.getText().isEmpty()) {
			
			states = Arrays.asList(statesField.getText().split(CHAR_SEPARATOR));
			alphabet = Arrays.asList(alfabetField.getText().split(CHAR_SEPARATOR));
			
			openTransitionScreen();
			
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText(null);
    		alert.setTitle("Campos vacíos");
    		alert.setContentText("Por favor digita la información solicitada.");
    		alert.showAndWait();
		}
    }
	private void openTransitionScreen() {

		Stage transitionState = (Stage) statesField.getScene().getWindow();
		transitionState.close();
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TransitionMealyMachineScreen.fxml"));
		
		fxmlLoader.setController(this);
		
		try{
			Parent root = fxmlLoader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Moore Machine");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
			loadInfoTransitionScreen();
			prepareStateTable();
			printStateTable();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadInfoTransitionScreen() {
		ObservableList<String> states = FXCollections.observableList(this.states);
		ObservableList<String> alphabet = FXCollections.observableList(this.alphabet);
		
		comboTransition.setItems(states);
		comboStates.setItems(states);
		comboAlphabetOutput.setItems(alphabet);
		comboAlphabetInput.setItems(alphabet);
	}
	@FXML
    public void addTransition(ActionEvent event) {
		if(comboAlphabetInput.getSelectionModel().getSelectedItem() != null && comboStates.getSelectionModel().getSelectedItem() != null &&
    			comboTransition.getSelectionModel().getSelectedItem() != null && comboAlphabetOutput.getSelectionModel().getSelectedItem() != null) {
    		String s = comboStates.getSelectionModel().getSelectedItem();
    		String i = comboAlphabetInput.getSelectionModel().getSelectedItem();
    		String o = comboAlphabetOutput.getSelectionModel().getSelectedItem();
    		String t = comboTransition.getSelectionModel().getSelectedItem();
    		
    		stateTable[states.indexOf(s)+1][alphabet.indexOf(i)+1] = t + o;
    		transitions[states.indexOf(s)][alphabet.indexOf(i)] = t + o;
    		
    		printStateTable();
    	}else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setTitle("Transición");
    		alert.setContentText("Por favor rellene las transiciones.");
    		alert.setHeaderText(null);
    		alert.showAndWait();
    	}
    }

    @FXML
    public void addedTransition(ActionEvent event) {
    	if(testTransition()) {
    		
    		mealyMachine = new Machine(states, alphabet, transitions);
    		openResultScreen();
    		
    	} else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText(null);
    		alert.setTitle("Transiciones vacías");
    		alert.setContentText("Por favor completa las transiciones de la maquina.");
    		alert.showAndWait();
    	}
    }
    
    private boolean testTransition() {
    	
    	boolean accepted = true;
    	
    	for(int i = 0; i < stateTable.length;i++) {
    		for(int j = 0; j < stateTable[0].length;j++) {
    			if(stateTable[i][j] == null) {
    				accepted = false;
    			}
    		}
    	}
    	
    	return accepted;
    }
    
	private void printStateTable() {
	    	
    	String table = "";
    	
    	for(int i = 0; i < stateTable.length;i++) {
    		for(int j = 0; j < stateTable[0].length;j++) {
    			table += stateTable[i][j] + " ";
    		}
    		
    		table += "\n";
    	}
    	
    	txtTransition.setText(table);
    }
    
	private void prepareStateTable() {
	    	
	    	int w = states.size()+1;
	    	int e = alphabet.size()+1;
	    	
	    	stateTable = new String[w][e];
	    	transitions = new String[w-1][e-1];
	    	
	    	for(int i = 1; i < e; i++) {
	    		stateTable[0][i] = alphabet.get(i-1);
	    	}
	    	
	    	for(int j = 1; j < w; j++) {
				stateTable[j][0] = states.get(j-1);
			}
	    	
	    	stateTable[0][0] = "/";
	}
	
	private void openResultScreen() {
		Stage transitionState = (Stage) comboTransition.getScene().getWindow();
		transitionState.close();
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResultMachineScreen.fxml"));
		
		fxmlLoader.setController(this);
		
		try{
			Parent root = fxmlLoader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Moore Machine");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
			printResultStateTable();
			printResultReducedStateTable();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void printResultStateTable() {
		String table = "";
    	
    	for(int i = 0; i < stateTable.length;i++) {
    		for(int j = 0; j < stateTable[0].length;j++) {
    			table += stateTable[i][j] + " ";
    		}
    		
    		table += "\n";
    	}
    	
    	txtResultStateTable.setText(table);
	}
	
	private void printResultReducedStateTable() {
		String table = "";
    	
		/*
    	String[][] reducedMachine = 
    	
    	for(int i = 0; i < reducedMachine.length;i++) {
    		for(int j = 0; j < reducedMachine[0].length;j++) {
    			table += reducedMachine[i][j] + " ";
    		}
    		
    		table += "\n";
    	}
    	*/
    	txtReducedStateTable.setText(table);
	}
	
	@FXML
    public void finishedMachineOperation(ActionEvent event) {

    }
	
}
