package com.common;

public class QBTotals extends Totals
{
	public int currentRank;
	public int completions;
	public int attempts;
	public double completionPct;
	public double attemptsPerGame;
	public double totalYards;
	public double yardsPerAttempt;
	public double yardsPerGame;
	public int touchdowns;
	public int interceptions;
	public int firstDowns;
	public int sacks;
	public double rating;
	
	public QBTotals()
	{
		
	}
	public QBTotals(int currentRank, int completions, int attempts, double completionPct, double attemptsPerGame,
			double totalYards, double yardsPerAttempt, double yardsPerGame, int touchdowns, int interceptions,
			int firstDowns, int sacks, double rating)
	{
		this.currentRank = currentRank;
		this.completions = completions;
		this.attempts = attempts;
		this.completionPct=  completionPct;
		this.attemptsPerGame = attemptsPerGame;
		this.totalYards = totalYards;
		this.yardsPerAttempt = yardsPerAttempt;
		this.yardsPerGame = yardsPerGame;
		this.touchdowns = touchdowns;
		this.interceptions = interceptions;
		this.firstDowns = firstDowns;
		this.sacks = sacks;
		this.rating = rating;
	}
}
