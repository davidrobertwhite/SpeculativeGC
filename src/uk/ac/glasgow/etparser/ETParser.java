package uk.ac.glasgow.etparser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import uk.ac.glasgow.etparser.events.*;
import uk.ac.glasgow.etparser.handlers.*;
import uk.ac.glasgow.etparser.handlers.reporters.CountDead;

/**
 * This class parses a file into lines, keeps a list of event handlers and every
 * time an event occurs it notifies them. It also prints a report of the
 * percentage and kind of errors caused by attempt to access not born or dead
 * objects.
 * 
 * @author Emi
 * @version 1.0
 */
public class ETParser {
	
	/**
	 * Name of ET file to be read as input.
	 */
	private String inputFilename;
	
	/**
	 * List of registered objects to handle events.
	 */
	private List<EventHandler> handlers;
	/**
	 * This variable counts the lines read so far.
	 */
	private int lines;
	/**
	 * Counter for dead errors.
	 */
	private CountDead dead;
	
	/**
	 * This heap is analogous to the memory. It contains all the objects
	 * allocated and also keeps track of all the objects tried to be accessed at
	 * any point of the program.
	 */
	private Heap heap;

    /**
     * Report progress after a certain number of events processed.
     */
    private int reportInterval = 0;

    /*
        Last time progress was output to stdout.
     */
    private long lastReport = 0;

	/**
	 * Constructor initializing the ETParser which takes an ParameterSetting
	 * instance and does all its work using the the current settings.
	 * 
	 * @param p
	 *            the parameters specifying the way the parser to proceed
	 */

	public ETParser(ParameterSettings p) {
		
		inputFilename = p.getFile();
		
		handlers = new ArrayList<EventHandler>();
		lines = 0;
		SmartHeapFactory factory = new SmartHeapFactory();
		if (p.getHeuristic() != null) {
			heap = factory.createHeap(p.getHeuristic());
			if (heap != null) {
				((SmartHeap) heap).specifyPercentageToDeallocate(p
						.getPercentage());
				((SmartHeap) heap).specifyThreshold(p.getThreshold());
			}

		} else {
			heap = new Heap();
		}

        reportInterval = p.getCounterInterval();

		initialiseHandlers();

	}

	/**
	 * 
	 * For each line of the scanner create an event and notify the handlers for
	 * it.
	 */

	public synchronized void processFile() {
		
		GZIPInputStream input = null;
		
		try {
			input = new GZIPInputStream(new FileInputStream(inputFilename));
		} catch (Exception e) {
			System.err.println("Error opening input file: " + e);
			e.printStackTrace();
			System.exit(-1);
		}
		
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine()) {

			String nextLine = scanner.nextLine();
			lines++;
            if ((lines - lastReport) % reportInterval == 0) {
                System.out.println("Processed: " + lines + " lines");
                lastReport = lines;
            }
			Event event = new Event(nextLine);
			notifyHandlers(event);

		}
		scanner.close();

	}

	/**
	 * 
	 * @return the heap representing the current memory.
	 */
	public Heap getTheHeap() {
		return heap;
	}

	/**
	 * @return the number of lines read by the parser
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * A method that notifies all event handlers of the occurrence of an event.
	 * 
	 * @param e
	 *            : a new event read by the parser When an event occurs all
	 *            handlers are notified of it
	 */
	public void notifyHandlers(Event e) {
		for (EventHandler eh : handlers) {
			eh.handle(e);

		}
	}

	/**
	 * @param eh
	 *            event handler be added to the list of handlers
	 */
	public void registerHandler(EventHandler eh) {
		handlers.add(eh);
	}

	/**
	 * Adds to the list of handlers all necessary EventHandlers for the program.
	 */
	public void initialiseHandlers() {
		registerHandler(heap);
		dead = new CountDead();
		registerHandler(dead);
	}

	/**
	 * 
	 * @return the registered CountDead instance
	 */
	public CountDead getDead() {
		return dead;
	}

	/**
	 * 
	 * @return the percentage of objects that cause dead error
	 */
	public float getErrorCount() {
		return dead.getErrors(heap.numberOfTotalObjects());
	}

	/**
	 * Getter method for the registered handlers.
	 * 
	 * @return the list of currently registered handlers
	 */
	public List<EventHandler> getHandlers() {
		return handlers;
	}

	/**
	 * prints the final report from a single test to the console
	 */
	public void printReport() {
		System.out.println("Percentage of objects that caused postacess errors is " + getErrorCount());
	}

}
