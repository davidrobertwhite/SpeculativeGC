package uk.ac.glasgow.etparser.handlers;

import uk.ac.glasgow.etparser.SimulateGC.Heuristic;

/**
 * A factory class for creating SmartHeaps according to the specified heuristic.
 * 
 * @author Emi
 * 
 */
public class SmartHeapFactory {
	/**
	 * A method for creating a heap according to our needs.
	 * 
	 * @param h
	 *            the heuristic that determines what heap to be created.
	 * @return the corresponding heap.
	 */
	public SmartHeap createHeap(Heuristic h) {
		SmartHeap heap;
		switch (h) {
		case FIRST:
			heap = new SmartHeapFIFO();
			break;
		case LRU:
			heap = new SmartHeapLeastRecentlyUsed();
			break;
		case GC:
			heap = new SmartHeapGC();
			System.out.println("gc");
			break;
		case LAST:
			heap = new SmartHeapLIFO();
			break;
		case MRU:
			heap = new SmartHeapMostRecentlyUsed();
			break;
		case SMALLEST:
			heap = new SmartHeapSmallestSize();
			break;
		case LARGEST:
			heap = new SmartHeapLargestSize();
			break;
		case RANDOM:
			heap = new SmartHeapRandom();
			break;
		default:
			heap = null;
			break;
		}

		return heap;

	}

}
