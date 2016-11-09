package soc.helpers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import soc.deco.AnimateDialog;

/**
 *
 * @author Wellington
 */
public class DeleteFile {
	ResultSet rs;
	Statement stm;

	public DeleteFile(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	// cleanup method that caters for cleaning up all files and folders
	public void cleanUp() {
		try {
			ArrayList<Integer> filenames = new GetAccountNumbers(rs, stm).getAccounts();
			for (int id : filenames) {
				File f = new File(System.getProperty("user.home") + File.separatorChar + id + ".pdf");
				if (f.exists()) {
					if (f.delete()) {
						System.out.println(f.getName() + " is deleted");
					} else {
						System.out.println(f.getName() + " is not deleted");
					}
				} else {
					System.out.println(f.getName() + " not found");
				}
			}
			System.out.println("=======================DONE=============================");
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// this is a class for the cleanup, removing all unneccessary files that do
	// not need to be there and all unneccessary folders that do not need to
	// present in the execution.
	public class CleanUpWorker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;
		JTable jtable1, jtable2, jtable3, jtable4;
		String name;
		String acc;

		public CleanUpWorker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				dialog.setVisible(false);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.1f, 1f));
			dialog.getContentPane().add(box, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			cleanUp();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.dispose();
			System.exit(0);
		}
	}
}
