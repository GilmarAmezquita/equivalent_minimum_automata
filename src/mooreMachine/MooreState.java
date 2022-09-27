package mooreMachine;

public class MooreState {
	
	public String state;
	public Integer out;
	
	public MooreState(String n, Integer s) {
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
