package uk.ac.glasgow.etparser;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ETPool extends  ScheduledThreadPoolExecutor{
	public ETPool(int size){
		super(size);
		
	}
	
}
