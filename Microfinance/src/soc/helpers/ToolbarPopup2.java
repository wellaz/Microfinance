/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import soc.supervisor.Cmd;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class ToolbarPopup2 extends JPopupMenu implements ActionListener {

	JCheckBoxMenuItem showmenu, showtoolbar, showstartpanel, smallicons;
	JMenuItem resettoolbar, customise, preferences;

	Cmd cmd;

	public ToolbarPopup2(Cmd temp) {
		cmd = temp;
		init();
	}

	public final void init() {
		showmenu = new JCheckBoxMenuItem("Hide Menu");
		showtoolbar = new JCheckBoxMenuItem("Hide Tool Bar");
		showstartpanel = new JCheckBoxMenuItem("Hide Start Panel");
		smallicons = new JCheckBoxMenuItem("Small Tool Bar Icons ");
		showmenu.addActionListener(this);
		showtoolbar.addActionListener(this);
		showstartpanel.addActionListener(this);
		smallicons.addActionListener(this);

		preferences = new JMenuItem("Preferences");
		preferences.setEnabled(false);
		customise = new JMenuItem("Customise...");
		customise.setEnabled(false);
		resettoolbar = new JMenuItem("Reset Tool Bar");
		resettoolbar.setEnabled(false);
		preferences.addActionListener(this);
		customise.addActionListener(this);
		resettoolbar.addActionListener(this);
		this.add(showmenu);
		this.add(showtoolbar);
		this.addSeparator();
		this.add(smallicons);
		this.add(showstartpanel);
		this.addSeparator();
		this.add(resettoolbar);
		this.add(customise);
		this.addSeparator();
		this.add(preferences);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showtoolbar) {
			if (showtoolbar.getState()) {
				removeAllTools();
			} else {
				repaintTools();
			}
		}

		if (e.getSource() == showmenu) {
			if (showmenu.getState()) {
				EventQueue.invokeLater(() -> {
					cmd.setJMenuBar(null);
					cmd.revalidate();
					cmd.repaint();
				});
			} else {
				EventQueue.invokeLater(() -> {
					cmd.setJMenuBar(cmd.mnb);
					cmd.revalidate();
					cmd.repaint();
				});
			}
		}
		if (e.getSource() == resettoolbar) {
		}
	}

	private void removeAllTools() {
		EventQueue.invokeLater(() -> {
			cmd.toolbar.removeAll();
			cmd.toolbar.add(new JButton(new ImageIcon(new IconImage().createToggleImage())));
			cmd.toolbar.revalidate();
			cmd.toolbar.repaint();
		});
	}

	private void repaintTools() {
		EventQueue.invokeLater(() -> {
			cmd.toolbar.removeAll();
			cmd.toolbar.add(cmd.createToolbar());
			cmd.toolbar.add(Box.createHorizontalGlue());
			revalidate();
			repaint();
		});
	}
}
