package model.Moore;

import java.util.List;
/**
 * 
 * @author Gilmar Andres Amezquita Romero
 * @version 0.1
 */
public class State {
	public String state;
	public int out;
	public List<String> blockStates;
	/**
	 * Constructor of the machine states.
	 * @param n name of the state.
	 * @param s state out (acceptance state or not)
	 */
	public State(String n, int s) {
		this.state= n;
		this.out = s;
	}
	/**
	 * Constructor of the minimized machine states.
	 * @param n name of the state.
	 * @param s state out (acceptance state or not).
	 * @param bS list of original states that are part of the new state.
	 */
	public State(String n, int s, List<String> bS) {
		this.state = n;
		this.out = s;
		this.blockStates = bS;
	}
	/**
	 * Method to get the name of the state
	 * @return name of the state.
	 */
	public String getStateName() {
		return state;
	}
	/**
	 * Method to get if the state is an acceptance state or not.
	 * @return 1 if it is an acceptance state, else 0
	 */
	public int getStateOut() {
		return out;
	}
	/**
	 * Method to receive the states that are part of a minimized machine state.
	 * @return states that are part of a minimized machine state.
	 */
	public List<String> getBlockStates(){
		return blockStates;
	}
}
