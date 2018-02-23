package com.client;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.client.lib.SplashScreen;
import com.server.PlayerData;

public class Menu extends JFrame
{
	protected JMenuBar menuBar 		  = new JMenuBar();
	protected JMenu statsMenu  		  = new JMenu("Player Stats");
	protected JMenu suggestionsMenu	  = new JMenu("Suggestions");
	protected JMenuItem favoritesItem = new JMenuItem("Picks of the week");
	protected PlayerData playerData   = new PlayerData();
	
	/*   TODO:
	 *   1. Think of an "official" splash screen image.
	 *   2. Think of an "official" application name and setTitle appropriately.
	 *   3. Implement statsMenu for RB, WR, TE, DST, etc.
	 *   4. Implement "Suggestions Menu"
	 *   	a. Currently the only thought I had was 'Picks of the week' which represented all 9 players.
	 *   		However, perhaps I can do QBPicks, RBPicks, etc. I don't know the value of this yet as
	 *   		PanelQBStats (etc) is intended to be that guide.
	 *   5. Implement a better "Look and Feel"; specifically a custom skin.
	 */
	public Menu()
	{
		init();
	}
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Menu menu = new Menu();
			menu.displayScreen();
		} 
		catch (Throwable thr) { thr.printStackTrace(); }
	}

	public void init()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(300, 100));
		pack();
		setLocationRelativeTo(null);
		setTitle("DraftKings Add-On");
		
		statsMenu.add(getQBItem());
//		statsMenu.add(getRBItem());
//		statsMenu.add(getWRItem());
//		statsMenu.add(getTEItem());
//		statsMenu.add(getDSTItem());
		
		favoritesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new PanelWeeklyPick());	
			}
		});
		suggestionsMenu.add(favoritesItem);
		
		// add menus to menubar
		menuBar.add(statsMenu);
		menuBar.add(suggestionsMenu);
		
		// put the menubar on the frame
		setJMenuBar(menuBar);
	}
	
	public void displayScreen()
	{
		SplashScreen splash = new SplashScreen();
        try 
        {
            splash.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingUtilities.invokeLater(splash);
            playerData.loadData();
            splash.dispose();
            setVisible(true);
        } 
        finally { splash.setCursor(Cursor.getDefaultCursor()); }
	}
	
	public JMenuItem getQBItem()
	{
		JMenuItem QBItem = new JMenuItem("Quarterbacks");
		QBItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new PanelQBStats(playerData.quarterBackList));			
			}			
		});
		
		return QBItem;
	}
}