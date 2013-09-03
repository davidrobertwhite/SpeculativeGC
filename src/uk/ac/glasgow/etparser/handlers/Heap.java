package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import uk.ac.glasgow.etparser.ObjectStatus;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

/**
 * 
 * @author Emi This class represents the heap memory. It keeps track of objects
 *         currently allocated in memory and also all objects that have ever
 *         been "seen" or in other words tried to be accessed.
 * 
 */
public class Heap implements EventHandler {

	/**
	 * Measures time sequentially (1, 2, 3...).
	 */
	protected int timeSequence;

	/**
	 * A hash map that keeps the object id and the last event that happened to
	 * that object. Represents all objects currently allocated in memory.
	 */
	protected HashMap<String, ObjectStatus> memory;

	/**
	 * A hashmap of all objects ever tried to be accessed. Keeps track of which
	 * objects have been born or have died.
	 */

	protected HashMap<String, ObjectStatus> everSeen;
	/**
	 * Variable storing the current livesize, i.e. the MB of all live objects
	 * allocated in memory. Increases by the size of object when it's allocated
	 * in memory and decreases once the object has been deallocated.
	 */
	protected int livesize;
	/**
	 * Total size of memory been allocated, i.e. increases by the size of the
	 * object once been allocated in the memory. It never decreases even when
	 * the object has been deallocated.
	 */
	protected int allocatedMemSize;


	/**
	 * Initializes the class variables.
	 */
	public Heap() {
		livesize = 0;
		allocatedMemSize = 0;
		timeSequence = 0;
		memory = new HashMap<String, ObjectStatus>();
		everSeen = new HashMap<String, ObjectStatus>();
		System.out.println("You created a new Heap");

	}

	/**
	 * Handles events. If the event is allocation, it allocates the object in
	 * the memory. If it's a death-deallocates the object. Else does whatever
	 * changes require the event.
	 * 
	 * @param e
	 *            instance of the Event super class
	 */

	public void handle(Event e) {

		timeSequence++;
		assert timeSequence > 0;
		String currentObjectID = e.getObjectID();
		TypeOfEvent currentEventType = e.getTypeOfEvent();
		// if never seen before
		if (!existsInEverSeen(currentObjectID)) {

			// create a new livetime for this object
			ObjectStatus livetime = new ObjectStatus(timeSequence,
					currentObjectID, currentEventType);
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

	/**
	 * When a death event is encountered, the respective object is deallocated
	 * from memory.
	 * 
	 * @param objectID
	 *            the object to be deallocated
	 */
	protected void deallocate(String objectID) {
		ObjectStatus remove = memory.remove(objectID);
		allocatedMemSize -= remove.getSize();
	}

	/**
	 * Changes the status of an object to dead. It can't be updates anymore and
	 * any following access of this object will cause a "dead" error.
	 * 
	 * @param objectID
	 *            the object to be claimed as dead
	 */
	protected void killObject(String objectID) {
		everSeen.get(objectID).kill();
		livesize -= memory.get(objectID).getSize();

	}

	/**
	 * Checks whether the specified object is currently in the memory.
	 * 
	 * @param objectID
	 *            the id of the object we want to know whether it exists in
	 *            memory.
	 * @return true if the object is currently in memory
	 */
	protected boolean existsInHeap(String objectID) {
		return memory.get(objectID) != null;
	}

	/**
	 * Checks whether the specified object was ever accessed.
	 * 
	 * @param objectID
	 *            the id of the object we want to know whether it was ever
	 *            accessed.
	 * @return true if the object has ever been seen
	 */

	protected boolean existsInEverSeen(String objectID) {
		return everSeen.get(objectID) != null;
	}

	/**
	 * Allocates an object in the memory.
	 * 
	 * @param e
	 *            instance of Event class: consists of all the information
	 *            needed to allocate an objects such as the object id and it's
	 *            size
	 */
	protected void allocateObject(Event e) {
		String currentObjectID = e.getObjectID();
		int size = e.getSize();
		livesize += size;
		allocatedMemSize += size;
		ObjectStatus object = new ObjectStatus(timeSequence, currentObjectID,
				TypeOfEvent.ALLOCATION);
		object.setSize(size);
		everSeen.get(currentObjectID).giveBirth();
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);

	}

	/**
	 * The same as allocateObject(Event e) but used if the way to deal with
	 * preaccess was chosen to be allocate before first access.
	 * 
	 * @param e
	 */
	protected void allocateObjectCheater(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectStatus object = new ObjectStatus(timeSequence, currentObjectID,
				TypeOfEvent.ALLOCATION);
		object.setSize(e.getSize());
		everSeen.get(currentObjectID).giveBirth();
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);

	}

	/**
	 * Updates an object in the memory according the type of event specified.
	 * 
	 * @param e
	 *            tells what king of update to make to the object.
	 */
	protected void updateObject(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectStatus accessedObject = memory.get(currentObjectID);
		if (accessedObject != null) {
			accessedObject.updateEvent(timeSequence, e.getTypeOfEvent());
			memory.put(currentObjectID, accessedObject);
			e.setCheck(Check.LEGAL);

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
	/**
	 * 
	 * @return the current megabites of live objects.
	 */
	public long getLiveSize() {
		return livesize;
	}

	/**
	 * 
	 * @return the total megabite ever allocated in memory
	 */
	public long getAllocatedMemSize() {
		return allocatedMemSize;
	}

	/**
	 * 
	 * @return a hashmap of all objects ever tried to be accessed.
	 */
	public HashMap<String, ObjectStatus> getEverSeen() {
		return everSeen;

	}

	public void removeRecord(String objectID) {
		memory.remove(objectID);

	}

	public HashMap<String, ObjectStatus> getObjectStates() {

		return memory;
	}
	public int numberOfTotalObjects(){
		return everSeen.size();
	}

}
