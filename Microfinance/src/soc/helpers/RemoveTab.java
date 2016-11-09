package soc.helpers;

import java.awt.EventQueue;

import javax.swing.JTabbedPane;

/**
 *
 * @author Wellington
 */
public class RemoveTab {
	JTabbedPane tabs;

	public RemoveTab(JTabbedPane tabs) {
		this.tabs = tabs;
	}

	public void removeTab(String tabTitle) {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals(tabTitle)) {
					tabs.removeTabAt(a);
				}
			}
		});
	}
}
