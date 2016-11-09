package org.license;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class KeyDialog extends JDialog {

	JPanel mainp, upperp, centralp;
	public JButton log, can, pro;
	public JTextField key;
	JLabel left, lbl1, right;

	public KeyDialog() {
		super.setTitle("License Renewal Window");
		begin();

	}

	public final void begin() {
		this.setUndecorated(true);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
			}
		});
		log = new JButton("Submit");
		log.setToolTipText("Hit to validate the license");
		can = new JButton("Cancel");
		can.setToolTipText("Close and quit");
		pro = new JButton("Proceed");
		pro.setToolTipText("Proceed without validating");
		left = new JLabel("           ");
		right = new JLabel("           ");

		lbl1 = new JLabel(
				"<html><p>Cashier has been registered with a serial number<br>that has expired. Thus we advise you to purchase a<br>new product key!</p></html>",
				SwingConstants.CENTER);
		lbl1.setFont(new Font("", Font.BOLD, 13));
		lbl1.setForeground(Color.WHITE);

		Color color = new Color(150, 75, 190);
		mainp = new TranslucentJPanel(color);
		mainp.setLayout(new BorderLayout(10, 10));
		centralp = new JPanel(new GridLayout(3, 3, 8, 8));
		centralp.setOpaque(false);
		upperp = new JPanel(new GridLayout(2, 1, 5, 5));
		upperp.setOpaque(false);
		centralp.add(new JLabel(" Key :"));

		key = new JTextField("", 15);
		key.setToolTipText("Input Key Here");
		key.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		centralp.add(key);
		key.setFocusable(true);

		centralp.add(log);
		centralp.add(can);
		centralp.add(pro);
		upperp.add(lbl1);
		upperp.add(new JLabel(""));
		mainp.add(upperp, BorderLayout.NORTH);
		mainp.add(centralp, BorderLayout.CENTER);
		mainp.add(left, BorderLayout.EAST);
		mainp.add(right, BorderLayout.WEST);

		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);

		bar.add(new JMenu("License Key Required"));
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton close = new JButton(new ImageIcon(new IconImage().createCloseImage()));
		close.addActionListener((event) -> {
			System.exit(0);
		});
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(close);
		bar.add(toolbar);

		setContentPane(mainp);
		setSize(500, 300);
		setFocusable(true);
		setAlwaysOnTop(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
			}
		});

	}
}
