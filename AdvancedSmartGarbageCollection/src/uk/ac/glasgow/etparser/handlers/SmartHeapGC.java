package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.etparser.ObjectStatus;

/**
 * A class representing the normal way a garbage collector would work- remove
 * all dead objects at a specified interval of time.
 * 
 * @author Emi
 * 
 */
public class SmartHeapGC extends SmartHeap {
	private int multiplier = 1;

	/**
	 * specifies when to do garbage collection
	 * 
	 * @return true if it's time to do garbage collection
	 */
	@Override
	protected boolean checkSizeLimitExcess() {
		return allocatedMemSize >= threshold * multiplier;

	}

	/**
	 * 
	 * @return a list of all objects that are in memory but are dead and have to
	 *         be deallocated.
	 */
	private List<String> currentlyDeadObjects() {
		List<String> deadObjects = new ArrayList<String>();
		for (ObjectStatus o : everSeen.values()) {
			if (o.isDead()) {
				deadObjects.add(o.getID());
			}
		}
		return deadObjects;
	}

	/**
	 * Deallocates all dead objects from memory.
	 */
	@Override
	protected void deallocate() {
		multiplier++;
		for (String currentObjectID : currentlyDeadObjects()) {
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			deallocate(currentObjectID);

		}

	}

}
