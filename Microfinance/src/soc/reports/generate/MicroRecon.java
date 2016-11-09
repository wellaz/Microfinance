package soc.reports.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

/**
 *
 * @author Wellington
 */
public class MicroRecon {
	ResultSet rs, rs1;
	Statement stm, stmt;

	double sum = 0;
	DoubleForm df;
	private JTable table;
	Object[][] data;

	public MicroRecon(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		df = new DoubleForm();
	}

	@SuppressWarnings("serial")
	public JPanel createMidPanel(String id, double totalsub, double totalcomm, double totaldebts, double posa,
			double posb) {

		JPanel mainp = new TranslucentJPanel(Color.BLUE);
		mainp.setLayout(new BorderLayout());

		data = new Object[5][RecoHeader.header.length];

		data[0][0] = "Subscriptions (SUM)";
		data[0][1] = totalsub;
		data[1][0] = "Add Commission (SUM)";
		data[1][1] = totalcomm;
		data[2][0] = "";
		data[2][1] = posa;
		data[3][0] = "Less Outstanding Debts (SUM)";
		data[3][1] = totaldebts;
		data[4][0] = (posb > 0) ? "Cash Payout" : "Owing";
		data[4][1] = posb;
		DefaultTableModel model = new DefaultTableModel(data, RecoHeader.header);
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
					// an empty line
				}
				return tip;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		});
		TableRenderer.setJTableColumnsWidth(getTable(), 480, 50, 50);
		getTable().setRowHeight(30);
		getTable().setAutoCreateRowSorter(false);

		new TableColumnResizer(getTable());
		new TableRowResizer(getTable());
		getTable().setShowGrid(true);
		getTable().setForeground(Color.BLUE);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(getTable());

		mainp.add(scroll, BorderLayout.CENTER);
		return mainp;
	}

	public double getTotalSubscriptions() {
		return sum;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTable crrateMicroReconTable(String acc, double totalsub, double totalcomm, double totaldebts, double posa,
			double posb) {
		JTable table = null;
		data = new Object[5][RecoHeader.header.length];

		data[0][0] = "Subscriptions (SUM)";
		data[0][1] = totalsub;
		data[1][0] = "Add Commission (SUM)";
		data[1][1] = totalcomm;
		data[2][0] = "";
		data[2][1] = posa;
		data[3][0] = "Less Outstanding Debts (SUM)";
		data[3][1] = totaldebts;
		data[4][0] = (posb > 0) ? "Cash Payout" : "Owing";
		data[4][1] = posb;
		DefaultTableModel model = new DefaultTableModel(data, RecoHeader.header);
		table = new JTable(model);

		return table;
	}
}
