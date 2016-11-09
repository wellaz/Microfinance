/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.commission.swip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import soc.deco.AnimateDialog;
import soc.deco.TranslucentJPanel;
import soc.helpers.PauseThread;
import soc.helpers.SetDateCreated;
import soc.months.MonthsList;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Swip extends JPanel implements ActionListener {

	JComboBox<Object> months;
	JButton start, cancel;
	JLabel error;
	JTabbedPane tabs;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JFrame frame;

	public Swip(JTabbedPane tabs, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.frame = frame;
		init();
	}

	public final void init() {
		this.setLayout(new BorderLayout());

		JLabel toplabel = new JLabel("Run Commission Sweep", SwingConstants.CENTER);
		toplabel.setFont(new Font("Dialog", Font.PLAIN, 19));
		toplabel.setForeground(Color.BLUE);
		this.add(toplabel, BorderLayout.NORTH);

		JPanel midp = createMidPanel();
		// this.add(midp, BorderLayout.CENTER);
		error = new JLabel();
		error.setForeground(Color.RED);
		error.setFont(new Font("", Font.BOLD, 13));
		JPanel pann = new TranslucentJPanel(Color.BLUE);
		pann.setLayout(new BorderLayout());
		pann.add(midp, BorderLayout.NORTH);
		pann.add(error, BorderLayout.CENTER);
		this.add(pann, BorderLayout.CENTER);
	}

	private JPanel createMidPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new GridLayout(3, 2));
		JLabel id = new JLabel("Specify The Month");
		id.setFont(new Font("", Font.BOLD, 13));

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		months.setPreferredSize(new Dimension(10, 50));
		months.setFont(new Font("Dialog", Font.PLAIN, 19));
		panel.add(id);
		panel.add(months);

		start = new JButton("<html><h3>Start</h3></html>");
		start.setBackground(Color.BLUE);
		start.setForeground(Color.WHITE);
		start.addActionListener(this);
		cancel = new JButton("<html><h3>Cancel</h3></html>");
		cancel.addActionListener(this);
		cancel.setBackground(Color.RED);
		cancel.setForeground(Color.WHITE);
		Box b = Box.createHorizontalBox();
		b.add(start);
		b.add(Box.createHorizontalStrut(50));
		b.add(cancel);

		panel.add(b);
		panel.setBorder(new TitledBorder("Run Commission Sweep"));

		return panel;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Commission Sweep")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Commission Sweep   ", null, this, "Commission Sweep");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public void confirmSwip(String month) {
		String mnth = "INSERT INTO swip_check(month_of,year)VALUES('" + month + "','" + new SetDateCreated().getYear()
				+ "')";
		try {
			stm.execute(mnth);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	class Worker1 extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		Worker1() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				new AnimateDialog().fadeOut(dialog, 100);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			JPanel pann = new TranslucentJPanel(Color.BLUE);
			pann.setOpaque(false);
			pann.setLayout(new BorderLayout());
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.getContentPane().setBackground(Color.BLUE);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			processData();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}

	public void processData() {
		error.setText("");
		String date = months.getSelectedItem().toString();
		Check c = new Check(rs, stm);
		if (!c.isMonthValid(date)) {
			ManagementCommision mc = new ManagementCommision(rs, stm);
			MembersCommissionSwip mcs = new MembersCommissionSwip(rs, rs1, stm, stmt);
			mc.postMgntAcc(date);
			new PauseThread().pause(30);
			mcs.processCommission(date);
			new PauseThread().pause(30);
			confirmSwip(date);
			JOptionPane.showMessageDialog(frame, "Done", "Success", JOptionPane.INFORMATION_MESSAGE);
			tabs.removeTabAt(tabs.getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(frame,
					"An Attempt to run commission swip more than once for the same month is blocked!\nMake sure you've selected the proper month.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			Worker1 w = new Worker1();
			w.execute();
		}
	}
}
