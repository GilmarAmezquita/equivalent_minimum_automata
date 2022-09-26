package mooreMachine;

public class mooreState {
	
	public String state;
	public Integer out;
	
	public mooreState(String n, Integer s) {
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
