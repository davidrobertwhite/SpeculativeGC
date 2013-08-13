package uk.ac.glasgow.etparser;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.ParameterSettings;
import uk.ac.glasgow.etparser.handlers.EventReporters.CountDead;

public class BatchParser {

	private int[] thresholds = { 50000
	  , 75000, 100000, 125000, 150000,
	  175000, 200000
	 	};
	private int[] percentage = { 5, 10, 15, 20, 25, 30, 50, 100 };
	private String[] files = { "C://Users//Emi//Desktop//traces//ET0.6//java-mandelbrot_500.trace.gz" };
	private Heuristic[] heuristics = { Heuristic.FIRST,
	  Heuristic.LEASTRECENTLYUSED, Heuristic.GC, Heuristic.LARGEST,
	  Heuristic.SMALLEST, Heuristic.RANDOM, Heuristic.MOSTRECENTLYUSED,
	  Heuristic.LAST
	};
	private FileWriter fileWriter;
	private PrintWriter printWriter;

	public BatchParser() {
		try {
			fileWriter = new FileWriter("results2.csv");
		} catch (IOException e) {
			System.out.println("IOException");
			System.exit(0);
		}
		printWriter = new PrintWriter(fileWriter);

	}

	public void doExperiments() throws IOException {
		addHeader();
		for (String file : files) {
			System.out.println(file);
			for (Heuristic h : heuristics) {
				System.out.println(h);
				for (int threshold : thresholds) {
					System.out.println(threshold);
					for (int p : percentage) {
						System.out.println(p);

						ParameterSettings param = new ParameterSettings(file,
								h, threshold, p);
						ETParser parser = new ETParser(param);
						parser.processFile();
						Results result = new Results(file, h, threshold, p,
								CountDead.getErrors());
						System.out.println(result);
						printWriter.println(result.toString());
						printWriter.flush();

						//addColumn(result);

						// generate a Report object
						// output param values and report results to a single
						// line of the CSV output file

					}
				}
			}
		}
		printWriter.close();
		fileWriter.close();

	}

	private void addHeader() throws IOException{
		printWriter.print("Benchmark");
		printWriter.print(", ");
		printWriter.print("Heuristic");
		printWriter.print(", ");
		printWriter.print("Threshold");
		printWriter.print(", ");
		printWriter.print("Percentage");
		printWriter.print(", ");
		printWriter.println("Errors");

		
	}

}
