package uk.ac.glasgow.etparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.ParameterSettings;

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
	private HashMap<ParameterSettings, Results> previousResults;
	private boolean resume;

	public BatchParser(String name) throws IOException {
		csvFile = name;
		previousResults = new HashMap<ParameterSettings, Results>();

	}

	public void doExperiments() throws IOException {

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
						boolean contains = false;
						for (ParameterSettings ps : previousResults.keySet()) {
							if (ps.equals(param)) {
								contains = true;
								break;
							}
						}
						if (!contains) {
							ETParser parser = new ETParser(param);
							parser.processFile();
							Results result = new Results(param);
							previousResults.put(param, result);
							System.out.println(result);
							printWriter.println(result.toString());
							printWriter.flush();

						}

					}

				}
			}
		}
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
			if (resume) {
				Scanner scan = new Scanner(f);

				if (scan.hasNextLine() && headerCorrect(scan.nextLine())) {
					System.out.println("header");
					while (scan.hasNextLine()) {
						String nextLine = scan.nextLine();
						Scanner lineScanner = new Scanner(nextLine);
						lineScanner.useDelimiter(", ");
						ParameterSettings p = new ParameterSettings(
								lineScanner.next(),
								CommandParser
										.heuristicEnumConverter(lineScanner
												.next()),
								Integer.parseInt(lineScanner.next()),
								(int) Double.parseDouble(lineScanner.next()));

						Results r = new Results(p, Float.parseFloat(lineScanner
								.next()));
						previousResults.put(p, r);

					}
					;
				} else {
					System.out
							.println("Error- trying to modify a file in different format!!!");
					System.exit(0);
				}

			} else {
				csvFile = csvFile
						+ " "
						+ new SimpleDateFormat("yyyyMMdd_HHmmss")
								.format(Calendar.getInstance().getTime())
						+ ".csv";
			}
			fileWriter = new FileWriter(csvFile, true);
			printWriter = new PrintWriter(fileWriter);
		}

		else {
			fileWriter = new FileWriter(csvFile);
			printWriter = new PrintWriter(fileWriter);
			addHeader();
		}

		doExperiments();
		printWriter.close();
		fileWriter.close();

	}

	public void setResume(boolean r) {
		resume = r;
	}

	private boolean headerCorrect(String s) {
		Scanner scan = new Scanner(s);
		scan.useDelimiter(", ");

		return scan.next().equalsIgnoreCase("benchmark")
				&& scan.next().equalsIgnoreCase("heuristic")
				&& scan.next().equalsIgnoreCase("threshold")
				&& scan.next().equalsIgnoreCase("percentage")
				&& scan.next().equalsIgnoreCase("errors") && !scan.hasNext();

	}

}
