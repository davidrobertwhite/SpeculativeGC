package uk.ac.glasgow.etparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.ParameterSettings;
import uk.ac.glasgow.etparser.handlers.EventReporters.CountDead;

public class BatchParser {

	private int[] thresholds = { 50000, 75000, 100000, 125000, 150000, 175000,
			200000 };
	private int[] percentage = { 5, 10, 15, 20, 25, 30, 50, 100 };
	private String[] files = { "C://Users//Emi//Desktop//traces//ET0.6//java-mandelbrot_500.trace.gz" };
	private Heuristic[] heuristics = { Heuristic.FIRST,
			Heuristic.LEASTRECENTLYUSED, Heuristic.GC, Heuristic.LARGEST,
			Heuristic.SMALLEST, Heuristic.RANDOM, Heuristic.MOSTRECENTLYUSED,
			Heuristic.LAST };
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	private String csvFile;
	private HashMap<ParameterSettings,Results> previousResults;

	public BatchParser(String name) {
		csvFile = name;
		previousResults=new HashMap<ParameterSettings,Results>();
		// try {
		// fileWriter = new FileWriter(CSVFILE);
		// } catch (IOException e) {
		// System.out.println("IOException");
		// System.exit(0);
		// }
		// printWriter = new PrintWriter(fileWriter);

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
				
						if(previousResults.containsKey(param)){
							continue;
						}
						Results result = new Results(param);
						previousResults.put(param, result);
						System.out.println(result);
						printWriter.println(result.toString());
						printWriter.flush();

						// addColumn(result);

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

	private void addHeader() throws IOException {
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

	public void processFile() throws IOException {
		File f = new File(csvFile);
		if (f.exists()) {
			Scanner scan = new Scanner(csvFile);

			if (scan.hasNextLine() && headerCorrect(scan.nextLine())) {
				while (scan.hasNextLine()) {
					String nextLine = scan.nextLine();
					Scanner lineScanner = new Scanner(nextLine);
					ParameterSettings p = new ParameterSettings(
							lineScanner.next(),CommandParser.heuristicEnumConverter( lineScanner.next()),
							lineScanner.nextInt(), lineScanner.nextInt());
					Results r=new Results(p);
					previousResults.put(p,r);
				}
				;
			}
			else {
				System.out.println("Error- trying to modify a file in different format!!!");
				System.exit(0);
			}



		}
		doExperiments();
		
	}

	private boolean headerCorrect(String s) {
		Scanner scan = new Scanner(s);
		return scan.next().equalsIgnoreCase("benchmark")
				&& scan.next().equalsIgnoreCase("heuristic")
				&& scan.next().equalsIgnoreCase("threshold")
				&& scan.next().equalsIgnoreCase("percentage")
				&& scan.next().equalsIgnoreCase("errors");
	}

}
