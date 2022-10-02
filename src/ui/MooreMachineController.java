package ui;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Moore.Machine;

public class MooreMachineController {
	
	private final static String CHAR_SEPARATOR = ",";
	
	@FXML
    private TextArea txtReducedStateTable;

    @FXML
    private TextArea txtResultStateTable;
	
	@FXML
    private Button buttonOkTransition;
	
	@FXML
    private Label labelTableStateView;
	
	@FXML
    private ComboBox<String> comboAlphabet;

    @FXML
    private ComboBox<String> comboStates;

    @FXML
    private ComboBox<String> comboTransition;

    @FXML
    private TextArea textTransition;
	
	@FXML
    private TextField acceptanceStatesField;

    @FXML
    private TextField alfabetField;

    @FXML
    private TextField statesField;
    
    private Machine mooreMachine;
	
    private List<String> states;
    
    private List<String> acceptanceStates;
    
    private List<String> alphabet;
    
    private String[][] transitions;
    
    private String[][] stateTable;
    
    private String[][] reduceStateTable;
    
	public MooreMachineController() {
		
	}
	
	private void loadInfo() {
		
		String[] states;
		String[] acceptanceStates;
		String[] alphabet;
		try {
			states = statesField.getText().split(CHAR_SEPARATOR);
			acceptanceStates = acceptanceStatesField.getText().split(CHAR_SEPARATOR);
			alphabet = alfabetField.getText().split(CHAR_SEPARATOR);
			
			this.states = Arrays.asList(states);
			this.acceptanceStates = Arrays.asList(acceptanceStates);
			this.alphabet = Arrays.asList(alphabet);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

    @FXML
    public void addMachineOk(ActionEvent event) {
    	if(!statesField.getText().isEmpty() && !alfabetField.getText().isEmpty() && !acceptanceStatesField.getText().isEmpty()) {
    		loadInfo();
    		openTransitionsWindows();
    		
    	} else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText(null);
    		alert.setTitle("Campos vacíos");
    		alert.setContentText("Por favor digita la información solicitada.");
    		alert.showAndWait();
    	}
    }
    
    private void openTransitionsWindows(){
    	
    	Stage thisState = (Stage) statesField.getScene().getWindow();
    	thisState.close();
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TransitionMooreMachineScreen.fxml"));
		
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
			
			loadComboBoxInfo();
			prepareStateTable();
			printStateTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void loadComboBoxInfo() {
    	ObservableList<String> states = FXCollections.observableList(this.states);
    	comboStates.setItems(states);
    	
    	ObservableList<String> alphabet = FXCollections.observableList(this.alphabet);
    	comboAlphabet.setItems(alphabet);
    	
    	ObservableList<String> transitionStates = FXCollections.observableList(this.states);
    	comboTransition.setItems(transitionStates);
    }
    
    @FXML
    public void addTransition(ActionEvent event) {
    	if(comboAlphabet.getSelectionModel().getSelectedItem() != null && comboStates.getSelectionModel().getSelectedItem() != null &&
    			comboTransition.getSelectionModel().getSelectedItem() != null) {
    		String s = comboStates.getSelectionModel().getSelectedItem();
    		String a = comboAlphabet.getSelectionModel().getSelectedItem();
    		String t = comboTransition.getSelectionModel().getSelectedItem();
    		
    		stateTable[states.indexOf(s)+1][alphabet.indexOf(a)+1] = t;
    		transitions[states.indexOf(s)][alphabet.indexOf(a)] = t;
    		
    		labelTableStateView.setText("");
    		printStateTable();
    	}else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setTitle("Transición");
    		alert.setContentText("Por favor rellene las transiciones.");
    		alert.setHeaderText(null);
    		alert.showAndWait();
    	}
    }
    
    private void printStateTable() {
    	
    	String table = "";
    	
    	for(int i = 0; i < stateTable.length;i++) {
    		for(int j = 0; j < stateTable[0].length;j++) {
    			table += stateTable[i][j] + " ";
    		}
    		
    		table += "\n";
    	}
    	
    	labelTableStateView.setText(table);
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
    
    private void prepareReduceStateTable() {
    	
    }
    
    @FXML
    public void addedTransition(ActionEvent event) {
    	if(testTransition()) {
    		
    		mooreMachine = new Machine(states, acceptanceStates, alphabet, transitions);
    		openResultScreen();
    		
    	} else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText(null);
    		alert.setTitle("Transiciones vacías");
    		alert.setContentText("Por favor completas la transiciones de la maquina.");
    		alert.showAndWait();
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
    	
    	List<List<String>> reducedMachine = mooreMachine.mooreReduced();
    	
    	for(int i = 0; i < reducedMachine.size();i++) {
    		for(int j = 0; j < reducedMachine.get(0).size();j++) {
    			table += reducedMachine.get(i).get(j) + " ";
    		}
    		
    		table += "\n";
    	}
    	
    	txtReducedStateTable.setText(table);
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
    
    @FXML
    public void finishedMachineOperation(ActionEvent event) {
    	Stage stage = (Stage) txtResultStateTable.getScene().getWindow();
    	stage.close();
    }
}
