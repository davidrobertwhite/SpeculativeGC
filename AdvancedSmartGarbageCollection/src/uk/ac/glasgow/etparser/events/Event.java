package uk.ac.glasgow.etparser.events;

import java.util.Scanner;

/**
 * Class consisi=ting of information about a single event such as type and time
 * of the event, object and its size participation in the event.
 * 
 * @author Emi
 * 
 */
public class Event {
	/**
	 * The id of the object participating in the event.
	 */
	protected String objectId;
	/**
	 * The type of the particular event.
	 */
	protected TypeOfEvent eventType;
	/**
	 * The size of the object participating in the event.
	 */
	private int size;

	/**
	 * Enumeration of the types of possible events.
	 * 
	 * @author Emi
	 * 
	 */
	public enum TypeOfEvent {
		ALLOCATION, DEATH, UPDATE, METHOD, R, EXCEPTION, OTHER
	}

	/**
	 * Enumeration whether the event is legal, meaning whether the participating
	 * object is alive, allocated in memory and not dead.
	 * 
	 * @author Emi
	 * 
	 */
	public enum Check {
		CREATION, CREATED, NOTBORN, LEGAL, DEAD
	};

	/**
	 * Hold the category in which event falls so that it be treated(handled)
	 * correctly
	 */
	private Check check;

	/**
	 * Creates an event
	 * 
	 * @param line
	 *            a single line of the parsed input.
	 */

	public Event(String line) {

		Scanner scanner = new Scanner(line);
		String typeOfEvent = scanner.next();
		eventType = typeConverter(typeOfEvent);

		switch (eventType) {
		case ALLOCATION:
		case DEATH:
		case R:
			objectId = scanner.next();
			break;
		// if it is T or H in ET2
		case EXCEPTION:
			scanner.next();
			scanner.next();
			objectId = scanner.next();
			break;
		case METHOD:
		case UPDATE:
			scanner.next();
			objectId = scanner.next();
			break;

		case OTHER:
		default:
			break;
		}
		if (eventType == TypeOfEvent.ALLOCATION) {
			String s = scanner.next();
			size = Integer.parseInt(s.trim(), 16);

		} else {

			size = 0;
		}
		scanner.close();
	}

	/**
	 * 
	 * @return the size of the object participating in the current event.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Converter from String to TypeOfEvent
	 * 
	 * @param s
	 *            the type of event in a string format
	 * @return the type of event in TypeOfEvent format
	 */
	private TypeOfEvent typeConverter(String s) {
		if (s.equalsIgnoreCase("a") || s.equalsIgnoreCase("i")
				|| s.equalsIgnoreCase("n") || s.equalsIgnoreCase("v")
				|| s.equalsIgnoreCase("p")) {
			return TypeOfEvent.ALLOCATION;
		} else if (s.equalsIgnoreCase("u")) {
			return TypeOfEvent.UPDATE;

		} else if (s.equalsIgnoreCase("d")) {
			return TypeOfEvent.DEATH;

		} else if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("h")) {
			return TypeOfEvent.EXCEPTION;
		} else if (s.equalsIgnoreCase("r")) {
			return TypeOfEvent.R;
		} else if (s.equalsIgnoreCase("m") || s.equalsIgnoreCase("e")
				|| s.equalsIgnoreCase("x")) {
			return TypeOfEvent.METHOD;
		} else
			return TypeOfEvent.OTHER;
	}

	/**
	 * 
	 * @return the id of the object involved in the event
	 */
	public String getObjectID() {
		return objectId;
	}

	/**
	 * 
	 * @return the type of the current event
	 */
	public TypeOfEvent getTypeOfEvent() {
		return eventType;
	}

	/**
	 * 
	 * @return the legality of the event
	 */
	public Check getCheck() {
		return check;
	}

	/**
	 * Sets the legality of the event
	 * 
	 * @param ch
	 *            the legality of the event
	 */
	public void setCheck(Check ch) {
		check = ch;
	}

}
