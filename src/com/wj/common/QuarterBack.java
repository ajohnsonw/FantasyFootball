package com.wj.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QuarterBack extends Player implements Comparable<QuarterBack>
{
	public int completions;
	public int attempts;
	public int sacks;
	public int interceptions;
	
	public double rating;
	public double completionPct;
	public double yardsPerAttempt;
	public double attemptsPerGame;
	
	public int currentDKValue;
	
	public Totals currentTotals;
	public ArrayList<Totals> weeklyTotals = new ArrayList<Totals>();

	public QuarterBack() {}
	public QuarterBack(String name) { this.name = name; }
	
	/*   TODO:
	 *   1. Finish weekly totals from ffotoday.com
	 *   2. Improve compareTo functionality to better judge QB A is better than QB B.
	 *   3. Implement calculateAverageDKPoints
	 *   	a. I don't know that it'll be needed. It would probably be better to have
	 *   		a line graph for DK Points vs Cost (per week).
	 */
	
	
	//TODO: 
	@Override
	public void setInitialData (String[] elementData)
	{
		name = elementData[1] + " " + elementData[2];
		team = elementData[3];
		
		currentTotals = new QBTotals(
		currentRank = Integer.parseInt(elementData[0]),
		completions = Integer.parseInt(elementData[5].replace(",", "")),
		attempts = Integer.parseInt(elementData[6].replace(",", "")),
		completionPct = Double.parseDouble(elementData[7].replace(",", "")),
		attemptsPerGame = Double.parseDouble(elementData[8].replace(",", "")),
		totalYards = Double.parseDouble(elementData[9].replace(",", "")),
		yardsPerAttempt = Double.parseDouble(elementData[10].replace(",", "")),
		yardsPerGame = Double.parseDouble(elementData[11].replace(",", "")),
		touchdowns = Integer.parseInt(elementData[12].replace(",", "")),
		interceptions = Integer.parseInt(elementData[13].replace(",", "")),
		firstDowns = Integer.parseInt(elementData[14].replace(",", "")),
		sacks = Integer.parseInt(elementData[19].replace(",", "")),
		rating = Double.parseDouble(elementData[20].replace(",", ""))
		);
	}
	
	public String toString()
	{
		String toString = "Rank\tName\t\tTeam\tCompletions\tAttempts\tCompletionPct\tAttempts-G\tTotal Yards\tYards-A\tYards-G\t\tTD\tInt\t1stDowns\tSacks\tRating\n"
			+currentRank+"\t"+String.format("%8.12s", name)+"\t"+team+"\t"+completions+"\t\t"+attempts+"\t\t"+completionPct+"\t\t"+attemptsPerGame+"\t\t"+totalYards
			+"\t\t"+yardsPerAttempt+"\t"+yardsPerGame+"\t\t"+touchdowns+"\t"+interceptions+"\t"+firstDowns+"\t\t"+sacks+"\t"+rating+"\n";
		
		return toString;
	}
	
	@Override
	public void addWeeklyTotals(Elements weeklyData)
	{
		QBTotals weeklyTotals = new QBTotals();
		
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
					weeklyTotals.completions = Integer.parseInt(completionsAndAttempts[0]);
					weeklyTotals.attempts = Integer.parseInt(completionsAndAttempts[1]);
					weeklyTotals.completionPct = BigDecimal.valueOf(weeklyTotals.attempts != 0? (double)weeklyTotals.completions / (double)weeklyTotals.attempts : 0)
				    .setScale(3, RoundingMode.HALF_UP).doubleValue();

					break;
				}
				case 1:	//Passing Yards
				{
					weeklyTotals.yardsPerGame = Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = BigDecimal.valueOf(weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts : 0)
				    .setScale(3, RoundingMode.HALF_UP).doubleValue();

					break;
				}
				case 2: weeklyTotals.touchdowns = Integer.parseInt(playerData.text()); break;
				case 3: weeklyTotals.interceptions = Integer.parseInt(playerData.text()); break;
				case 4: weeklyTotals.attempts +=  Integer.parseInt(playerData.text()); break;
				case 5:	// Rushing Yards
				{
					weeklyTotals.yardsPerGame += Integer.parseInt(playerData.text());
					weeklyTotals.yardsPerAttempt = BigDecimal.valueOf(weeklyTotals.attempts != 0? weeklyTotals.yardsPerGame / weeklyTotals.attempts : 0)
				    .setScale(3, RoundingMode.HALF_UP).doubleValue();
					
					break;
				}
				case 6: weeklyTotals.touchdowns += Integer.parseInt(playerData.text()); break;
			}
		}
		this.weeklyTotals.add(weeklyTotals);
	}

	@Override
	public void setDKValue(String value)
	{
		this.currentDKValue = Integer.valueOf(value);
		this.name += " " + currentDKValue;
	}
	
	public double getTouchdownsPerGame()
	{
		return (touchdowns == 0 || weeklyTotals.size() == 0)? 0 : touchdowns / weeklyTotals.size();
	}
	public double getPassingYardsPerGame()
	{
		return yardsPerGame;
	}
	
	public double getPassingAttemptsPerGame()
	{
		return attemptsPerGame;
	}

	public int compareTo(QuarterBack o)
	{
		int returnValue = -1;
		
		if(attemptsPerGame == o.attemptsPerGame
				&& getTouchdownsPerGame() == o.getTouchdownsPerGame()
				&& yardsPerGame == o.yardsPerGame)
			returnValue = 0;
		else
		{
			if(getTouchdownsPerGame() > o.getTouchdownsPerGame()
				&& yardsPerGame > (o.yardsPerGame)) 
			{
				System.out.println("touchdownsPerGame["+getTouchdownsPerGame()+"] vs oTouchdownsPerGame["+o.getTouchdownsPerGame()+"] "
						+ " && yardsPerGame["+yardsPerGame+"] vs oYardsPerGame["+o.yardsPerGame+"]");
				returnValue = 1;
			}
			else if(getTouchdownsPerGame() > (o.getTouchdownsPerGame() * 2 ))
			{
				System.out.println("touchdownsPerGame["+getTouchdownsPerGame()+"] vs oTouchdownsPerGame["+(o.getTouchdownsPerGame() * 2)+"]");
				returnValue = 1;
			}
			else if(yardsPerGame > (o.yardsPerGame * 1.50))
			{
				System.out.println("yardsPerGame["+yardsPerGame+"] vs oYardsPerGame["+(o.yardsPerGame * 1.50)+"]");
				returnValue = 1;
			}
			else
			{
				System.out.print("touchdownsPerGame["+getTouchdownsPerGame()+"] vs oTouchdownsPerGame["+o.getTouchdownsPerGame()+"] "
						+ " && yardsPerGame["+yardsPerGame+"] vs oYardsPerGame["+o.yardsPerGame+"] ||");
				System.out.print("touchdownsPerGame["+getTouchdownsPerGame()+"] vs oTouchdownsPerGame["+(o.getTouchdownsPerGame() * 2)+"] ||");
				System.out.print("yardsPerGame["+yardsPerGame+"] vs oYardsPerGame["+(o.yardsPerGame * 1.50)+"]\n");
				
				returnValue = -1;
			}
		}
		
		System.out.println(this.name + " vs " + o.name + " ["+returnValue+"]");
		
		return returnValue;
	}
	
	@Override
	public void calculateAverageDKPoints()
	{
		
	}
}