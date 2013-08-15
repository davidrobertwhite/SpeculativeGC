package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.handlers.EventReporters.CountDead;

public class Results {
	private String benchmark;
	private Heuristic heuristic;
	private int threshold;
	private double percentage;
	private float errors;

	public Results() {

	}

	public Results(ParameterSettings p) {
		benchmark = p.getFile();
		heuristic = p.getHeuristic();
		threshold = p.getThreshold();
		percentage = p.getPercentage();
		errors = CountDead.getErrors();
	}

	public Results(ParameterSettings p, float e) {
		benchmark = p.getFile();
		heuristic = p.getHeuristic();
		threshold = p.getThreshold();
		percentage = p.getPercentage();
		errors = e;
	}

	public String getBenchmark() {
		return benchmark;

	}

	public String getHeuristic() {
		return heuristic.toString();
	}

	public int getThreshold() {
		return threshold;
	}

	public double getPercentage() {
		return percentage;
	}

	public float getErrors() {
		return errors;
	}

	public String toString() {
		return benchmark + ", " + heuristic + ", " + threshold + ", "
				+ percentage + ", " + errors;
	}

}
