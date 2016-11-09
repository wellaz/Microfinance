package soc.commission.swip;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

/**
 * @author Wellington
 *
 */
public class GetCommissionBalance {
	ResultSet rs;
	Statement stm;

	public GetCommissionBalance(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public double getBalance(String date) {
		String text = "SELECT amount FROM commission_acc WHERE month_of = '" + date + "' AND year = '"
				+ new SetDateCreated().getYear() + "'";
		double balance = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				balance = rs.getDouble(1);
			else
				balance = 0;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return balance;
	}
}
