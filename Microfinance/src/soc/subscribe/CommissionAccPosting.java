package soc.subscribe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

/**
 * @author Wellington
 *
 */

public class CommissionAccPosting {
	ResultSet rs;
	Statement stm;

	public CommissionAccPosting(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public void postings(int accno, double amount, String month) {
		String year = new SetDateCreated().getYear();
		String query0 = "SELECT * FROM commission_acc WHERE month_of = '" + month + "' AND year = '" + year + "'";
		try {
			rs = stm.executeQuery(query0);
			if (!rs.next()) {
				String query1 = "INSERT INTO commission_acc (month_of,amount,year)VALUES('" + month + "','" + amount
						+ "','" + year + "')";
				stm.execute(query1);
			} else {
				double newbalance = amount + rs.getDouble(2);
				String query3 = "UPDATE commission_acc SET amount = '" + newbalance + "' WHERE month_of = '" + month
						+ "' AND year ='" + year + "' ";
				stm.executeUpdate(query3);

			}

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}
	}

	public void debitComm(int accno, double amount, String month) {
		String year = new SetDateCreated().getYear();
		String query0 = "SELECT * FROM commission_acc WHERE month_of = '" + month + "' AND year = '" + year + "'";
		try {
			rs = stm.executeQuery(query0);
			if (!rs.next()) {
				double newbalance = 0 - amount;
				String query1 = "INSERT INTO commission_acc (month_of,amount,year)VALUES('" + month + "','" + newbalance
						+ "','" + year + "')";
				stm.execute(query1);
			} else {
				double newbalance = rs.getDouble(2) - amount;
				String query3 = "UPDATE commission_acc SET amount = '" + newbalance + "' WHERE month_of = '" + month
						+ "' AND year ='" + year + "' ";
				stm.executeUpdate(query3);

			}

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}
	}

}
