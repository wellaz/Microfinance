/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Wellington
 */
public class CustomTabbedPaneUI extends BasicTabbedPaneUI {

	Rectangle xRect;

	@Override
	protected void installListeners() {
		super.installListeners();
		tabPane.addMouseListener(new MyMouseHandler());
	}

	@Override
	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);

		Font f = g.getFont();
		g.setFont(new Font("Courier", Font.BOLD, 12));
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int charWidth = fm.charWidth('x');
		int maxAscent = fm.getMaxAscent();
		g.drawString("x", textRect.x + textRect.width - 3, textRect.y + textRect.height - 3);
		// g.drawRect(textRect.x + textRect.width - 5,
		// textRect.y + textRect.height - maxAscent, charWidth + 2, maxAscent -
		// 1);
		xRect = new Rectangle(textRect.x + textRect.width - 5, textRect.y + textRect.height - maxAscent, charWidth + 2,
				maxAscent - 1);
		g.setFont(f);
	}

	public class MyMouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			JTabbedPane tabPane = (JTabbedPane) e.getSource();
			if (xRect.contains(e.getPoint())) {
				int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());
				tabPane.remove(tabIndex);
			}
		}

	}
}
