package soc.deco;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class BlinkingButton extends JButton {
	Color c;

	public BlinkingButton(Color c) {
		this.c = c;
		blinkButton(c);
	}

	void blinkButton(Color c) {
		Timer blinkTimer = new Timer(500, new ActionListener() {
			private int counter = 0;
			private int maxCount = 8;
			private boolean on = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (counter >= maxCount) {
					BlinkingButton.this.setBackground(null);
					BlinkingButton.this.setForeground(Color.BLACK);
					((Timer) e.getSource()).stop();
				} else {
					BlinkingButton.this.setBackground(on ? c : null);
					BlinkingButton.this.setForeground(Color.WHITE);
					on = !on;
					counter++;
				}
			}
		});
		blinkTimer.start();
	}
}
