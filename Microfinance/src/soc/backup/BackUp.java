package soc.backup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import soc.deco.AnimateDialog;
import soc.deco.TranslucentJPanel;
import soc.helpers.MyNativeFileView;
import soc.helpers.OpenFile;

/**
 *
 * @author Wellington
 */
public class BackUp {
	String Database = "esociety";
	String backUpPath = "";
	JFrame frame;

	public BackUp(JFrame frame) {
		this.frame = frame;
	}

	public String getBackUpPath() {

		JFileChooser fc = null;
		if (fc == null) {
			fc = new JFileChooser();
			fc.setDialogTitle("Choose Folder To Save The BackUp File");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileView(new MyNativeFileView());
			fc.setPreferredSize(new Dimension(500, 350));
		}
		int returnVal = fc.showDialog(frame, "Save");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			backUpPath = file.getAbsolutePath();
		}
		return backUpPath;
	}

	public void extract() {
		String backuppath = getBackUpPath();

		String Password = "";
		String user = "root";
		BackUp1 b = new BackUp1();
		try {
			byte[] data = b.getData("localhost", "3307", user, Password, Database).getBytes();
			File filedst = new File(backuppath + "\\" + "cashier" + ".zip");
			FileOutputStream dest = new FileOutputStream(filedst);
			ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(dest));
			zip.setMethod(ZipOutputStream.DEFLATED);
			zip.setLevel(Deflater.BEST_COMPRESSION);
			zip.putNextEntry(new ZipEntry("esociety.sql"));
			zip.write(data);
			zip.close();
			dest.close();

			JOptionPane.showMessageDialog(frame,
					"Back Up Successfully.\nData saved from inception to date\nGo to " + backUpPath
							+ " and find cashier.zip.\nYou can burn that zipped file on a DVD or any external memory chip.\nDONE! ",
					"Database BackUp Wizard", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile().open(backUpPath);
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Back Up Failed.\nThe backup process might have been interrupted",
					"Database BackUp Wizard", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		public Worker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				new AnimateDialog().fadeOut(dialog, 100);
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
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			JPanel pann = new TranslucentJPanel(Color.BLUE);
			pann.setLayout(new BorderLayout());
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			extract();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}
}
