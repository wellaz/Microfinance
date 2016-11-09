package soc.deco;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class BlinkingLabel extends JLabel {
	static int counter = 0;
	static ScheduledExecutorService service;
	String message;

	public BlinkingLabel(String message) {
		this.message = message;
		displayHelpMessage(message);
	}

	void displayHelpMessage(String message) {
		Runnable r = () -> {
			if (counter++ >= 6) {
				// service.shutdown();
				counter = 0;
			} else {
				if (counter % 2 == 1) {
					this.setForeground(Color.RED);
				} else {
					this.setForeground(Color.BLACK);
				}
			}
		};
		this.setText(message);
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(r, 0, 750, TimeUnit.MILLISECONDS);
	}
}
