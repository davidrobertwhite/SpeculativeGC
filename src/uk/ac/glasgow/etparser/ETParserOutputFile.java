package uk.ac.glasgow.etparser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class encapsulating the file to which results to be written.
 * 
 * @author Emi
 * 
 */
public class ETParserOutputFile {
	/**
	 * FileWriter to write to a file.
	 */
	private FileWriter fileWriter;
	/**
	 * PrintWriter to write to a file.
	 */
	private PrintWriter printWriter;

	/**
	 * 
	 * @param file
	 *            to which the results to be written
	 * @param append
	 *            true if you want to append the new results to the existing
	 *            ones
	 * @throws IOException
	 */
	public ETParserOutputFile(String file, boolean append) {
		// May not need this @drw - just put it in whilst working on other code
		try {
			fileWriter = new FileWriter(file, append);
		} catch (Exception e) {
			System.err.println("Error writing to output file: " + e);
			e.printStackTrace();
			System.exit(-1);
		}
		printWriter = new PrintWriter(fileWriter);
		addHeader();
	}

	/**
	 * Writes the desired header to the file.
	 * 
	 * @throws IOException
	 */
	private void addHeader() {
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

	/**
	 * Writes a given result to the file.
	 * 
	 * @param result
	 *            to be written in the file
	 */
	public synchronized void write(Results result) {
		printWriter.println(result.toString());
		printWriter.flush();

	}
}
