package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.List;
import uk.ac.glasgow.etparser.events.Event;

/**
 * A superclass for the different heuristics for deallocating objects from
 * memory.
 * 
 * @author Emi
 * 
 */
public abstract class SmartHeap extends Heap {
	/**
	 * The limit of allocated memory in megabytes to reach before starting to
	 * deallocate objects.
	 */
	protected int threshold; // default would be 30 mb for now unless
								// otherwise specified
	/**
	 * What percentage of the currently allocated objects to deallocate after
	 * reaching the threshold.
	 */
	private double percentageToDeallocate; // default would be 20% if not
											// otherwise specified
	/**
	 * A list holding all allocated objects.
	 */
	protected List<String> allocatedObjects;

	/**
	 * Constructor for initializing a SmartHeap.
	 */
	public SmartHeap() {
		super();
		allocatedObjects = new ArrayList<String>();
		threshold = 30;
		percentageToDeallocate = 0.2;

	}

	/**
	 * 
	 * @param t
	 *            megabytes to reach before starting to deallocate from memory.
	 */
	public void specifyThreshold(int t) {
		threshold = t;
	}

	/**
	 * 
	 * @param p
	 *            percentage of the threshold to be freed from memory after
	 *            reaching the threshold.
	 */

	public void specifyPercentageToDeallocate(int p) {
		percentageToDeallocate = p / 100.0;
	}

	/**
	 * 
	 * @return true if we have reached the specified threshold.
	 */
	protected boolean checkSizeLimitExcess() {
		return allocatedMemSize >= threshold;
	}

	/**
	 * 
	 * @return true if the size is reached it's desired limits after
	 *         deallocation.
	 */
	protected boolean sizeNormal() {
		return allocatedMemSize <= threshold
				- (percentageToDeallocate * threshold)
				&& allocatedMemSize >= 0;
	}

	/**
	 * Allocates an object into memory, check whether the threshold is reached
	 * and if it is does the proper deallocation.
	 */
	@Override
	protected void allocateObject(Event e) {
		super.allocateObject(e);
		allocatedObjects.add(e.getObjectID());
		// check for memory excess
		if (checkSizeLimitExcess()) {
			deallocate();

		}

	}

	/**
	 * Specifies the heuristic according to which to deallocate objects after
	 * reaching the specified threshold. Must be implemented by all subclasses.
	 */
	// must be overridden by subclasses
	protected abstract void deallocate();

}
