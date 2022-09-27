package model.Moore;

public class State {
	
	public String state;
	public Integer out;
	
	public State(String n, Integer s) {
		this.state= n;
		this.out = s;
	}
	
	public String getStateName() {
		return state;
	}
	
	public Integer getStateOut() {
		return out;
	}
}
