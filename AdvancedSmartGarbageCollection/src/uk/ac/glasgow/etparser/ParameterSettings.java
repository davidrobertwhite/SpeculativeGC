package uk.ac.glasgow.etparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


import uk.ac.glasgow.etparser.CommandParser.Heuristic;
import uk.ac.glasgow.etparser.CommandParser.WayToDealWithErrors;
import uk.ac.glasgow.etparser.handlers.EventHandler;
import uk.ac.glasgow.etparser.handlers.Heap;
import uk.ac.glasgow.etparser.handlers.SmartHeap;
import uk.ac.glasgow.etparser.handlers.HeapFactory;

public class ParameterSettings {

	public boolean interactive = false;
	public boolean chart = false;
	public boolean help = false;
	public EventHandler errorLogger;
	public boolean statisticsLogger;

	// default would be to ignore these errors. if unborn or dead in args
	// specify...
	public WayToDealWithErrors preaccess = WayToDealWithErrors.IGNORE;
	public WayToDealWithErrors postaccess = WayToDealWithErrors.IGNORE;

	public Heuristic heuristic;

	public String inputFile;

	public Heap heap; // default if to run the normal heap.
								// If the user chose a heuristic-change it.

	public InputStream fileStream;
	public int threshold;
	public double percentage;



	public ParameterSettings(String file, Heuristic h,int threshold,int percentage) throws FileNotFoundException, IOException{

		HeapFactory factory=new HeapFactory();
		heap=factory.createHeap(h); // move me
		((SmartHeap)heap).specifyPercentageToDeallocate(percentage); // move me
		((SmartHeap)heap).specifyThreshold(threshold); // move me
		inputFile=file;
		fileStream=new GZIPInputStream(new FileInputStream(file)); // move me
		System.out.println(fileStream==null); // move me
		System.out.println(fileStream+"fs"); // move me
		this.threshold=threshold;
		this.percentage=percentage;
	}
	
	public ParameterSettings(){
		
	}


}
