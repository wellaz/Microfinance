package soc.commission.swip;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.SetDateCreated;

public class MemberShare {
	ResultSet rs;
	Statement stm;

	public MemberShare(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public void postMemberComm(int acc, double amount, String month) {
		String text = "INSERT INTO clubmembers_interest50share(member_id,credit,month_of,date,time,description)VALUES('"
				+ acc + "','" + amount + "','" + month + "','" + new SetDateCreated().getDate() + "','"
				+ new SetDateCreated().getTime() + "','" + "Commission Sweep" + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

}
