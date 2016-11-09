package soc.reconciliation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import soc.helpers.DoubleForm;

public class GetLedgerBalance {
	ResultSet rs;
	Statement stm;
	DoubleForm df;

	public GetLedgerBalance(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
		df = new DoubleForm();
	}

	public double getLedgerBalance(String from) {
		String text = "SELECT debit,credit FROM ledger WHERE date  < '" + from + "'";
		double balance = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				do {
					balance = balance - rs.getDouble(1) + rs.getDouble(2);
				} while (rs.next());
			else
				balance = 0;
		} catch (SQLException ee) {
ee.printStackTrace();
		}
		return df.form(balance);
	}

}
