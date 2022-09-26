package mooreMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mooreMachine {
	public List<String> alphabet;
	public List<mooreState> mooreStates;
	public List<mooreState> mooreReducted;
	
	public HashMap<String, mooreState> mooreTransitions;
	
	public mooreMachine(List<String> states, List<String> aStates, List<String> alphabet) {
		this.alphabet = alphabet;
		mooreStates = new ArrayList<mooreState>();
		addStates(states, aStates);
	}
	
	public boolean addStates(List<String> states, List<String> aStates) {
		boolean repeatedState = false;
		for(String s : states) {
			if(!isAlreadyAState(s)) {
				mooreState newS = new mooreState(s, isAcceptedState(s,aStates));
				mooreStates.add(newS);
			}else repeatedState = true;
		}
		return repeatedState;
	}
	
	public boolean isAlreadyAState(String s) {
		for(mooreState mS : mooreStates) {
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
