package com.wj.client;

import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.wj.client.lib.PlayerDataPanel;
import com.wj.client.lib.PlayerPanel;
import com.wj.common.QBTotals;
import com.wj.common.QuarterBack;

public class PanelQBStats extends PlayerPanel
{
	protected ArrayList<QuarterBack> QBList = null;
	protected Object[] columnNames 			= { "Week#", "team", "attempts", "attemptsPerGame", "completionPct", "touchdowns",
												"yardsPerGame",	"completions", "currentRank", "firstDowns", "interceptions", 
												"rating", "sacks", "totalYards", "yardsPerAttempt", };
	
	/*   TODO:
	 *   1. Implement a PanelXXStats for each player type.
	 *   2. Implement a line graph on dk points vs cost (per week)
	 */
	public PanelQBStats(ArrayList<QuarterBack> QBList)
	{
		this.QBList = QBList;
		setupScreen();
	}
	
	public static void main(String[] args)
	{
		try
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			ArrayList<QuarterBack> defaultQBs = new ArrayList<QuarterBack>();
			defaultQBs.add(new QuarterBack("test player"));
			SwingUtilities.invokeLater(new PanelQBStats(defaultQBs));	
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}
	
	public void setupScreen()
	{
		setTitle("QB Stats");
		setupQBView();
	}

	public void setupQBView()
	{
		if(QBList != null)
		{
			for(int i = 0; i < QBList.size(); i++)
			{
				QBPanel panel = new QBPanel(QBList.get(i));
				playerTab.addTab(panel.getName(), panel);
				if(i == 0)
				{
					((DefaultTableModel)grid.getModel()).setDataVector(panel.getRowData(), columnNames);
				}
			}
			
			for(int i = 0; i < grid.getModel().getColumnCount(); i++)
			{
				if(i == 3 || i == 4 || i == 6 || i == grid.getModel().getColumnCount() -1)
					grid.getColumnModel().getColumn(i).setMinWidth(96);
			}			
		}
	}

	class QBPanel extends PlayerDataPanel
	{
		QuarterBack QB = null;
		public QBPanel(QuarterBack QB)
		{
			this.QB = QB;
			setName(QB.name);
		}
		
		
		@Override
		public Object[][] getRowData()
		{
			Object[][] list = new Object[20][15];
			
			if(QB != null && QB.weeklyTotals != null)
			for(int j = 0; j < QB.weeklyTotals.size(); j++)
			{
				QBTotals total = (QBTotals)QB.weeklyTotals.get(j);

				list[j][0] = j+1;	
				list[j][1] = QB.team;
				list[j][2] = total.attempts;
				list[j][3] = total.attemptsPerGame;
				list[j][4] = total.completionPct;
				list[j][5] = total.touchdowns;
				list[j][6] = total.yardsPerGame;				
				list[j][7] = total.completions;
				list[j][8] = total.currentRank;
				list[j][9] = total.firstDowns;
				list[j][10] = total.interceptions;
				list[j][11] = total.rating;
				list[j][12] = total.sacks;
				list[j][13] = total.totalYards;
				list[j][14] = total.yardsPerAttempt;
			}
			
			return list;
		}		
	}
}
