package soc.deco;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;

/**
 *
 * @author Wellington
 */
public class AnimateDialog {

	public AnimateDialog() {

	}

	public void fadeIn(final JDialog dialog, int delay) {
		new Timer(delay, new ActionListener() {
			int counter = 0;

			public void actionPerformed(ActionEvent e) {
				counter++;
				if (counter == 10) {
					((Timer) e.getSource()).stop();
				}
				dialog.setOpacity(counter * 0.1f);
			}
		}).start();
		dialog.setVisible(true);
	}

	public void fadeOut(final JDialog dialog, int delay) {
		new Timer(delay, new ActionListener() {
			int counter = 10;

			public void actionPerformed(ActionEvent e) {
				counter--;
				if (counter == 0) {
					((Timer) e.getSource()).stop();
					dialog.dispose();
				}
				dialog.setOpacity(counter * 0.1f);
			}
		}).start();
	}
}
