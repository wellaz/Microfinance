/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;

/**
 *
 * @author Wellington
 */

public class TreeMouseListener extends MouseAdapter {
	public String path;
	JTree tree;
	TreePopup popup;
	JFrame frame;

	public TreeMouseListener(JTree tree, JFrame frame) {
		this.tree = tree;
		this.frame = frame;
		popup = new TreePopup();
		this.tree.setComponentPopupMenu(popup);
		popup.open.addActionListener((ActionEvent event) -> {
			EventQueue.invokeLater(() -> {
				openFile();
			});
		});
		createKeybindings(this.tree);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2) {
			EventQueue.invokeLater(() -> {
				path = tree.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "\\");
				openFile();
			});
		}
	}

	public void openFile() {
		try {
			path = tree.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "\\");
			File file = new File(path);
			if (file.exists()) {
				if (Desktop.isDesktopSupported()) {
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					Desktop.getDesktop().open(file);
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			} else {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(null, "The file is hidden or does not exists", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException ee) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(null, "Error\n" + ee.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	@SuppressWarnings("serial")
	public void createKeybindings(JTree tree) {
		tree.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		tree.getActionMap().put("Enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				EventQueue.invokeLater(() -> {
					path = tree.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "\\");
					openFile();
				});
			}
		});
	}
}
