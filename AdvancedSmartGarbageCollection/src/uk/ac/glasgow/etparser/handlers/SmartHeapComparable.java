package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.glasgow.etparser.ObjectStatus;

public abstract class SmartHeapComparable extends SmartHeap{
	
	
	
	
	public List<ObjectStatus> getListOfObjectClassTimeSorted() {
		HashMap<String, ObjectStatus> objects = getObjectStates();
		ArrayList<ObjectStatus> listOfObjects = new ArrayList<ObjectStatus>();
		for (ObjectStatus obj : objects.values()) {
			listOfObjects.add(obj);

		}

		sort(listOfObjects);
		return listOfObjects;
	}
	
	protected abstract void sort(List<ObjectStatus> objects);

}
