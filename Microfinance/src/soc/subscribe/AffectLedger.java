package soc.subscribe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

public class AffectLedger {
	ResultSet rs;
	Statement stm;
	int count = 0;

	public AffectLedger(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void creditLedger(double amount, int acc) {
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
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + 0 + "','"
					+ amount + "','" + newbalance + "','" + "Monthly SUB Acc" + acc + "')";
			stm.execute(text);

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void debitLedger(double interest, int acc) {
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
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + interest + "','"
					+ 0 + "','" + newbalance + "','" + "Debit Acc " + acc + "')";
			stm.execute(text);

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void creditLedgerwithComm(double amount, int acc) {
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
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + 0 + "','"
					+ amount + "','" + newbalance + "','" + "Comm Transf" + acc + "')";
			stm.execute(text);

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void creditLedgerWithSubTopup(double amount, int acc) {
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
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + 0 + "','"
					+ amount + "','" + newbalance + "','" + "Monthly SUB Topup Acc" + acc + "')";
			stm.execute(text);

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void termination(String acc, double interest) {
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
			String text = "INSERT INTO ledger(date,time,debit,credit,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + interest + "','"
					+ 0 + "','" + newbalance + "','" + "Terminating Acc " + acc + "')";
			stm.execute(text);

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
