package model.Moore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Machine {
	public List<String> alphabet;
	public List<State> mooreStates;
	public List<State> mooreReducted;
	
	public HashMap<String, State> mooreTransitions;
	
	public Machine(List<String> states, List<String> aStates, List<String> alphabet) {
		this.alphabet = alphabet;
		mooreStates = new ArrayList<State>();
		addStates(states, aStates);
	}
	
	public boolean addStates(List<String> states, List<String> aStates) {
		boolean repeatedState = false;
		for(String s : states) {
			if(!isAlreadyAState(s)) {
				State newS = new State(s, isAcceptedState(s,aStates));
				mooreStates.add(newS);
			}else repeatedState = true;
		}
		return repeatedState;
	}
	
	public boolean isAlreadyAState(String s) {
		for(State mS : mooreStates) {
			if(mS.getStateName() == s) {
				return true;
			}
		}
		return false;
	}
	
	public Integer isAcceptedState(String s, List<String> aStates) {
		for(String a : aStates) {
			if(a == s) {
				return 1;
			}
		}
		return 0;
	}
	
	public void addTransitions() {
		
	}
}
