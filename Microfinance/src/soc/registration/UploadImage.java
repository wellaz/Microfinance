
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.registration;

import java.awt.Dimension;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import soc.helpers.MyNativeFileView;

/**
 *
 * @author Wellington
 */

public class UploadImage {
	String filename;
	Image image;
	JFrame component;
	JTextField field;
	byte[] scan_image = null;

	public UploadImage(JFrame component, JTextField field) {
		this.component = component;
		this.field = field;
	}

	public Image getImage() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setDragEnabled(true);
		chooser.setFileView(new MyNativeFileView());
		chooser.setPreferredSize(new Dimension(500, 350));
		chooser.showDialog(null, "Select Image File");
		File file = chooser.getSelectedFile();
		filename = file.getAbsolutePath();
		try {
			image = ImageIO.read(file);
			if (image == null)
				JOptionPane.showMessageDialog(component, "The file detected is not an image", "Error",
						JOptionPane.ERROR_MESSAGE);
			else
				field.setText(filename);
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(component, "An Error occured.\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	@SuppressWarnings("resource")
	public byte[] imageConverter() {
		try {
			File imagefile = new File(filename);
			FileInputStream fis = new FileInputStream(imagefile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			for (int readNum; (readNum = fis.read(buff)) != -1;) {
				bos.write(buff, 0, readNum);
			}
			scan_image = bos.toByteArray();

		} catch (Exception ee) {
			JOptionPane.showMessageDialog(component, "The file might have been corrupted\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return scan_image;
	}
}
