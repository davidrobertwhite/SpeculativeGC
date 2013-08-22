package uk.ac.glasgow.etparser;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class for creating threads representing a single test.
 * 
 * @author Emi
 * 
 */
public class ETThread implements Runnable {
	/**
	 * ETParser to process the file.
	 */
	private ETParser etParser;
	/**
	 * Parameters for the desired test case.
	 */
	private ParameterSettings param;
	/**
	 * The file to which to write the result.
	 */
	private ETParserOutputFile output;

	/**
	 * Constructor for creating a new thread.
	 * 
	 * @param p
	 *            parameter settings for the particular test
	 * @param output
	 *            the file to which to write the results
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ETThread(ParameterSettings p, ETParserOutputFile output)
			throws FileNotFoundException, IOException {
		etParser = new ETParser(p);
		param = p;
		this.output = output;

	}

	/**
	 * Determines what to be done when executing the thread, i.e. process the et
	 * file, create results and write them to the specfied file for output.
	 */
	@Override
	public void run() {

		etParser.processFile();
		Results result = new Results(param);
		output.write(result);

	}

}
