package com.common;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WideReceiver extends Player implements Comparable<WideReceiver>
{
	/*   TODO:
	 *   1. Implement compareTo to actually rank players (like I did for QB)
	 */
	public int receptions;
	public double yardsPerAttempt;
	
	public boolean isPuntReturner;
	public double returnedYards;
	
	public Totals currentTotals;
	public ArrayList<Totals> weeklyTotals = new ArrayList<Totals>();
	
	@Override
	public void setInitialData (String[] elementData)
	{
		name = elementData[1] + " " + elementData[2];
		team = elementData[3];
		
		currentTotals = new WRTotals(
		currentRank = Integer.parseInt(elementData[0]),
		receptions = Integer.parseInt(elementData[5].replace(",", "")),
		totalYards = Integer.parseInt(elementData[6].replace(",", "")),
		yardsPerAttempt = Double.parseDouble(elementData[7].replace(",", "")),
		yardsPerGame = Double.parseDouble(elementData[8].replace(",", "")),
		touchdowns = Integer.parseInt(elementData[10].replace(",", "")),
		firstDowns = Integer.parseInt(elementData[13].replace(",", "")),
		fumbles = Integer.parseInt(elementData[15].replace(",", ""))
		);
	}
	
	public String toString()
	{
		String toString = "Rank\tName\t\tTeam\tReceptions\tTotal Yards\tYards-A\tYards-G\t\tTD\t1stDowns\tFumbles\n"
			+currentRank+"\t"+String.format("%8.12s", name)+"\t"+team+"\t"+receptions+"\t\t"+totalYards
			+"\t\t"+yardsPerAttempt+"\t"+yardsPerGame+"\t\t"+touchdowns+"\t"+firstDowns+"\t\t"+fumbles+"\n";
		
		return toString;
	}
	public void addWeeklyTotals(Elements weeklyData)
	{
		WRTotals weeklyTotals = new WRTotals();
		
		if(weeklyData != null)
		for(int k = 0; k < weeklyData.size(); k++)
		{
			Element playerData = weeklyData.get(k);
			if(!playerData.text().matches("\\d+(\\.\\d*)?|\\.\\d+")) {
				continue;
			}
				
			switch(k)
			{
				case 0:
				{
					String[] completionsAndAttempts = playerData.text().split("/");
					weeklyTotals.attempts = Integer.parseInt(completionsAndAttempts[1]);						

					break;
				}
				case 1:	//Passing Yards
				{
					weeklyTotals.yardsPerGame = Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = weeklyTotals.yardsPerGame / weeklyTotals.attempts;
					break;
				}
				case 2: weeklyTotals.touchdowns = Integer.parseInt(playerData.text()); break;
				case 4: weeklyTotals.attempts +=  Integer.parseInt(playerData.text()); break;
				case 5:	// Rushing Yards
				{
					weeklyTotals.yardsPerGame += Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts :0;
					break;
				}
				case 6: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
				case 7:	weeklyTotals.attempts +=  Integer.parseInt(playerData.text()); break;
				case 8:
				{
					weeklyTotals.yardsPerGame += Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts :0;
					break;
				}
				case 9: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
				//case 10: receiving targets
				case 12: weeklyTotals.fumbles = Integer.parseInt(playerData.text()); break;
				case 13: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
				//case 14: fantasy points
			}
		}
		this.weeklyTotals.add(weeklyTotals);
	}

	@Override
	public int compareTo(WideReceiver o)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}

class WRTotals extends Totals
{
	public int currentRank;
	public int receptions;
	public int attempts;
	public double attemptsPerGame;
	public double totalYards;
	public double yardsPerAttempt;
	public double yardsPerGame;
	public int touchdowns;
	public int firstDowns;
	public int fumbles;
	
	public WRTotals()
	{
		
	}
	public WRTotals(int currentRank, int receptions, double totalYards, double yardsPerAttempt, double yardsPerGame, 
			int touchdowns, int firstDowns, int fumbles)
	{
		this.currentRank = currentRank;
		this.totalYards = totalYards;
		this.yardsPerAttempt = yardsPerAttempt;
		this.yardsPerGame = yardsPerGame;
		this.touchdowns = touchdowns;
		this.firstDowns = firstDowns;
		this.fumbles = fumbles;
	}
}
