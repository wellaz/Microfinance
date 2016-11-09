package soc.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Wellington
 */
public class GetMembers {

	Statement stm;
	ResultSet rs;

	public GetMembers(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public ArrayList<String> members() {
		ArrayList<String> list = new ArrayList<>();
		String query = "SELECT member FROM bank_with WHERE returned = '" + "false" + "'";
		String query1 = "SELECT member FROM bank_with WHERE returned = '" + "false" + "'";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			int rows = rs.getRow();
			if (rows > 0) {
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					list.add(rs.getString(1));
				}
			} else {
				list.add("");
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return list;
	}
}
