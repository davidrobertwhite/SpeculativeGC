package uk.ac.glasgow.etparser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import uk.ac.glasgow.etparser.handlers.ErrorLogger;
import uk.ac.glasgow.etparser.handlers.Heap;
import uk.ac.glasgow.etparser.handlers.SmartHeap;
import uk.ac.glasgow.etparser.handlers.SmartHeapFIFO;
import uk.ac.glasgow.etparser.handlers.SmartHeapGC;
import uk.ac.glasgow.etparser.handlers.SmartHeapLIFO;
import uk.ac.glasgow.etparser.handlers.SmartHeapLargestSize;
import uk.ac.glasgow.etparser.handlers.SmartHeapLeastRecentlyUsed;
import uk.ac.glasgow.etparser.handlers.SmartHeapMostRecentlyUsed;
import uk.ac.glasgow.etparser.handlers.SmartHeapRandom;
import uk.ac.glasgow.etparser.handlers.SmartHeapSmallestSize;

public class CommandParser {

	public enum WayToDealWithErrors {
		IGNORE, MOVE
	};
	public enum Heuristic {
		FIRST, LEASTRECENTLYUSED, GC, LARGEST, SMALLEST, RANDOM, MOSTRECENTLYUSED, LAST
	};

	
	private static ParameterSettings settings=new ParameterSettings();

	public static void main(String args[]) {

		Options options = new Options();

		// First parameter is the option name
		// Second parameter is whether this is a switch or argument
		// Third is a description
		options.addOption("i", false, "run the program interactively");
		options.addOption("ch", false, "display a livechart");
		options.addOption("r", true, "number of times to repeat the run");
		options.addOption("f", true, "input file name");
		options.addOption("gz", true, "input gz file");
		options.addOption("unborn", true,
				"how to deal with unborn accesses: ignore or move");
		options.addOption("dead", true, "how to deal with dead accesses");
		options.addOption("h", false, "ask for help");
		options.addOption(
				"heuristic",
				true,
				"choose a heuristic for deleting objects: 'fifo' for first in first out, " +
				"'lifo' for last in first ot, 'lru' for least recently used," +
				" 'mru' for most recently used, 'gc' for normal garbage collection," +
				" 'r' for random, 'ss' for smallest size first, 'ls' for largest size first.");
		options.addOption("el", false,
				"do you want an error logger to show errors?");
		options.addOption("sl", false,
				"do you want an statistics logger to record the statistics?");
		options.addOption("t", true, "enter the threshold");
		options.addOption("p", true,
				"enter the percentage you want to deallocate");
		options.addOption("ei", true,
				"specify the event interval at which to update the chart");

		// Parse the arguments
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Error parsing commandline options.");
			System.err.println(e);
			e.printStackTrace();
			System.exit(-1);
		}

		int reps = 1;
		if (cmd.hasOption("r")) {
			reps = Integer.parseInt(cmd.getOptionValue("r"));
		}

		if (cmd.hasOption("unborn")) {
			settings.preaccess = wayToDealEnumConverter(cmd.getOptionValue("unborn"));
		}

		if (cmd.hasOption("dead")) {
			settings.postaccess = wayToDealEnumConverter(cmd.getOptionValue("dead"));
		}

		if (cmd.hasOption("el")) {
			settings.errorLogger = new ErrorLogger();
		}

		if (cmd.hasOption("heuristic")) {
			settings.heuristic = heuristicEnumConverter(cmd.getOptionValue("heuristic"));
			switch (settings.heuristic) {
			case FIRST:
				settings.heap = new SmartHeapFIFO();
				break;
			case LEASTRECENTLYUSED:
				settings.heap = new SmartHeapLeastRecentlyUsed();
				break;
			case GC:
				settings.heap = new SmartHeapGC();
				System.out.println("gc");
				break;
			case LAST:
				settings.heap = new SmartHeapLIFO();
				break;
			case MOSTRECENTLYUSED:
				settings.heap = new SmartHeapMostRecentlyUsed();
				break;
			case SMALLEST:
				settings.heap = new SmartHeapSmallestSize();
				break;
			case LARGEST:
				settings.heap = new SmartHeapLargestSize();
				break;
			case RANDOM:
				settings.heap = new SmartHeapRandom();
				break;
			}
			if (cmd.hasOption("t")) {

				((SmartHeap) settings.heap).specifyThreshold(Integer.parseInt(cmd
						.getOptionValue("t")));
			}
			if (cmd.hasOption("p")) {
				((SmartHeap) settings.heap).specifyPercentageToDeallocate(Integer
						.parseInt(cmd.getOptionValue("p")));
			}
		}
		if (!cmd.hasOption("heuristic")) {
			settings.heap = new Heap();
		}

		// Now we can interrogate them
		settings.interactive = cmd.hasOption("i");
		settings.chart = cmd.hasOption("ch");
		settings.help = cmd.hasOption("h");
		settings.statisticsLogger = cmd.hasOption("sl");

		// Want at least time or date
		if (settings.help) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CommandlineParser", options);
			System.exit(0);

		}
