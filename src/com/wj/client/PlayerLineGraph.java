package com.wj.client;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.wj.common.Player;
import com.wj.server.PlayerData;


public class PlayerLineGraph extends JPanel {

   private String[] draftKingsSalary;

   public void setupGraph(Player p)
   {
	   removeAll();
	   this.draftKingsSalary = p.dkValue;
	   
	   XYSeries series = new XYSeries("Salary");
	   for(int i = 0; i < PlayerData.WEEK_NUMBER; i++) 
	   {
		   if(i >= draftKingsSalary.length || draftKingsSalary[i] == null) {
			   series.add(Integer.valueOf(i+1), Integer.valueOf(0));
			   continue;
		   }
			   
		   series.add(Integer.valueOf(i+1), Integer.valueOf(draftKingsSalary[i].replaceAll("[$,]+", "")));
	   }

	   XYSeriesCollection dataset = new XYSeriesCollection();
	   dataset.addSeries(series);

	// Generate the graph
	   JFreeChart chart = ChartFactory.createXYLineChart(
	   "", // Title
	   "Week#", // x-axis Label
	   "Salary", // y-axis Label
	   dataset, // Dataset
	   PlotOrientation.VERTICAL, // Plot Orientation
	   false, // Show Legend
	   true, // Use tooltips
	   false // Configure chart to generate URLs?
	   );
	   
	   XYPlot plot = chart.getXYPlot();
       NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
       NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
       rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
       domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
       
	   ChartPanel chartPanel = new ChartPanel(chart);
	   chartPanel.setMouseWheelEnabled(true);
	   setLayout(new java.awt.BorderLayout());
	   add(chartPanel,BorderLayout.CENTER);
	   
	   setVisible(true);
   }

}