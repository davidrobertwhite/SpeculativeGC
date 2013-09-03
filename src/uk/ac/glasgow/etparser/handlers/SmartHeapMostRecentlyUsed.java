package uk.ac.glasgow.etparser.handlers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.glasgow.etparser.ObjectStatus;

/**
 * A Smart heap that deallocates the most recently accessed objects first.
 * 
 * @author Emi
 * 
 */
public class SmartHeapMostRecentlyUsed extends SmartHeapComparable {

	public SmartHeapMostRecentlyUsed() {
		super();
		System.out.println("You created a new MRUHeap");
	}

	/**
	 * Deallocate the objects that were most recently used.
	 */
	protected void deallocate() {
		// create a list of objects ordered by the time of last access
		List<ObjectStatus> timeOrderedObjects = getListOfObjectClassTimeSorted();
		while ((!sizeNormal()) && timeOrderedObjects.size() > 0) {
			// take the least recently used object and remove it from the list
			String currentObjectID = timeOrderedObjects.remove(0).getID();
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);
		}

	}

	/**
	 * Sort objects according to the time of last access- from most recently
	 * used to least recently used.
	 */
	@Override
	protected void sort(List<ObjectStatus> objects) {
		Collections.sort(objects, new Comparator<ObjectStatus>() {
			public int compare(ObjectStatus o2, ObjectStatus o1) {
				return Integer.compare(o1.getTimeOfLastEvent(),
						o2.getTimeOfLastEvent());

			}
		});

	}

}
