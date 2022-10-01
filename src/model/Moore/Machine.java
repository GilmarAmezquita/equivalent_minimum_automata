package model.Moore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Machine {
	public List<String> alphabet;
	public List<State> mooreStates;
	public HashMap<String, HashMap<String, State>> mooreTransitions;
	
	public Machine(List<String> states, List<String> aStates, List<String> alphabet) {
		this.alphabet = alphabet;
		mooreStates = new ArrayList<State>();
		addStates(states, aStates);
		addTransitions();
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
}
