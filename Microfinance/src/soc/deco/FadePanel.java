package soc.deco;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class FadePanel extends JPanel {
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private final int PREF_W = screen.width;
	private final int PREF_H = screen.height;
	private static final int COMP_SIZE = 200;
	private static final int RULE = AlphaComposite.SRC_OVER;
	private static final int TIMER_DELAY = 40;
	private Composite[] comps = new Composite[COMP_SIZE];
	private int compsIndex = 0;
	private Composite comp;

	public FadePanel(final JFrame frame) {
		setOpaque(false);
		for (int i = 0; i < comps.length; i++) {
			float alpha = (float) Math.cos(2 * Math.PI * i / COMP_SIZE);
			alpha += 1;
			alpha /= 2.0;
			comps[i] = AlphaComposite.getInstance(RULE, alpha);
		}
		comp = comps[compsIndex];
		setBackground(Color.LIGHT_GRAY);
		new Timer(TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				compsIndex++;
				compsIndex %= COMP_SIZE;
				comp = comps[compsIndex];
				frame.repaint();
			}
		}).start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(comp);
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g2);
		g2.dispose();
	}

	/*
	 * private static void createAndShowGui() { JFrame frame = new
	 * JFrame("FadeInAndOut"); JLayeredPane layeredPane = new JLayeredPane();
	 * 
	 * FadePanel fadePanel = new FadePanel(frame); fadePanel.setLocation(0, 0);
	 * fadePanel.setSize(fadePanel.getPreferredSize());
	 * 
	 * JPanel bluePanel = new JPanel(new BorderLayout());
	 * bluePanel.setBackground(Color.blue);
	 * bluePanel.setSize(fadePanel.getPreferredSize()); JLabel label = new
	 * JLabel("Fubars Rule!", SwingConstants.CENTER);
	 * label.setFont(label.getFont().deriveFont(Font.BOLD, 50f));
	 * bluePanel.add(label);
	 * 
	 * layeredPane.setPreferredSize(fadePanel.getPreferredSize());
	 * layeredPane.add(bluePanel, JLayeredPane.DEFAULT_LAYER);
	 * layeredPane.add(fadePanel, JLayeredPane.PALETTE_LAYER);
	 * 
	 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * frame.getContentPane().add(layeredPane); frame.pack();
	 * frame.setLocationByPlatform(true); frame.setVisible(true); }
	 * 
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() { public void run() { createAndShowGui(); } }); }
	 */
}
