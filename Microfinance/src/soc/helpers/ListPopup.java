/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import soc.supervisor.Cmd;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class ListPopup extends JPopupMenu implements ActionListener {

    public JMenuItem open, deletep, remove, print, properties;
    public JMenuItem edittest, compilemarks;
    public JMenu sharewith, sendto;
    public Cmd t;

    public ListPopup(Cmd temp) {
        t = temp;
        init();
    }

    public final void init() {
        open = new JMenuItem("Open");
        open.addActionListener(this);
        deletep = new JMenuItem("Delete Permanantly");
        deletep.addActionListener(this);
        remove = new JMenuItem("Remove from List");
        remove.addActionListener(this);
        print = new JMenuItem("Print...");
        print.addActionListener(this);
        properties = new JMenuItem("Poperties");
        properties.addActionListener(this);
        edittest = new JMenuItem("Edit");
        edittest.addActionListener(this);
        compilemarks = new JMenuItem("Compile Marks");
        compilemarks.addActionListener(this);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) {
            EventQueue.invokeLater(() -> {
                
            });
        }
        if (e.getSource() == edittest) {

        }
        if (e.getSource() == deletep) {

        }
        if (e.getSource() == remove) {

        }
        if (e.getSource() == print) {

        }
        if (e.getSource() == compilemarks) {
            EventQueue.invokeLater(() -> {
                
            });

        }
        if (e.getSource() == properties) {
            EventQueue.invokeLater(() -> {
                

            });

        }
    }

}
