package model.Mealy;
/**
 * Transition from a Mealy machine
 * @author Gilmar Andres Amezquita Romero
 * @version 0.1
 */
public class Transition {
	private String transitionEnd;
	private int transitionOut;
	/**
	 * Constructor of the transition object.
	 * @param tE state to which it passes when a symbol is injected.
	 * @param tO parameter to define if it is an acceptance transition.
	 */
	public Transition(String tE, int tO) {
		transitionEnd = tE;
		transitionOut = tO;
	}
	/**
	 * Method to get the final state after the transition.
	 * @return final state.
	 */
	public String getTransitionEnd() {
		return transitionEnd;
	}
	/**
	 * method to get the value of the transition.
	 * @return 1 if is an acceptance transition, else 0.
	 */
	public int getTransitionOut() {
		return transitionOut;
	}
}
