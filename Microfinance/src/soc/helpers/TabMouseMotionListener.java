/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;

/**
 *
 * @author Wellington
 */

public class TabMouseMotionListener extends MouseMotionAdapter {
	public void mouseMoved(MouseEvent e) {
		JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
		if (findTabPaneIndex(e.getPoint(), tabbedPane) > -1) {
			tabbedPane.setCursor(new Cursor((Cursor.HAND_CURSOR)));
		} else {
			tabbedPane.setCursor(new Cursor((Cursor.DEFAULT_CURSOR)));
		}
	}

	private static int findTabPaneIndex(Point p, JTabbedPane tabbedPane) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getBoundsAt(i).contains(p.x, p.y)) {
				return i;
			}
		}
		return -1;
	}

}
