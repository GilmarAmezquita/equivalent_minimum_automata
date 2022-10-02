package model.Mealy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Machine {
	private List<String> alphabet;
	private String initialState;
	private List<String> nameStates;
	private HashMap<String, HashMap<String, Transition>> stateTransitions;
	
	private String reducedInitialState;
	private List<String> reducedStates;
	private List<List<String>> reducedBlocks;
	private HashMap<String, HashMap<String, Transition>> reducedStateTransitions;
	
	public Machine(List<String> states, List<String> alphabet, String[][] transitions) {
		this.alphabet = alphabet;
		this.initialState = states.get(0);
		this.nameStates = states;
		addTransitions(transitions);
		removeInaccessible();
		reducedBlocks = mealyReduced();
		defineNewStates();
		defineNewTransitions();
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
	
	public String getReducedInitialState() {
		return reducedInitialState;
	}
	public List<String> getReducedStates(){
		return reducedStates;
	}
	public HashMap<String,HashMap<String, Transition>> getReducedStateTransitions(){
		return reducedStateTransitions;
	}
	
	private void addTransitions(String[][] transitions) {
		stateTransitions = new HashMap<>();
		for(int i = 0; i<nameStates.size(); i++) {
			HashMap<String, Transition> aT = new HashMap<>();
			for(int j = 0; j<alphabet.size(); j++) {
				String tE = transitions[i][j].substring(0, 1);
				int tO = Integer.parseInt(transitions[i][j].substring(1));
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
	public List<List<String>> mealyReduced() {
		List<List<String>> partitions = new ArrayList<>();
		List<String> alreadyPartitioned = new ArrayList<>();
		for(int i = 0; i<nameStates.size(); i++) {
			List<String> p1 = new ArrayList<>();
			if(!alreadyPartitioned.contains(nameStates.get(i))) {
				p1.add(nameStates.get(i));
				alreadyPartitioned.add(nameStates.get(i));
				for(int j = 1; j<nameStates.size(); j++) {
					if(!alreadyPartitioned.contains(nameStates.get(j))) {
						if(sameTransitionsOut(nameStates.get(i), nameStates.get(j))) {
							p1.add(nameStates.get(j));
							alreadyPartitioned.add(nameStates.get(j));
						}
					}
				}
			}
			if(!p1.isEmpty()) {
				partitions.add(p1);	
			}
		}
		return mealyReduced(partitions);
	}
	private List<List<String>> mealyReduced(List<List<String>> partitions){
		List<List<String>> newPartitions = new ArrayList<>();
		for(int i = 0; i<partitions.size(); i++) {
			List<String> p = partitions.get(i);
			newPartitions.addAll(reducedPartition(p, partitions));
		}
		return mealyReduced(partitions, newPartitions);
	}
	private List<List<String>> mealyReduced(List<List<String>> partitions, List<List<String>> newPartitions){
		if(partitions.size() != newPartitions.size()) {
			return mealyReduced(newPartitions);
		}else return newPartitions;
	}
	private boolean sameTransitionsOut(String state1, String state2) {
		for(String a : alphabet) {
			if(stateTransitions.get(state1).get(a).getTransitionOut() != stateTransitions.get(state2).get(a).getTransitionOut()) {
				return false;
			}
		}
		return true;
	}
	private List<List<String>> reducedPartition(List<String> p, List<List<String>> prevPartitions){
		List<List<String>> newP = new ArrayList<>();
		if(p.size() == 1) {
			newP.add(p);
			return newP;
		}
		List<String> pT = new ArrayList<>();
		List<String> pNotYet = new ArrayList<>();
		pT.add(p.get(0));
		
		for(int i = 1; i<p.size(); i++) {
			boolean sameTransitionParts = true;
			for(int j = 0; j<alphabet.size() && sameTransitionParts; j++) {
				HashMap<String,Transition> s0 = stateTransitions.get(p.get(0));
				HashMap<String, Transition> si = stateTransitions.get(p.get(i));
				Transition s0Transition = s0.get(alphabet.get(j));
				Transition siTransition = si.get(alphabet.get(j));
				sameTransitionParts = samePreviousPartition(s0Transition, siTransition, prevPartitions);
			}
			if(sameTransitionParts) {
				pT.add(p.get(i));
			}else pNotYet.add(p.get(i));
		}
		newP.add(pT);
		if(!pNotYet.isEmpty()) {
			newP.addAll(reducedPartition(pNotYet, prevPartitions));
		}
		return newP;
	}
	private boolean samePreviousPartition(Transition s0, Transition si, List<List<String>> prevPartitions) {
		for(List<String> s : prevPartitions) {
			boolean s0Same = false;
			boolean siSame = false;
			for(String sS : s) {
				if(sS.equals(s0.getTransitionEnd())) {
					s0Same = true;
				}
				if(sS.equals(si.getTransitionEnd())) {
					siSame = true;
				}
			}
			if(s0Same && siSame) {
				return true;
			}
		}
		return false;
	}
	private void defineNewStates() {
		reducedStates = new ArrayList<>();
		char z = 'Z';
		for(int i = reducedBlocks.size()-1; i>=0; i--) {
			reducedStates.add(0,String.valueOf(z));
			if(i == 0) {
				reducedInitialState = String.valueOf(z);
			}
			z--;
		}
	}
	private void defineNewTransitions() {
		reducedStateTransitions = new HashMap<>();
		for(int i = 0; i<reducedStates.size(); i++) {
			HashMap<String, Transition> nST = new HashMap<>();
			for(int j = 0; j<alphabet.size(); j++) {
				String tEnd = stateTransitions.get(reducedBlocks.get(i).get(0)).get(alphabet.get(j)).getTransitionEnd();
				int tOut = stateTransitions.get(reducedBlocks.get(i).get(0)).get(alphabet.get(j)).getTransitionOut();
				int index = 0;
				for(int k = 0; k<reducedBlocks.size(); k++) {
					if(reducedBlocks.get(k).contains(tEnd)) {
						index = k;
					}
				}
				Transition t = new Transition(reducedStates.get(index),tOut);
				nST.put(alphabet.get(j), t);
			}
			reducedStateTransitions.put(reducedStates.get(i), nST);
		}
	}
}