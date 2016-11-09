package soc.borrowers.limit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class UpdateLimitData {
	Statement stm;
	ResultSet rs;

	public UpdateLimitData(Statement stm, ResultSet rs) {
		this.stm = stm;
		this.rs = rs;
	}

	public void updateData(String date, String time, double amount) {
		/*
		 * String text = "UPDATE borrowers_limit SET date = '" + date +
		 * "',time = '" + time + "',limit_amount = '" + amount + "'";
		 */
		String text = "INSERT INTO borrowers_limit(date,time,limit_amount)VALUES('" + date + "','" + time + "','"
				+ amount + "')";
		try {
			stm.execute(text);
			storeNewLimitValue(amount);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void storeNewLimitValue(double amount) {
		StoreLimitValue store = new StoreLimitValue(stm, rs);
		store.storeLimitValue().add(amount);
	}
}
