package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;

/**
 * A class encapsulating the results from a current test.
 * @author Emi
 *
 */
public class Results {
	/**
	 * The tested benchmark.
	 */
	private String benchmark;
	/**
	 * The tested heuristic.
	 */
	private Heuristic heuristic;
	/**
	 * The tested threshold.
	 */
	private int threshold;
	/**
	 * The tested percentage.
	 */
	private double percentage;
	/**
	 * The number of errors caused by accessing objects that were deallocated from memory.
	 */
	private float errors;

	/**
	 * 
	 * @param p parameters with which the tests were executed
	 * @param e errors caused by accessing already dead objects
	 */

	public Results(ParameterSettings p, float e) {
		benchmark = p.getFile();
		heuristic = p.getHeuristic();
		threshold = p.getThreshold();
		percentage = p.getPercentage();
		errors = e;
	}
/**
 * 
 * @return the tested bechmark.
 */
	public String getBenchmark() {
		return benchmark;

	}
/**
 * 
 * @return the tested heuristic
 */
	public String getHeuristic() {
		return heuristic.toString();
	}
/**
 * 
 * @return the size of memory
 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * @return the percentage of memory size to be deallocated once the threshold has been reached
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * 
	 * @return the percentage of errors caused by accessing deallocated objects from memory
	 */
	public float getErrors() {
		return errors;
	}
/**
 * 
 */
	public String toString() {
		return benchmark + ", " + heuristic + ", " + threshold + ", "
				+ percentage + ", " + errors;
	}

}