//		if (!cmd.hasOption("f") && (!cmd.hasOption("gz"))) {
//			HelpFormatter formatter = new HelpFormatter();
//			formatter.printHelp("CommandlineParser", options);
//			System.out.println("Please enter a filename to process");
//
//		}

		for (int i = 0; i < reps; i++) {
			if (settings.interactive) {
				// System.out.println(tf.format(new Date()));
				displayChoices();
				askForPreaccess();
				askForPostaccess();
			}

		}
		if (settings.chart) {
			settings.heap.createChart();
			if (cmd.hasOption("ei")) {
				settings.heap.specifyWhenToUpdateTheChart(Integer.parseInt(cmd
						.getOptionValue("ei")));

			}
		}

		long startOfProcess = System.currentTimeMillis();
		try {

			if (cmd.hasOption("f")) {
				settings.inputFile = cmd.getOptionValue("f");
				settings.fileStream = new FileInputStream(settings.inputFile);
			}

			if (cmd.hasOption("gz")) {
				settings.inputFile = cmd.getOptionValue("gz");
				settings.fileStream = new GZIPInputStream(new FileInputStream(settings.inputFile));
			}

			ETParser etparser = new ETParser(settings);
			if (settings.errorLogger != null) {
				etparser.registerHandler(settings.errorLogger);

			}
			if (settings.statisticsLogger) {
				etparser.addStatsLogger();
			}
//			etparser.processFile();
//			etparser.printReport();
			//settings.fileStream.close();
			BatchParser bp=new BatchParser();
			bp.doExperiments();
			long endOfProcess = System.currentTimeMillis();
			long timeTakenInMillisecs = endOfProcess - startOfProcess;
			long timeTakenInSeconds = timeTakenInMillisecs / 1000;
			long timeTakenInMinutes = timeTakenInSeconds / 60;
			long linesPerSecond = etparser.getLines() / timeTakenInSeconds;

			System.out.println("Time taken " + timeTakenInMinutes + " minutes");
			System.out.println("The program reads " + linesPerSecond
					+ " lines per second");
		}

		catch (IOException io) {
			System.out.println("IOException" + io);
			System.exit(0);
		}

	}

	private static void displayChoices() {

		System.out
				.println("Hello, dear user! Before you start the smart garbage collector simulator"
						+ " you must choose how to deal with pre-access and post-access errors.");
		System.out.println();
	}

	private static void askForPreaccess() {
		System.out.println("First choose dealing with pre-access errors:");
		System.out.println();
		System.out.println("Enter 'IGNORE' to ignore them.");
		System.out
				.println("Enter 'MOVE' to allocate them at their first access.");
		System.out.println();
		Scanner scanner = new Scanner(System.in);
		String preAccess = scanner.nextLine();
		while (!preAccess.equalsIgnoreCase("IGNORE")
				&& !preAccess.equalsIgnoreCase("MOVE")) {
			System.out.println("Please enter a valid option");
			preAccess = scanner.next();
		}
		scanner.close();

		System.out.println();
		settings.preaccess = wayToDealEnumConverter(preAccess);
	}

	private static void askForPostaccess() {

		System.out.println("Now choose dealing with post-access errors");
		System.out.println();
		System.out.println("Enter 'Ignore' to ignore them.");
		System.out
				.println("Enter 'Move' to kill objects at the end of the program.");
		System.out.println("Enter 'Don't count' not to count these errors");
		Scanner scanner = new Scanner(System.in);
		String postAccess = scanner.nextLine();
		while (!postAccess.equalsIgnoreCase("IGNORE")
				&& !postAccess.equalsIgnoreCase("MOVE")) {
			System.out.println("Please enter a valid option");
			postAccess = scanner.next();
		}
		settings.postaccess = wayToDealEnumConverter(postAccess);
		scanner.close();
	}

	public static WayToDealWithErrors wayToDealEnumConverter(String s) {
		if (s.equalsIgnoreCase("IGNORE") || s.equalsIgnoreCase("i")) {
			return WayToDealWithErrors.IGNORE;
		} else if (s.equalsIgnoreCase("MOVE") || s.equalsIgnoreCase("m")) {
			return WayToDealWithErrors.MOVE;
		}
		return null;
	}

	public static Heuristic heuristicEnumConverter(String s) {
		if (s.equalsIgnoreCase("first") || s.equalsIgnoreCase("fifo")) {
			return Heuristic.FIRST;
		} else if (s.equalsIgnoreCase("least") || s.equalsIgnoreCase("lru")) {
			return Heuristic.LEASTRECENTLYUSED;
		} else if (s.equalsIgnoreCase("gc")) {
			return Heuristic.GC;
		} else if (s.equalsIgnoreCase("most") || s.equalsIgnoreCase("mru")) {
			return Heuristic.MOSTRECENTLYUSED;
		} else if (s.equalsIgnoreCase("smallest") || s.equalsIgnoreCase("ss")) {
			return Heuristic.SMALLEST;
		} else if (s.equalsIgnoreCase("largest") || s.equalsIgnoreCase("ls")) {
			return Heuristic.LARGEST;
		} else if (s.equalsIgnoreCase("random") || s.equalsIgnoreCase("r")) {
			return Heuristic.RANDOM;
		} else if (s.equalsIgnoreCase("last") || s.equalsIgnoreCase("lifo")) {
			return Heuristic.LAST;
		}
		return null;

	}

}
