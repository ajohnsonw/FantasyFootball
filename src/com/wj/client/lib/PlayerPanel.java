package com.wj.client.lib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;


/**
 * The intent of this class is to abstract out common functionality
 * between players related on their stats screen
 */
public class PlayerPanel extends JFrame implements Runnable
{
	protected JPanel gridPanel 				= new JPanel();	
	protected JTabbedPane playerTab 		= new JTabbedPane();
	protected JTable grid 					= new JTable(new DefaultTableModel());
	protected Object[] columnNames 			= null;
	
	public PlayerPanel()
	{
		init();
	}
	
	public static void main(String[] args)
	{
		try
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeLater(new PlayerPanel());	
		}
		catch(Throwable thr) {thr.printStackTrace();}
	}
	
	public void init()
	{
		setSize(new Dimension(800, 500));
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		add(new Toolbar(), BorderLayout.NORTH);
		setupPlayerTab();
		setupGrid();
	}
	
	public void setupGrid()
	{
		gridPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		gridPanel.setPreferredSize(new Dimension(100, 150));
		gridPanel.setLayout(new BorderLayout());
		add(gridPanel, BorderLayout.SOUTH);
				
		grid.setAutoResizeMode(0);
		JScrollPane scrollPane = new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gridPanel.add(scrollPane, BorderLayout.CENTER);
	}
	public void setupPlayerTab()
	{
		playerTab.setTabPlacement(JTabbedPane.LEFT);
		playerTab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		playerTab.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e)
			{
				if(e.getSource() instanceof JTabbedPane)
				{
					JTabbedPane pane = (JTabbedPane) e.getSource();
					PlayerDataPanel panel = (PlayerDataPanel)playerTab.getComponent(pane.getSelectedIndex());
					
					DefaultTableModel gridModel = (DefaultTableModel)grid.getModel();
					for(int i = 0; i < gridModel.getRowCount(); i++)
						gridModel.removeRow(i);
					
					gridModel.setDataVector(panel.getRowData(), columnNames);
					for(int i = 0; i < grid.getModel().getColumnCount(); i++)
					{
						if(i == 3 || i == 4 || i == 6 || i == grid.getModel().getColumnCount() -1)
							grid.getColumnModel().getColumn(i).setMinWidth(96);
					}
				}
			}

			public void focusLost(FocusEvent e) {}			
		});
		playerTab.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e)
			{
				if(e.getSource() instanceof JTabbedPane)
				{
					JTabbedPane pane = (JTabbedPane) e.getSource();
					Component component = playerTab.getComponent(pane.getSelectedIndex());
					if(component instanceof PlayerDataPanel)
					{
						PlayerDataPanel panel = (PlayerDataPanel)component;
						
						DefaultTableModel gridModel = (DefaultTableModel)grid.getModel();
						for(int i = 0; i < gridModel.getRowCount(); i++)
							gridModel.removeRow(i);
						
						gridModel.setDataVector(panel.getRowData(), columnNames);
						for(int i = 0; i < grid.getModel().getColumnCount(); i++)
						{
							if(i == 3 || i == 4 || i == 6 || i == grid.getModel().getColumnCount() -1)
								grid.getColumnModel().getColumn(i).setMinWidth(96);
						}						
					}
				}				
			}			
		});
		
		playerTab.addMouseWheelListener(new MouseWheelListener() {

		    public void mouseWheelMoved(MouseWheelEvent e) {
		        JTabbedPane pane = (JTabbedPane) e.getSource();
		        int units = e.getWheelRotation();
		        int oldIndex = pane.getSelectedIndex();
		        int newIndex = oldIndex + units;
		        if (newIndex < 0)
		            pane.setSelectedIndex(0);
		        else if (newIndex >= pane.getTabCount())
		            pane.setSelectedIndex(pane.getTabCount() - 1);
		        else
		            pane.setSelectedIndex(newIndex);
		    }
		});
		add(playerTab, BorderLayout.CENTER);
	}
	
	public void run()
	{
		setVisible(true);
	}
}
