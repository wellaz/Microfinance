package soc.helpers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;

/**
*
* @author Wellington
*/
public class DefaultDialogClose {
	
	public DefaultDialogClose(JDialog dialog) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(new Runnable() {
			public void run() {
				// should be invoked on the Event Dispatch Thread
				dialog.dispose();
			}
		}, 5, TimeUnit.SECONDS);
	}
}
