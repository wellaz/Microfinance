package soc.expenses.post;

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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soc.deco.BlinkingButton;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.GetCurrentMonth;
import soc.helpers.RandomNumberGenerator;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;
import soc.months.MonthsList;
import soc.subscribe.AffectLedger;
import soc.subscribe.CommissionAccPosting;
import soc.subscribe.CommissionSusPosting;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class PostExpense extends JPanel implements ActionListener {
	JTextField amounttxt;
	JTextArea area;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JComboBox<Object> months;

	public PostExpense(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
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
		JLabel toplbl = new JLabel("Submit  Details of An Expense ");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));

		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		JLabel monthoflbl = new JLabel("Month Of :");
		monthoflbl.setForeground(Color.WHITE);
		monthoflbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		months.setSelectedItem(GetCurrentMonth.currentMonth().toString());

		Box box = Box.createVerticalBox();
		JLabel natlbl = new JLabel("Description :(nature of the expense)");
		natlbl.setForeground(Color.WHITE);
		natlbl.setFont(new Font("", Font.BOLD, 15));

		area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);

		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(monthoflbl);
		midpanel.add(months);

		midpanel.add(new JLabel(""));
		box.add(natlbl);
		box.add(new JScrollPane(area));
		midpanel.add(box);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new BlinkingButton(Color.BLUE);
		submit.setText("<html><h3>Submit</h3></html>");
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
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Expenses")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Expenses   ", null, this, "Post Expenses");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void postings(double amount) {
		try {
			String query = "INSERT INTO expenses_acc(ref,amount,month_of,date,time,description)VALUES('" + "E"
					+ new RandomNumberGenerator().generateRandomNumber() + "','" + amount + "','"
					+ months.getSelectedItem().toString() + "','" + new SetDateCreated().getDate() + "','"
					+ new SetDateCreated().getTime() + "','" + area.getText() + "')";
			stm.execute(query);

			CommissionSusPosting sus = new CommissionSusPosting(rs, stm);
			sus.debitCommSus(4171, amount, months.getSelectedItem().toString());

			CommissionAccPosting ca = new CommissionAccPosting(rs, stm);
			ca.debitComm(4171, amount, months.getSelectedItem().toString());

			affectLedger(4171, amount);

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void debtPostings(int accno, double amount) {
		try {
			String duedate = new SetDateCreated().getThirtythDate();

			String query = "INSERT INTO debt(member_id,amount,due,date,time)VALUES('" + accno + "','" + amount + "','"
					+ duedate + "','" + new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "')";
			stm.execute(query);
			JOptionPane.showMessageDialog(frame, "The reimbursement date is " + duedate, "Information",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void affectLedger(int accno, double amount) {
		AffectLedger aff = new AffectLedger(rs, stm);
		aff.debitLedger(amount, accno);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			String amount = amounttxt.getText();
			if (!amount.equals("")) {
				postings(Integer.parseInt(amount));
				JOptionPane.showMessageDialog(frame, "$" + amount + " expense successfully posted!\nDONE!",
						"Information", JOptionPane.INFORMATION_MESSAGE);
				amounttxt.setText("");
				area.setText("");
			} else
				JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
						JOptionPane.WARNING_MESSAGE);
		}
	}
}
