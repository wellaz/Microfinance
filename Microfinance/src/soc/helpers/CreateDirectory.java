package soc.helpers;

import java.io.File;

/**
 *
 * @author Wellington
 */
public class CreateDirectory {

	public void createDir(String dir) {
		String directory = CommonPath.path() + dir;
		try {
			File f = new File(directory);
			if (!f.exists()) {
				f.mkdir();
				System.out.println("Directory created");
			} else {
				System.out.println("Folder Exists");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
