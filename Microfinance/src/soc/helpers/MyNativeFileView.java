package soc.helpers;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

/**
 *
 * @author Wellington
 */
public class MyNativeFileView extends FileView {
	public Icon getIcon(File f) {
		FileSystemView view = FileSystemView.getFileSystemView();
		return view.getSystemIcon(f);

	}
}
