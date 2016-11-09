/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class TreePopup extends JPopupMenu {

	public JMenuItem open, deletep, remove, print, properties;
	public JMenuItem edittest, compilemarks;
	public JMenu sharewith, sendto;

	public TreePopup() {
		
		init();

	}

	public final void init() {
		open = new JMenuItem("Open");
		deletep = new JMenuItem("Delete Permanantly");
		remove = new JMenuItem("Remove from List");
		print = new JMenuItem("Print...");
		properties = new JMenuItem("Poperties");
		edittest = new JMenuItem("Edit");
		compilemarks = new JMenuItem("Compile Marks");

		sharewith = new JMenu("Share with...");
		sendto = new JMenu("Send to...");
		this.add(open);
		this.addSeparator();
		this.add(edittest);
		this.add(compilemarks);
		this.addSeparator();
		this.add(sharewith);
		this.add(sendto);
		this.addSeparator();
		this.add(deletep);
		this.add(remove);
		this.addSeparator();
		this.add(print);
		this.add(properties);

	}

	

}
