package fr.etu.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import fr.etu.ea.util.Writer;

public class Chart {
	
	private JFreeChart chart;
	private XYSeries series;
	private XYSeriesCollection data;
	private ApplicationFrame plotFrame;
	private XYSeries seriesMoyenne;
	private XYSeries seriesMin;
	
	private XYSeries seriesSwap1;
	private XYSeries seriesSwap3;
	private XYSeries seriesSwap5;
	private XYSeries seriesNoC;
	private XYSeries seriesNoM;
	private XYSeries seriesInversion5;
	private XYSeries seriesInversion3;
	private XYSeries seriesInversion1;
	private XYSeries seriesMoveAfter1;
	private XYSeries seriesMoveAfter3;
	private XYSeries seriesMoveAfter5;
	private XYSeries seriesScramble1;
	private XYSeries seriesScramble3;
	private XYSeries seriesScramble5;
	
	public Chart(String title) {
		this.series = new XYSeries("Best Fitness");
		this.data = new XYSeriesCollection(this.series);
		this.plotFrame = new ApplicationFrame("");
	    this.plotFrame.setVisible(true);
	    this.chart = ChartFactory.createXYLineChart(
	        "",
	        "Générations", 
	        "Fitness", 
	        this.data,
	        PlotOrientation.VERTICAL,
	        true,
	        false,
	        false
	    );
	    
		ChartPanel chartPanel = new ChartPanel(this.chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(600, 1000));
	    this.plotFrame.add(chartPanel);
		this.plotFrame.pack();
	    RefineryUtilities.centerFrameOnScreen(this.plotFrame);
		
		this.chart.setTitle(title);
		this.seriesMoyenne = new XYSeries("Moyenne Fitness");
		this.seriesMin = new XYSeries("Minimum Fitness");
		this.data.addSeries(this.seriesMoyenne);
		this.data.addSeries(this.seriesMin);
	}
	
	public Chart() {
	    this.seriesSwap1 = new XYSeries("Swap 1");
		this.seriesSwap3 = new XYSeries("Swap 3");
		this.seriesSwap5 = new XYSeries("Swap 5");
		
		this.setSeriesInversion1(new XYSeries("Inversion 1"));
		this.setSeriesInversion3(new XYSeries("Inversion 3"));
		this.setSeriesInversion5(new XYSeries("Inversion 5"));
		
		this.setSeriesMoveAfter1(new XYSeries("MoveAfter 1"));
		this.setSeriesMoveAfter3(new XYSeries("MoveAfter 3"));
		this.setSeriesMoveAfter5(new XYSeries("MoveAfter 5"));
		
		this.setSeriesScramble1(new XYSeries("Scramble 1"));
		this.setSeriesScramble3(new XYSeries("Scramble 3"));
		this.setSeriesScramble5(new XYSeries("Scramble 5"));

		
		XYSeriesCollection dataMutation = new XYSeriesCollection(this.seriesSwap5);
		
		dataMutation.addSeries(this.seriesSwap3);
		dataMutation.addSeries(this.seriesSwap1);

		dataMutation.addSeries(this.seriesInversion5);
		dataMutation.addSeries(this.seriesInversion3);
		dataMutation.addSeries(this.seriesInversion1);
		
		dataMutation.addSeries(this.seriesMoveAfter5);
		dataMutation.addSeries(this.seriesMoveAfter3);
		dataMutation.addSeries(this.seriesMoveAfter1);
		
		dataMutation.addSeries(this.seriesScramble5);
		dataMutation.addSeries(this.seriesScramble3);
		dataMutation.addSeries(this.seriesScramble1);
		
		this.chart = ChartFactory.createXYLineChart(
	    		"Opérateurs mutation",
	    		"Generations", 
			"Utilité", 
	        dataMutation,
	        PlotOrientation.VERTICAL,
	        true,
	        false,
	        false
	    );
		
		ChartPanel chartPanel = new ChartPanel(this.chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(600, 1000));

		
	    ApplicationFrame plotFrame = new ApplicationFrame("");
	    plotFrame.add(chartPanel);
		plotFrame.pack();
		plotFrame.setVisible(true);
	}
	
