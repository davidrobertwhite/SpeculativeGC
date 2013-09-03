package uk.ac.glasgow.etparser;

import java.io.IOException;
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
 * 
 */
public class CommandParser {

	/**
	 * Enum class representing all possible heuristics to test
	 * 
	 * @author Emi
	 * 
	 */
	public enum Heuristic {
		FIRST, LEASTRECENTLYUSED, GC, LARGEST, SMALLEST, RANDOM, MOSTRECENTLYUSED, LAST
	};

	public static void main(String args[]) {
		ParameterSettings settings = new ParameterSettings();

		Options options = new Options();

		// First parameter is the option name
		// Second parameter is whether this is a switch or argument
		// Third is a description
		options.addOption("ch", false, "display a livechart");
		options.addOption("gz", true, "input gz file");
		options.addOption("h", false, "ask for help");
		options.addOption(
				"heuristic",
				true,
				"choose a heuristic for deleting objects: 'fifo' for first in first out, "
						+ "'lifo' for last in first ot, 'lru' for least recently used,"
						+ " 'mru' for most recently used, 'gc' for normal garbage collection,"
						+ " 'r' for random, 'ss' for smallest size first, 'ls' for largest size first.");
		options.addOption("t", true, "enter the threshold");
		options.addOption("p", true,
				"enter the percentage you want to deallocate");

		options.addOption("rsm", false, "resume writing to existing file");
		options.addOption(
				"batch",
				true,
				"run the program for the purpose of multiple experiments. specify the file to output in.");
		options.addOption("et", false,
				"run the program with concrete parameters");
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

		if (cmd.hasOption("heuristic")) {
			settings.setHeuristic(heuristicEnumConverter(cmd
					.getOptionValue("heuristic")));

			if (cmd.hasOption("t")) {
				settings.setThreshold(Integer.parseInt(cmd.getOptionValue("t")));
			}
			if (cmd.hasOption("p")) {
				settings.setPercentage(Integer.parseInt(cmd.getOptionValue("p")));
			}
		}

		// Now we can interrogate them
		settings.setChart(cmd.hasOption("ch"));

		// Want at least time or date
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CommandlineParser", options);
			System.exit(0);

		}

		long startOfProcess = System.currentTimeMillis();
		try {


			if (cmd.hasOption("gz")) {
				settings.setFile(cmd.getOptionValue("gz"));

			}

			if (cmd.hasOption("et")) {
				ETParser etparser = new ETParser(settings);
				etparser.processFile();
				etparser.printReport();
			}
			if (cmd.hasOption("batch")) {
				BatchParser bp = new BatchParser(cmd.getOptionValue("batch"));
				bp.setResume(cmd.hasOption("rsm"));
				bp.processFile();
			}
			long endOfProcess = System.currentTimeMillis();
			long timeTakenInMillisecs = endOfProcess - startOfProcess;
			long timeTakenInSeconds = timeTakenInMillisecs / 1000;
			long timeTakenInMinutes = timeTakenInSeconds / 60;
			System.out.println("Time taken " + timeTakenInMinutes + " minutes");

		}

		catch (IOException io) {
			System.out.println("IOException" + io);
			System.exit(0);
		}

	}

	/**
	 * Method for conversion of Strings to enum Heuristic.
	 * 
	 * @param s
	 *            string to be converted into it's enum equivalent of heuristic
	 * @return
	 */

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
