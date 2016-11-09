package soc.subscribe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

/**
 *
 * @author Wellington
 */
public class GetShareCount {

	ResultSet rs;
	Statement stm;
	int count = 0;
	int newcount = 0;

	public GetShareCount(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public void countShares(double amount, String month_of, int member_id) {
		count = (int) (amount / 20);
		String query = "INSERT INTO membershare_monthly_count(member_id,month_of,total_shares,date,time,year)VALUES('"
				+ member_id + "','" + month_of + "','" + count + "','" + new SetDateCreated().getDate() + "','"
				+ new SetDateCreated().getTime() + "','" + new SetDateCreated().getYear() + "')";
		try {
			stm.execute(query);

		} catch (SQLException ee) {
			System.out.println(ee.getMessage());
			ee.printStackTrace();

		}

	}

	public void updateShares(int id, String month, double amount) {
		String qr = "SELECT total_shares FROM membershare_monthly_count WHERE member_id = '" + id
				+ "' AND month_of =  '" + month + "' AND year = '" + new SetDateCreated().getYear() + "'";
		try {
			rs = stm.executeQuery(qr);
			rs.next();
			int shares = rs.getInt(1);
			int count = (int) (amount / 20);
			newcount = count + shares;
			String r = "UPDATE membershare_monthly_count SET total_shares = '" + newcount + "' WHERE member_id = '" + id
					+ "' AND month_of =  '" + month + "' AND year = '" + new SetDateCreated().getYear() + "'";
			stm.executeUpdate(r);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int howmanyShares() {
		return count;
	}

	public int topupShares() {
		return newcount;
	}
}
