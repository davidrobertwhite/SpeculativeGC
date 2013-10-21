package uk.ac.glasgow.etparser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import uk.ac.glasgow.etparser.SimulateGC.Heuristic;
import uk.ac.glasgow.etparser.ParameterSettings;

/**
 * Class for testing heuristics. Outputs the results to a specified file for
 * further analysis.
 * 
 * @author Emi
 * 
 */
public class BatchExperiment {
	/**
	 * Thresholds to be tested, in other words- the size of the memory
	 */
	private int[] thresholds = { 50000, 75000, 100000, 125000, 150000, 175000,
			200000 };
	/**
	 * Percentage of the memory size to deallocate after the specified threshold
	 * has been reached.
	 */
	private int[] percentage = { 5, 10, 15, 20, 25, 30, 50, 100 };
	/**
	 * Files to be tested
	 */
	private String[] files = { "C://Users//Emi//Desktop//traces//ET0.6//java-mandelbrot_500.trace.gz" };


	/**
	 * The file in which the results will be printed.
	 */
	private String csvFile;
	/**
	 * Hash map storing existing results in the specified output file if the
	 * resume mode is on, i.e. if the user chose that he wants to resume an
	 * already existing file this hashmap stores the results already found in
	 * the file.
	 */
	private HashMap<ParameterSettings, Results> previousResults;
	/**
	 * Specifies whether the user wants to resume writing to an already existing
	 * file or create a new one.
	 */
	private boolean resume;
	/**
	 * Event pool for scheduling the threads to execute.
	 */
	private ScheduledThreadPoolExecutor eventPool;
	/**
	 * Holds the name of the file to which to send the results.
	 */
	private ETParserOutputFile outputWriter;

	/**
	 * Constructor initialising the BatchParser
	 * 
	 * @param name
	 *            of the file to which to send the results
	 * @throws IOException
	 */
	public BatchExperiment(String name) {
		csvFile = name;
		previousResults = new HashMap<ParameterSettings, Results>();
		eventPool = new ScheduledThreadPoolExecutor(10);

	}

	/**
	 * Do all the experiments, creating threads for all experiments and output
	 * to the specified file.
	 * 
	 * @throws IOException
	 */
	public void doExperiments() {

		for (String file : files) {
			System.out.println(file);
			for (Heuristic h : SimulateGC.Heuristic.values()) {
				System.out.println(h);
				for (int threshold : thresholds) {
					System.out.println(threshold);
					for (int p : percentage) {
						System.out.println(p);

						ParameterSettings param = new ParameterSettings(file,
								h, threshold, p, 0);
						boolean contains = false;
						for (ParameterSettings ps : previousResults.keySet()) {
							if (ps.equals(param)) {
								contains = true;
								break;
							}
						}
						// have a thread factory and create a new thread that I
						// add to a predefined list of threads.
						// all this method will do is return the list of threads
						// in the main method I invoke
						// addThreadsToPool(List<Threads> l) method
						// below the main method I define this method
						if (!contains) {
							// the code for a new thread put into a method/class
							ETThread thread = new ETThread(param,outputWriter);
							addToPool(thread);

						}

					}

				}
			}
		}
	}

	/**
	 * Schedule a new thread to the eventPool.
	 * 
	 * @param thread
	 *            the thread to be scheduled
	 */
	private void addToPool(Runnable thread) {
		eventPool.schedule(thread, 0, TimeUnit.SECONDS);
	}

	/**
	 * Determines whether the file exists depending on this it processes it in
	 * the specified by the user way.
	 * 
	 * @throws IOException
	 */
	public void processFile() {
		File f = new File(csvFile);

		if (f.exists()) {
			if (resume) {
				
				Scanner scan= null;
				try {
					scan = new Scanner(f);
				} catch (Exception e) {
					System.err.println("Error scanning output file " + e);
					e.printStackTrace();
					System.exit(-1);
				}

				if (scan.hasNextLine() && headerCorrect(scan.nextLine())) {
					System.out.println("header");
					while (scan.hasNextLine()) {
						String nextLine = scan.nextLine();
						Scanner lineScanner = new Scanner(nextLine);
						lineScanner.useDelimiter(", ");
						ParameterSettings p = new ParameterSettings(
								lineScanner.next(),
								SimulateGC.Heuristic.valueOf(lineScanner.next()),
								Integer.parseInt(lineScanner.next()),
								(int) Double.parseDouble(lineScanner.next()),0);

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
			outputWriter = new ETParserOutputFile(csvFile, true);
		}

		else {
			outputWriter = new ETParserOutputFile(csvFile, false);
		}

		doExperiments();


	}

	/**
	 * Specify that you want to resume writing results to an existing file.
	 * 
	 * @param r
	 *            true if you want to resume writing to the existing file
	 */
	public void setResume(boolean r) {
		resume = r;
	}

	/**
	 * Checks whether the header of the existing file is the same as desired for
	 * making sure that we are writing to the correct file.
	 * 
	 * @param s
	 *            the header of the existing file
	 * @return true if the file is in the correct format
	 */
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
