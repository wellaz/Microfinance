package soc.deco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Wellington
 */
public class BGDialog {
	private JDialog dialog;
	private JProgressBar prog;
	private JLabel waitlbl;
	private JButton hider;
	private JFrame frame;

	public BGDialog() {

		dialog = new JDialog(getFrame(), "");
		dialog.getContentPane().setLayout(new BorderLayout());
		prog = new JProgressBar();
		dialog.setUndecorated(true);
		hider = new JButton("Run in Background");
		hider.addActionListener((ActionEvent event) -> {
			dialog.setVisible(false);
		});
		waitlbl = new JLabel("Downloading....");
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
		JPanel deco = new TranslucentJPanel(Color.BLUE);
		deco.setLayout(new BorderLayout());
		deco.add(box, BorderLayout.CENTER);
		dialog.getContentPane().add(deco, BorderLayout.CENTER);
		dialog.setSize(300, 100);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
		dialog.setLocation(a, b);
		frame = new JFrame();
	}

	public void animate() {
		prog.setIndeterminate(true);
		dialog.setVisible(true);
	}

	public void closeAnimation() {
		prog.setIndeterminate(false);
		dialog.setVisible(false);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame parentFrame) {
		frame = parentFrame;
	}
}
