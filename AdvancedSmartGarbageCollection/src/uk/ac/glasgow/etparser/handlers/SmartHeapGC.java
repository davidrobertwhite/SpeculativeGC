package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.etparser.ObjectStatus;


public class SmartHeapGC extends SmartHeap {
	private int multiplier = 1;

	@Override
	protected boolean checkSizeLimitExcess() {
		return allocatedMemSize >= threshold * multiplier;

	}

	private List<String> currentlyDeadObjects() {
		List<String> deadObjects = new ArrayList<String>();
		for (ObjectStatus o : everSeen.values()) {
			if (o.isDead()) {
				deadObjects.add(o.getID());
			}
		}
		return deadObjects;
	}

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
