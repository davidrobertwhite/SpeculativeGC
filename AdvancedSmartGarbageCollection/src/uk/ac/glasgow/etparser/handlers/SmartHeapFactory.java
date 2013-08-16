package uk.ac.glasgow.etparser.handlers;

import uk.ac.glasgow.etparser.CommandParser.Heuristic;

public class SmartHeapFactory {
	
	public SmartHeap createHeap(Heuristic h){
		SmartHeap heap;
		switch (h) {
		case FIRST:
			heap = new SmartHeapFIFO();
			break;
		case LEASTRECENTLYUSED:
			heap = new SmartHeapLeastRecentlyUsed();
			break;
		case GC:
			heap = new SmartHeapGC();
			System.out.println("gc");
			break;
		case LAST:
			heap = new SmartHeapLIFO();
			break;
		case MOSTRECENTLYUSED:
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
			heap=null;
			break;
		}

		return heap;
		
		
	}

}
