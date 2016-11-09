package soc.helpers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author Wellington
 */
public class PopupTrigger extends MouseAdapter {

	int cellX, cellY;
	JPopupMenu popup;

	public PopupTrigger(JPopupMenu popup) {
		this.popup = popup;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e) == true) {
			JTable source = (JTable) e.getSource();
			int row = source.rowAtPoint(e.getPoint());
			int column = source.columnAtPoint(e.getPoint());
			if (!source.isRowSelected(row)) {
				source.changeSelection(row, column, false, false);
			}
			getMousePos(row, column);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void getMousePos(int x, int y) {
		cellX = x;
		cellY = y;
	}

}
