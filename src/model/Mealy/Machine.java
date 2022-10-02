package model.Mealy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Machine class to create a mealy machine and obtain it minimized equivalent machine.
 * @author Gilmar Andres Amezquita Romero
 * @author Brayan Andres Ortiz
 */
public class Machine {
	private List<String> alphabet;
	private String initialState;
	private List<String> nameStates;
	private HashMap<String, HashMap<String, Transition>> stateTransitions;
	
	private String reducedInitialState;
	private List<String> reducedStates;
	private List<List<String>> reducedBlocks;
	private HashMap<String, HashMap<String, Transition>> reducedStateTransitions;
	
	/**
	 * Constructor method to create the Mealy machine state.
	 * @param states machine state list.
	 * @param alphabet machine alphabet list.
	 * @param transitions machine transitions matrix.
	 */
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
	/**
	 * Method to get the machine alphabet.
	 * @return machine alphabet list.
	 */
	public List<String> getAlphabet(){
		return alphabet;
	}
	/**
	 * Method to get the machine initial state.
	 * @return a string with the name of the initial state.
	 */
	public String getInitialState() {
		return initialState;
	}
	/**
	 * Method to get the machine states.
	 * @return machine state list with the names of the states.
	 */
	public List<String> getNameStates(){
		return nameStates;
	}
	/**
	 * Method to get all the state machine transitions.
	 * @return a hashmap with the transition with the name and the symbol as keys.
	 */
	public HashMap<String, HashMap<String, Transition>> getStateTransitions(){
		return stateTransitions;
	}
	
	/**
	 * Method to obtain the initial state of the equivalent minimum state machine.
	 * @return a string with the name of the initial state.
	 */
	public String getReducedInitialState() {
		return reducedInitialState;
	}
	/**
	 * Method to obtain the state list of the equivalent minimum machine.
	 * @return list with the name of the states.
	 */
	public List<String> getReducedStates(){
		return reducedStates;
	}
	/**
	 * Method to obtain the transitions of the equivalent minimum machine.
	 * @return a HashMap of the transitions with the state name and alphabet symbols as keys.
	 */
	public HashMap<String,HashMap<String, Transition>> getReducedStateTransitions(){
		return reducedStateTransitions;
	}
	
	/**
	 * Method to add the list of transitions in the HashMap.
	 * @param transitions matrix of transitions with states as rows and symbols as columns
	 * the value of each coordinate is a state name.
	 */
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
	/**
	 * Primary method to remove the states that are inaccessible.
	 */
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
	/**
	 * Method to obtain the accessible states by iteration.
	 * @param accessibleStates list of the accessible states found until the current iteration.
	 * @param iteration current iteration of the method.
	 * @return the final list of accessible states.
	 */
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
	/**
	 * Primary method to minimize the state machine.
	 * @return The blocks of the final partition.
	 */
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
	/**
	 * Method to obtain the current partition and the next partition.
	 * @param partitions list with the blocks of the current partition.
	 * @return List with the blocks of the final partition.
	 */
	private List<List<String>> mealyReduced(List<List<String>> partitions){
		List<List<String>> newPartitions = new ArrayList<>();
		for(int i = 0; i<partitions.size(); i++) {
			List<String> p = partitions.get(i);
			newPartitions.addAll(reducedPartition(p, partitions));
		}
		return mealyReduced(partitions, newPartitions);
	}
	/**
	 * Method to check if the previous partition equals to the current partition.
	 * @param partitions list with the blocks of the previous partition.
	 * @param newPartitions list with the blocks of the current partition.
	 * @return the next partition if are not equals, else return the final partition.
	 */
	private List<List<String>> mealyReduced(List<List<String>> partitions, List<List<String>> newPartitions){
		if(partitions.size() != newPartitions.size()) {
			return mealyReduced(newPartitions);
		}else return newPartitions;
	}
	/**
	 * Method to check if two states have the same output with each of the symbols.
	 * @param state1 First state.
	 * @param state2 Second state.
	 * @return True if both have the same output for each symbol, otherwise return false.
	 */
	private boolean sameTransitionsOut(String state1, String state2) {
		for(String a : alphabet) {
			if(stateTransitions.get(state1).get(a).getTransitionOut() != stateTransitions.get(state2).get(a).getTransitionOut()) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Method to check if the block states stay in the same block or should be split and create new blocks.
	 * @param p a block of a partition.
	 * @param prevPartitions the previous partition of the current.
	 * @return the separate block for the next partition.
	 */
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
	/**
	 * Method to check if two states are in the same block in the previous partition.
	 * @param s0 State one.
	 * @param si State two.
	 * @param prevPartitions blocks of the previous partition.
	 * @return true if both are in the same block in the previous partition, else return false.
	 */
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
	/**
	 * Method to define the names of the states of the minimized machine.
	 */
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
	/**
	 * Method to define the transition from a state with each symbol.
	 */
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
	/**
	 * Method to obtain a table of the equivalent minimum machine.
	 * @return a matrix with the states, transitions and outputs.
	 */
	public String[][] getReducedTable(){
		String[][] table = new String[reducedStates.size()][(alphabet.size())+1];
		for(int i = 0; i<reducedStates.size(); i++) {
			table[i][0] = reducedStates.get(i);
			for(int j = 0; j<alphabet.size(); j++) {
				table[i][j+1] = ""+reducedStateTransitions.get(reducedStates.get(i)).get(alphabet.get(j)).getTransitionEnd()
						+","+reducedStateTransitions.get(reducedStates.get(i)).get(alphabet.get(j)).getTransitionOut();
			}
		}
		return table;
	}
}