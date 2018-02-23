package com.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.server.PlayerData;

public class Team implements Comparable<Team>
{
	/*   TODO:
	 *   1. Improve compareTo functionality
	 *   2. Clean up some of the calculate methods. Are they best handled in this class?
	 *   3. Find one source for computeCurrentTotalsFromWeb to reduce network calls
	 */
	public int comparisonPoints = 0;
	
	public String name = "";	
	public Totals currentTotals = new TeamTotals();
	public ArrayList<Totals> weeklyTotalsList = new ArrayList<Totals>();
	
	public int counter = 0;
	
	public Team (String[] elementData)
	{
		try
		{
			try { Double.parseDouble(elementData[2]); } catch(Throwable thr) { counter = 1; }
			name = counter > 0 ? elementData[1] + " " + elementData[2] : elementData[1];
			computeCurrentTotalsFromWeb(elementData, (TeamTotals)currentTotals);
			printToFile((TeamTotals)currentTotals);
			for(int i = 1; i <= PlayerData.WEEK_NUMBER; i++)
			{

					Totals weeklyTotals = new TeamTotals();
					computeWeeklyTotals(i, (TeamTotals)weeklyTotals);
					printToFile(i, (TeamTotals)weeklyTotals);
					weeklyTotalsList.add(weeklyTotals);
			}
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}	
	public Team (String lineFromData)
	{
		try
		{
			String[] elementData = lineFromData.split(" ");
			try { Double.parseDouble(elementData[1]); } catch(Throwable thr) { counter = 1; }
			name = counter > 0 ? elementData[0] + " " + elementData[1] : elementData[0];
			
			computeCurrentTotalsFromFile(elementData, (TeamTotals)currentTotals);


			for(int i = 1; i <= PlayerData.WEEK_NUMBER; i++)
			{
				File currentDataFile = new File("F:/draftKings/week_"+i+"_totals.txt");
				FileReader fr = new FileReader(currentDataFile);
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				for(int j = 0; line != null; j++, line = br.readLine())
				{
					if(!line.replace(" ", "").contains(name.replace(" ", "")))
						continue;
					String[] weeklyElementData = line.split(" ");
					Totals weeklyTotals = new TeamTotals();
					computeCurrentTotalsFromFile(weeklyElementData, (TeamTotals)weeklyTotals);
					weeklyTotalsList.add(weeklyTotals);
					break;
				}				

			}
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}
	
	public void computeCurrentTotalsFromFile(String[] elementData, TeamTotals currentTotals) throws Exception
	{
		currentTotals.gamesPlayed = Double.parseDouble(elementData[1+counter]);
		currentTotals.wins = Double.parseDouble(elementData[2+counter]);
		currentTotals.losses = Double.parseDouble(elementData[3+counter]);
		currentTotals.passingCompletions = Double.parseDouble(elementData[4+counter]);
		currentTotals.passingAttempts = Double.parseDouble(elementData[5+counter]);
		currentTotals.passingYards = Double.parseDouble(elementData[6+counter]);
		currentTotals.passingAvgYards = Double.parseDouble(elementData[7+counter]);
		currentTotals.passingYardsPerGame = Double.parseDouble(elementData[8+counter]);
		currentTotals.passingTouchdowns = Double.parseDouble(elementData[9+counter]);
		currentTotals.passingInterceptions = Double.parseDouble(elementData[10+counter]);
		currentTotals.passingQBRating = Double.parseDouble(elementData[11+counter]);
		currentTotals.passingBIGPasses = Double.parseDouble(elementData[12+counter]);
		currentTotals.passingSacks = Double.parseDouble(elementData[13+counter]);
		currentTotals.passingSackedYards = Double.parseDouble(elementData[14+counter]);
		currentTotals.passing1stDowns = Double.parseDouble(elementData[15+counter]);			
		currentTotals.rushingAttempts = Double.parseDouble(elementData[16+counter]);
		currentTotals.rushingYards = Double.parseDouble(elementData[17+counter]);
		currentTotals.rushingAvgYards = Double.parseDouble(elementData[18+counter]);
		currentTotals.rushingYardsPerGame = Double.parseDouble(elementData[19+counter]);
		currentTotals.rushingTouchdowns = Double.parseDouble(elementData[20+counter]);
		currentTotals.rushingBIGRushes = Double.parseDouble(elementData[21+counter]);
		currentTotals.rushingFumbles = Double.parseDouble(elementData[22+counter]);
		currentTotals.kickingPoints = Double.parseDouble(elementData[23+counter]);
		currentTotals.fieldGoalsMade = Double.parseDouble(elementData[24+counter]);
		currentTotals.fieldGoalsAttempted = Double.parseDouble(elementData[25+counter]);
		currentTotals.fieldGoalPctCompletion = Double.parseDouble(elementData[26+counter]);
		currentTotals.extraPointsMade = Double.parseDouble(elementData[27+counter]);
		currentTotals.extraPointsAttempted = Double.parseDouble(elementData[28+counter]);
		currentTotals.punts = Double.parseDouble(elementData[29+counter]);
		currentTotals.blockedPunts = Double.parseDouble(elementData[30+counter]);			
		currentTotals.kicksReturned = Double.parseDouble(elementData[31+counter]);
		currentTotals.kickReturnedYards = Double.parseDouble(elementData[32+counter]);
		currentTotals.kickAvgReturnedYards = Double.parseDouble(elementData[33+counter]);
		currentTotals.kickReturnedTouchdowns = Double.parseDouble(elementData[34+counter]);
		currentTotals.puntReturns = Double.parseDouble(elementData[35+counter]);
		currentTotals.puntReturnedYards = Double.parseDouble(elementData[36+counter]);
		currentTotals.puntAvgReturnedYards = Double.parseDouble(elementData[37+counter]);
		currentTotals.puntReturnedTouchdowns = Double.parseDouble(elementData[38+counter]);
		currentTotals.pointsAllowed = Double.parseDouble(elementData[39+counter]);
		currentTotals.pointsAllowedPerGame = Double.parseDouble(elementData[40+counter]);
		currentTotals.tacklesMade = Double.parseDouble(elementData[41+counter]);
		currentTotals.sacksMade = Double.parseDouble(elementData[42+counter]);
		currentTotals.sackForYards = Double.parseDouble(elementData[43+counter]);
		currentTotals.interceptionsCaught = Double.parseDouble(elementData[44+counter]);
		currentTotals.interceptionTouchdowns = Double.parseDouble(elementData[45+counter]);		
		currentTotals.forcedFumbles = Double.parseDouble(elementData[46+counter]);
		currentTotals.fumbleRecoveries = Double.parseDouble(elementData[47+counter]);
		currentTotals.fumbleRecoveryTouchdowns = Double.parseDouble(elementData[48+counter]);
		currentTotals.passesDefended = Double.parseDouble(elementData[49+counter]);
		currentTotals.saftiesGiven = Double.parseDouble(elementData[50+counter]);
		currentTotals.thirdDownsMade = Double.parseDouble(elementData[51+counter]);
		currentTotals.fourthDownsMade = Double.parseDouble(elementData[52+counter]);
		currentTotals.fourthDownsAttempted = Double.parseDouble(elementData[53+counter]);
		currentTotals.penalties = Double.parseDouble(elementData[54+counter]);
		currentTotals.penaltyYards = Double.parseDouble(elementData[55+counter]);
		currentTotals.totalYards = Double.parseDouble(elementData[56+counter]);
	}
	public void computeCurrentTotalsFromWeb(String[] elementData, TeamTotals currentTotals) throws Exception
	{
		currentTotals.gamesPlayed = Double.parseDouble(elementData[2+counter]);
		currentTotals.passingCompletions = Double.parseDouble(elementData[3+counter]);
		currentTotals.passingAttempts = Double.parseDouble(elementData[4+counter]);
		currentTotals.passingPctCompletion = Double.parseDouble(elementData[5+counter]);
		currentTotals.passingYards = Double.parseDouble(elementData[6+counter]);
		currentTotals.passingAvgYards = Double.parseDouble(elementData[7+counter]);
		currentTotals.passingYardsPerGame = Double.parseDouble(elementData[8+counter]);
		currentTotals.passingTouchdowns = Double.parseDouble(elementData[9+counter]);
		currentTotals.passingInterceptions = Double.parseDouble(elementData[10+counter]);
		currentTotals.passingQBRating = Double.parseDouble(elementData[11+counter]);
		currentTotals.passingBIGPasses = Double.parseDouble(elementData[13+counter]);
		currentTotals.passingSacks = Double.parseDouble(elementData[14+counter]);
		currentTotals.passingSackedYards = Double.parseDouble(elementData[15+counter]);
		currentTotals.passing1stDowns = Double.parseDouble(elementData[16+counter]);
		
		Document standingsDoc = Jsoup.parse(new URL("http://www.nfl.com/standings"), 30000);
		Elements standingsTeamList = standingsDoc.getElementsByTag("tr");
		for(int i = 2; i < standingsTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = standingsTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] standingsData = teamData.text().split(" ");
				currentTotals.wins = Double.parseDouble(standingsData[2+counter]);
				currentTotals.losses = Double.parseDouble(standingsData[3+counter]);
			}
		}
		Document rushingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Rushing&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements rushingTeamList = rushingDoc.getElementsByTag("tr");
		for(int i = 1; i < rushingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = rushingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] rushingData = teamData.text().split(" ");
				currentTotals.rushingAttempts = Double.parseDouble(rushingData[3+counter]);
				currentTotals.rushingYards = Double.parseDouble(rushingData[4+counter]);
				currentTotals.rushingAvgYards = Double.parseDouble(rushingData[5+counter]);
				currentTotals.rushingYardsPerGame = Double.parseDouble(rushingData[6+counter]);
				currentTotals.rushingTouchdowns = Double.parseDouble(rushingData[7+counter]);
				currentTotals.rushingBIGRushes = Double.parseDouble(rushingData[8+counter]);
				currentTotals.rushingFumbles = Double.parseDouble(rushingData[9+counter]);					
			}
		}
		Document kickingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Kicking&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements kickingTeamList = kickingDoc.getElementsByTag("tr");
		for(int i = 1; i < kickingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = kickingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] kickingData = teamData.text().replace("%", "").split(" ");
				currentTotals.kickingPoints = Double.parseDouble(kickingData[3+counter]);
				currentTotals.fieldGoalsMade = Double.parseDouble(kickingData[4+counter]);
				currentTotals.fieldGoalsAttempted = Double.parseDouble(kickingData[5+counter]);
				currentTotals.fieldGoalPctCompletion = Double.parseDouble(kickingData[6+counter]);
				currentTotals.extraPointsMade = Double.parseDouble(kickingData[13+counter]);
				currentTotals.extraPointsAttempted = Double.parseDouble(kickingData[14+counter]);
			}
		}
		Document puntingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Punting&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements puntingTeamList = puntingDoc.getElementsByTag("tr");
		for(int i = 1; i < puntingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = puntingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] puntingData = teamData.text().replace("%", "").split(" ");
				currentTotals.punts = Double.parseDouble(puntingData[3+counter]);
				currentTotals.blockedPunts = Double.parseDouble(puntingData[11+counter]);
			}
		}
		Document returningDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Returning&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements returningTeamList = returningDoc.getElementsByTag("tr");
		for(int i = 1; i < returningTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = returningTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] returningData = teamData.text().replace("%", "").split(" ");
				currentTotals.kicksReturned = Double.parseDouble(returningData[3+counter]);
				currentTotals.kickReturnedYards = Double.parseDouble(returningData[4+counter]);
				currentTotals.kickAvgReturnedYards = Double.parseDouble(returningData[5+counter]);
				currentTotals.kickReturnedTouchdowns = Double.parseDouble(returningData[6+counter]);
				currentTotals.puntReturns = Double.parseDouble(returningData[9+counter]);
				currentTotals.puntReturnedYards = Double.parseDouble(returningData[10+counter]);
				currentTotals.puntAvgReturnedYards = Double.parseDouble(returningData[11+counter]);
				currentTotals.puntReturnedTouchdowns = Double.parseDouble(returningData[12+counter]);
			}
		}
		Document defenseDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Defense&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements defenseTeamList = defenseDoc.getElementsByTag("tr");
		for(int i = 1; i < defenseTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = defenseTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] defenseData = teamData.text().replace("%", "").split(" ");
				currentTotals.pointsAllowed = Double.parseDouble(defenseData[3+counter]);
				currentTotals.pointsAllowedPerGame = Double.parseDouble(defenseData[4+counter]);
				currentTotals.tacklesMade = Double.parseDouble(defenseData[5+counter]);
				currentTotals.sacksMade = Double.parseDouble(defenseData[8+counter]);
				currentTotals.sackForYards = Double.parseDouble(defenseData[9+counter]);
				currentTotals.interceptionsCaught = Double.parseDouble(defenseData[10+counter]);
				currentTotals.interceptionTouchdowns = Double.parseDouble(defenseData[11+counter]);
				currentTotals.forcedFumbles = Double.parseDouble(defenseData[12+counter]);
				currentTotals.fumbleRecoveries = Double.parseDouble(defenseData[13+counter]);
				currentTotals.fumbleRecoveryTouchdowns = Double.parseDouble(defenseData[14+counter]);
				currentTotals.passesDefended = Double.parseDouble(defenseData[15+counter]);
			}
		}
		Document downsDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Downs&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements downsTeamList = downsDoc.getElementsByTag("tr");
		for(int i = 1; i < downsTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = downsTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] downsData = teamData.text().replace("%", "").split(" ");
				currentTotals.fourthDownsMade = Double.parseDouble(downsData[9+counter]);
				currentTotals.fourthDownsAttempted = Double.parseDouble(downsData[10+counter]);
			}
		}
		Document yardageDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week=0&category=Yardage&team=1&opp=0&qualified=1&sortOrder=0"), 30000);
		Elements yardageTeamList = yardageDoc.getElementsByTag("tr");
		for(int i = 1; i < yardageTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = yardageTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] yardageData = teamData.text().replace("%", "").split(" ");
				currentTotals.totalYards = Double.parseDouble(yardageData[3+counter]);
			}
		}
	}
	
	public void computeWeeklyTotals(int weekNumber, TeamTotals weeklyTotals) throws Exception
	{		
		Document standingsDoc = Jsoup.parse(new URL("http://www.nfl.com/standings"), 30000);
		Elements standingsTeamList = standingsDoc.getElementsByTag("tr");
		for(int i = 2; i < standingsTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = standingsTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] standingsData = teamData.text().split(" ");
				weeklyTotals.wins = Double.parseDouble(standingsData[2+counter]);
				weeklyTotals.losses = Double.parseDouble(standingsData[3+counter]);
			}
		}
		
		Document passingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=PASSING&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements passingTeamList = passingDoc.getElementsByTag("tr");
		for(int i = 1; i < passingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = passingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] passingData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.passingCompletions = Double.parseDouble(passingData[2+counter]);
				weeklyTotals.passingAttempts = Double.parseDouble(passingData[3+counter]);
				weeklyTotals.passingPctCompletion = Double.parseDouble(passingData[4+counter]);
				weeklyTotals.passingYards = Double.parseDouble(passingData[5+counter]);
				weeklyTotals.passingAvgYards = Double.parseDouble(passingData[6+counter]);
				weeklyTotals.passingTouchdowns = Double.parseDouble(passingData[8+counter]);
				weeklyTotals.passingInterceptions = Double.parseDouble(passingData[9+counter]);
				weeklyTotals.passingQBRating = Double.parseDouble(passingData[10+counter]);
				weeklyTotals.passingSacks = Double.parseDouble(passingData[11+counter]);
				weeklyTotals.passingSackedYards = Double.parseDouble(passingData[12+counter]);
				weeklyTotals.passing1stDowns = Double.parseDouble(passingData[13+counter]);				
			}
		}
		Document rushingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Rushing&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements rushingTeamList = rushingDoc.getElementsByTag("tr");
		for(int i = 1; i < rushingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = rushingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] rushingData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.rushingAttempts = Double.parseDouble(rushingData[2+counter]);
				weeklyTotals.rushingYards = Double.parseDouble(rushingData[3+counter]);
				weeklyTotals.rushingAvgYards = Double.parseDouble(rushingData[4+counter]);
				weeklyTotals.rushingTouchdowns = Double.parseDouble(rushingData[5+counter]);				
			}
		}
		Document kickingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Kicking&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements kickingTeamList = kickingDoc.getElementsByTag("tr");
		for(int i = 1; i < kickingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = kickingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] kickingData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.kickingPoints = Double.parseDouble(kickingData[2+counter]);
				weeklyTotals.fieldGoalsMade = Double.parseDouble(kickingData[3+counter]);
				weeklyTotals.fieldGoalsAttempted = Double.parseDouble(kickingData[4+counter]);
				if(weeklyTotals.fieldGoalsAttempted  > 0)
				{
					weeklyTotals.fieldGoalPctCompletion = Double.parseDouble(kickingData[5+counter]);
				}
				weeklyTotals.extraPointsMade = Double.parseDouble(kickingData[8+counter]);
				weeklyTotals.extraPointsAttempted = Double.parseDouble(kickingData[9+counter]);
			}
		}
		Document puntingDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Punting&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements puntingTeamList = puntingDoc.getElementsByTag("tr");
		for(int i = 1; i < puntingTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = puntingTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] puntingData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.punts = Double.parseDouble(puntingData[2+counter]);
			}
		}
		Document returningDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Returning&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements returningTeamList = returningDoc.getElementsByTag("tr");
		for(int i = 1; i < returningTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = returningTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] returningData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.kicksReturned = Double.parseDouble(returningData[2+counter]);
				if(weeklyTotals.kicksReturned > 0)
				{
					weeklyTotals.kickReturnedYards = Double.parseDouble(returningData[3+counter]);
					weeklyTotals.kickAvgReturnedYards = Double.parseDouble(returningData[4+counter]);
				}
				weeklyTotals.kickReturnedTouchdowns = Double.parseDouble(returningData[5+counter]);
				weeklyTotals.puntReturns = Double.parseDouble(returningData[7+counter]);
				if(weeklyTotals.puntReturns > 0)
				{
					weeklyTotals.puntReturnedYards = Double.parseDouble(returningData[8+counter]);
					weeklyTotals.puntAvgReturnedYards = Double.parseDouble(returningData[9+counter]);
				}
				weeklyTotals.puntReturnedTouchdowns = Double.parseDouble(returningData[10+counter]);
			}
		}
		Document defenseDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Defense&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements defenseTeamList = defenseDoc.getElementsByTag("tr");
		for(int i = 1; i < defenseTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = defenseTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] defenseData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.pointsAllowed = Double.parseDouble(defenseData[2+counter]);
				weeklyTotals.tacklesMade = Double.parseDouble(defenseData[3+counter]);
				weeklyTotals.sacksMade = Double.parseDouble(defenseData[6+counter]);
				weeklyTotals.sackForYards = Double.parseDouble(defenseData[7+counter]);
				weeklyTotals.interceptionsCaught = Double.parseDouble(defenseData[8+counter]);
				weeklyTotals.interceptionTouchdowns = Double.parseDouble(defenseData[9+counter]);
				weeklyTotals.forcedFumbles = Double.parseDouble(defenseData[10+counter]);
				weeklyTotals.fumbleRecoveries = Double.parseDouble(defenseData[11+counter]);
				weeklyTotals.fumbleRecoveryTouchdowns = Double.parseDouble(defenseData[12+counter]);
				weeklyTotals.passesDefended = Double.parseDouble(defenseData[13+counter]);
			}
		}
		Document downsDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Downs&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements downsTeamList = downsDoc.getElementsByTag("tr");
		for(int i = 1; i < downsTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = downsTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] downsData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.thirdDownsMade = Double.parseDouble(downsData[5+counter]);
				weeklyTotals.fourthDownsMade = Double.parseDouble(downsData[8+counter]);
				weeklyTotals.fourthDownsAttempted = Double.parseDouble(downsData[9+counter]);
			}
		}
		Document yardageDoc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season=2015&seasonType=1&week="+weekNumber+"&category=Yardage&team=1&opp=0&sort=3&qualified=0"), 30000);
		Elements yardageTeamList = yardageDoc.getElementsByTag("tr");
		for(int i = 1; i < yardageTeamList.size(); i++)	//i Starts at 1 to skip header
		{
			Element teamData = yardageTeamList.get(i);
			if(teamData.text().replace(" ", "").contains(name.replace(" ", "")))
			{
				String[] yardageData = teamData.text().replace("%", "").split(" ");
				weeklyTotals.totalYards = Double.parseDouble(yardageData[2+counter]);
			}
		}
	}
	
	public void printToFile(TeamTotals totals) throws Exception
	{
		printToFile(0, totals);
	}
	public void printToFile(int weekNumber, TeamTotals totals) throws Exception
	{
		String fileName = weekNumber == 0? "F:/draftKings/currentTotals.txt" : "F:/draftKings/week_"+weekNumber+"_totals.txt";
		File file = new File(fileName);
		if(!file.exists())
		{
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append(name + " " + totals.gamesPlayed + " " + totals.wins + " " + totals.losses + " " + totals.passingCompletions+ " " + totals.passingAttempts+ " " + totals.passingYards+ " " + totals.passingAvgYards+ " " 
				+ totals.passingYardsPerGame + " " + totals.passingTouchdowns+ " " + totals.passingInterceptions+ " " + totals.passingQBRating+ " " + totals.passingBIGPasses+ " " + totals.passingSacks+ " " 
				+ totals.passingSackedYards + " " + totals.passing1stDowns+ " " + totals.rushingAttempts+ " " + totals.rushingYards+ " " + totals.rushingAvgYards+ " " + totals.rushingYardsPerGame+ " " 
				+ totals.rushingTouchdowns + " " + totals.rushingBIGRushes+ " " + totals.rushingFumbles+ " " + totals.kickingPoints+ " " + totals.fieldGoalsMade+ " " + totals.fieldGoalsAttempted+ " " 
				+ totals.fieldGoalPctCompletion + " " + totals.extraPointsMade+ " " + totals.extraPointsAttempted+ " " + totals.punts+ " " + totals.blockedPunts+ " " + totals.kicksReturned+ " " + totals.kickReturnedYards
				+ " " + totals.kickAvgReturnedYards+ " " + totals.kickReturnedTouchdowns+ " " + totals.puntReturns+ " " + totals.puntReturnedYards+ " " + totals.puntAvgReturnedYards
				+ " " + totals.puntReturnedTouchdowns+ " " + totals.pointsAllowed+ " " + totals.pointsAllowedPerGame+ " " + totals.tacklesMade+ " " + totals.sacksMade+ " " + totals.sackForYards
				+ " " + totals.interceptionsCaught+ " " + totals.interceptionTouchdowns+ " " + totals.forcedFumbles+ " " + totals.fumbleRecoveries+ " " + totals.fumbleRecoveryTouchdowns
				+ " " + totals.passesDefended+ " " + totals.saftiesGiven+ " " + totals.thirdDownsMade+ " " + totals.fourthDownsMade+ " " + totals.fourthDownsAttempted+ " " + totals.penalties
				+ " " + totals.penaltyYards+ " " + totals.totalYards+"\n");
		
		fw.flush();
		bw.flush();
	}
	
	public void calculateBestOverall(Team o)
	{
		int weeksPlayed = weeklyTotalsList.size();
		if(weeklyTotalsList.size() > o.weeklyTotalsList.size())
			weeksPlayed = o.weeklyTotalsList.size();
		
		if(((TeamTotals)this.currentTotals).wins > ((TeamTotals)o.currentTotals).wins)
			this.comparisonPoints += (((TeamTotals)this.currentTotals).wins - ((TeamTotals)o.currentTotals).wins) * 10;
//		else
//			o.comparisonPoints += (((TeamTotals)o.currentTotals).wins - ((TeamTotals)this.currentTotals).wins) * 10;

		for(int i = 0; i < weeksPlayed; i++)
		{
			TeamTotals week1TotalsA = (TeamTotals)this.weeklyTotalsList.get(i);
			TeamTotals week1TotalsB = (TeamTotals)o.weeklyTotalsList.get(i);
			
			double touchdownsA = week1TotalsA.passingTouchdowns + week1TotalsA.rushingTouchdowns + week1TotalsA.interceptionTouchdowns
				+ week1TotalsA.kickReturnedTouchdowns + week1TotalsA.puntReturnedTouchdowns;
			double touchdownsB = week1TotalsB.passingTouchdowns + week1TotalsB.rushingTouchdowns + week1TotalsB.interceptionTouchdowns
			+ week1TotalsB.kickReturnedTouchdowns + week1TotalsB.puntReturnedTouchdowns;
			
			if(touchdownsA > touchdownsB)
				this.comparisonPoints += (touchdownsA - touchdownsB) * 2;
//			else if (touchdownsA < touchdownsB)
//				o.comparisonPoints += (touchdownsB - touchdownsA) * 2;
			if(week1TotalsA.interceptionsCaught > week1TotalsB.interceptionsCaught)
				this.comparisonPoints += week1TotalsA.interceptionsCaught - week1TotalsB.interceptionsCaught;
//			else if(week1TotalsA.interceptionsCaught < week1TotalsB.interceptionsCaught)
//				o.comparisonPoints += week1TotalsB.interceptionsCaught - week1TotalsA.interceptionsCaught;
			if(week1TotalsA.passesDefended > week1TotalsB.passesDefended)
				this.comparisonPoints += week1TotalsA.passesDefended - week1TotalsB.passesDefended;
//			else if(week1TotalsA.passesDefended < week1TotalsB.passesDefended)
//				o.comparisonPoints += week1TotalsB.passesDefended - week1TotalsA.passesDefended;
			if(week1TotalsB.pointsAllowed > week1TotalsA.pointsAllowed)
				this.comparisonPoints += week1TotalsB.pointsAllowed - week1TotalsA.pointsAllowed;
//			else if(week1TotalsB.pointsAllowed < week1TotalsA.pointsAllowed)
//				o.comparisonPoints += week1TotalsA.pointsAllowed - week1TotalsB.pointsAllowed;
			if(week1TotalsA.forcedFumbles > week1TotalsB.forcedFumbles)
				this.comparisonPoints += week1TotalsA.forcedFumbles - week1TotalsB.forcedFumbles;
//			else if(week1TotalsA.forcedFumbles < week1TotalsB.forcedFumbles)
//				o.comparisonPoints += (week1TotalsB.forcedFumbles - week1TotalsA.forcedFumbles) * 5;
			if(week1TotalsB.punts > week1TotalsA.punts)
				this.comparisonPoints += week1TotalsB.punts - week1TotalsA.punts;
//			else if(week1TotalsB.punts < week1TotalsA.punts)
//				o.comparisonPoints += week1TotalsA.punts - week1TotalsB.punts;
		}
	}
	public void calculateBestRushingOffense(Team o)
	{
		int weeksPlayed = weeklyTotalsList.size();
		if(weeklyTotalsList.size() > o.weeklyTotalsList.size())
			weeksPlayed = o.weeklyTotalsList.size();
		
		if(((TeamTotals)this.currentTotals).wins > ((TeamTotals)o.currentTotals).wins)
			this.comparisonPoints += (((TeamTotals)this.currentTotals).wins - ((TeamTotals)o.currentTotals).wins) * 10;

		for(int i = 0; i < weeksPlayed; i++)
		{
			TeamTotals week1TotalsA = (TeamTotals)this.weeklyTotalsList.get(i);
			TeamTotals week1TotalsB = (TeamTotals)o.weeklyTotalsList.get(i);
			
			double touchdownsA = week1TotalsA.rushingTouchdowns + week1TotalsA.kickReturnedTouchdowns + week1TotalsA.puntReturnedTouchdowns;
			double touchdownsB = week1TotalsB.rushingTouchdowns + week1TotalsB.kickReturnedTouchdowns + week1TotalsB.puntReturnedTouchdowns;
			
			if(touchdownsA > touchdownsB)
				this.comparisonPoints += (touchdownsA - touchdownsB) * 2;
			if(week1TotalsA.fourthDownsMade > week1TotalsB.fourthDownsMade)
				this.comparisonPoints += week1TotalsA.fourthDownsMade - week1TotalsB.fourthDownsMade;
			if(week1TotalsA.kickAvgReturnedYards > week1TotalsB.kickAvgReturnedYards)
				this.comparisonPoints += week1TotalsA.kickAvgReturnedYards - week1TotalsB.kickAvgReturnedYards;
			if(week1TotalsA.kickReturnedYards > week1TotalsB.kickReturnedYards)
				this.comparisonPoints += week1TotalsA.kickReturnedYards - week1TotalsB.kickReturnedYards;
			if(week1TotalsA.kicksReturned > week1TotalsB.kicksReturned)
				this.comparisonPoints += week1TotalsA.kicksReturned - week1TotalsB.kicksReturned;
			if(week1TotalsA.puntAvgReturnedYards > week1TotalsB.puntAvgReturnedYards)
				this.comparisonPoints += week1TotalsA.puntAvgReturnedYards - week1TotalsB.puntAvgReturnedYards;
			if(week1TotalsA.puntReturnedYards > week1TotalsB.puntReturnedYards)
				this.comparisonPoints += week1TotalsA.puntReturnedYards - week1TotalsB.puntReturnedYards;
			if(week1TotalsA.puntReturns > week1TotalsB.puntReturns)
				this.comparisonPoints += week1TotalsA.puntReturns - week1TotalsB.puntReturns;
			if(week1TotalsA.rushingAttempts > week1TotalsB.rushingAttempts)
				this.comparisonPoints += week1TotalsA.rushingAttempts - week1TotalsB.rushingAttempts;
			if(week1TotalsA.rushingAvgYards > week1TotalsB.rushingAvgYards)
				this.comparisonPoints += week1TotalsA.rushingAvgYards - week1TotalsB.rushingAvgYards;
			if(week1TotalsA.rushingBIGRushes > week1TotalsB.rushingBIGRushes)
				this.comparisonPoints += week1TotalsA.rushingBIGRushes - week1TotalsB.rushingBIGRushes;
			if(week1TotalsA.rushingYards > week1TotalsB.rushingYards)
				this.comparisonPoints += week1TotalsA.rushingYards - week1TotalsB.rushingYards;
			if(week1TotalsA.thirdDownsMade > week1TotalsB.thirdDownsMade)
				this.comparisonPoints += week1TotalsA.thirdDownsMade - week1TotalsB.thirdDownsMade;
		}
	}
	public void calculateBestPassingOffense(Team o)
	{
		int weeksPlayed = weeklyTotalsList.size();
		if(weeklyTotalsList.size() > o.weeklyTotalsList.size())
			weeksPlayed = o.weeklyTotalsList.size();
		
		if(((TeamTotals)this.currentTotals).wins > ((TeamTotals)o.currentTotals).wins)
			this.comparisonPoints += (((TeamTotals)this.currentTotals).wins - ((TeamTotals)o.currentTotals).wins) * 10;

		for(int i = 0; i < weeksPlayed; i++)
		{
			TeamTotals week1TotalsA = (TeamTotals)this.weeklyTotalsList.get(i);
			TeamTotals week1TotalsB = (TeamTotals)o.weeklyTotalsList.get(i);
			
			double touchdownsA = week1TotalsA.passingTouchdowns;
			double touchdownsB = week1TotalsA.passingTouchdowns;
			
			if(touchdownsA > touchdownsB)
				this.comparisonPoints += (touchdownsA - touchdownsB) * 2;
			if(week1TotalsA.passing1stDowns > week1TotalsB.passing1stDowns)
				this.comparisonPoints += week1TotalsA.passing1stDowns - week1TotalsB.passing1stDowns;
			if(week1TotalsA.passingAttempts > week1TotalsB.passingAttempts)
				this.comparisonPoints += week1TotalsA.passingAttempts - week1TotalsB.passingAttempts;
			if(week1TotalsA.passingAvgYards > week1TotalsB.passingAvgYards)
				this.comparisonPoints += week1TotalsA.passingAvgYards - week1TotalsB.passingAvgYards;
			if(week1TotalsA.passingBIGPasses > week1TotalsB.passingBIGPasses)
				this.comparisonPoints += week1TotalsA.passingBIGPasses - week1TotalsB.passingBIGPasses;
			if(week1TotalsA.passingCompletions > week1TotalsB.passingCompletions)
				this.comparisonPoints += week1TotalsA.passingCompletions - week1TotalsB.passingCompletions;
			if(week1TotalsA.passingInterceptions > week1TotalsB.passingInterceptions)
				this.comparisonPoints -= week1TotalsA.passingInterceptions - week1TotalsB.passingInterceptions;
			if(week1TotalsA.passingPctCompletion > week1TotalsB.passingPctCompletion)
				this.comparisonPoints += week1TotalsA.passingPctCompletion - week1TotalsB.passingPctCompletion;
			if(week1TotalsA.passingSacks > week1TotalsB.passingSacks)
				this.comparisonPoints -= week1TotalsA.passingSacks - week1TotalsB.passingSacks;
			if(week1TotalsA.passingYards > week1TotalsB.passingYards)
				this.comparisonPoints += week1TotalsA.passingYards - week1TotalsB.passingYards;
			if(week1TotalsA.thirdDownsMade > week1TotalsB.thirdDownsMade)
				this.comparisonPoints += week1TotalsA.thirdDownsMade - week1TotalsB.thirdDownsMade;
		}
	}
	@Override
	public int compareTo(Team o)
	{
		return Integer.valueOf(comparisonPoints).compareTo(Integer.valueOf(o.comparisonPoints));
	}
}

