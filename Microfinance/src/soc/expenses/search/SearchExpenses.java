package soc.expenses.search;

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
public class SearchExpenses {
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public SearchExpenses(ResultSet rs, Statement stm, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}
	
	public void search(String date, String date1, JTextArea display) {
		String text = "SELECT * FROM expenses_acc WHERE date BETWEEN '" + date + "' AND '" + date1 + "' ORDER BY date,time ASC";
		try {
			rs = stm.executeQuery(text);
			if (!rs.next()) {
				JOptionPane.showMessageDialog(frame, "Account Number  IS IDLE!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				do {
					String datt = rs.getString(4)+" "+rs.getString(5) + " " + rs.getString(6);
					double debit = rs.getDouble(2);

					String mo = " Month Of " + rs.getString(3);
					String details = " ref: " + rs.getString(1);
					String d = "$" + debit;

					String trans = "Dated " + datt + "\n" + details + "\n" + mo + "\t\t\t\t" + d + "\n\n\n";
					display.append(trans);

				} while (rs.next());
			}

		} catch (SQLException ee) {

		}

	}

}
