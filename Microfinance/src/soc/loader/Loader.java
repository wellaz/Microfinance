/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.loader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import soc.deco.AnimateDialog;
import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;
import soc.helpers.LookAndFeelClass;
import soc.supervisor.Cmd;

/**
 *
 * @author Wellington
 */
public class Loader extends SwingWorker<Void, Void> {
	JPanel toppanel, progresspanel, rpanel;
	JProgressBar bar;
	Timer t;
	int i;
	LookAndFeelClass looks;
	IconImage icon;
	static int interval = 500;
	JDialog dialog;

	public Loader() {
		init();
	}

	public final void init() {
		looks = new LookAndFeelClass();
		looks.setLookAndFeels();
		icon = new IconImage();
		toppanel = createTopPanel();
		progresspanel = reateProgressPanel();
		rpanel = createRPanel();

		JPanel mainp = new TranslucentJPanel(Color.BLUE);
		mainp.setLayout(new BorderLayout());
		mainp.add(toppanel, BorderLayout.CENTER);

		mainp.add(progresspanel, BorderLayout.SOUTH);
		// mainp.add(rpanel, BorderLayout.SOUTH);

		dialog = new JDialog();
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});
		dialog.setContentPane(mainp);
		// dialog.pack();
		dialog.setSize(400, 300);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		//dialog.setVisible(true);
	}

	private JPanel createTopPanel() {
		JPanel top = new JPanel(new GridBagLayout());
		String sysuser = System.getProperty("user.name");
		JLabel eq4 = new JLabel("C A S H I E R ");
		eq4.setFont(new Font("", Font.BOLD, 35));
		eq4.setForeground(Color.WHITE);
		JLabel on = new JLabel("on");
		JLabel pc = new JLabel(sysuser + "-PC");
		Box b = Box.createVerticalBox();
		b.add(eq4);
		b.add(on);
		b.add(pc);

		top.setOpaque(false);

		JPanel imgpanel = new JPanel(new FlowLayout());
		imgpanel.add(new JLabel(new ImageIcon(new IconImage().createSigmaImage())), SwingConstants.CENTER);
		imgpanel.setOpaque(false);
		top.add(imgpanel);
		top.add(b);
		return top;
	}

	private JPanel reateProgressPanel() {
		bar = new JProgressBar(0, 30);
		JPanel prg = new JPanel(new GridLayout(2, 1));
		prg.setOpaque(false);
		JLabel loading = new JLabel("Loading modules...");
		prg.add(loading);
		prg.add(bar);
		return prg;
	}

	private JPanel createRPanel() {
		JPanel rights = new JPanel(new BorderLayout());
		rights.setOpaque(false);
		JLabel lbl = new JLabel("The Bearer of this Software is a Trusted Client of DBS Inc");
		lbl.setFont(new Font("", Font.ITALIC, 14));
		rights.add(lbl, BorderLayout.CENTER);
		return rights;
	}

	@Override
	protected Void doInBackground() throws Exception {
		bar.setIndeterminate(true);
		new AnimateDialog().fadeIn(dialog, 100);
		load();
		return null;
	}

	@Override
	public void done() {
		bar.setIndeterminate(false);
		new AnimateDialog().fadeOut(dialog, 100);
	}

	public void load() {
		new Cmd().setVisible(true);
	}
}
