package soc.reports.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.Font;

import soc.deco.AnimateDialog;
import soc.deco.BlinkingButton;
import soc.deco.BlinkingLabel;
import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.GetAccountNumbers;
import soc.helpers.IconImage;
import soc.helpers.SplitPane;
import soc.helpers.TextValidator;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Find extends JPanel {
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;
	JPanel mainp;
	DoubleForm df;
	String name = null, id = null;
	private JTextField find;
	private JLabel error;
	private JDialog dialog;
	private BlinkingButton collreport;

	public Find(JTabbedPane tabs, ResultSet rs, Statement stm, ResultSet rs1, Statement stmt, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.frame = frame;
		this.tabs = tabs;

		this.setLayout(new BorderLayout());

		df = new DoubleForm();
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("General Info")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				arrange();
				tabs.addTab("General Info   ", null, this, "General Information on an account.");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public void insertMassRecordTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("General Info")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				massFile();
				tabs.addTab("General Info   ", null, this, "General Information on an account.");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});

		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");

		find = new JTextField();
		error = new BlinkingLabel("");
		find.addKeyListener(new TextValidator());
		find.addActionListener(event -> buttonAction());
		JPanel midpanel = new TranslucentJPanel(Color.BLUE);
		midpanel.setLayout(new GridLayout(3, 1));
		midpanel.add(top);
		midpanel.add(find);

		// error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		Box box = Box.createHorizontalBox();
		box.setOpaque(false);
		JButton defaultButton = new JButton("OK");
		defaultButton.addActionListener(event -> buttonAction());
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(event -> new AnimateDialog().fadeOut(dialog, 100));
		collreport = new BlinkingButton(Color.BLUE);
		collreport.setText("Collective Report");
		collreport.addActionListener((event) -> {
			MassRecordWorker w = new MassRecordWorker();
			w.execute();
			new AnimateDialog().fadeOut(dialog, 100);
		});
		box.add(Box.createHorizontalGlue());
		box.add(defaultButton);
		box.add(Box.createHorizontalStrut(5));
		box.add(cancel);

		box.add(Box.createHorizontalStrut(30));
		box.add(collreport);

		dialog.getContentPane().setBackground(Color.GRAY);

		dialog.getRootPane().setDefaultButton(defaultButton);
		dialog.getContentPane().add(box, BorderLayout.SOUTH);

		dialog.setSize(300, 155);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		new AnimateDialog().fadeIn(dialog, 100);
		dialog.setAlwaysOnTop(true);
	}

	// carefully analyse this method as it might confuse you
	public void massFile() {
		ArrayList<Integer> allmembers = new GetAccountNumbers(rs, stm).getAccounts();
		int arraySize = allmembers.size();
		mainp = new JPanel(new GridLayout(arraySize, 1));
		Subscriptions subs = new Subscriptions(rs, stm);
		Debts debts = new Debts(rs, stm, rs1, stmt);
		Commissions commissions = new Commissions(rs, stm, rs1, stmt);
		MicroRecon microrecon = new MicroRecon(rs, rs1, stm, stmt);

		JPanel subpp = subs.getSubscriptions(id);
		JPanel commpp = commissions.midPanel(id);
		JPanel dedtspp = debts.midPanel(id);

		double totalsub = df.form(subs.getTotalSubscriptions());
		double totalcomm = df.form(commissions.getTotalCommission());
		double totaldebts = df.form(debts.getOutstandingBalance());
		double posa = df.form(totalsub + totalcomm);
		double posb = df.form(posa - totaldebts);

		JPanel pam = microrecon.createMidPanel(id, totalsub, totalcomm, totaldebts, posa, posb);
		pam.setLayout(null);
		subpp.setLayout(null);
		commpp.setLayout(null);
		dedtspp.setLayout(null);

		for (int member_id : allmembers) {
			String mid = Integer.toString(member_id);

			JPanel panel = new JPanel(new GridLayout(3, 1));
			panel.setBorder(new TitledBorder(getName(mid).toUpperCase()));
			panel.add(subs.getSubscriptions(mid));
			panel.add(debts.midPanel(mid));
			panel.add(commissions.midPanel(mid));

			mainp.add(panel);
		}

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(mainp);

		JLabel o = new JLabel("General Transactions History Enquiry For All Members");
		o.setForeground(Color.RED);
		o.setFont(new java.awt.Font("", Font.BOLD, 30));
		JPanel p = new TranslucentJPanel(Color.BLUE);
		p.setLayout(new FlowLayout());
		p.add(o, SwingConstants.CENTER);
		this.add(p, BorderLayout.NORTH);

		// this button downloads all overall buttons.
		JButton overall = new BlinkingButton(Color.BLUE);
		p.add(Box.createHorizontalGlue());
		p.add(overall);
		overall.setText("<html><h3>Download Full Report<h3></html>");
		BatchPDF batchpdf = new BatchPDF(frame, rs, stm);
		for (int member_id : allmembers) {
			String mid = Integer.toString(member_id);

			double totalsubb = df.form(subs.totalSubscriptions(mid));
			double totalcommm = df.form(commissions.totalCommission(mid));
			double totaldebtss = df.form(debts.totalDebt(mid));
			double posaa = df.form(totalsubb + totalcommm);
			double posbb = df.form(posaa - totaldebtss);
			JTable jtable1 = subs.createSubsTable(mid);
			JTable jtable2 = commissions.createCommissionsTable(mid);
			JTable jtable3 = debts.createDebtsTable(mid);
			JTable jtable4 = microrecon.crrateMicroReconTable(mid, totalsubb, totalcommm, totaldebtss, posaa, posbb);

			BatchPDF.MinireportsWorker w1 = batchpdf.new MinireportsWorker(jtable1, jtable2, jtable3, jtable4,
					getName(mid), mid);
			w1.execute();
		}
		overall.addActionListener((event) -> {
			BatchPDF.Worker w = batchpdf.new Worker();
			w.execute();
		});
		this.add(scroll, BorderLayout.CENTER);
	}

	public String getName(String id) {
		String membername = null;
		String text = "SELECT first_name,last_name FROM members  WHERE member_id = '" + id + "' ";
		try {
			rs = stm.executeQuery(text);
			if (rs.next()) {
				membername = rs.getString(1) + " " + rs.getString(2);

			} else {
				membername = "";
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return membername;
	}

	public void buttonAction() {
		EventQueue.invokeLater(() -> {
			error.setText("");
			id = find.getText();
			String text = "SELECT first_name,last_name FROM members  WHERE member_id = '" + id + "' ";
			try {
				rs = stm.executeQuery(text);
				if (rs.next()) {
					name = rs.getString(1) + " " + rs.getString(2);
					Worker w = new Worker();
					w.execute();
					new AnimateDialog().fadeOut(dialog, 100);
				} else {
					new AnimateDialog().fadeIn(dialog, 100);
					error.setText("Account " + id + " DO NOT HONOR!");
				}
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		});
	}

	public void arrange() {
		mainp = new JPanel(new GridLayout(3, 1));
		Subscriptions subs = new Subscriptions(rs, stm);
		Debts debts = new Debts(rs, stm, rs1, stmt);
		Commissions commissions = new Commissions(rs, stm, rs1, stmt);
		MicroRecon microrecon = new MicroRecon(rs, rs1, stm, stmt);
		mainp.add(subs.getSubscriptions(id));
		mainp.add(debts.midPanel(id));
		mainp.add(commissions.midPanel(id));
		// this.add(mainp, BorderLayout.CENTER);

		double totalsub = df.form(subs.getTotalSubscriptions());
		double totalcomm = df.form(commissions.getTotalCommission());
		double totaldebts = df.form(debts.getOutstandingBalance());
		double posa = df.form(totalsub + totalcomm);
		double posb = df.form(posa - totaldebts);

		JLabel o = new JLabel("General Transactions History Enquiry For " + name.toUpperCase());
		o.setForeground(Color.RED);
		o.setFont(new java.awt.Font("", Font.BOLD, 30));
		JPanel p = new TranslucentJPanel(Color.BLUE);
		p.setLayout(new FlowLayout());
		p.add(o, SwingConstants.CENTER);
		this.add(p, BorderLayout.NORTH);

		SplitPane split = new SplitPane();
		JPanel scrollpanel = new TranslucentJPanel(Color.BLUE);
		scrollpanel.setLayout(new BorderLayout());

		JButton overall = new JButton("Download Full Report");
		overall.addActionListener((event) -> {
			OverallPDF ow = new OverallPDF(subs.getTable(), commissions.getTable(), debts.getTable(),
					microrecon.getTable(), name, id);
			OverallPDF.Worker w = ow.new Worker();
			w.execute();
		});

		JLabel sm = new JLabel("Summary");
		sm.setForeground(Color.GREEN);
		sm.setFont(new java.awt.Font("", Font.BOLD, 15));

		Box bx = Box.createVerticalBox();
		JButton micro = new JButton("Download Micro - Recon");
		micro.addActionListener((event) -> {
			RecoPDF ow = new RecoPDF(microrecon.getTable(), name, id);
			RecoPDF.Worker w = ow.new Worker();
			w.execute();
		});
		bx.add(sm);
		bx.add(micro);
		scrollpanel.add(bx, BorderLayout.NORTH);

		scrollpanel.add(microrecon.createMidPanel(id, totalsub, totalcomm, totaldebts, posa, posb));

		scrollpanel.add(overall, BorderLayout.SOUTH);
		split.split.setLeftComponent(scrollpanel);
		split.split.setRightComponent(mainp);
		this.add(split.split, BorderLayout.CENTER);
	}

	class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		Worker() {
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
			dialog.getContentPane().setBackground(Color.BLUE);
			JPanel pann = new TranslucentJPanel(Color.BLUE);
			pann.setOpaque(false);
			pann.setLayout(new BorderLayout());
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			insertTab();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}

	class MassRecordWorker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		MassRecordWorker() {
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
			pann.setLayout(new BorderLayout());
			pann.setOpaque(false);
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			insertMassRecordTab();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}

	public String getMemberName(String id) {
		String text = "SELECT first_name,last_name FROM members  WHERE member_id = '" + id + "' ";
		String name = null;
		try {
			rs = stm.executeQuery(text);
			if (rs.next()) {
				name = rs.getString(1) + " " + rs.getString(2);

			} else {
				name = "";
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return name;
	}

}
