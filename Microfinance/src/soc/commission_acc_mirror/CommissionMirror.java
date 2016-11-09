package soc.commission_acc_mirror;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class CommissionMirror {
	ResultSet rs, rs1;
	Statement stm, stmt;

	public CommissionMirror(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	// this is a class method that copies backup data, just in case we lose
	// commissionfor a given month.
	public void mirrorCopier() {
		String text = "SELECT * FROM commission_acc";
		try {
			rs = stm.executeQuery(text);
			if (rs.next()) {
				do {
					String month = rs.getString(1);
					double amount = rs.getDouble(2);
					int year = rs.getInt(3);

					String selectQuery = "SELECT * FROM commission_acc_backup WHERE month_of = '" + month + "'";
					rs1 = stmt.executeQuery(selectQuery);
					if (rs1.next()) {
						String query = "UPDATE commission_acc_backup SET amount = '" + amount + "' WHERE month_of = '"
								+ month + "' AND year = '" + year + "'";
						stmt.executeUpdate(query);
					} else {
						String insertQuery = "INSERT INTO commission_acc_backup(month_of,amount,year)VALUES('" + month
								+ "','" + amount + "','" + year + "') ";
						stmt.execute(insertQuery);
					}
				} while (rs.next());
				//System.out.println("Done Copying");
			} else {

			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
