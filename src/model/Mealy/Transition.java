package model.Mealy;

public class Transition {
	private String transitionEnd;
	private int transitionOut;
	
	public Transition(String tE, int tO) {
		transitionEnd = tE;
		transitionOut = tO;
	}
	
	public String getTransitionEnd() {
		return transitionEnd;
	}
	public int getTransitionOut() {
		return transitionOut;
	}
}
