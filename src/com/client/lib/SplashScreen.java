package com.client.lib;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SplashScreen extends JWindow implements Runnable
{
	/*   TODO:
	 *   1. Change /images/splash.png text from 'Fantasy Football' to whatever the official name is
	 */
	Image img = new ImageIcon(this.getClass().getResource("/images/splash.png")).getImage();

	public SplashScreen()
	{

	}

	@Override
	public void run()
	{
		setSize(880, 430);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(img, 0, 0, this);
	}

	public static void main(String[] args)
	{
		try
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeLater(new SplashScreen());		
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}
}
