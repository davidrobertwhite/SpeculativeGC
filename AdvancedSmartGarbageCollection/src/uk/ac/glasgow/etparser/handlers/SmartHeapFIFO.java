package uk.ac.glasgow.etparser.handlers;

/**
 * A Smart heap that deallocates objects according to the first-in-first-out
 * principle..
 * 
 * @author Emi
 * 
 */
public class SmartHeapFIFO extends SmartHeap {

	public SmartHeapFIFO() {
		super();
		System.out.println("You created a new FIFOHeap");
	}

	/**
	 * Deallocates the first allocated objects.
	 */
	protected void deallocate() {
		while ((!sizeNormal())) {
			// get the id of the first allocated object
			// and remove it from the list of allocated objects
			String currentObjectID = allocatedObjects.remove(0);
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);
		}
	}
}
