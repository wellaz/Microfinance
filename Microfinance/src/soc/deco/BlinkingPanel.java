package soc.deco;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class BlinkingPanel extends JPanel {
	private int count;
	private Timer animator;
	String text;

	public BlinkingPanel(String text) {
		this.text = text;
		setBackground(Color.BLACK);
	}

	public void addNotify() {
		super.addNotify();
		animator = new Timer(300, animatorTask);
		animator.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		count++;
		if (count % 2 == 0) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}
		Font f = g.getFont();
		g.setFont(new Font("Courier", Font.BOLD, 30));
		g.drawString(text, (getWidth() / 2) - 30, getHeight() / 2);
		g.setFont(f);
	}

	private ActionListener animatorTask = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	};
}
