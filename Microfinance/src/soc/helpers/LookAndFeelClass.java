/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Wellington
 */
public class LookAndFeelClass {

	public void setLookAndFeels() {
		try {
			
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.getLookAndFeelDefaults().put("nimbusOrange", (Color.BLUE));
					UIManager.put("TabbedPane.contentOpaque", false);
					/*
					 * UIManager.getLookAndFeelDefaults().put(
					 * "PopupMenu[Enabled].backgroundPainter", new
					 * FillPainter(Color.BLACK));
					 */
				}
			}
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException exc) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException ex) {
				JOptionPane.showMessageDialog(null, "Screen rendering error!\nCode 98", "General Error",
						JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	class FillPainter implements Painter<JComponent> {

		private final Color color;

		FillPainter(Color c) {
			color = c;
		}

		@Override
		public void paint(Graphics2D g, JComponent object, int width, int height) {
			g.setColor(color);
			g.fillRect(0, 0, width - 1, height - 1);

		}
	}
}
