package soc.reports.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRecordsNarration;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

public class Debts {
	ResultSet rs, rs1;
	Statement stm, stmt;

	private JTable table;
	double b = 0;
	DoubleForm df;

	public Debts(ResultSet rs, Statement stm, ResultSet rs1, Statement stmt) {
		this.rs = rs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	@SuppressWarnings("serial")
	public JPanel midPanel(String acc) {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		double sum = 0, c = 0;
		String rowsQuerry = "SELECT * FROM debts WHERE member_id  = '" + acc + "'";
		String previewQuerry = "SELECT member_id,amount,due,paid,balance,commission,date,time,month_of FROM debts WHERE member_id  = '"
				+ acc + "' ORDER BY balance ASC";
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][DebtsHeader.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					int account = rs1.getInt(1);
					double amounts = rs1.getDouble(2);
					double balance = rs1.getDouble(5);
					double comm = rs1.getDouble(6);

					data[i][0] = amounts;
					data[i][1] = rs1.getString(3);
					data[i][2] = rs1.getDouble(4);
					data[i][3] = balance;
					data[i][5] = comm;
					String date = rs1.getString(7);
					String time = rs1.getString(8);
					data[i][6] = date;
					data[i][7] = time;
					data[i][8] = rs1.getString(9);

					String qr = "SELECT name FROM beneficiary WHERE member_id = '" + account + "' AND date = '" + date
							+ "' AND time =  '" + time + "'";
					rs = stm.executeQuery(qr);
					String ben = null;
					if (rs.next())
						ben = rs.getString(1);
					else
						ben = "-";
					data[i][4] = ben;

					sum += amounts;
					c += comm;
					b += balance;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, DebtsHeader.header);
				setTable(new JTable(model) {
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
				});
				TableRenderer.setJTableColumnsWidth(getTable(), 480, 10, 10, 10, 10, 10, 10, 10, 15, 15);
				getTable().setRowHeight(30);
				getTable().setAutoCreateRowSorter(true);

				new TableColumnResizer(getTable());
				new TableRowResizer(getTable());
				getTable().setShowGrid(true);

				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(getTable());
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new JPanel(new FlowLayout());
				lowerpanel.add(new JLabel());
				JButton generate = new JButton("Download PDF File");
				lowerpanel.add(generate);
				generate.addActionListener((ActionEvent event) -> {
					DebtsPDF gnpdf = new DebtsPDF(getTable());
					DebtsPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				JLabel label = new JLabel("Total Amount (Borrowed) : $" + df.form(sum) + " Outstanding balance $"
						+ df.form(b) + ". Outstanding Commission $" + df.form(c) + ". "
						+ new TableRecordsNarration().narrate(getTable()));
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 15));
				panel.add(label, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

			} else {
				// panel.add(new JLabel("No Data"), BorderLayout.CENTER);
				setTable(new JTable(new String[][] { { "null", "null" }, { "null", "null" } },
						new String[] { "Column", "Header" }));
				panel.add(getTable(), BorderLayout.CENTER);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}

		return panel;
	}

	public double getOutstandingBalance() {
		return b;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public double totalDebt(String acc) {
		String previewQuerry = "SELECT SUM(balance) FROM debts WHERE member_id  = '" + acc + "' ORDER BY balance ASC";
		double balance = 0;
		try {
			rs = stm.executeQuery(previewQuerry);
			if (rs.next()) {
				balance = rs.getDouble(1);
			} else {
				balance = 0.00;
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return balance;
	}

	public JTable createDebtsTable(String acc) {
		String rowsQuerry = "SELECT * FROM debts WHERE member_id  = '" + acc + "'";
		String previewQuerry = "SELECT member_id,amount,due,paid,balance,commission,date,time,month_of FROM debts WHERE member_id  = '"
				+ acc + "' ORDER BY balance ASC";
		JTable table = null;
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][DebtsHeader.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					int account = rs1.getInt(1);
					double amounts = rs1.getDouble(2);
					double balance = rs1.getDouble(5);
					double comm = rs1.getDouble(6);

					data[i][0] = amounts;
					data[i][1] = rs1.getString(3);
					data[i][2] = rs1.getDouble(4);
					data[i][3] = balance;
					data[i][5] = comm;
					String date = rs1.getString(7);
					String time = rs1.getString(8);
					data[i][6] = date;
					data[i][7] = time;
					data[i][8] = rs1.getString(9);

					String qr = "SELECT name FROM beneficiary WHERE member_id = '" + account + "' AND date = '" + date
							+ "' AND time =  '" + time + "'";
					rs = stm.executeQuery(qr);
					String ben = null;
					if (rs.next())
						ben = rs.getString(1);
					else
						ben = "-";
					data[i][4] = ben;

					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, DebtsHeader.header);
				table = new JTable(model);

			} else {
				table = new JTable(new String[][] { { "00", "00" }, { "00", "00" } },
						new String[] { "Column", "Header" });
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return table;
	}
}
