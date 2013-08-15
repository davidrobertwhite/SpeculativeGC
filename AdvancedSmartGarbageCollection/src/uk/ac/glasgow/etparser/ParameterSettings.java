package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.CommandParser.WayToDealWithErrors;
import uk.ac.glasgow.etparser.handlers.ErrorLogger;
import uk.ac.glasgow.etparser.handlers.EventHandler;

public class ParameterSettings {

	private boolean interactive = false;
	private boolean chart = false;
	private boolean intervals;
	private EventHandler errorLogger;
	private boolean statisticsLogger;
	private String inputFile;
	private int threshold;
	private int percentage;
	private Heuristic heuristic;
	private WayToDealWithErrors preaccess;
	private WayToDealWithErrors postaccess;
	private int intervalToUpdateChart;

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

	public void setIntervalToUpdateChart(int i) {
		intervalToUpdateChart = i;
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

	public boolean interactive() {
		return interactive;
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

	public int getChartIntervals() {
		return intervalToUpdateChart;
	}

	public void specifyIntervals() {
		intervals = true;
	}

	public boolean intervalsSpecified() {
		return intervals;
	}

	public void setErrorLogger() {
		errorLogger = new ErrorLogger();
	}

	public EventHandler getErrorLogger() {
		return errorLogger;
	}

	public void addStatisticsLogger(boolean b) {
		statisticsLogger = b;
	}

	public void setInteractive(boolean b) {
		interactive = b;
	}

	public boolean statisticsLogger() {
		return statisticsLogger;
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
