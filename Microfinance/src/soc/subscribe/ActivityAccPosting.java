package soc.subscribe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

/**
 * @author Wellington
 *
 */
public class ActivityAccPosting {

	ResultSet rs;
	Statement stm;

	public ActivityAccPosting(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public void postings(int accno, double amount) {
		String query0 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "'";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = balance + amount;
			String query = "INSERT INTO activity_acc(member_id,debit,credit,balance,date,time,description)VALUES('"
					+ accno + "','" + 0 + "','" + amount + "','" + newbalance + "','" + new SetDateCreated().getDate()
					+ "','" + new SetDateCreated().getTime() + "','" + "SUB DD" + new SetDateCreated().setDate() + "')";
			stm.execute(query);

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}
	}

	public void interestPosting(int accno, double interest) {
		String query0 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = balance + interest;
			String query = "INSERT INTO activity_acc(member_id,debit,credit,balance,date,time,description)VALUES('"
					+ accno + "','" + 0 + "','" + interest + "','" + newbalance + "','" + new SetDateCreated().getDate()
					+ "','" + new SetDateCreated().getTime() + "','" + "20% INT DD " + new SetDateCreated().setDate()
					+ "')";
			stm.execute(query);

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}

	}

	public void commPosting(int accno, double interest) {
		String query0 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = balance + interest;
			String time =  new SetDateCreated().getTime();
			String query = "INSERT INTO activity_acc(member_id,debit,credit,balance,date,time,description)VALUES('"
					+ accno + "','" + 0 + "','" + interest + "','" + newbalance + "','" + new SetDateCreated().getDate()
					+ "','" +time+ "','" + "Comm DD " + new SetDateCreated().setDate()
					+ "')";
			stm.execute(query);

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}
	}

	public void subIncPostings(int accno, double amount) {
		String query0 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "' ";
		try {
			rs = stm.executeQuery(query0);
			rs.last();
			int rows = rs.getRow();
			double balance = 0;

			if (rows == 0)
				balance = 0;
			else {
				String query1 = "SELECT balance FROM activity_acc WHERE member_id = '" + accno + "'";
				rs = stm.executeQuery(query1);
				rs.last();
				balance = rs.getDouble(1);
			}

			double newbalance = balance + amount;
			String query = "INSERT INTO activity_acc(member_id,debit,credit,balance,date,time,description)VALUES('"
					+ accno + "','" + 0 + "','" + amount + "','" + newbalance + "','" + new SetDateCreated().getDate()
					+ "','" + new SetDateCreated().getTime() + "','" + "SUB Incr DD" + new SetDateCreated().setDate()
					+ "')";
			stm.execute(query);

		} catch (SQLException ee) {
			ee.printStackTrace();

		}
	}

}
