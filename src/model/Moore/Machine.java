package model.Moore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Machine {
	public List<String> alphabet;
	public State initialState;
	public HashMap<String, State> mooreStates;
	public List<String> nameStates;
	public HashMap<String, HashMap<String, State>> transitions;
	/**
	 * Machine object constructor, of type Moore.
	 * @param states machine state list.
	 * @param aStates machine acceptance state list.
	 * @param alphabet machine alphabet list.
	 * @param t machine transition matrix.
	 */
	public Machine(List<String> states, List<String> aStates, List<String> alphabet, String[][] t) {
		this.alphabet = alphabet;
		mooreStates = new HashMap<>();
		transitions = new HashMap<>();
		nameStates = states;
		initialState = new State(states.get(0), acceptanceState(states.get(0), aStates));
		addStates(states, aStates);
		addTransitions(t);
	}
	/**
	 * Method to get the machine alphabet
	 * @return machine alphabet list.
	 */
	public List<String> getAlphabet(){
		return alphabet;
	}
	/**
	 * Method to get the machine states.
	 * @return machine state list.
	 */
	public List<String> getStates(){
		return nameStates;
	}
	/**
	 * Method to add the different states to a HashMap of machine states.
	 * @param states list of names of the states.
	 * @param aStates list of names of the acceptance states.
	 * @return returns true if repeated states were entered, otherwise return false.
	 */
	public boolean addStates(List<String> states, List<String> aStates) {
		boolean repeatedState = false;
		for(String s : states) {
			if(!mooreStates.containsKey(s)) {
				State newS = new State(s, acceptanceState(s,aStates));
				mooreStates.put(s, newS);
			}else repeatedState = true;
		}
		return repeatedState;
	}
	/**
	 * Method that checks if a state is an acceptance state.
	 * @param s state to check.
	 * @param aStates accept state string list.
	 * @return
	 */
	public Integer acceptanceState(String s, List<String> aStates) {
		if(aStates.contains(s)) {
			return 1;
		}
		return 0;
	}
	/**
	 * Method to add all the transitions entered by the user.
	 * @param t matrix with rows as states, columns as symbols, and transitions in the coordinates.
	 */
	public void addTransitions(String[][] t) {
		for(int i = 0; i<nameStates.size(); i++) {
			HashMap<String, State> tr = new HashMap<>();
			for(int j = 0; j<alphabet.size(); j++) {
				tr.put(alphabet.get(j), mooreStates.get(t[i][j]));
			}
			transitions.put(nameStates.get(i), tr);
		}
	}
	/**
	 * Primary method for removing inaccessible machine states.
	 */
	public void removeInaccesible() {
		List<String> accessibleStates = new ArrayList<>();
		accessibleStates.add(initialState.getStateName());
		HashMap<String, State> start = transitions.get(initialState.getStateName());
		for(String a : alphabet) {
			if(!accessibleStates.contains(start.get(a).getStateName())) {
				accessibleStates.add(start.get(a).getStateName());
			}
		}
		if(accessibleStates.size() > 1) {
			accessibleStates = removeInaccessible(accessibleStates, 0);
		}
		List<String> rmFromStates = new ArrayList<>();
	    for (String state : nameStates) { 
	        if (!accessibleStates.contains(state)) { 
	            mooreStates.remove(state);
	            transitions.remove(state);
	            rmFromStates.add(state);
	        }
	    }
	    for(String rm : rmFromStates) {
	    	nameStates.remove(rm);
	    }
	}
	/**
	 * 
	 * @param accessibleStates
	 * @param iteration
	 * @return
	 */
	private List<String> removeInaccessible(List<String> accessibleStates, int iteration) {
		iteration += 1;
		HashMap<String, State> current = transitions.get(accessibleStates.get(iteration));
		for(String a: alphabet) {
			if(!accessibleStates.contains(current.get(a).getStateName())) {
				accessibleStates.add(current.get(a).getStateName());
			}
		}
		if(accessibleStates.size() <= iteration+1) {
			return accessibleStates;
		}else return removeInaccessible(accessibleStates, iteration);
	}
	/*
	public List<List<State>> mooreReduced() {
		List<State> outFalse = new ArrayList<>();
		List<State> outTrue = new ArrayList<>();
		for(State s : mooreStates) {
			if(s.getStateOut() == 0) {
				outFalse.add(s);
			}else outTrue.add(s);
		}
		List<List<State>> partitions = new ArrayList<>();
		partitions.add(outFalse);
		partitions.add(outTrue);
		return mooreReduced(partitions);
	}
	private List<List<State>> mooreReduced(List<List<State>> partitions){
		List<List<State>> newPartitions = new ArrayList<>();
		for(int i = 0; i<partitions.size(); i++) {
			List<State> p = partitions.get(i);
			newPartitions.addAll(reducedPartition(p, partitions));
		}
		if(partitions.size() != newPartitions.size()) {
			return mooreReduced(newPartitions);
		}else return newPartitions;
	}
	
	/**
	 * Obtiene un bloque de la partición y revisa si los estados del bloque se mantienen
	 * en la mismo bloque o deben de separarse y crear nuevos bloques.
	 
	private List<List<State>> reducedPartition(List<State> p, List<List<State>> parts){
		List<List<State>> newP = new ArrayList<>();
		if(p.size()<1) {
			newP.add(p);
			return newP;
		}
		List<State> p1 = new ArrayList<>();
		List<State> pPosible = new ArrayList<>();
		p1.add(p.get(0));
		for(int i = 1; i<p.size(); i++) {
			boolean sameTransitionParts = true;
			for(int j = 0; j<alphabet.size() && sameTransitionParts; j++) {
				HashMap<String, State> s0 = mooreTransitions.get(p.get(0).getStateName());
				HashMap<String, State> sa = mooreTransitions.get(p.get(i).getStateName());
				State s0Transition = s0.get(alphabet.get(j));
				State saTransition = sa.get(alphabet.get(j));
				sameTransitionParts = samePreviousPartition(s0Transition, saTransition, parts);
			}
			if(sameTransitionParts) {
				p1.add(p.get(i));
			}else pPosible.add(p.get(i));
		}
		newP.add(p1);
		if(!pPosible.isEmpty()) {
			newP.addAll(reducedPartition(pPosible, parts));
		}
		return newP;
	}
	
	/**
	 * Revisa si las transiciones de dos estados de encuentran en el mismo bloque
	 * de la partición anterior.
	 
	private boolean samePreviousPartition(State s0, State sa, List<List<State>> parts) {
		for(List<State> s : parts) {
			boolean s0Same = false;
			boolean saSame = false;
			for(State sS : s) {
				if(sS.getStateName() == s0.getStateName()) {
					s0Same = true;
				}
				if(sS.getStateName() == sa.getStateName()) {
					saSame = true;
				}
			}
			if(s0Same && saSame) {
				return true;
			}
		}
		return false;
	}
	*/
}
