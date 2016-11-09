package soc.bank;

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

/**
 *
 * @author Wellington
 */
public class DebtorsTable {

	Statement stm;
	ResultSet rs;
	private JTable table;

	public DebtorsTable(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	@SuppressWarnings("serial")
	public JPanel getDebtorsTable() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		String query = "SELECT member,amount,month,date,time,returned FROM bank_with";
		double sum = 0;
		try {
			rs = stm.executeQuery(query);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				Object[][] data = new Object[rows][DebtorsHeader.header.length];
				String query1 = "SELECT member,amount,month,date,time,returned FROM bank_with";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					data[i][0] = rs.getString(1).toUpperCase();
					double amm = rs.getDouble(2);
					data[i][1] = amm;
					data[i][2] = rs.getString(3);
					data[i][3] = rs.getString(4);
					data[i][4] = rs.getString(5);
					data[i][5] = rs.getString(6).toUpperCase();
					sum += amm;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, DebtorsHeader.header);
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
				TableRenderer.setJTableColumnsWidth(getTable(), 480, 20, 20, 20, 20, 10, 10);
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

				});

				JLabel label = new JLabel("Total Withdrawal Amount $" + new DoubleForm().form(sum) + ".  "
						+ new TableRecordsNarration().narrate(table));
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 19));
				panel.add(label, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

			} else {
				// panel.add(new JLabel("No Data"), BorderLayout.CENTER);

				setTable(new JTable(new String[][] { { "No Data", "No Data" }, { "No Data", "No Data" } },
						new String[] { "Column", "Header" }));
				panel.add(getTable(), BorderLayout.CENTER);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return panel;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
}
