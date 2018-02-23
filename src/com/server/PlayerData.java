package com.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.collections4.ListUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.common.Player;
import com.common.QuarterBack;
import com.common.RunningBack;
import com.common.Team;
import com.common.TightEnd;
import com.common.WideReceiver;

public class PlayerData
{
	public ArrayList<QuarterBack> quarterBackList;
	public static ArrayList<RunningBack> runningBackList;
	public static ArrayList<WideReceiver> wideReceiversList;
	public static ArrayList<TightEnd> tightEndList;
	public static ArrayList<Team> teamsList;
	public static ConcurrentHashMap<String, String> injuryMap = new ConcurrentHashMap<String, String>();
	
	public static final int SEASON_START_WEEK = 37;		// Regular Season usually starts in September-ish
	public static final int WEEK_OVERRIDE = 3, YEAR_OVERRIDE = 2017;
	public static final int WEEK_NUMBER = WEEK_OVERRIDE == 0? Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - SEASON_START_WEEK : WEEK_OVERRIDE;
	public static final int CALENDAR_YEAR = YEAR_OVERRIDE == 0? Calendar.getInstance().get(Calendar.YEAR) : YEAR_OVERRIDE;
	
	public static final int THREAD_COUNT = Integer.valueOf(System.getProperty("project.loaddata.threadpool", "64"));
	public static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
	
