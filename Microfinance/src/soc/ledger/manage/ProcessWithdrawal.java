package soc.ledger.manage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import soc.borrowingwindow.PostComm;
import soc.commission.swip.ManagementCommision;
import soc.helpers.RandomNumberGenerator;
import soc.helpers.SetDateCreated;
import soc.subscribe.AffectLedger;
import soc.subscribe.CommissionAccPosting;
import soc.subscribe.CommissionSusPosting;

public class ProcessWithdrawal {
	JTable table;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	JDialog d;

	public ProcessWithdrawal(JTable table, JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame, JDialog d) {
		;
		this.table = table;
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.d = d;
	}

	public void posting() {
		PostComm pc = new PostComm(rs, stm);

		String am = table.getValueAt(table.getSelectedRow(), 1).toString();
		String mnth = table.getValueAt(table.getSelectedRow(), 2).toString();
		String name = table.getValueAt(table.getSelectedRow(), 0).toString();
		double amount = Double.parseDouble(am);
		double interest = 0.2 * amount;
		double indv = 0.5 * interest;
		double mgint = 0.03 * indv;
		double realised = indv - mgint;

		double ledgerd = amount + realised;
		double insystem = indv + mgint;
		debitLedger(ledgerd);
		postWithdrawal(ledgerd);
		ManagementCommision mc = new ManagementCommision(rs, stm);
		mc.crMgntAcc("Investor", mgint);
		postInteresrOnly(4131, indv, mnth);
		update(name);
		pc.postCommissionLump(insystem);
		
		JOptionPane.showMessageDialog(frame,
				"An investment Withdrawal of $" + ledgerd + " is validated\nManagement commission of $" + mgint
						+ " earned from $" + indv + " individual interest.\n$" + realised
						+ " is the realised investment commission. The Commission suspense(4161) is credited with $"
						+ indv + "\nMake sure that you give the investor a total amount of $" + ledgerd
						+ "\nTHANK YOU!",
				"Information ", JOptionPane.INFORMATION_MESSAGE);
		affectLedger(4131, insystem);
		d.dispose();

	}

	public void update(String name) {
		String text = "UPDATE  investors SET 	valid = '" + "false" + "'";
		try {
			stm.executeUpdate(text);
		} catch (SQLException ee) {

		}
	}

	public void affectLedger(int accno, double amount) {
		AffectLedger aff = new AffectLedger(rs, stm);
		aff.creditLedgerwithComm(amount, accno);
	}

	public void postInteresrOnly(int accno, double amount, String month) {
		CommissionSusPosting css = new CommissionSusPosting(rs, stm);
		css.postings(accno, amount, month);
		CommissionAccPosting cas = new CommissionAccPosting(rs, stm);
		cas.postings(accno, amount, month);

	}

	public void debitLedger(double interest) {
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
			
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}

	}

	public void postWithdrawal(double amount) {
		String text = "INSERT INTO ledger_with(amount,date,time)VALUES('" + amount + "','"
				+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {

		}
	}
}