class TeamTotals extends Totals
{
	public double gamesPlayed;
	public double wins;
	public double losses;
	public double passingCompletions;
	public double passingAttempts;
	public double passingPctCompletion;
	public double passingYards;
	public double passingAvgYards;
	public double passingYardsPerGame;
	public double passingTouchdowns;
	public double passingInterceptions;
	public double passingQBRating;
	public double passingBIGPasses;
	public double passingSacks;
	public double passingSackedYards;
	public double passing1stDowns;
	public double rushingAttempts;
	public double rushingYards;
	public double rushingAvgYards;
	public double rushingYardsPerGame;
	public double rushingTouchdowns;
	public double rushingBIGRushes;
	public double rushingFumbles;
	public double kickingPoints;
	public double fieldGoalsMade;
	public double fieldGoalsAttempted;
	public double fieldGoalPctCompletion;
	public double extraPointsMade;
	public double extraPointsAttempted;
	public double punts;
	public double blockedPunts;
	public double kicksReturned;
	public double kickReturnedYards;
	public double kickAvgReturnedYards;
	public double kickReturnedTouchdowns;
	public double puntReturns;
	public double puntReturnedYards;
	public double puntAvgReturnedYards;
	public double puntReturnedTouchdowns;
	public double pointsAllowed;
	public double pointsAllowedPerGame;
	public double tacklesMade;
	public double sacksMade;
	public double sackForYards;
	public double interceptionsCaught;
	public double interceptionTouchdowns;
	public double forcedFumbles;
	public double fumbleRecoveries;
	public double fumbleRecoveryTouchdowns;
	public double passesDefended;
	public double saftiesGiven;
	public double thirdDownsMade;
	public double fourthDownsMade;
	public double fourthDownsAttempted;
	public double penalties;
	public double penaltyYards;
	public double totalYards;
}