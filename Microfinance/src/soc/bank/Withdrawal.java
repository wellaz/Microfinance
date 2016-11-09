package soc.bank;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.RandomNumberGenerator;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;
import soc.months.MonthsList;

@SuppressWarnings("serial")
public class Withdrawal extends JPanel implements ActionListener {

	JTextField amounttxt;
	JComboBox<Object> memberids;
	JComboBox<Object> months;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JTextField member;

	public Withdrawal(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
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
		JLabel toplbl = new JLabel("Submit All Withdrawal Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));
		JLabel acclbl = new JLabel("Month Of");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));
		JLabel memberlbl = new JLabel("Specify The Member's Full Name");
		memberlbl.setForeground(Color.WHITE);
		memberlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());
		member = new JTextField();

		Object[] da1 = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da1[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da1);
		int whichmonth = new SetDateCreated().getMonth() - 1;
		months.setSelectedIndex(whichmonth);

		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(acclbl);
		midpanel.add(months);
		midpanel.add(memberlbl);
		midpanel.add(member);

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
				if (tabs.getTitleAt(x).trim().equals("Revenue Withdrawal")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Revenue Withdrawal   ", null, this, "Withdrawal");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void debitLedger(double interest, String month, String membertxt) {
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
			double newbalance = balance - interest;
			String dt = new SetDateCreated().getDate(), tm = new SetDateCreated().getTime();
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('" + dt + "','" + tm + "','"
					+ interest + "','" + 0 + "','" + newbalance + "','" + "Ledger WithDr WRF"
					+ new RandomNumberGenerator().generateRandomNumber() + "')";
			stm.execute(text);
			postWithdrawal(interest, month, membertxt);
			amounttxt.setText("");
			member.setText("");
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void postWithdrawal(double amount, String month, String membertxt) {
		String text = "INSERT INTO bank_with(amount,member,date,time,month,returned)VALUES('" + amount + "','"
				+ membertxt + "','" + new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','"
				+ month + "','" + "false" + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {

		}
	}

	public void proceed() {
		String amount = amounttxt.getText();
		String month = months.getSelectedItem().toString();
		String membertxt = member.getText();
		if (!(amount.equals(""))) {
			debitLedger(Double.parseDouble(amount), month, membertxt);
			JOptionPane.showMessageDialog(frame, "A Withdrawal of $" + amount + " is validated.\nDONE!", "Information ",
					JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			proceed();
		}
	}
}
