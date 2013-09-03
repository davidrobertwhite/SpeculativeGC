package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.SimulateGC.Heuristic;

/**
 * A class specifying the user's needs on what they want to test.
 * 
 * @author Emi
 * 
 */
public class ParameterSettings {
	/**
	 * True if the user wants a chart of currently allocated memory size to be
	 * shown.
	 */
	private boolean chart = false;
	/**
	 * File to process and analyse.
	 */
	private String inputFile;
	/**
	 * Size of the memory- a limit which when reached determines when to start
	 * deallocationg objects from memory.
	 */
	private int threshold;
	/**
	 * Percentage of the memory size to be deallocated after the threshold has
	 * been reached.
	 */
	private int percentage;
	/**
	 * Heuristic for specifying how to deallocate objects from memory.
	 */
	private Heuristic heuristic;

	/**
	 * Return string representation of the main parameters in this object.
	 * @return String including line breaks.
	 */
	public String toString() {
		String input = "Filename: " + inputFile + "\n";
		String t = "Threshold: " + threshold + "MB\n";
		String p = "Percentage: " + percentage + "%\n";
		return input + t + p;
	}
	
	/**
	 * 
	 * @param file
	 *            to be analysed
	 * @param h
	 *            heuristic to be tested
	 * @param threshold
	 *            specifying the size of the heap memory
	 * @param percentage
	 *            of objects to deallocated once the threshold has been reached
	 */
	public ParameterSettings(String file, Heuristic h, int threshold,
			int percentage) {
		heuristic = h;
		inputFile = file;
		this.threshold = threshold;
		this.percentage = percentage;
	}

	/**
	 * Default constructor.
	 */
	public ParameterSettings() {

	}

	/**
	 * 
	 * @return the file that is being analysed
	 */
	public String getFile() {
		return inputFile;
	}

	/**
	 * 
	 * @return the heuristic being tested
	 */
	public Heuristic getHeuristic() {
		return heuristic;
	}

	/**
	 * 
	 * @return the current size of memory
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * @return the percentage of memory size being deallocated once the threshod
	 *         is reached
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * 
	 * @param h
	 *            heuristic to be tested
	 */
	public void setHeuristic(Heuristic h) {
		heuristic = h;
	}

	/**
	 * 
	 * @param f
	 *            file to be analysed
	 */
	public void setFile(String f) {
		inputFile = f;
	}

	/**
	 * Specifies whether a chart to be displayed.
	 * 
	 * @param ch
	 *            true is the user needs a chart
	 */
	public void setChart(boolean ch) {
		chart = ch;
	}

	/**
	 * Sets the desired size of the memory.
	 * 
	 * @param t
	 *            the desired size of the memory
	 */
	public void setThreshold(int t) {
		threshold = t;

	}

	/**
	 * Sets the percentage of memory size to deallocate once the threshold has
	 * been reached.
	 * 
	 * @param p
	 *            percentage to deallocate form memory when the threshold is
	 *            reached
	 */
	public void setPercentage(int p) {
		percentage = p;
	}

	/**
	 * 
	 * @return true if the user specified that the want a chart to be displayed
	 */
	public boolean chart() {
		return chart;
	}

}
