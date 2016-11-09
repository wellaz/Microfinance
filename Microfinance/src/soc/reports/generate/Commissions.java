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

public class Commissions {
	ResultSet rs, rs1;
	Statement stm, stmt;

	private JTable table;
	double sum = 0;
	DoubleForm df;

	public Commissions(ResultSet rs, Statement stm, ResultSet rs1, Statement stmt) {
		this.rs = rs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	// this method is used to extract all commissions that were earned by a
	// given
	// member
	@SuppressWarnings("serial")
	public JPanel midPanel(String acc) {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());

		String rowsQuerry = "SELECT credit,month_of,date,time FROM clubmembers_interest50share WHERE member_id  = '"
				+ acc + "'";
		String previewQuerry = "SELECT credit,month_of,date,time FROM clubmembers_interest50share WHERE member_id  = '"
				+ acc + "'";
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][CommHeader.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					double amounts = rs1.getDouble(1);
					data[i][0] = amounts;

					data[i][1] = rs1.getString(2);
					data[i][2] = rs1.getString(3) + " " + rs1.getString(4);

					sum += amounts;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, CommHeader.header);
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
				TableRenderer.setJTableColumnsWidth(getTable(), 480, 40, 40, 20);
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
					CommPDF gnpdf = new CommPDF(getTable());
					CommPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				JLabel label = new JLabel(
						"Total Commission $" + df.form(sum) + ".  " + new TableRecordsNarration().narrate(getTable()));
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 19));
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

	public double getTotalCommission() {
		return sum;
	}

	public double totalCommission(String acc) {
		String rowsQuerry = "SELECT SUM(credit) FROM clubmembers_interest50share WHERE member_id  = '" + acc + "'";
		double total = 0;
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.next()) {
				total = rs.getDouble(1);
			} else {
				total = 0.00;
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return total;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTable createCommissionsTable(String acc) {
		String rowsQuerry = "SELECT credit,month_of,date,time FROM clubmembers_interest50share WHERE member_id  = '"
				+ acc + "'";
		String previewQuerry = "SELECT credit,month_of,date,time FROM clubmembers_interest50share WHERE member_id  = '"
				+ acc + "'";
		JTable table = null;
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][CommHeader.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					double amounts = rs1.getDouble(1);
					data[i][0] = amounts;

					data[i][1] = rs1.getString(2);
					data[i][2] = rs1.getString(3) + " " + rs1.getString(4);

					// sum += amounts;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, CommHeader.header);
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
