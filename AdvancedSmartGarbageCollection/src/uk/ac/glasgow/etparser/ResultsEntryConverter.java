package uk.ac.glasgow.etparser;

import com.googlecode.jcsv.writer.CSVEntryConverter;

public class ResultsEntryConverter implements CSVEntryConverter<Results> {

	@Override
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
