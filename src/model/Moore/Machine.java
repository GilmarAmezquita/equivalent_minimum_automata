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
	 * Method to identify the states that are accessible in the machine.
	 * @param accessibleStates list of accessible states identified up to that iteration.
	 * @param iteration current iteration of the method
	 * @return the list of accessible states
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
	/**
	 * Primary method for reduce the machine states.
	 * @return machine reduced.
	 */
	public List<List<String>> mooreReduced() {
		List<String> outFalse = new ArrayList<>();
		List<String> outTrue = new ArrayList<>();
		for(String s : nameStates) {
			if(mooreStates.get(s).getStateOut() == 0) {
				outFalse.add(s);
			}else outTrue.add(s);
		}
		List<List<String>> partitions = new ArrayList<>();
		partitions.add(outFalse);
		partitions.add(outTrue);
		return mooreReduced(partitions);
	}
	/**
	 * Method to start reducing the machine from P2
	 * @param partitions partitions list
	 * @return returns the list of states to reduce the machine
	 */
	private List<List<String>> mooreReduced(List<List<String>> partitions){
		List<List<String>> newPartitions = new ArrayList<>();
		for(int i = 0; i<partitions.size(); i++) {
			List<String> p = partitions.get(i);
			newPartitions.addAll(reducedPartition(p, partitions));
		}
		return mooreReduced(partitions, newPartitions);
	}
	/**
	 * Method to check if partition k is equal to partition k-1
	 * @param partitions partition k-1
	 * @param newPartitions partition k
	 * @return return the new partition.
	 */
	private List<List<String>> mooreReduced(List<List<String>> partitions, List<List<String>> newPartitions){
		if(partitions.size() != newPartitions.size()) {
			return mooreReduced(newPartitions);
		}else return newPartitions;
	}
	/**
	 * Get a block from the partition and check if the block states stay in the same block 
	 * or should be split and create new blocks.
	 * @param p block of the previous partition
	 * @param prevPartitions previous partition.
	 * @return the new blocks for the next partition.
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
				HashMap<String, State> s0 = transitions.get(p.get(0));
				HashMap<String, State> sa = transitions.get(p.get(i));
				State s0Transition = s0.get(alphabet.get(j));
				State siTransition = sa.get(alphabet.get(j));
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
	 * @param s0 first state.
	 * @param si second state.
	 * @param prevPartitions previous partition.
	 * @return true if they are on the same partition, otherwise false.
	 */
	private boolean samePreviousPartition(State s0, State si, List<List<String>> prevPartitions) {
		for(List<String> s : prevPartitions) {
			boolean s0Same = false;
			boolean siSame = false;
			for(String sS : s) {
				if(sS == s0.getStateName()) {
					s0Same = true;
				}
				if(sS== si.getStateName()) {
					siSame = true;
				}
			}
			if(s0Same && siSame) {
				return true;
			}
		}
		return false;
	}
}