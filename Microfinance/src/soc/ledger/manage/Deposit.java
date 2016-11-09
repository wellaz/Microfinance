package soc.ledger.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.PauseThread;
import soc.helpers.RandomNumberGenerator;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;
import soc.months.MonthsList;

@SuppressWarnings("serial")
public class Deposit extends JPanel implements ActionListener {
	JTextField amounttxt;
	JComboBox<Object> months;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JTextField intxt;

	public Deposit(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Deposit Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(5, 2, 1, 10));
		JLabel acclbl = new JLabel("Month Of");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));
		JLabel inlbl = new JLabel("Name of Investor :");
		inlbl.setForeground(Color.WHITE);
		inlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());
		intxt = new JTextField();

		Object[] da1 = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da1[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da1);

		midpanel.add(acclbl);
		midpanel.add(months);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(new JLabel());
		midpanel.add(new JLabel());
		midpanel.add(inlbl);
		midpanel.add(intxt);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("Proceed");
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Investor Deposit")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Investor Deposit   ", null, this, "Deposit");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void creditLedgerwithComm(double amount) {
		String query = "SELECT balance FROM ledger";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;
			if (rows == 0)
				balance = 0;
			else {
				String query11 = "SELECT balance FROM ledger";
				rs = stm.executeQuery(query11);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = amount + balance;
			String dt = new SetDateCreated().getDate(), tm = new SetDateCreated().getTime();
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('" + dt + "','" + tm + "','"
					+ 0 + "','" + amount + "','" + newbalance + "','" + "Ledger Deposit DFR"
					+ new RandomNumberGenerator().generateRandomNumber() + "')";
			stm.execute(text);
			new PauseThread().pause(10);
			String text1 = "INSERT INTO ledger_dep(amount,date,time)VALUES('" + amount + "','" + dt + "','" + tm
					+ "')";
			stm.execute(text1);
			amounttxt.setText("");

		} catch (SQLException ee) {

		}

	}

	public class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;
		JTable table;

		public Worker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				dialog.setVisible(false);
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
			dialog.getContentPane().add(box, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			dialog.setVisible(true);
			proceed();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.setVisible(false);
		}

	}

	public void proceed() {
		String amount = amounttxt.getText();
		String due = new SetDateCreated().getThirtythDate();
		String month = months.getSelectedItem().toString();
		String name = intxt.getText();
		double comm = 0.2 * Double.parseDouble(amount);
		double invcomm = 0.5 * (comm - (0.03 * comm));
		if (!(amount.equals("") && intxt.equals(""))) {
			creditLedgerwithComm(Double.parseDouble(amount));
			postInvestor(name, Double.parseDouble(amount), month, due);
			JOptionPane.showMessageDialog(frame,
					"An investment of $" + amount + " is validated\nA commission of $" + comm
							+ " will be generated on or before " + due + "\nAdvice the investor that a commission of $"
							+ invcomm + " is guaranteed!\nDONE!",
					"Information ", JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	public void postInvestor(String name, double amount, String month, String with_date) {
		String query = "INSERT INTO  investors(name,amount,month_of,date,time,year,with_date,valid)VALUES('" + name
				+ "','" + amount + "','" + month + "','" + new SetDateCreated().getDate() + "','"
				+ new SetDateCreated().getTime() + "','" + new SetDateCreated().getYear() + "','" + with_date + "','"
				+ "true" + "')";
		try {
			stm.execute(query);
			intxt.setText("");

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			Worker w = new Worker();
			w.execute();
		}

	}

}
