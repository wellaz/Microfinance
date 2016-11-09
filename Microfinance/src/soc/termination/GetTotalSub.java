package soc.termination;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.DoubleForm;

/**
 *
 * @author Wellington
 */
public class GetTotalSub {

	ResultSet rs;
	Statement stm;
	DoubleForm df;

	public GetTotalSub(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
		df = new DoubleForm();
	}

	public double totalSub(int accno) {
		String text = "SELECT SUM(subscription) FROM subscriptions WHERE member_id = '" + accno + "'";
		double sub = 0;
		try {
			rs = stm.executeQuery(text);
			rs.next();
			sub = rs.getDouble(1);
		} catch (SQLException ee) {

		}
		return df.form(sub);
	}

	public int totalShare(int accno) {
		String text = "SELECT SUM(total_shares) FROM membershare_monthly_count WHERE member_id = '" + accno + "'";
		int sub = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.first())
				sub = rs.getInt(1);
			else
				sub = 0;
		} catch (SQLException ee) {

		}
		return sub;
	}

	public double totalComm(int accno) {
		String text = "SELECT SUM(credit) FROM clubmembers_interest50share WHERE member_id = '" + accno + "'";
		double sub = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				sub = rs.getDouble(1);
			else
				sub = 0;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return df.form(sub);
	}

	public String getNameComm(int accno) {
		String text = "SELECT first_name,last_name FROM members WHERE member_id = '" + accno + "'";
		String sub = null;
		try {
			rs = stm.executeQuery(text);
			rs.next();
			sub = rs.getString(1) + " " + rs.getString(2);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return sub;
	}

	public double totalDebt(int accno) {
		String text = "SELECT SUM(balance) FROM debts WHERE member_id = '" + accno + "'";
		double sub = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				sub = rs.getDouble(1);
			else
				sub = 0;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return df.form(sub);
	}

	public double getSubscriptionsInBonds(int accno) {
		String text = "SELECT SUM(subscription) FROM bonds_subscriptions WHERE member_id = '" + accno + "'";
		double bondssub = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				bondssub = rs.getDouble(1);
			else
				bondssub = 0;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return df.form(bondssub);
	}

	public double getTotalBadDebt() {
		String query = "SELECT SUM(balance) FROM bad_debts";
		double baddebt = 0;
		try {
			rs = stm.executeQuery(query);
			if (rs.next())
				baddebt = rs.getDouble(1);
			else
				baddebt = 0;
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return baddebt;
	}

	public int getTotalShareCount() {
		String query = "SELECT SUM(total_shares) FROM membershare_monthly_count";
		int totalshares = 0;
		try {
			rs = stm.executeQuery(query);
			if (rs.next())
				totalshares = rs.getInt(1);
			else
				totalshares = 0;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return totalshares;
	}

	public double getDeduction(int accno) {
		int individualshares = totalShare(accno);
		int groupshares = getTotalShareCount();
		// System.out.println("Ind Shares = " + individualshares);
		// System.out.println("Group Shares = " + groupshares);
		double totalbaddebt = getTotalBadDebt();
		// System.out.println("Total Debt $" + totalbaddebt);
		double quot = (double) individualshares / groupshares;
		// System.out.println("Quot " + quot);
		double result = quot * totalbaddebt;
		return result;
	}

	/*
	 * public static void main(String[] args) { AccessDbase adbase = new
	 * AccessDbase();
	 * 
	 * adbase.connectionDb(); GetTotalSub g = new GetTotalSub(adbase.rs,
	 * adbase.stm); System.out.println("deduction is: " + g.getDeduction(1002));
	 * }
	 */
}
