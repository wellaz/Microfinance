package soc.borrowers.limit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class GetLimitValue {
	Statement stm;
	ResultSet rs;

	public GetLimitValue(Statement stm, ResultSet rs) {
		this.stm = stm;
		this.rs = rs;
	}

	public double getLimitValue() {
		double val = 0;

		String text = "SELECT limit_amount FROM borrowers_limit ";
		try {
			rs = stm.executeQuery(text);
			if (!rs.last())
				val = 0;
			else {
				val = rs.getDouble(1);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return val;
	}
}
