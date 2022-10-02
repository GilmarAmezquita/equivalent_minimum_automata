package model.Mealy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Machine {
	private List<String> alphabet;
	private String initialState;
	private List<String> nameStates;
	private HashMap<String, HashMap<String, Transition>> stateTransitions;
	
	public Machine(List<String> states, List<String> alphabet, String[][] transitions) {
		this.alphabet = alphabet;
		this.initialState = states.get(0);
		this.nameStates = states;
		addTransitions(transitions);
		removeInaccessible();
	}
	public List<String> getAlphabet(){
		return alphabet;
	}
	public String getInitialState() {
		return initialState;
	}
	public List<String> getNameStates(){
		return nameStates;
	}
	public HashMap<String, HashMap<String, Transition>> getStateTransitions(){
		return stateTransitions;
	}
	
	private void addTransitions(String[][] transitions) {
		stateTransitions = new HashMap<>();
		for(int i = 0; i<nameStates.size(); i++) {
			HashMap<String, Transition> aT = new HashMap<>();
			for(int j = 0; j<alphabet.size(); j++) {
				String tE = transitions[i][j].substring(0, 1);
				String tO = transitions[i][j].substring(1);
				Transition t = new Transition(tE, tO);
				aT.put(alphabet.get(j), t);
			}
			stateTransitions.put(nameStates.get(i), aT);
		}
	}
	public void removeInaccessible() {
		List<String> accessibleStates = new ArrayList<>();
		accessibleStates.add(initialState);
		HashMap<String, Transition> start = stateTransitions.get(initialState);
		for(String a : alphabet) {
			if(!accessibleStates.contains(start.get(a).getTransitionEnd())) {
				accessibleStates.add(start.get(a).getTransitionEnd());
			}
		}
		if(accessibleStates.size() > 1) {
			accessibleStates = removeInaccessible(accessibleStates, 0);
		}
		List<String> rmFromStates = new ArrayList<>();
	    for (String state : nameStates) { 
	        if (!accessibleStates.contains(state)) {
	            stateTransitions.remove(state);
	            rmFromStates.add(state);
	        }
	    }
	    for(String rm : rmFromStates) {
	    	nameStates.remove(rm);
	    }
	}
	private List<String> removeInaccessible(List<String> accessibleStates, int iteration) {
		iteration += 1;
		HashMap<String, Transition> current = stateTransitions.get(accessibleStates.get(iteration));
		for(String a: alphabet) {
			if(!accessibleStates.contains(current.get(a).getTransitionEnd())) {
				accessibleStates.add(current.get(a).getTransitionEnd());
			}
		}
		if(accessibleStates.size() <= iteration+1) {
			return accessibleStates;
		}else return removeInaccessible(accessibleStates, iteration);
	}
	
}
