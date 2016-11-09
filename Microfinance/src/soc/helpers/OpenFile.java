package soc.helpers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
/**
*
* @author Wellington
*/
public class OpenFile {
	public void open(String path) {
		if (Desktop.isDesktopSupported()) {
			try {
				File myfile = new File(path);
				Desktop.getDesktop().open(myfile);
			} catch (IOException ee) {
				JOptionPane.showMessageDialog(null,
						"Problem opening this file.\nFollow path " + path + " to manually open the file");
			}
		}
	}

}
