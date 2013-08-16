package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.List;
import uk.ac.glasgow.etparser.events.Event;


public abstract class SmartHeap extends Heap {

	protected int threshold; // default would be 70000 for now unless
								// otherwise specified

	private double percentageToDeallocate; // default would be 20% if not
											// otherwise specified

	protected List<String> allocatedObjects;

	public SmartHeap() {
		super();
		allocatedObjects = new ArrayList<String>();
		threshold = 30;
		percentageToDeallocate = 0.2;

	}

	public void specifyThreshold(int t) {
		threshold = t;
		System.out.println("threshold is " + threshold);

	}

	public void specifyPercentageToDeallocate(int p) {
		percentageToDeallocate = p / 100.0;
		System.out.println("percentage is " + percentageToDeallocate);
	}

	protected boolean checkSizeLimitExcess() {
		return allocatedMemSize >= threshold;
	}

	protected boolean sizeNormal() {
		return allocatedMemSize <= threshold
				- (percentageToDeallocate * threshold)
				&& allocatedMemSize >= 0;
	}


	@Override
	protected void allocateObject(Event e){
		super.allocateObject(e);
		allocatedObjects.add(e.getObjectID());
		// check for memory excess
		if (checkSizeLimitExcess()) {
			deallocate();

		}
		
	}
	// must be overridden by subclasses
	protected abstract void deallocate();

}
