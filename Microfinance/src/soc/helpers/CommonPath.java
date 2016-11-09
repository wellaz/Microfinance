package soc.helpers;

import java.io.File;

/**
 *
 * @author Wellington
 */
public class CommonPath {

	public static String path() {
		return System.getProperty("user.home") + File.separatorChar;
	}
}
