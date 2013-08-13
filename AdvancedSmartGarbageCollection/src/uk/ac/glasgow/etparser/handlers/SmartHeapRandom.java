package uk.ac.glasgow.etparser.handlers;

import java.util.Random;

public class SmartHeapRandom extends SmartHeap {
	private Random randomGenerator = new Random();

	@Override
	protected void deallocate() {
		while ((!sizeNormal())) {
			int index = randomGenerator.nextInt(allocatedObjects.size());
			// get the id of the first allocated object
			// and remove it from the list of allocated objects
			String currentObjectID = allocatedObjects.remove(index);
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);

		}

	}

}
