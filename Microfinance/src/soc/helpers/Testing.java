package soc.helpers;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Testing extends JPanel {
	JProgressBar bar1;
	int in1;
	Timer t1;

	public Testing() {
		this.setLayout(new BorderLayout());
		this.add(createTestMarkLoader(), BorderLayout.CENTER);

	}

	public JPanel createTestMarkLoader() {
		bar1 = new JProgressBar(0, 30);
		bar1.setValue(0);
		bar1.setStringPainted(true);
		
		bar1.setUI(new ProgressComponent());
		in1 = 0;
		t1 = new Timer(100, (ActionEvent e) -> {
			if (in1 == 30) {
				t1.stop();

			} else {
				in1++;
				bar1.setValue(in1);
			}
		});
		t1.start();
		JPanel prg = new JPanel(new GridLayout(2, 1));
		JLabel loading = new JLabel("Please wait...");
		prg.add(loading);
		prg.add(bar1);
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(prg);
		return panel;
	}

	public static void main(String[] args) {
		Testing t = new Testing();
		JFrame f = new JFrame();
		f.getContentPane().add(t);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
