package model.Moore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Moore's state machine and its minimization.
 * @author Gilmar Andres Amezquita Romero
 * @version 0.1
 */
public class Machine {
	private List<String> alphabet;
	private State initialState;
	private List<String> nameStates;
	public HashMap<String, State> mooreStates;
	public HashMap<String, HashMap<String, State>> transitions;
	
	private State reducedInitialState;
	private List<String> reducedStates;
	private HashMap<String, State> statesReduced;
	private HashMap<String, HashMap<String, State>> transitionsReduced;
	
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
		removeInaccesible();
		List<List<String>> reducedBlocks = mooreReduced();
		defineNewStates(reducedBlocks);
		defineNewTransitions(reducedBlocks);
	}
	
	/**
	 * Method to get the machine alphabet
	 * @return machine alphabet list.
	 */
	public List<String> getAlphabet(){
		return alphabet;
	}
	/**
	 * Method to get the machine initial state.
	 * @return initial state.
	 */
	public State getInitialState() {
		return initialState;
	}
	/**
	 * Method to get the machine states.
	 * @return machine state list.
	 */
	public List<String> getStates(){
		return nameStates;
	}
	
	/**
	 * Method to obtain the initial state of the reduced machine.
	 * @return the initial state.
	 */
	public State getReducedInitialState() {
		return reducedInitialState;
	}
	/**
	 * Method to obtain the states list of the minimized machine.
	 * @return states list of the minimized machine.
	 */
	public List<String> getReducedStates(){
		return reducedStates;
	}
	/**
	 * Method to obtain the HashMap of the minimized machine states.
	 * @return HashMap of the minimized machine states
	 */
	public HashMap<String, State> getStatesReduced(){
		return statesReduced;
	}
	/**
	 * Method to obtain the HashMap of the minimized machine transitions.
	 * @return HashMap of the transitions.
	 */
	public HashMap<String, HashMap<String, State>> getTransitionsReduced(){
		return transitionsReduced;
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
	
	/**
	 * Method to obtain the new states of the reduced machine.
	 * @param reducedBlocks blocks of the final partition.
	 */
	private void defineNewStates(List<List<String>> reducedBlocks) {
		List<String> rStates = new ArrayList<>(5);
		statesReduced = new HashMap<>();
		char z = 'Z';
		for(int i = reducedBlocks.size()-1; i>=0; i--) {
			int out = defineAcceptanceStates(reducedBlocks.get(i));
			State s = new State(String.valueOf(z),out,reducedBlocks.get(i));
			statesReduced.put(String.valueOf(z),s);
			if(i == 0) {
				reducedInitialState = s;
			}
			rStates.add(0,String.valueOf(z));
			z--;
		}
		reducedStates = rStates;
	}
	/**
	 * Method to define if a state is an acceptance states.
	 * @param newStates list of reduced machine states
	 * @return 1 if it is an acceptance state, else 0
	 */
	private int defineAcceptanceStates(List<String> newStates) {
		for(String s : newStates) {
			if(mooreStates.get(s).getStateOut() == 1) {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Method to define the transitions of the reduced machine.
	 * @param reducedBlocks final minimization blocks
	 */
	private void defineNewTransitions(List<List<String>> reducedBlocks) {
		transitionsReduced = new HashMap<>();
		for(int i = 0; i<reducedBlocks.size(); i++) {
			HashMap<String, State> nTS = new HashMap<>();
			for(int j = 0; j<alphabet.size(); j++) {
				nTS.put(alphabet.get(j),defineNewTransitions(reducedBlocks.get(i).get(0), alphabet.get(j)));
			}
			transitionsReduced.put(reducedStates.get(i),nTS);
		}
	}
	/**
	 * Method to define to which new state a symbol transitions.
	 * @param state start state.
	 * @param symbol symbol to transition.
	 * @return state after transition.
	 */
	private State defineNewTransitions(String state, String symbol) {
		String tState = transitions.get(state).get(symbol).getStateName();
		for(int i = 0; i<reducedStates.size(); i++) {
			if(statesReduced.get(reducedStates.get(i)).getBlockStates().contains(tState)) {
				return statesReduced.get(reducedStates.get(i));
			}
		}
		return null;
	}
}