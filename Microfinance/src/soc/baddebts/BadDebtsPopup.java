package soc.baddebts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import soc.helpers.SetDateCreated;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class BadDebtsPopup extends JPopupMenu implements ActionListener {
	public JMenuItem open, viewmark, viewperf, prop;
	JTable accJTable;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	// private JTable table;

	public BadDebtsPopup(ResultSet rs, Statement stm, JFrame frame, JTable accJTable) {
		init();
		this.accJTable = accJTable;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public final void init() {
		open = new JMenuItem("Open");
		open.setEnabled(false);
		viewmark = new JMenuItem("Add To Bad Debts");
		viewperf = new JMenuItem("Properties");
		viewperf.setEnabled(false);
		prop = new JMenuItem("Others...");
		prop.setEnabled(false);

		this.add(open);
		open.setEnabled(false);
		this.addSeparator();
		this.add(viewmark);
		viewmark.addActionListener(this);
		this.add(viewperf);
		viewperf.addActionListener(this);
		this.addSeparator();
		this.add(prop);
		prop.addActionListener(this);
	}

	public void postBadDebt(String acc, String amount, String due, String paid, String balance, String outcomm,
			String postedon, String time, String year) {

		String query = "INSERT INTO bad_debts(member_id,amount,due,paid,balance,outcomm, date,time,year) VALUES('" + acc
				+ "','" + amount + "','" + due + "','" + paid + "','" + balance + "','" + outcomm + "','" + postedon
				+ "','" + time + "','" + year + "')";

		String updateQuery = "UPDATE debts SET bad_debt = '" + "yes" + "' WHERE member_id = '" + acc + "'AND amount = '"
				+ amount + "' AND due ='" + due + "' AND date = '" + postedon + "' AND time = '" + time + "'";

		try {
			stm.execute(query);
			stm.executeUpdate(updateQuery);
			DefaultTableModel model = (DefaultTableModel) accJTable.getModel();
			int selectedRow = accJTable.getSelectedRow();
			model.removeRow(selectedRow);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewmark) {
			String acc = accJTable.getValueAt(accJTable.getSelectedRow(), 1).toString();
			String amount = accJTable.getValueAt(accJTable.getSelectedRow(), 4).toString();
			String due = accJTable.getValueAt(accJTable.getSelectedRow(), 5).toString();
			String paid = accJTable.getValueAt(accJTable.getSelectedRow(), 6).toString();
			String balance = accJTable.getValueAt(accJTable.getSelectedRow(), 7).toString();
			String outcomm = accJTable.getValueAt(accJTable.getSelectedRow(), 8).toString();
			String postedon = accJTable.getValueAt(accJTable.getSelectedRow(), 9).toString();
			String time = accJTable.getValueAt(accJTable.getSelectedRow(), 10).toString();
			String year = new SetDateCreated().getYear();

			postBadDebt(acc, amount, due, paid, balance, outcomm, postedon, time, year);
		}
	}
}
