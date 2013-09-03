package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

/**
 * Class encapsulating an Object, its characteristics and life status.
 * 
 * @author Emi
 * 
 */

public class ObjectStatus {
	/**
	 * The size of an object.
	 */

	private int size;
	/**
	 * The time the object was last accessed.
	 */

	private int timeOfLastEvent;
	/**
	 * The id of the object.
	 */

	private String id;
	/**
	 * True if the object was born.
	 */

	private boolean born;
	/**
	 * True if the object died.
	 */

	private boolean dead;

	private TypeOfEvent lastEvent;

	/**
	 * 
	 * @param t
	 *            time of last event
	 * @param id
	 *            of the object
	 */

	public ObjectStatus(int t, String id, TypeOfEvent event) {
		timeOfLastEvent = t;
		this.id = id;
		lastEvent = event;
		born = false;
		dead = false;
	}

	/**
	 * 
	 * @return the size of an object
	 */

	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of an object.
	 * 
	 * @param size
	 *            to be assigned to the object.
	 */

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 
	 * @return the time of last event
	 */

	public int getTimeOfLastEvent() {
		return timeOfLastEvent;
	}

	/**
	 * 
	 * @return the id of the object
	 */

	public String getID() {
		return id;
	}

	public TypeOfEvent getLastEvent() {
		return lastEvent;
	}

	/**
	 * Updates the last access of an object.
	 * 
	 * @param time
	 *            to be assigned to the timeOfLatEvent
	 * @param event
	 *            the type of the lst event
	 */

	public void updateEvent(int time, TypeOfEvent event) {
		if (lastEvent == TypeOfEvent.DEATH) {
			return;

		}
		this.timeOfLastEvent = time;
		lastEvent = event;

	}

	/**
	 * State that the object is allocated into memory and is live.
	 */

	public void giveBirth() {
		born = true;
	}

	/**
	 * State that the object is no longer live.
	 */

	public void kill() {
		dead = true;
	}

	/**
	 * 
	 * @return true if the object has been born.
	 */

	public boolean isBorn() {
		return born;
	}

	/**
	 * 
	 * @return true if the object is dead
	 */

	public boolean isDead() {
		return dead;
	}

}
