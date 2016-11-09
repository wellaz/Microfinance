package soc.ledger.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import soc.helpers.DoubleForm;
/**
 * @author Wellington
 *
 */
public class SearchLedger {

	ResultSet rs;
	Statement stm;
	JFrame frame;
	DoubleForm df;

	public SearchLedger(ResultSet rs, Statement stm, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		df = new DoubleForm();
	}

	public void search(String date, String date1, JTextArea display) {
		String text = "SELECT * FROM ledger WHERE date >= '" + date + "' AND date <='" + date1 + "' ORDER BY date,time ASC";
		try {
			rs = stm.executeQuery(text);
			double balance = 0;
			if (!rs.next()) {
				JOptionPane.showMessageDialog(frame, "Account Number  IS IDLE!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				do {
					String datt = rs.getString(1) + " " + rs.getString(2);
					double debit = rs.getDouble(3);
					double credit = rs.getDouble(4);
					
					balance = balance+credit-debit;

					String details = rs.getString(6);
					String d = null;
					String c = null;
					String b = null;

					if (debit == 0)
						d = "";
					else
						d = "$" + debit;
					if (credit == 0)
						c = "";
					else
						c = "$" + credit;
					if (balance == 0)
						b = "";
					else
						b = "$" + df.form(balance);

					String trans = "Date " + datt + "\n" + details + "\t\t" + d + "\t\t" + c + "\t\t" + b + "\n\n\n";
					display.append(trans);

				} while (rs.next());
			}

		} catch (SQLException ee) {

		}

	}
}
