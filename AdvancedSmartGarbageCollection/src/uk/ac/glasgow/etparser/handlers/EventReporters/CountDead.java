package uk.ac.glasgow.etparser.handlers.EventReporters;

import java.util.HashSet;
import java.util.Set;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.handlers.EventHandler;

/**
 * This class keeps track of all dead objects tried to be accessed illegally.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class CountDead implements EventHandler {
	/**
	 * Used for calculating the result as a percentage.
	 */
	public static final int PERCENTAGE = 100;

	/**
	 * Set of all objects accessed after their death.
	 */
	private Set<String> dead;

	/**
	 * Initializes the empty set of accessed dead objects.
	 */
	public CountDead() {
		dead = new HashSet<String>();
	}

	/**
	 * 
	 * @return the set of all objects accessed after their death.
	 */
	public Set<String> getDead() {
		return dead;
	}

	/**
	 * Receives an event as input and if it is an access of dead object it
	 * records it, otherwise it ignores it.
	 * 
	 * @param e
	 *            an event to be recorded
	 */
	@Override
	public synchronized void handle(Event e) {
		if (e.getCheck() != null && e.getCheck().equals(Check.DEAD)) {
			dead.add(e.getObjectID());
			// System.out.println("Object with id " + e.getObjectID()
			// + " is dead.");
		}

	}

	/**
	 * A method for calculating the percentage of objects that caused dead
	 * error.
	 * 
	 * @param totalObjectsInHeap
	 *            the total objects ever seen by the program
	 * @return the percentage of objects that caused dead error
	 */
	public float getErrors(int totalObjectsInHeap) {
		return (float) dead.size() / totalObjectsInHeap * PERCENTAGE;
	}

}
