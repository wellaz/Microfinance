/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soc.deco;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class CentralPanel extends JPanel {
	private final int strokeThickness = 3;
	private final int radius = 10;
	private final int arrowSize = 12;
	private final int padding = strokeThickness / 2;

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0.5f, 0.5f, 1f));
		int bottomLineY = getHeight() - strokeThickness;
		int width = getWidth() - arrowSize - (strokeThickness * 2);
		g2d.fillRect(padding, padding, width, bottomLineY);
		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(padding, padding, width, bottomLineY, radius,
				radius);
		Area area = new Area(rect);
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setStroke(new BasicStroke(strokeThickness));
		g2d.draw(area);
	}
}
