package soc.borrowingwindow;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.toedter.calendar.JDateChooser;

import soc.deco.BlinkingButton;
import soc.deco.BlinkingPanel;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.DoubleForm;
import soc.helpers.GetAccountNumbers;
import soc.helpers.IconImage;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;
import soc.months.MonthsList;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class BorrowWindow extends JPanel implements ActionListener {
	JTextField amounttxt;
	JComboBox<Object> memberids;
	JComboBox<Object> months;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JTextField bentext;
	private JDateChooser whendate;
	DoubleForm df;
	Account_Info account_info;
	private JTextField bondtxt;

	public BorrowWindow(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		df = new DoubleForm();

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Account Debt Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(6, 2, 1, 10));
		JLabel acclbl = new JLabel("Member Account :");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));
		JLabel monthlbl = new JLabel("Month Of :");
		monthlbl.setForeground(Color.WHITE);
		monthlbl.setFont(new Font("", Font.BOLD, 15));
		JLabel datelbl = new JLabel("Date :");
		datelbl.setForeground(Color.WHITE);
		datelbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[new GetAccountNumbers(rs, stm).getAccounts().size()];
		for (int i = 0; i < new GetAccountNumbers(rs, stm).getAccounts().size(); i++) {
			da[i] = "" + new GetAccountNumbers(rs, stm).getAccounts().get(i);
		}

		memberids = new JComboBox<>(da);

		Object[] da1 = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da1[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da1);
		int whichmonth = new SetDateCreated().getMonth() - 1;
		months.setSelectedIndex(whichmonth);
		whendate = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		whendate.setDate(new Date());

		midpanel.add(acclbl);
		midpanel.add(memberids);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(monthlbl);
		midpanel.add(months);
		midpanel.add(datelbl);
		midpanel.add(whendate);

		midpanel.add(new JLabel());
		midpanel.add(new JLabel());

		JLabel blabel = new JLabel("Add Beneficiary (If any) :");
		blabel.setForeground(Color.WHITE);
		blabel.setFont(new Font("", Font.BOLD, 15));

		bentext = new JTextField();

		midpanel.add(blabel);
		midpanel.add(bentext);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new BlinkingButton(Color.BLUE);
		submit.setText("<html><h1>Proceed</h1></html>");
		submit.setBackground(Color.BLUE);
		submit.setForeground(Color.BLACK);

		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);

		this.add(temp, BorderLayout.CENTER);

		JPanel bondpanel = new JPanel(new GridLayout(3, 1));
		bondpanel.setOpaque(false);
		bondpanel.setBorder(new MatteBorder(1, 1, 3, 4, Color.BLACK));
		bondpanel.add(new BlinkingPanel("Attention !!!"));
		JLabel bondlabel = new JLabel(
				"<html><h3><u>Specify, IF ANY, how much worth of BOND Notes and Coins have you given away?</u><br/>\u2193</h3></html>");
		bondlabel.setFont(new Font("", Font.BOLD, 14));
		bondlabel.setForeground(Color.WHITE);
		bondtxt = new JTextField();
		bondtxt.setFont(new Font("", Font.ROMAN_BASELINE, 30));
		bondtxt.addKeyListener(new TextValidator());
		bondtxt.setBackground(Color.BLACK);
		bondtxt.setForeground(Color.WHITE);
		bondpanel.add(bondlabel);
		bondpanel.add(bondtxt);

		temp.add(bondpanel, BorderLayout.SOUTH);

		this.add(bp, BorderLayout.SOUTH);
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Lending")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Lending   ", null, this, "Lending Money");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void postings(int accno, double amount, String month, String due, String date, String time, String year) {
		account_info = new Account_Info(rs, stm, accno);
		String query0 = "SELECT balance FROM ledger";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM ledger";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			String query1 = "SELECT first_name,last_name FROM members WHERE member_id = '" + accno + "'";
			rs = stm.executeQuery(query1);
			rs.first();
			String username = rs.getString(1);
			String surname = rs.getString(2);

			JLabel message = new JLabel(
					"<html><p>Member ID " + accno + " <br/>Name : " + username + " " + surname + "<br/>Borrowing $"
							+ amount + "</p><br><h2><i style='color:red'>Confirm ?</i></h2></html>",
					SwingConstants.CENTER);
			String title = "Confirm";
			message.setFont(new Font("", Font.BOLD, 15));
			message.setForeground(Color.WHITE);

			JDialog dialog = new JDialog((JFrame) null, title, true);
			dialog.setLayout(new BorderLayout());
			dialog.setIconImage(new IconImage().createIconImage());

			JPanel panel = new TranslucentJPanel(Color.BLUE);
			panel.setLayout(new BorderLayout());
			panel.add(message, BorderLayout.NORTH);

			// comm
			dialog.getContentPane().add(panel, BorderLayout.CENTER);

			JButton proceedbtn = new JButton("OK");
			double newbalance = df.form(balance - amount);
			proceedbtn.addActionListener((event) -> {
				String query = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('" + date + "','"
						+ time + "','" + amount + "','" + 0 + "','" + newbalance + "','" + "Lending Acc" + accno + "')";

				try {
					stm.execute(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				debtPostings(accno, amount, month, due, date, time, year);
				dialog.dispose();
			});
			JButton cancelbtn = new JButton("Cancel");

			cancelbtn.addActionListener((event) -> {
				dialog.dispose();
			});
			JButton accinfobtn = new JButton("Account Micro-Statement");
			accinfobtn.addActionListener((event) -> {
				account_info.showDialog();
			});

			Box lowerbox = Box.createHorizontalBox();

			lowerbox.add(Box.createHorizontalGlue());
			lowerbox.add(proceedbtn);
			lowerbox.add(cancelbtn);
			lowerbox.add(accinfobtn);
			dialog.getRootPane().setDefaultButton(proceedbtn);
			panel.add(lowerbox, BorderLayout.SOUTH);

			dialog.setSize(400, 200);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
			dialog.setLocation(x, y);
			dialog.setVisible(true);
			dialog.setAlwaysOnTop(true);

		} catch (SQLException ee) {
			JOptionPane.showMessageDialog(frame, "No Date Chosen", "Error", JOptionPane.ERROR_MESSAGE);
			// ee.printStackTrace();
		}
	}

	public void benPostings(int accno, double amount, String name, String date, String time) {
		try {
			String query = "INSERT INTO beneficiary(member_id,name,amount,month_of,date,time,year)VALUES('" + accno
					+ "','" + name + "','" + amount + "','" + months.getSelectedItem().toString() + "','" + date + "','"
					+ time + "','" + new SetDateCreated().getYear() + "')";
			stm.execute(query);
			JOptionPane.showMessageDialog(frame,
					"Account " + accno + " borrowed $" + amount + " for " + name + "\nTransaction validated",
					"Information", JOptionPane.INFORMATION_MESSAGE);
			bentext.setText("");

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void debtPostings(int accno, double amount, String month, String due, String date, String time,
			String year) {
		try {

			double interest = df.form(0.2 * amount);
			double balance = df.form(amount + interest);

			String query = "INSERT INTO debts(member_id,amount,month_of,due,date,time,paid,balance,year,commission,bad_debt)VALUES('"
					+ accno + "','" + amount + "','" + month + "','" + due + "','" + date + "','" + time + "','" + 0
					+ "','" + balance + "','" + year + "','" + interest + "','" + "no" + "')";
			stm.execute(query);

			if (!bondtxt.getText().trim().equals("")) {
				double bondsammount = df.form(Double.parseDouble(bondtxt.getText()));
				bondsPostings(accno, bondsammount, month, due, date, time, year);
				bondtxt.setText("");
			}

			JOptionPane.showMessageDialog(frame,
					"The reimbursement date is " + due
							+ "\nWould you please remind the member of this date!\nAnd most importantly, a commission of $"
							+ interest + " is expected\non top of $" + amount + " to make $" + balance
							+ " as reimbursement figure!",
					"Information", JOptionPane.INFORMATION_MESSAGE);
			amounttxt.setText("");

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			int accno = Integer.parseInt(memberids.getSelectedItem().toString());
			// double totaldebt = new BorrowersOutStandingBalance(stm,
			// rs).getTotalOutstandingBalance(accno);
			// double limit = new StoreLimitValue(stm, rs).actualLimitValue();
			String amount = amounttxt.getText();

			// if (totaldebt < limit) {

			if (!amount.equals(""))
				executeMethod(accno, amount);
			else
				JOptionPane.showMessageDialog(frame, "Null amount cannot be submitted!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			/*
			 * } else { JOptionPane.showMessageDialog(frame, "Account number " +
			 * accno + " has a total of $" + totaldebt +
			 * " as their\n outstanding debt.\nThis violates the Debtors Agreement Policy."
			 * , "Transaction Rejected", JOptionPane.WARNING_MESSAGE); }
			 */
		}
	}

	public void executeMethod(int accno, String amount) {
		String ben = bentext.getText();
		String due = new SetDateCreated().getThirtythDate();
		String month = months.getSelectedItem().toString();
		String date = new SetDateCreated().getExactDate(whendate);
		String time = new SetDateCreated().getTime();
		String year = new SetDateCreated().getYear();
		if (!(amount.equals("") && date.equals(""))) {
			if (!ben.trim().equals("")) {
				postings(accno, Double.parseDouble(amount), month, due, date, time, year);
				benPostings(accno, Double.parseDouble(amount), ben, date, time);
			} else {
				postings(accno, Double.parseDouble(amount), month, due, date, time, year);
			}
		} else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	public void bondsPostings(int accno, double amm, String month, String due, String date, String time, String year) {
		String query = "INSERT INTO bonds_debts(member_id,amount,month_of,due,date,time,year)VALUES('" + accno + "','"
				+ amm + "','" + month + "','" + due + "','" + date + "','" + time + "','" + year + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
