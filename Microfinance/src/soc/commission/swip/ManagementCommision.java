package soc.commission.swip;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.DoubleForm;
import soc.helpers.RandomNumberGenerator;
import soc.helpers.SetDateCreated;

/**
 * @author Wellington
 *
 */
public class ManagementCommision {

	ResultSet rs;
	Statement stm;
	DoubleForm df;

	public ManagementCommision(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
		df = new DoubleForm();
	}

	public void postMgntAcc(String date) {
		try {

			double balance = new GetCommissionBalance(rs, stm).getBalance(date);
			double mgntcomm = df.form(0.03 * balance);
			double newbalance = df.form(balance - mgntcomm);
			String newtext = "UPDATE commission_acc SET amount = '" + newbalance + "' WHERE month_of = '" + date
					+ "' AND year = '" + new SetDateCreated().getYear() + "' ";

			stm.executeUpdate(newtext);

			String query1 = "SELECT balance FROM commission_sus ";
			rs = stm.executeQuery(query1);
			if (rs.last()) {
				double susbalance = df.form(rs.getDouble(1) - mgntcomm);
				String des = "Mgnt Comm ";

				String query = "INSERT INTO commission_sus(member_id,debit,credit,balance,month_of,date,time,description)VALUE('"
						+ 4181 + "','" + mgntcomm + "','" + 0 + "','" + susbalance + "','" + date + "','"
						+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + des + "')";
				stm.execute(query);
				crMgntAcc(date, mgntcomm);
			} else {

			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void crMgntAcc(String acc, double balance) {
		try {

			String query1 = "SELECT balance FROM management_acc ";
			String des = "Mgnt Comm from Acc " + acc;
			rs = stm.executeQuery(query1);
			if (rs.last()) {
				double susbalance = df.form(rs.getDouble(1) + balance);
				String query = "INSERT INTO management_acc(ref,debit,credit,balance,date,time,year,description)VALUE('"
						+ "CR" + new RandomNumberGenerator().generateRandomNumber() + "','" + 0 + "','" + balance
						+ "','" + susbalance + "','" + new SetDateCreated().getDate() + "','"
						+ new SetDateCreated().getTime() + "','" + new SetDateCreated().getYear() + "','" + des + "')";
				stm.execute(query);
			} else {
				String query = "INSERT INTO management_acc(ref,debit,credit,balance,date,time,year,description)VALUE('"
						+ "CR" + new RandomNumberGenerator().generateRandomNumber() + "','" + 0 + "','" + balance
						+ "','" + balance + "','" + new SetDateCreated().getDate() + "','"
						+ new SetDateCreated().getTime() + "','" + new SetDateCreated().getYear() + "','" + des + "')";
				stm.execute(query);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

}
