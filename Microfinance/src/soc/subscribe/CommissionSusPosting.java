/**
 * 
 */
package soc.subscribe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.DoubleForm;
import soc.helpers.SetDateCreated;

/**
 * @author Wellington
 *
 */
public class CommissionSusPosting {
	ResultSet rs;
	Statement stm;

	DoubleForm df;
	public CommissionSusPosting(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
		df = new DoubleForm();

	}

	public void postings(int accno, double amount, String month) {
		String query0 = "SELECT balance FROM commission_sus";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM commission_sus ";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = df.form(balance + amount);
			String query = "INSERT INTO commission_sus(member_id,debit,credit,balance,month_of,date,time,description)VALUES('"
					+ accno + "','" + 0 + "','" + amount + "','" + newbalance + "','" + month + "','"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + "Comm DD"
					+ new SetDateCreated().setDate() + "')";
			stm.execute(query);

		} catch (SQLException ee) {
			ee.printStackTrace();

		}
	}
	public void debitCommSus(int accno, double amount, String month) {
		String query0 = "SELECT balance FROM commission_sus";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM commission_sus ";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = df.form(balance - amount);
			String query = "INSERT INTO commission_sus(member_id,debit,credit,balance,month_of,date,time,description)VALUES('"
					+ accno + "','" + amount + "','" + 0 + "','" + newbalance + "','" + month + "','"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + "Ex DD"
					+ new SetDateCreated().setDate() + "')";
			stm.execute(query);

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}
	}

}
