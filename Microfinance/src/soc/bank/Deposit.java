package soc.bank;

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

import soc.deco.AnimateDialog;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.DoubleForm;
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
	private JComboBox<Object> members;

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
		JLabel memberlbl = new JLabel("Select The Member's Full Name");
		memberlbl.setForeground(Color.WHITE);
		memberlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] da1 = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da1[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da1);
		int whichmonth = new SetDateCreated().getMonth() - 1;
		months.setSelectedIndex(whichmonth);

		int datalist = new GetMembers(rs, stm).members().size();

		Object[] data = new String[datalist];
		for (int i = 0; i < datalist; i++) {
			data[i] = new GetMembers(rs, stm).members().get(i);
		}
		members = new JComboBox<>(data);
		members.setFont(new Font("Dialog", Font.PLAIN, 18));

		midpanel.add(acclbl);
		midpanel.add(months);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(memberlbl);
		midpanel.add(members);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("<html><h3>Submit</h3</html>");
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
				if (tabs.getTitleAt(x).trim().equals("Revenue Deposit")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Revenue Deposit   ", null, this, "Deposit");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void creditLedgerwithComm(double amount, String month, String member) {
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
			String textt = "SELECT amount FROM bank_with WHERE member = '" + member + "'";
			rs = stm.executeQuery(textt);
			if (rs.next()) {
				double actualamount = rs.getDouble(1);
				if (amount <= actualamount) {
					String dt = new SetDateCreated().getDate(), tm = new SetDateCreated().getTime();
					String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('" + dt + "','" + tm
							+ "','" + 0 + "','" + amount + "','" + newbalance + "','" + "Ledger Deposit DFR"
							+ new RandomNumberGenerator().generateRandomNumber() + "')";
					stm.execute(text);
					new PauseThread().pause(10);
					String text1 = "INSERT INTO bank_dep(amount,member,date,time,month)VALUES('" + amount + "','"
							+ member + "','" + dt + "','" + tm + "','" + month + "')";
					stm.execute(text1);
					amounttxt.setText("");
					if (actualamount == amount) {
						updateWithdrawals("true", member);
					} else {
						updateWithdrawals("false", member);
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"You have exceeded the amount by $" + new DoubleForm().form(amount - actualamount), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(frame, "Specify the member Name", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
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
			new AnimateDialog().fadeIn(dialog, 100);
			proceed();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}

	public void proceed() {
		String amount = amounttxt.getText();
		String month = months.getSelectedItem().toString();
		String membertxt = members.getSelectedItem().toString();
		if (!(amount.equals(""))) {
			creditLedgerwithComm(Double.parseDouble(amount), month, membertxt);
			JOptionPane.showMessageDialog(frame, "A Deposit of $" + amount + " is validated.\nDONE!", "Information ",
					JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	public void updateWithdrawals(String spec, String member) {
		String updateString = "UPDATE bank_with SET returned = '" + spec + "' WHERE member = '" + member + "'";
		try {
			stm.executeUpdate(updateString);
		} catch (SQLException ee) {
			ee.printStackTrace();
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
