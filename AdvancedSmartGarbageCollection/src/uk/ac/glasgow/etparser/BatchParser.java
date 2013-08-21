package uk.ac.glasgow.etparser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
//	private FileWriter fileWriter;
//	private PrintWriter printWriter;
	private String csvFile;
	private HashMap<ParameterSettings, Results> previousResults;
	private boolean resume;
	private ScheduledThreadPoolExecutor eventPool; 
	private ETParserOutputFile outputWriter;

	public BatchParser(String name) throws IOException {
		csvFile = name;
		//outputWriter=new ETParserOutputFile(name, false);
		previousResults = new HashMap<ParameterSettings, Results>();
		eventPool = new ScheduledThreadPoolExecutor(10);

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
						// have a thread factory and create a new thread that I add to a predefined list of threads.
						// all this method will do is return the list of threads 
						//in the main method I invoke addThreadsToPool(List<Threads> l) method 
					// below the main method I define this method 
						if (!contains) {
							//the code for a new thread put into a method/class
							ETParser parser = new ETParser(param);
							ETThread thread=new ETThread(parser,param,outputWriter);
							addToPool(thread);


						}

					}

				}
			}
		}
	}
	
	
	private void addToPool(Runnable thread){
		eventPool.schedule(thread, 0, TimeUnit.SECONDS);
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
//				outputWriter.setFile(outputWriter.getFile()+						+ " "
//						+ new SimpleDateFormat("yyyyMMdd_HHmmss")
//								.format(Calendar.getInstance().getTime())
//						+ ".csv");
				csvFile = csvFile
						+ " "
						+ new SimpleDateFormat("yyyyMMdd_HHmmss")
								.format(Calendar.getInstance().getTime())
						+ ".csv";
			}
			outputWriter=new ETParserOutputFile(csvFile, true);
		}

		else {
			outputWriter=new ETParserOutputFile(csvFile, false);
			//addHeader();
		}

		doExperiments();
//		printWriter.close();
//		fileWriter.close();

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