	public void addBestXY(int x, double y) {
		this.series.add(x, y);
	}
	
	public void addMoyenneXY(int x, double y) {
		this.seriesMoyenne.add(x, y);
	}
	
	public void addMinXY(int x, double y) {
		this.seriesMin.add(x, y);
	}

	public String getTitleGraph() {
		return this.chart.getTitle().getText();
	}
	
	public void setTitleGraph(String title) {
		this.chart.setTitle(title);
	}
	
	public void addNoM(int x, double y) {
		this.seriesNoM.add(x, y);
	}
	
	public void addNoC(int x, double y) {
		this.seriesNoC.add(x, y);
	}

	public XYSeries getSeriesInversion5() {
		return this.seriesInversion5;
	}

	public void setSeriesInversion5(XYSeries seriesInversion5) {
		this.seriesInversion5 = seriesInversion5;
	}

	public XYSeries getSeriesInversion3() {
		return this.seriesInversion3;
	}

	public void setSeriesInversion3(XYSeries seriesInversion3) {
		this.seriesInversion3 = seriesInversion3;
	}

	public XYSeries getSeriesInversion1() {
		return this.seriesInversion1;
	}

	public void setSeriesInversion1(XYSeries seriesInversion1) {
		this.seriesInversion1 = seriesInversion1;
	}

	public XYSeries getSeriesMoveAfter1() {
		return this.seriesMoveAfter1;
	}

	public void setSeriesMoveAfter1(XYSeries seriesMoveAfter1) {
		this.seriesMoveAfter1 = seriesMoveAfter1;
	}

	public XYSeries getSeriesMoveAfter3() {
		return this.seriesMoveAfter3;
	}

	public void setSeriesMoveAfter3(XYSeries seriesMoveAfter3) {
		this.seriesMoveAfter3 = seriesMoveAfter3;
	}

	public XYSeries getSeriesMoveAfter5() {
		return this.seriesMoveAfter5;
	}

	public void setSeriesMoveAfter5(XYSeries seriesMoveAfter5) {
		this.seriesMoveAfter5 = seriesMoveAfter5;
	}

	public XYSeries getSeriesScramble1() {
		return this.seriesScramble1;
	}

	public void setSeriesScramble1(XYSeries seriesScramble1) {
		this.seriesScramble1 = seriesScramble1;
	}

	public XYSeries getSeriesScramble3() {
		return this.seriesScramble3;
	}

	public void setSeriesScramble3(XYSeries seriesScramble3) {
		this.seriesScramble3 = seriesScramble3;
	}

	public XYSeries getSeriesScramble5() {
		return this.seriesScramble5;
	}

	public void setSeriesScramble5(XYSeries seriesScramble5) {
		this.seriesScramble5 = seriesScramble5;
	}

	public void addSeriesInversion5(int x, double y) {
		this.seriesInversion5.add(x, y);
	}

	public void addSeriesInversion3(int x, double y) {
		this.seriesInversion3.add(x, y);
	}

	public void addSeriesInversion1(int x, double y) {
		this.seriesInversion1.add(x, y);
	}

	public void addSeriesMoveAfter1(int x, double y) {
		this.seriesMoveAfter1.add(x, y);
	}

	public void addSeriesMoveAfter3(int x, double y) {
		this.seriesMoveAfter3.add(x, y);
	}

	public void addSeriesMoveAfter5(int x, double y) {
		this.seriesMoveAfter5.add(x, y);
	}

	public void addSeriesScramble1(int x, double y) {
		this.seriesScramble1.add(x, y);
	}

	public void addSeriesScramble3(int x, double y) {
		this.seriesScramble3.add(x, y);
	}

	public void addSeriesScramble5(int x, double y) {
		this.seriesScramble5.add(x, y);
	}
	
	public void addSeriesSwap1(int x, double y) {
		this.seriesSwap1.add(x, y);
	}

	public void addSeriesSwap3(int x, double y) {
		this.seriesSwap3.add(x, y);
	}

	public void addSeriesSwap5(int x, double y) {
		this.seriesSwap5.add(x, y);
	}

}
