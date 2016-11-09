package soc.reports.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.Font;

import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.SetDateCreated;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRecordsNarration;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

public class Subscriptions {
	ResultSet rs;
	Statement stm;

	private JTable table;
	private JButton go;
	double sum = 0;
	DoubleForm df;

	public Subscriptions(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
		df = new DoubleForm();
	}

	@SuppressWarnings("serial")
	public JPanel getSubscriptions(String acc) {
		JPanel mainp = new TranslucentJPanel(Color.BLUE);
		mainp.setLayout(new BorderLayout());

		String text = "SELECT subscription,month_of,date,time FROM subscriptions WHERE member_id = '" + acc
				+ "' AND year = '" + new SetDateCreated().getYear() + "'";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				Object[][] data = new Object[rows][SubHeader.header.length];
				String text1 = "SELECT subscription,month_of,date,time FROM subscriptions WHERE member_id = '" + acc
						+ "' AND year = '" + new SetDateCreated().getYear() + "'";
				rs = stm.executeQuery(text1);
				while (rs.next()) {
					double sub = rs.getDouble(1);
					data[i][0] = sub;
					data[i][1] = rs.getString(2);
					data[i][2] = rs.getString(3) + " " + rs.getString(4);
					sum += sub;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, SubHeader.header);
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
							e1.printStackTrace(System.err);
							// catch null pointer exception if mouse is over
							// an
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
				mainp.add(scroll, BorderLayout.CENTER);
				go = new JButton("Download PDF");
				go.addActionListener((event) -> {
					SubPDF d = new SubPDF(getTable());
					SubPDF.Worker w = d.new Worker();
					w.execute();
				});
				JPanel canp = new TranslucentJPanel(Color.BLUE);
				canp.setLayout(new FlowLayout());
				canp.add(go, SwingConstants.CENTER);

				mainp.add(canp, BorderLayout.SOUTH);

				JLabel lbl = new JLabel("Subscriptions   Sum  $" + df.form(sum) + ". Total Shares :" + (int) (sum / 20)
						+ ". " + new TableRecordsNarration().narrate(getTable()));
				lbl.setFont(new java.awt.Font("", Font.BOLD, 19));
				lbl.setForeground(Color.WHITE);
				mainp.add(lbl, BorderLayout.NORTH);

			} else {
				// mainp.add(new JLabel("No Data"));
				setTable(new JTable(new String[][] { { "null", "null" }, { "null", "null" } },
						new String[] { "Column", "Header" }));
				mainp.add(getTable(), BorderLayout.CENTER);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}

		return mainp;
	}

	public double getTotalSubscriptions() {
		return sum;
	}

	public double totalSubscriptions(String acc) {
		String text = "SELECT SUM(subscription) FROM subscriptions WHERE member_id = '" + acc + "' AND year = '"
				+ new SetDateCreated().getYear() + "'";
		double total = 0;
		try {
			rs = stm.executeQuery(text);
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

	public JTable createSubsTable(String acc) {
		String text = "SELECT subscription,month_of,date,time FROM subscriptions WHERE member_id = '" + acc
				+ "' AND year = '" + new SetDateCreated().getYear() + "'";
		JTable table = null;
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				Object[][] data = new Object[rows][SubHeader.header.length];
				String text1 = "SELECT subscription,month_of,date,time FROM subscriptions WHERE member_id = '" + acc
						+ "' AND year = '" + new SetDateCreated().getYear() + "'";
				rs = stm.executeQuery(text1);
				while (rs.next()) {
					double sub = rs.getDouble(1);
					data[i][0] = sub;
					data[i][1] = rs.getString(2);
					data[i][2] = rs.getString(3) + " " + rs.getString(4);
					// sum += sub;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, SubHeader.header);
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
