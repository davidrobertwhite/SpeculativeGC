package uk.ac.glasgow.etparser.handlers;

public class SmartHeapLIFO extends SmartHeap {

	@Override
	protected void deallocate() {
		while ((!sizeNormal())) {
			// get the id of the last allocated object
			// and remove it from the list of allocated objects
			String currentObjectID = allocatedObjects.remove(allocatedObjects
					.size() - 1);
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);
		}

	}

}
