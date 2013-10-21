package uk.ac.glasgow.etparser;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Class for specifying command line arguments and processing them for the
 * correct working of the program depending on the user's needs.
 * 
 * @author Emi
 * @author David R. White
 * 
 */
public class SimulateGC {

	/**
	 * Enum class representing all possible heuristics to test
	 */
	public enum Heuristic {
		RANDOM, FIRST, LAST, SMALLEST, LARGEST, LRU, MRU, GC 
	};
	
	private static String BATCH_SHORT = "b";
	private static String BATCH_LONG = "batch";
	private static String INPUT_SHORT = "f";
	private static String INPUT_LONG = "file";
	private static String RESUME_SHORT = "r";
	private static String RESUME_LONG = "resume";
	private static String CHART_SHORT = "c";
	private static String CHART_LONG = "chart";
	private static String HELP_SHORT = "h";
	private static String HELP_LONG = "help";
	private static String GC_SHORT = "gc";
	private static String GC_LONG = "garbage_collector";
	private static String THRESHOLD_SHORT = "t";
	private static String THRESHOLD_LONG = "threshold";
	private static String DELETION_SHORT = "d";
	private static String DELETION_LONG = "deletion";
    private static String COUNTER_LONG = "report_interval";


	public static void main(String args[]) {

		Options opts = createOptions();
				
		// Parse the arguments
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(opts, args);
		} catch (ParseException e) {
			System.err.println("Error parsing commandline options.");
			System.err.println(e);
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Print help if required
		if (cmd.hasOption(HELP_SHORT)) {
			printHelpAndExit(opts);
		}
			
		// If this is Batch, Pass to BatchExperiment
		if (cmd.hasOption(BATCH_SHORT)) {
			
			BatchExperiment bp = new BatchExperiment(cmd.getOptionValue(BATCH_SHORT));
			bp.setResume(cmd.hasOption(RESUME_SHORT));
			bp.processFile();
			
		// Otherwise, create a parameters object and call ETParser once.
		} else {
			
			ParameterSettings params = createParameters(cmd,opts);
			long startOfProcess = System.currentTimeMillis();
			
			outputStartMessage(params);
			
			ETParser etparser = new ETParser(params);
			etparser.processFile();
			etparser.printReport();
			
			long endOfProcess = System.currentTimeMillis();
			long timeTaken = endOfProcess - startOfProcess / 1000;
			System.out.println("Time taken " + timeTaken + " seconds.");
			
		}
		
	}
	
	private static void outputStartMessage(ParameterSettings parameters) {
		System.out.println("Executing single simulation.");
		System.out.println(parameters.toString());
	}
	
	/**
	 * Parse CLI parsed arguments to instantiate a parameters object.
	 * @param cmd
	 * @return Parameter settings used to invoke ETParser in non-batch mode.
	 * @author David R White
	 */
	private static ParameterSettings createParameters(CommandLine cmd, 
													  Options opts) {
		
		if (!cmd.hasOption(GC_SHORT) ||
				!cmd.hasOption(THRESHOLD_SHORT) ||
				!cmd.hasOption(DELETION_SHORT) ||
				!cmd.hasOption(INPUT_SHORT)) {
			printHelpAndExit(opts);
		}
		
		ParameterSettings settings = new ParameterSettings();
		
		settings.setHeuristic(Heuristic.valueOf(cmd.getOptionValue(GC_SHORT)));
		settings.setThreshold(Integer.parseInt(cmd.getOptionValue(THRESHOLD_SHORT)));
		settings.setPercentage(Integer.parseInt(cmd.getOptionValue(DELETION_SHORT)));
		settings.setChart(cmd.hasOption(CHART_SHORT));
		settings.setFile(cmd.getOptionValue(INPUT_SHORT));

        if (cmd.hasOption(COUNTER_LONG)) {
            settings.setCounterInterval(Integer.parseInt(cmd.getOptionValue(COUNTER_LONG)));
        }

		return settings;
		
	}


	/**
	 * Print help message and exit.
	 * @param opts
	 */
	private static void printHelpAndExit(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("CommandlineParser", opts);
		System.exit(0);
	}

	/**
	 * Add details about commandline options to a CLI Options object.
	 * @return Completed options object.
	 */
	private static Options createOptions() {			
		
		// Commandline options
		Options opts = new Options();

		// Batch mode - take parameters from XML file instead
		// If this is set, only batchfile option
		opts.addOption(BATCH_SHORT,BATCH_LONG,true,"Run a batch of experiments based on " +
				"a batch file.");

		// Input File
		opts.addOption(INPUT_SHORT,INPUT_LONG, true,  "Input Elephant Tracks .gz file.");
		
		// Should I Resume?
		opts.addOption(RESUME_SHORT,RESUME_LONG, false, "Resume based on output file.");

		// GUI Chart
		opts.addOption(CHART_SHORT,CHART_LONG, false, "Display a livechart.");
		
		// Help Message
		opts.addOption(HELP_SHORT,HELP_LONG, false, "Print help information.");

        // Counter report frequency - how often to report (number of events) progress
        opts.addOption(COUNTER_LONG, true, "Interval between progress reports (events). Default - no reporting.");

		// Heuristic to use
		String gcs  = "Garbage Collector. Possible garbage collectors are:";
		for (Heuristic heuristic : Heuristic.values()) {
			gcs += " " + heuristic.toString();
		}
		opts.addOption(GC_SHORT,GC_LONG, true, gcs);
							
		// Parameters for GC
		opts.addOption(THRESHOLD_SHORT,THRESHOLD_LONG, true, "Threshold to trigger GC (MB).");
		opts.addOption(DELETION_SHORT,DELETION_LONG, true, "% to deallocate at GC");
		
		return opts;
	
	}
		
}
