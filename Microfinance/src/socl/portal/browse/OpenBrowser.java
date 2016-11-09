/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socl.portal.browse;

import javax.swing.JOptionPane;

/**
 *
 * @author Wellington
 */

public class OpenBrowser {
	public static void browse() {
		String url = "http://www.google.co.zw/";
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();
		try {
			if (os.indexOf("win") >= 0) {
				String[] cmd = new String[4];
				cmd[0] = "cmd.exe";
				cmd[1] = "/C";
				cmd[2] = "start";
				cmd[3] = url;
				rt.exec(cmd);
			} else if (os.indexOf("mac") >= 0) {
				rt.exec("open " + url);
			} else {
				// prioritized 'guess' of users' preference
				String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links",
						"lynx" };
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				rt.exec(new String[] { "sh", "-c", cmd.toString() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"An error has occured while trying to open a browser." + "\nReason :"
							+ " This computer does not have an Internet browser",
					"Browser Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
