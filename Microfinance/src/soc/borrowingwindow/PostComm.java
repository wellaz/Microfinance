package soc.borrowingwindow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

/**
 *
 * @author Wellington
 */
public class PostComm {
	ResultSet rs;
	Statement stm;

	public PostComm(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public void postCommissionLump(double amount) {
		String text = "INSERT INTO commission_lump(amount,date,time)VALUES('" + amount + "','"
				+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

}
