package soc.bank;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import com.itextpdf.text.Font;

import soc.deco.AnimateDialog;
import soc.helpers.IconImage;

/**
 *
 * @author Wellington
 */
public class Chooser {
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public Chooser(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public void showDialog() {
		JDialog dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});
		JLabel top = new JLabel("<html><h2>Specify Transaction Type<i>(by selecting from below)</i><h2></html>");
		top.setForeground(Color.RED);

		JRadioButton dep = new JRadioButton("<html><h3>Cash Deposit Into Cashier<i>(INTO RS Account)</i></h3></html>");
		dep.setForeground(Color.WHITE);
		JRadioButton with = new JRadioButton(
				"<html><h3>Cash Withdrawal From Cashier<i>(FROM RS Account)</i></h3></html>");
		with.setForeground(Color.WHITE);
		ButtonGroup gr = new ButtonGroup();
		gr.add(dep);
		gr.add(with);

		JButton go = new JButton("Proceed");

		JLabel error = new JLabel();
		error.setForeground(Color.red);
		error.setFont(new java.awt.Font("", Font.ITALIC, 15));

		JPanel midpanel = new JPanel();
		midpanel.setOpaque(false);
		midpanel.setLayout(new GridLayout(9, 1));
		midpanel.add(top);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(dep);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(with);
		midpanel.add(Box.createVerticalStrut(20));

		Box buttonlBox = Box.createHorizontalBox();
		JButton cancel = new JButton("Cancel");
		buttonlBox.add(Box.createHorizontalGlue());
		buttonlBox.add(go);
		buttonlBox.add(Box.createHorizontalStrut(30));
		buttonlBox.add(cancel);
		cancel.addActionListener(event -> new AnimateDialog().fadeOut(dialog, 100));

		midpanel.add(buttonlBox);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(error);

		go.addActionListener((event) -> {
			error.setText("");
			if (dep.isSelected()) {
				Deposit deposit = new Deposit(tabs, rs, stm, frame);
				EventQueue.invokeLater(() -> {
					new AnimateDialog().fadeOut(dialog, 100);
					deposit.insertTab();
				});

			} else if (with.isSelected()) {
				Withdrawal withdraw = new Withdrawal(tabs, rs, stm, frame);
				EventQueue.invokeLater(() -> {
					new AnimateDialog().fadeOut(dialog, 100);
					withdraw.insertTab();
				});
			} else {
				error.setText("Null selection cannot be processed!");
			}
		});

		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		dialog.getRootPane().setDefaultButton(go);
		dialog.getContentPane().setBackground(Color.BLUE);

		dialog.setSize(450, 300);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		new AnimateDialog().fadeIn(dialog, 100);
		dialog.setAlwaysOnTop(true);
	}
}
