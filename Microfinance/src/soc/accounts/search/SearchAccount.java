package soc.accounts.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import soc.helpers.DoubleForm;
import soc.helpers.GetAccountNumbers;

/**
 *
 * @author Wellington
 */
public class SearchAccount {

	ResultSet rs;
	Statement stm;
	JFrame frame;
	DoubleForm df;

	public SearchAccount(ResultSet rs, Statement stm, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		df = new DoubleForm();

	}

	public boolean isValidAcc(String acc) {
		GetAccountNumbers g = new GetAccountNumbers(rs, stm);
		List<String> imgTypes = null;
		int arrsize = g.getAccounts().size();
		String[] dat = new String[arrsize];
		for (int i = 0; i < arrsize; i++)
			dat[i] = Integer.toString(g.getAccounts().get(i));

		imgTypes = Arrays.asList(dat);
		return imgTypes.stream().anyMatch(t -> acc.equals(t));
	}

	public void search(String acc, String date, String date1, JTextArea display, JLabel tellername) {
		if (isValidAcc(acc)) {
			String text = "SELECT * FROM activity_acc WHERE member_id = '" + acc + "' AND date BETWEEN '" + date
					+ "' AND '" + date1 + "' ORDER BY date,time ASC";
			try {
				rs = stm.executeQuery(text);
				double balance = 0;
				if (!rs.next()) {
					JOptionPane.showMessageDialog(frame, "Account Number " + acc + " IS IDLE!", "Message",
							JOptionPane.WARNING_MESSAGE);

				} else {
					do {
						String datt = rs.getString(5) + " " + rs.getString(6);
						double debit = rs.getDouble(2);
						double credit = rs.getDouble(3);
						String account = "" + rs.getInt(1);

						balance = balance + credit - debit;

						String details = rs.getString(7);
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

						String trans = "Date " + datt + "\n" + details + "\n\tAcc " + account + "\t\t" + d + "\t\t" + c
								+ "\t\t" + b + "\n\n\n";
						display.append(trans);

					} while (rs.next());
					String textxt = "SELECT first_name,last_name FROM members WHERE member_id = '" + acc + "'";
					rs = stm.executeQuery(textxt);
					rs.next();
					tellername.setText(rs.getString(1) + " " + rs.getString(2));

				}

			} catch (SQLException ee) {
				ee.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(null, "Account Number " + acc + " DO NOT HONOR!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
