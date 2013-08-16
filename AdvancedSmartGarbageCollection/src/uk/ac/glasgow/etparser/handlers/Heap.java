package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import uk.ac.glasgow.etparser.ObjectStatus;
import uk.ac.glasgow.etparser.CommandParser.WayToDealWithErrors;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public class Heap implements EventHandler {

	/**
	 * 
	 * Measures time sequentially (1, 2, 3...).
	 */
	protected int timeSequence;

	/**
	 * A hash map that keeps the object id and the last event that happened to
	 * that object.
	 */
	protected HashMap<String, ObjectStatus> memory;

	protected HashMap<String, ObjectStatus> everSeen;

	protected int livesize, allocatedMemSize;


	protected WayToDealWithErrors dealWithPreaccess, dealWithPostAccess;



	/**
	 * Initializes the class variables. Private because of Singleton design
	 * pattern.
	 */
	public Heap() {
		livesize = 0;
		allocatedMemSize = 0;
		timeSequence = 0;
		memory = new HashMap<String, ObjectStatus>();
		everSeen = new HashMap<String, ObjectStatus>();
		System.out.println("You created a new Heap");

	}

	public void setDealWithPreaccess(WayToDealWithErrors wayToDeal) {

		dealWithPreaccess = wayToDeal;

	}

	public void setDealWithPostaccess(WayToDealWithErrors wayToDeal) {
		dealWithPostAccess = wayToDeal;

	}
	



	/**
	 * Checks whether the event is legal and if it is, it updates the event
	 * record in the heap and updates the time.
	 * 
	 * @param e
	 *            instance of the Event super class
	 */

	public void handle(Event e) {

		timeSequence++;
		assert timeSequence>0;
		String currentObjectID = e.getObjectID();
		TypeOfEvent currentEventType = e.getTypeOfEvent();
		// if never seen before
		if (!existsInEverSeen(currentObjectID)) {

			// create a new livetime for this object
			ObjectStatus livetime = new ObjectStatus(timeSequence,currentObjectID,currentEventType);
			everSeen.put(currentObjectID, livetime);

			// if the event is allocation- great
			if (currentEventType == TypeOfEvent.ALLOCATION) {
				livetime.giveBirth();
				allocateObject(e);

			}
			// if the event isn't allocation
			// report for notborn error
			else {

				e.setCheck(Check.NOTBORN);
			}

		}

		// the object has been seen before
		else {
			// if it wasn't allocated (check whether it has been deallocated
			if (!existsInHeap(currentObjectID)
					&& !everSeen.get(currentObjectID).isDead()) {
				// if the event is allocation- perfect
				if (currentEventType == TypeOfEvent.ALLOCATION) {
					everSeen.get(currentObjectID).giveBirth();
					allocateObject(e);

				}
				// if the event isn't allocation report not born error
				else {
					e.setCheck(Check.NOTBORN);

				}

			}
			// it has been allocated
			else {

				// take the object and check it's livetime
				ObjectStatus currentObjectLivetime = everSeen
						.get(currentObjectID);
				// if it was never born, probably preaccess before and now again
				// or probably dead
				if (!currentObjectLivetime.isBorn()) {

					e.setCheck(Check.NOTBORN);
				}
				// the object died before this access
				else if (currentObjectLivetime.isDead()) {
					e.setCheck(Check.DEAD);

				}
				// it's legal to update this object
				else {
					// if the object isn't dead just make the update
					e.setCheck(Check.LEGAL);

					// if it was a death event kill the object in everLived,
					if (currentEventType == TypeOfEvent.DEATH) {

						killObject(currentObjectID);
					} else {
						updateObject(e);

					}

				}

			}

		}

	}


	protected void deallocate(String objectID) {
		ObjectStatus remove = memory.remove(objectID);
		allocatedMemSize -= remove.getSize();
	}

	protected void killObject(String objectID) {
		everSeen.get(objectID).kill();
		livesize -= memory.get(objectID).getSize();

	}

	protected boolean existsInHeap(String objectID) {
		return memory.get(objectID) != null;
	}

	protected boolean existsInEverSeen(String objectID) {
		return everSeen.get(objectID) != null;
	}

	protected void allocateObject(Event e) {
		String currentObjectID = e.getObjectID();
		int size = e.getSize();
		livesize += size;
		allocatedMemSize += size;
		ObjectStatus object = new ObjectStatus(timeSequence,
				currentObjectID,TypeOfEvent.ALLOCATION);
		object.setSize(size);
		everSeen.get(currentObjectID).giveBirth();
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);

	}

	protected void allocateObjectCheater(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectStatus object = new ObjectStatus( timeSequence, currentObjectID,TypeOfEvent.ALLOCATION);
		object.setSize(e.getSize());
		everSeen.get(currentObjectID).giveBirth();
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);

	}

	protected void updateObject(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectStatus accessedObject = memory.get(currentObjectID);
		if (accessedObject != null) {
			accessedObject.updateEvent(timeSequence, e.getTypeOfEvent());
			memory.put(currentObjectID, accessedObject);
			e.setCheck(Check.LEGAL);

		}

	}

	protected void decisionMakerPreaccess(Event e) {

		switch (dealWithPreaccess) {
		case MOVE:
			handleAllocateAtFirstAccess(e);
			break;
		default:
			handleIgnorePreaccess();

		}
	}

	protected void decisionMakerPostaccess() {
		switch (dealWithPostAccess) {
		case MOVE:
			handleMoveDeath();
			break;
		default:
			handleIgnorePostaccess();

		}
	}

	/**
	 * 
	 * @return the number of objects tried to be accessed ever.
	 */
	public int getNumObjects() {
		return everSeen.size();
	}

	/**
	 * 
	 * @param oid
	 *            the id of the object we want to access in the heap.
	 * @return the last event record for the given object.
	 */
	public ObjectStatus getRecord(String oid) {
		return memory.get(oid);
	}

	/**
	 * 
	 * @return the current time expressed as sequence.
	 */
	public int getTimeSequence() {
		return timeSequence;
	}


	// getters

	public long getLiveSize() {
		return livesize;
	}

	public long getAllocatedMemSize() {
		return allocatedMemSize;
	}

	public HashMap<String, ObjectStatus> getEverSeen() {
		return everSeen;

	}

	protected void handleIgnorePreaccess() {
		System.out.println("ignore preaccess");
		// as this method just ignores preaccess it doesn't do anything

	}

	protected void handleAllocateAtFirstAccess(Event e) {
		System.out.println("allocate at first access");
		allocateObjectCheater(e);
		updateObject(e);

	}

	protected void handleIgnorePostaccess() {
		System.out.println("ignore postaccess");
		// as this method just ignores postaccess it doesn't do anything

	}

	// no need for this in my opinion. we don't need an object to be dead to
	// collect it
	protected void handleMoveDeath() {
		System.out.println("move death");

	}

	public void removeRecord(String objectID) {
		memory.remove(objectID);

	}

	public HashMap<String, ObjectStatus> getObjectStates() {

		return memory;
	}

}
