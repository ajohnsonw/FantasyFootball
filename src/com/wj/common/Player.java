package com.wj.common;

import org.jsoup.select.Elements;

public class Player
{
	public String name = "";
	public String team = "";
	
	public boolean isInjured;
	public double totalYards;
	public double averageYards;
	public double yardsPerGame;
	public int touchdowns;
	public int fumbles;
	public int currentRank;
	public int firstDowns;	
	
	public void setInitialData(String[] elementData){}
	public void addWeeklyTotals(Elements e){}
	public void setDKValue(String value){}
	public void calculateAverageDKPoints() {}

	public String getFullNameReversed()
	{
		String lastName  = name.substring(name.indexOf(" ")+1);
		String firstName = name.substring(0, name.indexOf(" "));
		String fullName  = lastName +", " + firstName;
		
		return fullName;
	}
	
	public void setIsInjured(boolean isInjured)
	{
		this.isInjured = isInjured;
	}
}
