
package soc.subscribe;

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
import java.awt.event.ItemEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import com.toedter.calendar.JDateChooser;

import soc.deco.AnimateDialog;
import soc.deco.BlinkingButton;
import soc.deco.BlinkingPanel;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.GetAccountNumbers;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;
import soc.months.MonthsList;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class Subscribe extends JPanel implements ActionListener {

	JTextField amounttxt;
	JComboBox<Object> memberids;
	JComboBox<Object> months;
	JDateChooser birth;
	JButton submit, cancel, uploadimage;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	PreparedStatement pstmt;
	Connection conn;
	JFrame frame;
	private JRadioButton general;
	private JRadioButton inc_sub;
	private JTextField bondtxt;

	public Subscribe(JTabbedPane tabs, ResultSet rs, Statement stm, PreparedStatement pstmt, Connection conn,
			JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.pstmt = pstmt;
		this.conn = conn;

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Subscription or Account Top-Up Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));
		JLabel acclbl = new JLabel("Member Account :");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));
		JLabel monthlbl = new JLabel("Scheduled for (month) :");
		monthlbl.setForeground(Color.WHITE);
		monthlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] daa = new String[new GetAccountNumbers(rs, stm).getAccounts().size()];
		for (int i = 0; i < new GetAccountNumbers(rs, stm).getAccounts().size(); i++) {
			daa[i] = "" + new GetAccountNumbers(rs, stm).getAccounts().get(i);
		}

		memberids = new JComboBox<>(daa);
		memberids.addItemListener((e) -> {
			Object item = e.getItem();
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int selecteditem = Integer.parseInt(item.toString());
				double foundsubscription = getSubscription(selecteditem);
				String amount = foundsubscription > 0 ? Double.toString(foundsubscription) : "";
				EventQueue.invokeLater(() -> {
					amounttxt.setText(amount);
				});
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			}
		});

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		int whichmonth = new SetDateCreated().getMonth() - 1;
		months.setSelectedIndex(whichmonth);

		midpanel.add(acclbl);
		midpanel.add(memberids);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(monthlbl);
		midpanel.add(months);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new BlinkingButton(Color.BLUE);
		submit.setText("<html><h1>Submit</h1></html>");
		submit.setBackground(Color.BLUE);
		submit.setForeground(Color.BLACK);
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		Box box = Box.createVerticalBox();
		JLabel label = new JLabel("Specify the type of Subscription Payment that you are processing?");
		label.setFont(new Font("", Font.BOLD, 15));
		label.setForeground(Color.WHITE);

		general = new JRadioButton("<html><h3>General Subscription</h3></html>");
		inc_sub = new JRadioButton("<html><h3>Increment My Previous Subscription Shares</h3></html>");

		ButtonGroup gr = new ButtonGroup();
		gr.add(general);
		gr.add(inc_sub);

		box.add(new JLabel(""));
		box.add(Box.createVerticalStrut(30));
		box.add(label);
		box.add(Box.createVerticalStrut(30));
		box.add(general);
		box.add(Box.createVerticalStrut(30));
		box.add(inc_sub);

		JPanel bondpanel = new JPanel(new GridLayout(3, 1));
		bondpanel.setOpaque(false);
		bondpanel.setBorder(new MatteBorder(1, 1, 3, 4, Color.BLACK));
		bondpanel.add(new BlinkingPanel("Attention !!!"));
		JLabel bondlabel = new JLabel(
				"<html><h3><u>Specify, IF ANY, how much worth of BOND Notes and Coins have you received?</u><br/>\u2193</h3></html>");
		bondlabel.setFont(new Font("", Font.BOLD, 14));
		bondlabel.setForeground(Color.WHITE);
		bondtxt = new JTextField();
		// bondtxt.setBorder(null);
		bondtxt.setFont(new Font("", Font.ROMAN_BASELINE, 30));
		bondtxt.addKeyListener(new TextValidator());
		bondtxt.setBackground(Color.BLACK);
		bondtxt.setForeground(Color.WHITE);
		bondpanel.add(bondlabel);
		bondpanel.add(bondtxt);
		box.add(Box.createVerticalStrut(30));
		box.add(bondpanel);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(box, BorderLayout.EAST);
		temp.add(bp, BorderLayout.SOUTH);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Subscription")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Subscription   ", null, this, "New Subscription");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void checkData() {
		if (!(amounttxt.getText().equals(""))) {
			int accno = Integer.parseInt(memberids.getSelectedItem().toString());
			double amm = Double.parseDouble(amounttxt.getText());
			String month = months.getSelectedItem().toString();
			if (general.isSelected()) {
				generalSub(accno, amm, month);
			} else if (inc_sub.isSelected()) {
				subInc(accno, month, amm);
			} else {
				JOptionPane.showMessageDialog(frame, "You forgot to specify the transaction type.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}

		} else {
			JOptionPane.showMessageDialog(frame, "Empty fields cannot be submitted!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void subInc(int acc, String month, double amount) {
		String qr = "SELECT subscription FROM subscriptions WHERE member_id = '" + acc + "' AND month_of = '" + month
				+ "'AND year = '" + new SetDateCreated().getYear() + "'";
		try {
			rs = stm.executeQuery(qr);
			double debtInterest = 0.2 * amount;
			String date = new SetDateCreated().getDate();
			String time = new SetDateCreated().getTime();
			String year = new SetDateCreated().getYear();
			String duedate = new SetDateCreated().getThirtythDate();
			if (rs.next()) {
				double sub = rs.getDouble(1);
				double check = amount % 20;
				if (check == 0) {
					GetShareCount getsharecount = new GetShareCount(rs, stm);
					String query1 = "SELECT first_name,last_name FROM members WHERE member_id = '" + acc + "'";
					rs = stm.executeQuery(query1);
					rs.first();
					String username = rs.getString(1);
					String surname = rs.getString(2);

					int v = JOptionPane.showConfirmDialog(frame,
							"Account Number :" + acc + "\nFirst Name :" + username + "\nSurname :" + surname
									+ "\nSubscription Inc $:" + amount + "\nA commission of $" + debtInterest
									+ " is expected before " + duedate + "\n\n\tContinue to submit?",
							"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (v == JOptionPane.YES_OPTION) {

						double newsub = sub + amount;

						String r = "UPDATE subscriptions SET subscription = '" + newsub + "' WHERE member_id = '" + acc
								+ "' AND month_of = '" + month + "'AND year = '" + year + "'";
						stm.executeUpdate(r);
						if (!bondtxt.getText().trim().equals("")) {
							double bondsammount = Double.parseDouble(bondtxt.getText());
							bondsPostings(acc, bondsammount, month, date, time, year);
							bondtxt.setText("");
						}

						ActivityAccPosting activityposting = new ActivityAccPosting(rs, stm);
						activityposting.subIncPostings(acc, amount);

						getsharecount.updateShares(acc, month, amount);
						AffectLedger affectLedger = new AffectLedger(rs, stm);
						affectLedger.creditLedgerWithSubTopup(amount, acc);

						debtPostings(acc, amount, date, time, month, debtInterest, duedate);
						postInstallment(acc, amount, date, time);

						amounttxt.setText("");

					} else {

					}

				} else {
					JOptionPane.showMessageDialog(frame,
							"Consider removing $" + check + " in order protect the integrity of the group policy.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				String query1 = "SELECT first_name,last_name FROM members WHERE member_id = '" + acc + "'";
				rs = stm.executeQuery(query1);
				rs.first();
				String username = rs.getString(1);
				String surname = rs.getString(2);
				int c = JOptionPane.showConfirmDialog(frame,
						"Member ID " + acc + " \nName : " + username + "\nSurname " + surname
								+ " \ndoes not have any subscription for " + month
								+ ".\nDo you want to proceed and charge them $" + debtInterest + " which is due on "
								+ duedate + " !",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (c == JOptionPane.YES_OPTION) {
					debtPostings(acc, amount, date, time, month, debtInterest, duedate);
					postInstallment(acc, amount, date, time);
					generalSub(acc, amount, month);
				} else {
					JOptionPane.showMessageDialog(frame, "Subscription Incerement cancelled", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void generalSub(int accno, double amm, String month) {
		String find = "SELECT member_id FROM members WHERE member_id = '" + accno + "'";
		try {
			rs = stm.executeQuery(find);
			if (!rs.next()) {
				JOptionPane.showMessageDialog(frame,
						"Member ID " + accno + " does not exist.\nConsider registering it!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				double remainder = amm % 20;
				if (!(remainder == 0)) {
					JOptionPane.showMessageDialog(frame,
							"Consider removing $" + remainder + " in order protect the integrity of the group policy.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					String test = "SELECT month_of,year FROM subscriptions WHERE member_id = '" + accno
							+ "' AND month_of = '" + month + "' AND year = '" + new SetDateCreated().getYear() + "'";
					rs = stm.executeQuery(test);
					if (!rs.next()) {
						GetShareCount getsharecount = new GetShareCount(rs, stm);

						String query1 = "SELECT first_name,last_name FROM members WHERE member_id = '" + accno + "'";
						rs = stm.executeQuery(query1);
						rs.first();
						String username = rs.getString(1);
						String surname = rs.getString(2);

						int c = JOptionPane.showConfirmDialog(frame,
								"Account Number :" + accno + "\nFirst Name :" + username + "\nSurname :" + surname
										+ "\nSubscription $:" + amm + "\n\n\tConfirm To Submit?",
								"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (c == JOptionPane.YES_OPTION) {
							String date = new SetDateCreated().getDate();
							String time = new SetDateCreated().getTime();
							String year = new SetDateCreated().getYear();

							String query = "INSERT INTO subscriptions(member_id,subscription,month_of,date,time,year)VALUES('"
									+ accno + "','" + amm + "','" + month + "','" + date + "','" + time + "','" + year
									+ "')";
							stm.execute(query);
							if (!bondtxt.getText().trim().equals("")) {
								double bondsammount = Double.parseDouble(bondtxt.getText());
								bondsPostings(accno, bondsammount, month, date, time, year);
								bondtxt.setText("");
							}

							ActivityAccPosting activityposting = new ActivityAccPosting(rs, stm);
							activityposting.postings(accno, amm);

							getsharecount.countShares(amm, month, accno);

							AffectLedger affectLedger = new AffectLedger(rs, stm);
							affectLedger.creditLedger(amm, accno);

							amounttxt.setText("");
						} else {

						}

					} else {
						JOptionPane.showMessageDialog(frame,
								"The account " + accno + " is already credited with \n$" + amm + " for the month of "
										+ month + " " + new SetDateCreated().getYear()
										+ ".\nConsider selecting the right month!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		} catch (SQLException ee) {
			JOptionPane.showMessageDialog(frame, "General Error code 14 SUB!\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	public void debtPostings(int accno, double amount, String date, String time, String month, double balance,
			String duedate) {
		try {
			String query = "INSERT INTO debts(member_id,amount,month_of,due,date,time,paid,balance,year,commission)VALUES('"
					+ accno + "','" + amount + "','" + month + "','" + duedate + "','" + date + "','" + time + "','"
					+ amount + "','" + balance + "','" + new SetDateCreated().getYear() + "','" + balance + "')";
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();

		}
	}

	public void postInstallment(int accno, double amm, String date, String time) {
		String text = "INSERT INTO debt_installments(member_id,amount,date,time,year)VALUES('" + accno + "','" + amm
				+ "','" + date + "','" + time + "','" + new SetDateCreated().getYear() + "')";
		try {
			stm.execute(text);
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
			JPanel pann = new TranslucentJPanel(Color.BLUE);
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
			checkData();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}

	public double getSubscription(int acc) {
		String qr = "SELECT subscription FROM subscriptions WHERE member_id = '" + acc + "'AND year = '"
				+ new SetDateCreated().getYear() + "'";
		double foundsub = 0;
		try {
			rs = stm.executeQuery(qr);
			if (rs.last())
				foundsub = rs.getDouble(1);
			else
				foundsub = 0;

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return foundsub;
	}

	public void bondsPostings(int accno, double amm, String month, String date, String time, String year) {
		String query = "INSERT INTO bonds_subscriptions(member_id,subscription,month_of,date,time,year)VALUES('" + accno
				+ "','" + amm + "','" + month + "','" + date + "','" + time + "','" + year + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
