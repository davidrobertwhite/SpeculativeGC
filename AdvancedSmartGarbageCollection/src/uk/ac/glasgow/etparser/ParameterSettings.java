package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.CommandParser.WayToDealWithErrors;

public class ParameterSettings {

	private boolean chart = false;
	private boolean intervals;
	private String inputFile;
	private int threshold;
	private int percentage;
	private Heuristic heuristic;
	private WayToDealWithErrors preaccess;
	private WayToDealWithErrors postaccess;


	public ParameterSettings(String file, Heuristic h, int threshold,
			int percentage) {
		heuristic = h;
		inputFile = file;
		this.threshold = threshold;
		this.percentage = percentage;
	}

	public ParameterSettings() {

	}

	public String getFile() {
		return inputFile;
	}

	public Heuristic getHeuristic() {
		return heuristic;
	}

	public int getThreshold() {
		return threshold;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setHeuristic(Heuristic h) {
		heuristic = h;
	}

	public void setFile(String f) {
		inputFile = f;
	}

	public void setChart(boolean ch) {
		chart = ch;
	}


	public void setThreshold(int t) {
		threshold = t;

	}

	public void setPercentage(int p) {
		percentage = p;
	}

	public boolean chart() {
		return chart;
	}


	public WayToDealWithErrors getPreaccess() {
		return preaccess;
	}

	public WayToDealWithErrors getPostAccess() {
		return postaccess;
	}

	public void setPreAceess(WayToDealWithErrors pre) {
		preaccess = pre;
	}

	public void setPostAccess(WayToDealWithErrors post) {
		postaccess = post;
	}


	public void specifyIntervals() {
		intervals = true;
	}

	public boolean intervalsSpecified() {
		return intervals;
	}



	public String toString() {
		return inputFile + heuristic + threshold + " " + percentage;
	}

	public boolean equals(ParameterSettings ps) {
		return inputFile.equalsIgnoreCase(ps.inputFile)
				&& heuristic.equals(ps.heuristic) && threshold == ps.threshold
				&& percentage == ps.percentage;
	}
}
