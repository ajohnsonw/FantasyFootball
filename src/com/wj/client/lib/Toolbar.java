package com.wj.client.lib;
 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
 

public class Toolbar extends JPanel implements Runnable
{
	private static final String SEARCH = "Search";

	public Toolbar()
	{
		super(new BorderLayout());

		init();
	}

	public void init()
	{	
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		addButtons(toolBar);

		setPreferredSize(new Dimension(450, 20));
		add(toolBar, BorderLayout.PAGE_START);
	}
	protected void addButtons(JToolBar toolBar)
	{
		JButton button = null;

		button = makeNavigationButton(SEARCH, "Search for a specific player's stats");
		toolBar.add(button);
	}

	//TODO: Maybe implement previous/next buttons. Not sure if valuable though.
	protected JButton makeNavigationButton(final String name, String toolTipText)
	{
		JButton button = new JButton();
		button.setActionCommand(name);
		button.setText(name);;
		button.setToolTipText(toolTipText);
		button.setBorder(BorderFactory.createRaisedBevelBorder());

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (SEARCH.equalsIgnoreCase(name))
				{
					String name = JOptionPane.showInputDialog("Please input a name to search for");
					
					//TODO: Optimize for performance by restructuring loops.
					ArrayList<Container> tabs = findComponent(getTopLevelAncestor());
					for(Container container : tabs)
					{
						for(Component c : container.getComponents())
						{
							if(c != null && c.getName() != null && name != null
								&& c.getName().replace(" ", "").contains(name.replace(" ", "")))
							{
								JTabbedPane pane = (JTabbedPane)c.getParent();
								pane.setSelectedComponent(c);
								break;
							}
						}
					}
				} 
				else {
					//TODO: Implement some new functionality later.
				}
			}
			public ArrayList<Container> findComponent(Container ancestor)
			{
				ArrayList<Container> containers = new ArrayList<Container>();
				Component[] components = ancestor.getComponents();
				
				
				for(Component com : components)
				{
					if( com instanceof JTabbedPane )
						containers.add((JTabbedPane)com);
					else if(com instanceof Container)
						containers.addAll(findComponent((Container)com));
				}
				return containers;
			}
		});

		return button;
	}

	public static void main(String[] args)
	{
		try
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeLater(new Toolbar());
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}

	public void run()
	{
		JFrame frame = new JFrame();
		frame.add(new Toolbar());

		frame.pack();
		frame.setVisible(true);
	}
}