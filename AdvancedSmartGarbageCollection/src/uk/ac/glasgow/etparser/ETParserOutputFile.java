package uk.ac.glasgow.etparser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ETParserOutputFile {

	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	public ETParserOutputFile(String file,boolean append) throws IOException{
		fileWriter = new FileWriter(file,append);
		printWriter = new PrintWriter(fileWriter);
		addHeader();
	}


	
	private void addHeader() throws IOException {
		printWriter.print("Benchmark");
		printWriter.print(", ");
		printWriter.print("Heuristic");
		printWriter.print(", ");
		printWriter.print("Threshold");
		printWriter.print(", ");
		printWriter.print("Percentage");
		printWriter.print(", ");
		printWriter.println("Errors");

	}
	
	public synchronized void write(Results result){
		printWriter.println(result.toString());
		printWriter.flush();
		
	}
}
