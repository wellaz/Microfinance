package soc.commission.swip;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.helpers.DoubleForm;
import soc.helpers.SetDateCreated;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.subscribe.ActivityAccPosting;

/**
 * @author Wellington
 *
 */
public class MembersCommissionSwip {
	ResultSet rs, rs1;
	Statement stm, stmt;
	ArrayList<Integer> acc_arr = new ArrayList<>();
	ArrayList<Integer> share_arr = new ArrayList<>();
	ArrayList<Double> comm_arr = new ArrayList<>();
	ArrayList<String> names_arr = new ArrayList<>();
	
	private JTable table;
	DoubleForm df;
	Check check;

	public MembersCommissionSwip(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.rs = rs;
		this.stmt = stmt;
		this.rs1 = rs1;
		df = new DoubleForm();
		check = new Check(rs, stm);
	}

	public void processCommission(String date) {
		double commissionbal = new GetCommissionBalance(rs, stm).getBalance(date);
		ActivityAccPosting acp = new ActivityAccPosting(rs, stm);

		MemberShare m = new MemberShare(rs, stm);

		int total = getTotalShares(date);
		if (total > 0) {
			for (String d : check.ids(date)) {
				String query = "SELECT total_shares FROM membershare_monthly_count WHERE member_id = '" + d
						+ "' AND month_of = '" + date + "'";
				try {
					rs1 = stmt.executeQuery(query);
					if (rs1.next()) {
						int share = rs1.getInt(1);
						acc_arr.add(Integer.parseInt(d));
						share_arr.add(share);

						double memcommission = (commissionbal * share) / total;
						comm_arr.add(df.form(memcommission));
						acp.commPosting(Integer.parseInt(d), df.form(memcommission));

						m.postMemberComm(Integer.parseInt(d), df.form(memcommission), date);
					}
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			}
			double newbalance = new GetCommissionBalance(rs, stm).getBalance(date) - commissionbal;
			String newtext = "UPDATE commission_acc SET amount = '" + newbalance + "' WHERE month_of = '" + date
					+ "' AND year = '" + new SetDateCreated().getYear() + "' ";
			try {
				stm.executeUpdate(newtext);

				String query11 = "SELECT balance FROM commission_sus ";
				rs = stm.executeQuery(query11);
				rs.last();
				double susbalance = rs.getDouble(1) - commissionbal;
				String des = "Members Comm Swip(SUM)";

				String query111 = "INSERT INTO commission_sus(member_id,debit,credit,balance,month_of,date,time,description)VALUE('"
						+ 4181 + "','" + commissionbal + "','" + 0 + "','" + susbalance + "','" + date + "','"
						+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + des + "')";
				stm.execute(query111);
				// AffectLedger af = new AffectLedger(rs, stm);
				// af.creditLedgerwithComm(commissionbal, 4161);
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			createTable(date);
		} else {

		}

	}

	@SuppressWarnings("serial")
	public void createTable(String date) {
		try {
			for (int id : acc_arr) {
				String query11 = "SELECT first_name,last_name FROM members WHERE member_id = '" + id + "' ";
				rs = stm.executeQuery(query11);
				while (rs.next()) {
					names_arr.add(rs.getString(1) + " " + rs.getString(2));
				}
			}
			int rows = names_arr.size();
			Object[][] data = new Object[rows][SwipHeader.header.length];
			int i = 0;
			while (i < rows) {
				data[i][0] = names_arr.get(i);
				data[i][1] = acc_arr.get(i);
				data[i][2] = comm_arr.get(i);
				i++;
			}
			DefaultTableModel model = new DefaultTableModel(data,SwipHeader.header);
			table = new JTable(model) {
				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
					Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
					if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
						comp.setBackground(new Color(235, 235, 235));
					} else {
						comp.setBackground(new Color(204, 204, 204));
					}
					if (isCellSelected(Index_row, Index_col)) {
						comp.setBackground(new Color(7, 66, 60));
					}
					return comp;
				}

				public String getToolTipText(MouseEvent e) {
					String tip = null;
					java.awt.Point p = e.getPoint();
					int rowIndex = rowAtPoint(p);
					int colIndex = columnAtPoint(p);

					try {
						tip = getValueAt(rowIndex, colIndex).toString();
					} catch (RuntimeException e1) {
						// catch null pointer exception if mouse is over an
						// empty line
					}

					return tip;
				}

				@Override
				public boolean isCellEditable(int rowIndex, int colIndex) {
					return false;
				}

			};
			TableRenderer.setJTableColumnsWidth(table, 480, 40, 40, 20);
			table.setRowHeight(30);
			table.setAutoCreateRowSorter(true);

			new TableColumnResizer(table);
			new TableRowResizer(table);
			table.setShowGrid(true);

			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(table);

			MemberCommPDF mpdf = new MemberCommPDF(rs, stm, table, date);
			MemberCommPDF.Worker w = mpdf.new Worker();
			w.execute();
		} catch (SQLException aa) {
			aa.printStackTrace();
		}
	}

	public int getTotalShares(String month) {
		int sum = 0;
		for (String f : check.ids(month)) {
			String query = "SELECT total_shares FROM membershare_monthly_count WHERE member_id = '" + f
					+ "' AND month_of = '" + month + "' AND year = '" + new SetDateCreated().getYear() + "'";
			try {
				rs = stm.executeQuery(query);
				if (rs.next())
					sum += rs.getInt(1);
			} catch (SQLException ee) {
				ee.printStackTrace();
			}

		}
		return sum;
	}

}
