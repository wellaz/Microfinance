package soc.borrowingwindow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class BorrowersOutStandingBalance {
	Statement stm;
	ResultSet rs;

	public BorrowersOutStandingBalance(Statement stm, ResultSet rs) {
		this.stm = stm;
		this.rs = rs;
	}

	public double getTotalOutstandingBalance(int accno) {
		double sum = 0;
		String query = "SELECT balance FROM debts WHERE member_id = '" + accno + "'";
		try {
			rs = stm.executeQuery(query);
			if (!rs.next()) {
				sum = 0;
			} else {
				do {
					double s = rs.getDouble(1);
					if (s > 0)
						sum += s;
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return sum;
	}
}
