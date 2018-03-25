package com.wj.client.lib;

import javax.swing.JPanel;

import com.wj.common.Player;

public abstract class PlayerDataPanel extends JPanel
{	
	/*   TODO:
	 *   1. Implement a PanelXXStats for each player type.
	 *   2. Implement a line graph on dk points vs cost (per week)
	 */
	public abstract Object[][] getRowData();
	public abstract String[] getColumnNames();
	public abstract Player getPlayer();
	
}
