package uk.ac.glasgow.etparser;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;
import uk.ac.glasgow.etparser.handlers.EventHandler;
import uk.ac.glasgow.etparser.handlers.Heap;

public class LiveSizeChart extends ApplicationFrame implements EventHandler{

	private static final long serialVersionUID = 1L;
	private XYSeries data;
	private Heap heap;
	private int EVENTSINTERVAL = 10000;
	public LiveSizeChart(Heap h) {

		super("Live size chart");
		heap=h;
		data = new XYSeries("Data");
		data.add(0, 0);
		

		XYSeriesCollection dataset = new XYSeriesCollection(data);

		// based on the dataset we create the chart
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Livesize change over time", "Time", "Livesize", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);

		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

		// add it to our application
		setContentPane(chartPanel);
		pack();

	}

	public void updateChart(double x, double y) {
		data.add(x, y);
	}

	@Override
	public void handle(Event e) {
		if (e.getTypeOfEvent()==TypeOfEvent.ALLOCATION||e.getTypeOfEvent()==TypeOfEvent.DEATH||heap.getTimeSequence() % EVENTSINTERVAL == 0)
			updateChart(heap.getTimeSequence(), heap.getLiveSize());		
	}

}
