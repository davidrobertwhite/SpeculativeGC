package uk.ac.glasgow.etparser;


public class ETThread implements Runnable {

	private ETParser etParser;
	private ParameterSettings param;
	private ETParserOutputFile output;

	public ETThread(ETParser et, ParameterSettings p, ETParserOutputFile output) {
		etParser = et;
		param=p;
		this.output=output;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		etParser.processFile();
		Results result = new Results(param);
		output.write(result);

	}

}
