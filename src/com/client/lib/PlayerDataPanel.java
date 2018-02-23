package com.client.lib;

import javax.swing.JPanel;

public abstract class PlayerDataPanel extends JPanel
{	
	/*   TODO:
	 *   1. Implement a PanelXXStats for each player type.
	 *   2. Implement a line graph on dk points vs cost (per week)
	 */
	public abstract Object[][] getRowData();
}
