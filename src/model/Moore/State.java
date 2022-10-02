package model.Moore;

import java.util.List;

public class State {
	
	public String state;
	public int out;
	
	public List<String> blockStates;
	
	public State(String n, int s) {
		this.state= n;
		this.out = s;
	}
	
	public State(String n, int s, List<String> bS) {
		this.state = n;
		this.out = s;
		this.blockStates = bS;
	}
	
	public String getStateName() {
		return state;
	}
	
	public int getStateOut() {
		return out;
	}
	
	public List<String> getBlockStates(){
		return blockStates;
	}
}
