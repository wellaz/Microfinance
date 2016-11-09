package soc.subscriptions.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * @author Wellington
 *
 */
public class SearchSubscriptions {

	ResultSet rs;
	Statement stm;
	JFrame frame;

	public SearchSubscriptions(ResultSet rs, Statement stm, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public void search(String date, String date1, JTextArea display) {
		String text = "SELECT * FROM subscriptions WHERE date BETWEEN '" + date + "' AND '" + date1 + "' ORDER BY date,time ASC";
		double balance = 0;
		try {
			rs = stm.executeQuery(text);
			if (!rs.next()) {
				JOptionPane.showMessageDialog(frame, "Account Number  IS IDLE!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				do {
					String datt = rs.getString(4) + " " + rs.getString(5);
					double debit = rs.getDouble(2);
					balance+=debit;

					String details = "Member ID:" + rs.getInt(1);
					String mo = " Month Of " + rs.getString(3);
					String d = "$" + debit;

					String trans = "Dated " + datt + "\n" + details + "\n" + mo + "\t\t\t\t" + d + "\t\t"+balance+"\n\n\n";
					display.append(trans);

				} while (rs.next());
			}

		} catch (SQLException ee) {

		}

	}

}