	/* TODO:
	 *     1. Improve performance by caching/saving website data to disk and loading from disk instead of from network call.
	 *        a. Implement structure for which files contain what data.
	 *        b. Could be worthwhile to connect to MSSQL/Oracle
	 *     2. Improve rankPlayers method. Specifically, improve (business) logic behind 'Player A is better than Player B'.
	 *     3. Improve logic behind injuryList to not have to re-loop through all the players and make sure that they're accounted for.
	 *     4. Evaluate abstracting out JSoup calls/implementation somehow to a data structure to clean up code.
	 *     	  a. Maybe this isn't needed. PlayerData.java is the only place JSoup is used, so encapsulation is good.
	 *     5. Clean up any excess code, and remove any hard-coded "fixes" like a name fix for E.J. Manual and R.Griffin III
	 *     6. Research the SEASON_START_WEEK to see if this can be controlled better than a hard-coded value
	 *     7. Migrate Teams Comparator to Team.java. You likely want to know best rushing defense, best passing defense, and best overall defense.
	 *     	  a. This logic will likely be implemented in PanelWeeklyPick as well so that Good RBs are pitted against Bad defenses etc.
	 */
	public void loadData()
	{
		try
		{
//			for(int weekNumber = 0; weekNumber <= WEEK_NUMBER; weekNumber++)
//			{
//				String fileName = weekNumber == 0? "F:/draftKings/currentTotals.txt" : "F:/draftKings/week_"+weekNumber+"_totals.txt";
//				File file = new File(fileName);
//				if(file.exists())
//				{
//					file.delete();
//				}
//			}
			try
			{
				Document doc = Jsoup.parse(new URL("http://www.nfl.com/injuries?week="+WEEK_NUMBER), 10000);
				Elements playersFromWebsite = doc.getElementsByClass("player-expanded");
				Pattern p = Pattern.compile("[{][p][l][a][y][e][r].*[}]");
				
				for(Element element : playersFromWebsite)
				{
					Matcher m = p.matcher(element.html());
					while( m.find() )
					{						
						String groupValue = m.group();
						
						String position = groupValue.substring((groupValue.indexOf("position: \"")+"position: \"".length()), groupValue.indexOf("position: \"")+"position: \"".length() + 2);
						if(!"WR".equals(position) && !"RB".equals(position) && !"TE".equals(position) && !"QB".equals(position))
							continue;
						
						String value = groupValue.substring(groupValue.indexOf("\""), groupValue.indexOf(",") -1);
						value = value.replace("\"", "");
						value = value.trim();
						
						injuryMap.put(value, groupValue);
					}
				}
				if(injuryMap.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "No players were found on injury list. Please confirm by visiting: http://www.nfl.com/injuries?week=" + WEEK_NUMBER);
				}			
			}
			catch(Throwable thr) { throw new RuntimeException(thr.getMessage()); }
			
			loadQB();
			loadRB();
			loadWR();
			loadTE();
			loadTeam();

			rankPlayers();
			while(((ThreadPoolExecutor)threadPool).getActiveCount() > 0)
			{
				System.out.println("Sleeping... " + ((ThreadPoolExecutor)threadPool).getActiveCount());
				Thread.sleep(1 * 1000);
			}


		}
		catch(Throwable thr)
		{
			thr.printStackTrace();
		}
	}
	public static void main(String... args)
	{
		PlayerData main = new PlayerData();
		main.loadData();
	}
	
	public void loadTeam()
	{
		try
		{
			File currentDataFile = new File("F:/draftKings/week_"+WEEK_NUMBER+"_totals.txt");
			if(currentDataFile.exists())
			{
				FileReader fr = new FileReader(currentDataFile);
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				for(int i = 0; line != null; i++, line = br.readLine())
				{
					Team team = new Team(line);
					teamsList.add(team);
				}				
			}
			else
			{
				Document doc = Jsoup.parse(new URL("http://www.foxsports.com/nfl/stats?season="+CALENDAR_YEAR+"&seasonType=1&week=0&category=Passing&team=1&opp=0"), 10000);
				Elements teamList = doc.getElementsByTag("tr");
				for(int i = 1; i < teamList.size(); i++)	//i Starts at 1 to skip header
				{
					Element teamData = teamList.get(i);
					Team team = new Team(teamData.text().replace("%", "").split(" "));
					teamsList.add(team);
					System.out.println(teamsList.get(i-1).name);
				}
			}
		}
		catch(Throwable thr)
		{
			thr.printStackTrace();
		}
	}
	
	class PlayerDataLoad extends Thread
	{
		private String type;
		private List<Element> playersFromWebsite;
		private ArrayList copyVector; 
		
		public PlayerDataLoad(String type, List<Element> playersFromWebsite, ArrayList copyVector)
		{
			this.type = type;
			this.playersFromWebsite = playersFromWebsite;
			this.copyVector = copyVector;
		}
		
		public void run()
		{
			try
			{
				URL salaryURL     = null;
				String categoryId = null;
				Class playerClass = null;
				
				if("QB".equals(type))
				{
					salaryURL     = new URL("http://rotoguru1.com/cgi-bin/fstats.cgi?pos=1&sort=4&game=p&colA=0&daypt=0&xavg=0&inact=0&maxprc=99999&outcsv=1");
					categoryId    = "0"; 
					playerClass   = QuarterBack.class;
				}
				else if("RB".equals(type))
				{
					salaryURL     = new URL("http://rotoguru1.com/cgi-bin/fstats.cgi?pos=2&sort=4&game=p&colA=0&daypt=0&xavg=0&inact=0&maxprc=99999&outcsv=1");
					categoryId    = "2";
					playerClass   = RunningBack.class;
				}
				else if("WR".equals(type))
				{
					salaryURL     = new URL("http://rotoguru1.com/cgi-bin/fstats.cgi?pos=3&sort=4&game=p&colA=0&daypt=0&xavg=0&inact=0&maxprc=99999&outcsv=1");
					categoryId    = "4"; 
					playerClass   = WideReceiver.class;
				}
				else if("TE".equals(type))
				{
					salaryURL     = new URL("http://rotoguru1.com/cgi-bin/fstats.cgi?pos=4&sort=4&game=p&colA=0&daypt=0&xavg=0&inact=0&maxprc=99999&outcsv=1");
					categoryId    = "6";
					playerClass   = TightEnd.class;
				}
				
				for(int i = 0; i < playersFromWebsite.size(); i++)	
				{
					Player singlePlayer            = (Player)playerClass.newInstance();
					Element singlePlayerElement    = playersFromWebsite.get(i);
					singlePlayer.setInitialData(singlePlayerElement.text().replace(" III", "").split(" "));	//Robert Griffin III messes up ordering and therefore calculation
					
					//Names are messed up, Need to get single source for name and perhaps hashmap it
					if(!injuryMap.containsKey(singlePlayer.name))
					{
						List list = (List) Arrays.asList(injuryMap.entrySet().toArray());
					    for(int j = 0; j < list.size(); j++)
					    { 
					        Map.Entry pair = (Map.Entry)list.get(j);
					        String s = (String)pair.getValue();
					        
					        String position = s.substring((s.indexOf("position: \"")+"position: \"".length()), s.indexOf("position: \"")+"position: \"".length() + 2);
							if(!type.equals(position)){ continue; }
							
							String lastName = s.substring((s.indexOf("lastName: \"")+"lastName: \"".length()), s.indexOf("firstName: \"") - 3);
							if(lastName.equals(singlePlayer.name.substring(singlePlayer.name.indexOf(" ")+1)))
							{
								injuryMap.put(singlePlayer.name, s);
							}
					    }
					}
					if(injuryMap.containsKey(singlePlayer.name))
						singlePlayer.setIsInjured(true);
					
					//load weekly totals
					for(int j = 1; j <= WEEK_NUMBER; j++)	// j Start at 1 because it's week 1
					{
						try
						{
							Document doc2 = Jsoup.parse(new URL("http://games.espn.go.com/ffl/leaders?&slotCategoryId="+categoryId+"&scoringPeriodId="+j+"&seasonId="+CALENDAR_YEAR), 10000);
							Elements weeklyPlayerData = null;
							int recordsInResultSet = 50;
							while(true)
							{
								try
								{
									weeklyPlayerData = doc2.getElementsContainingOwnText(singlePlayer.name).parents().get(1).getElementsByAttributeValue("class", "playertableStat");					
									if(weeklyPlayerData == null || weeklyPlayerData.size() <= 0) {
										throw new Exception("Could not find data for player " + singlePlayer.getFullNameReversed() );
									}
									break;
								}
								catch(Throwable thr )
								{									
									if(recordsInResultSet > 500)
										break;
									
									URL nextPage = new URL("http://games.espn.go.com/ffl/leaders?&slotCategoryId="+categoryId+"&scoringPeriodId="+j+
											"&seasonId="+CALENDAR_YEAR+"&search=&startIndex="+recordsInResultSet);
									doc2 = Jsoup.parse(nextPage, 10000);

									recordsInResultSet += 50;								
//									if(thr instanceof java.net.ConnectException)
//										j--;	// retry
//									else
//									{
//										Document byeWeeks = Jsoup.parse(new URL("http://www.nfl.com/news/story/0ap3000000487430/article/"+CALENDAR_YEAR+"-nfl-schedule-when-is-your-teams-bye-week"), 10000);
//										List children = byeWeeks.getElementsContainingOwnText("Week " + 4).parents().get(0).childNodes();
//										boolean foundBye = false;
//										for(int x = 0; x < children.size(); x++)
//										{
//											if ( x == children.size() -2)
//												break;
//											if(children.get(x).toString().contains("Week " + j +"<") && children.get(x + 2).toString().contains(rb.team))
//											{
//												foundBye = true;
//												break;
//											}
//										}
//										rb.setByeWeek(foundBye);
//									}
								}
							}

							singlePlayer.addWeeklyTotals(weeklyPlayerData);	
						}
						catch(Throwable thr) { thr.printStackTrace(); singlePlayer.addWeeklyTotals(null); continue; }		
					}
					Document doc3 = Jsoup.parse(salaryURL, 10000);
	
					String fullName  = singlePlayer.getFullNameReversed().replace(", EJ", ", E.J.").replace("Griffin", "Griffin III");
					String fullList  = doc3.text();
					fullList = fullList.substring(fullList.indexOf(fullName) +fullName.length());
					singlePlayer.setDKValue(fullList.split(";")[4]);
					singlePlayer.calculateAverageDKPoints();
					
					copyVector.add(singlePlayer);
				}
			}
			catch(Throwable thr)
			{
				thr.printStackTrace();
			}
		}
	}
	
	public void loadQB()
	{
		List<Element> playerList = getPlayerData("QB");	
		quarterBackList = new ArrayList<QuarterBack>(playerList.size());
		List<List<Element>> subSets = ListUtils.partition(playerList, 3);
		for(int i = 0; i < subSets.size(); i++)
		{
			PlayerDataLoad playerDataLoader = new PlayerDataLoad("QB", (List<Element>)subSets.get(i), quarterBackList);
			threadPool.submit(playerDataLoader);				
		}
	}
	public void loadRB()
	{
		List<Element> playerList = getPlayerData("RB");	
		runningBackList = new ArrayList<RunningBack>(playerList.size());
		List<List<Element>> subSets = ListUtils.partition(playerList, 3);
		for(int i = 0; i < subSets.size(); i++)
		{
			PlayerDataLoad playerDataLoader = new PlayerDataLoad("RB", (List<Element>)subSets.get(i), runningBackList);
			threadPool.submit(playerDataLoader);				
		}
	}
	public void loadWR()
	{
		List<Element> playerList = getPlayerData("WR");	
		wideReceiversList = new ArrayList<WideReceiver>(playerList.size());
		List<List<Element>> subSets = ListUtils.partition(playerList, 3);
		for(int i = 0; i < subSets.size(); i++)
		{
			PlayerDataLoad playerDataLoader = new PlayerDataLoad("WR", (List<Element>)subSets.get(i), wideReceiversList);
			threadPool.submit(playerDataLoader);				
		}
	}
	public void loadTE()
	{
		List<Element> playerList = getPlayerData("TE");	
		tightEndList = new ArrayList<TightEnd>(playerList.size());
		List<List<Element>> subSets = ListUtils.partition(playerList, 3);
		for(int i = 0; i < subSets.size(); i++)
		{
			PlayerDataLoad playerDataLoader = new PlayerDataLoad("TE", (List<Element>)subSets.get(i), tightEndList);
			threadPool.submit(playerDataLoader);				
		}
	}
	public List<Element> getPlayerData(String type)
	{
		List<Element> playerList = null;
		try
		{
			URL playerListURL = null;
			if("QB".equals(type))
			{
				playerListURL = new URL("http://www.nfl.com/stats/categorystats?tabSeq=1&statisticPositionCategory=QUARTERBACK&qualified=true&season="+CALENDAR_YEAR+"&seasonType=REG");
			}
			else if("RB".equals(type))
			{
				playerListURL = new URL("http://www.nfl.com/stats/categorystats?tabSeq=1&statisticPositionCategory=RUNNING_BACK&qualified=true&season="+CALENDAR_YEAR+"&seasonType=REG");
			}
			else if("WR".equals(type))
			{
				playerListURL = new URL("http://www.nfl.com/stats/categorystats?tabSeq=1&statisticPositionCategory=WIDE_RECEIVER&qualified=true&season="+CALENDAR_YEAR+"&seasonType=REG");
			}
			else if("TE".equals(type))
			{
				playerListURL = new URL("http://www.nfl.com/stats/categorystats?tabSeq=1&statisticPositionCategory=TIGHT_END&qualified=true&season="+CALENDAR_YEAR+"&seasonType=REG");
			}
			
			Document doc = Jsoup.parse(playerListURL, 10000);
			Elements websiteData = doc.getElementsByTag("tr");
			if(websiteData.size() <= 0)
				throw new Exception("No player data found from : " + playerListURL);
			
			playerList = websiteData.subList(1, websiteData.size());	//Starts at 1 to skip header
		}
		catch(Throwable thr) { thr.printStackTrace(); }
		
		return playerList;
	}

	class LoadTeam extends Thread
	{	
		PlayerData mainClass;
		public LoadTeam(PlayerData main)
		{
			this.mainClass = main;
		}
		public void run()
		{
			mainClass.loadTeam();
			for(int i = 0; i < teamsList.size(); i++)
			{
				Team t1 = teamsList.get(i);
				for(int j = 0; j < teamsList.size(); j++)
				{
					if( i == j)
						continue;
					else
					{					
						Team t2 = teamsList.get(j);
						t1.calculateBestOverall(t2);
					}
				}
			}
			Collections.sort(teamsList, new Comparator<Team>() {
				@Override
				public int compare(Team o1, Team o2)
				{
					return o1.compareTo(o2);
				}			
			});
			System.out.println("\n=== Best Teams ===\n");
			for(int i = teamsList.size() -1; i >= 0; i--)
			{
				System.out.println( teamsList.size() -i + " Name: " + teamsList.get(i).name + " Comparison Points: " + teamsList.get(i).comparisonPoints);
				teamsList.get(i).comparisonPoints = 0;
			}		
			System.out.println("\n=== Best Rushing Teams ===\n");
			for(int i = 0; i < teamsList.size(); i++)
			{
				Team t1 = teamsList.get(i);
				for(int j = 0; j < teamsList.size(); j++)
				{
					if( i == j)
						continue;
					else
					{					
						Team t2 = teamsList.get(j);
						t1.calculateBestRushingOffense(t2);
					}
				}
			}
			Collections.sort(teamsList, new Comparator<Team>() {
				@Override
				public int compare(Team o1, Team o2)
				{
					return o1.compareTo(o2);
				}			
			});
			for(int i = teamsList.size() -1; i >= 0; i--)
			{
				System.out.println( teamsList.size() -i + " Name: " + teamsList.get(i).name + " Comparison Points: " + teamsList.get(i).comparisonPoints);
				teamsList.get(i).comparisonPoints = 0;
			}
			System.out.println("\n=== Best Passing Teams ===\n");
			for(int i = 0; i < teamsList.size(); i++)
			{
				Team t1 = teamsList.get(i);
				for(int j = 0; j < teamsList.size(); j++)
				{
					if( i == j)
						continue;
					else
					{					
						Team t2 = teamsList.get(j);
						t1.calculateBestPassingOffense(t2);
					}
				}
			}
			Collections.sort(teamsList, new Comparator<Team>() {
				@Override
				public int compare(Team o1, Team o2)
				{
					return o1.compareTo(o2);
				}			
			});
			for(int i = teamsList.size() -1; i >= 0; i--)
			{
				System.out.println( teamsList.size() -i + " Name: " + teamsList.get(i).name + " Comparison Points: " + teamsList.get(i).comparisonPoints);
				teamsList.get(i).comparisonPoints = 0;
			}	
		}
	}
	
	
	public void rankPlayers()
	{
		Collections.sort(quarterBackList, new Comparator<QuarterBack>() {
			@Override
			public int compare(QuarterBack o1, QuarterBack o2)
			{
				return o2.compareTo(o1);
			}			
		});
		
		Collections.sort(runningBackList, new Comparator<RunningBack>() {
			@Override
			public int compare(RunningBack o1, RunningBack o2)
			{
				return o2.compareTo(o1);
			}			
		});
		
		Collections.sort(wideReceiversList, new Comparator<WideReceiver>() {
			@Override
			public int compare(WideReceiver o1, WideReceiver o2)
			{
				return o2.compareTo(o1);
			}			
		});
		
		Collections.sort(tightEndList, new Comparator<TightEnd>() {
			@Override
			public int compare(TightEnd o1, TightEnd o2)
			{
				return o2.compareTo(o1);
			}			
		});
	}
}
