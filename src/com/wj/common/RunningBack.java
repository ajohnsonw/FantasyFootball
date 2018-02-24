package com.wj.common;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RunningBack extends Player implements Comparable<RunningBack>
{
	public int attempts;
	public double attemptsPerGame;
	public double yardsPerAttempt;
	
	public boolean isPuntReturner;
	public double returnedYards;
	
	public Totals currentTotals;
	public ArrayList<Totals> weeklyTotals = new ArrayList<Totals>();
		
	/*   TODO:
	 *   1. Implement compareTo to actually rank players (like I did for QB)
	 */
	
	@Override
	public void setInitialData (String[] elementData)
	{
		name = elementData[1] + " " + elementData[2];
		team = elementData[3];
		
		currentTotals = new RBTotals(
		currentRank = Integer.parseInt(elementData[0]),
		attempts = Integer.parseInt(elementData[5].replace(",", "")),
		attemptsPerGame = Double.parseDouble(elementData[6].replace(",", "")),
		totalYards = Double.parseDouble(elementData[7].replace(",", "")),
		yardsPerAttempt = Double.parseDouble(elementData[8].replace(",", "")),
		yardsPerGame = Double.parseDouble(elementData[9].replace(",", "")),
		touchdowns = Integer.parseInt(elementData[10].replace(",", "")),
		firstDowns = Integer.parseInt(elementData[12].replace(",", "")),
		fumbles = Integer.parseInt(elementData[16].replace(",", ""))
		);
	}
	public String toString()
	{
		String toString = "Rank\tName\t\tTeam\tAttempts\tAttempts-G\tTotal Yards\tYards-A\tYards-G\t\tTD\t1stDowns\tFumbles\n"
			+currentRank+"\t"+String.format("%8.12s", name)+"\t"+team+"\t"+attempts+"\t\t"+attemptsPerGame+"\t\t"+totalYards
			+"\t\t"+yardsPerAttempt+"\t"+yardsPerGame+"\t\t"+touchdowns+"\t"+firstDowns+"\t\t"+fumbles+"\n";
		
		return toString;
	}
	
	public void setByeWeek(boolean foundBye)
	{
		RBTotals zeroTotals = new RBTotals();
		zeroTotals.isBye = foundBye;
		
		this.weeklyTotals.add(zeroTotals);
	}
	public void addWeeklyTotals(Elements weeklyData)
	{
		RBTotals weeklyTotals = new RBTotals();
		
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
					weeklyTotals.yardsPerAttempt = weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts : 0;
					break;
				}
				case 2: weeklyTotals.touchdowns = Integer.parseInt(playerData.text()); break;
				case 3: weeklyTotals.interceptions = Integer.parseInt(playerData.text()); break;
				case 4: weeklyTotals.attempts +=  Integer.parseInt(playerData.text()); break;
				case 5:	// Rushing Yards
				{
					weeklyTotals.yardsPerGame += Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts : 0;
					break;
				}
				case 6: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
				case 7:	weeklyTotals.attempts +=  Integer.parseInt(playerData.text()); break;
				case 8:
				{
					weeklyTotals.yardsPerGame += Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts : 0;
					break;
				}
				case 9: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
				//case 10: receiving targets
				case 12: weeklyTotals.fumbles = Integer.parseInt(playerData.text()); break;
				case 13: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
			}
		}
		this.weeklyTotals.add(weeklyTotals);
	}

	public int compareTo(RunningBack o)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
class RBTotals extends Totals
{
	public int currentRank;
	public int attempts;
	public double attemptsPerGame;
	public double totalYards;
	public double yardsPerAttempt;
	public double yardsPerGame;
	public int touchdowns;
	public int interceptions;
	public int firstDowns;
	public int fumbles;
	
	public RBTotals()
	{
		
	}
	public RBTotals(int currentRank, int attempts, double attemptsPerGame, double totalYards, double yardsPerAttempt, double yardsPerGame, 
			int touchdowns, int firstDowns, int fumbles)
	{
		this.currentRank = currentRank;
		this.attempts = attempts;
		this.totalYards = totalYards;
		this.yardsPerAttempt = yardsPerAttempt;
		this.yardsPerGame = yardsPerGame;
		this.touchdowns = touchdowns;
		this.firstDowns = firstDowns;
		this.fumbles = fumbles;
	}
	
	@Override
	public String toString()
	{		
		String toString = "Attempts\tAttempts-G\t\tTotalYards\tYards-A\tYards-G\tTouchdowns\t\tTD\t1stDowns\tFumbles\n"
		+"\t"+attempts+"\t\t"+attemptsPerGame+"\t"+totalYards+"\t\t"+yardsPerAttempt+"\t\t"+yardsPerGame+"\t\t"+touchdowns+"\t\t"
		+firstDowns+"\t\t"+fumbles+"\t\t";
	
		return toString;
	}
}