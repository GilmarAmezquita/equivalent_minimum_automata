package model.Mealy;

public class Transition {
	private String transitionEnd;
	private String transitionOut;
	
	public Transition(String tE, String tO) {
		transitionEnd = tE;
		transitionOut = tO;
	}
	
	public String getTransitionEnd() {
		return transitionEnd;
	}
	public String getTransitionOut() {
		return transitionOut;
	}
}
