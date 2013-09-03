package uk.ac.glasgow.etparser;

import com.googlecode.jcsv.writer.CSVEntryConverter;

/**
 * Class for converting the results in a format suitable for writing to a .csv
 * file
 * 
 * @author Emi
 * 
 */
public class ResultsEntryConverter implements CSVEntryConverter<Results> {

	@Override
	/**
	 * @param r the results to be converted in the proper format for outputting them to a .csv file.
	 */
	public String[] convertEntry(Results r) {
		String[] columns = new String[5];

		columns[0] = r.getBenchmark();
		columns[1] = r.getHeuristic();
		columns[2] = r.getThreshold() + "";
		columns[3] = r.getPercentage() + "";
		columns[4] = r.getErrors() + "";

		return columns;
	}
}
