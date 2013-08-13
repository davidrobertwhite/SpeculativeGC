package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;

public class Results {
	private String benchmark;
	private Heuristic heuristic;
	private int threshold;
	private double percentage;
	private float errors;

	public Results() {

	}

	public Results(String b, Heuristic h, int th, double p, float e) {
		benchmark = b;
		heuristic = h;
		threshold = th;
		percentage = p;
		errors = e;
	}
	
	public String getBenchmark(){
		return benchmark;
		
	}
	
	public String getHeuristic(){
		return heuristic.toString();
	}
	
	public int getThreshold(){
		return threshold;
	}
	
	public double getPercentage(){
		return percentage;
	}
	public float getErrors(){
		return errors;
	}
	
	public String toString(){
		return benchmark+", "+heuristic+", "+threshold+", "+percentage+", "+errors;
	}

}
