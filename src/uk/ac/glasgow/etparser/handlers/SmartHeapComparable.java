package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.glasgow.etparser.ObjectStatus;

/**
 * 
 * @author Emi Class that encapsulates all Smart Heaps that use comparison
 *         according to which they specify the objects to be deallocated.
 * 
 */
public abstract class SmartHeapComparable extends SmartHeap {
	/**
	 * 
	 * @return a list of allocated into memory objects sorted in the specified
	 *         way.
	 */
	public List<ObjectStatus> getListOfObjectClassTimeSorted() {
		HashMap<String, ObjectStatus> objects = getObjectStates();
		ArrayList<ObjectStatus> listOfObjects = new ArrayList<ObjectStatus>();
		for (ObjectStatus obj : objects.values()) {
			listOfObjects.add(obj);

		}

		sort(listOfObjects);
		return listOfObjects;
	}

	/**
	 * Sort the list of all objects in memory according to the specified
	 * heuristic.
	 * 
	 * @param listOfObjects
	 *            the list of allocated objects to be sorted.
	 */
	protected abstract void sort(List<ObjectStatus> listOfObjects);

}
