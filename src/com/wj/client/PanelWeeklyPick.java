package com.wj.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PanelWeeklyPick extends JFrame implements Runnable
{
	protected JPanel gridPanel = new JPanel();	
	protected JTable grid;
	
	/*   TODO:
	 *   1. Right now this class is completely unimplemented. 
	 *   2. Implement grid columns.
	 *   	a. I'm undecided on if I want player tabs and a data grid like in the other screens, or player names/data in the grid
	 *   3. Implement a weighted recommendation algorithm
	 *   	a. The thought is to compile the best players you can fit in a 50k salary.
	 */
	
	public PanelWeeklyPick()
	{
		init();
	}
	
	public static void main(String[] args)
	{
		try
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeLater(new PanelWeeklyPick());	
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}
	
	public void init()
	{
		setTitle("Picks of the Week");
		setSize(new Dimension(700, 220));
		setLayout(new BorderLayout());

		setupGrid();
	}
	
	public void setupGrid()
	{
		Object[] columnNames = {"column 1", "column 2", "column 3", "column 4", "column 5"};
		Object[][] rowData =
		{ 
			{"r1c1 data", "r1c2 data", "r1c3 data", "r1c4 data", "r1c5 data"},
			{"r2c1 data", "r2c2 data", "r2c3 data", "r2c4 data", "r2c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"},
			{"r3c1 data", "r3c2 data", "r3c3 data", "r3c4 data", "r3c5 data"}
		};
		grid = new JTable(rowData, columnNames);
		grid.setPreferredScrollableViewportSize(new Dimension(50, 50));
		grid.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(grid);

		add(scrollPane, BorderLayout.CENTER);
	}

	public void run()
	{
		setVisible(true);
	}
}
