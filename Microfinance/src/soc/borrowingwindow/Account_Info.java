package soc.borrowingwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.IconImage;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.termination.GetTotalSub;

/**
 *
 * @author Wellington
 */
public class Account_Info {
	ResultSet rs;
	Statement stm;
	String[] header = { "Description", "Amounts ($)" };
	String[] leftCollumn = { "Total Subscriptions", "Outstanding Debts", "Realizable Cash" };
	ArrayList<Double> rightCollumn = new ArrayList<>();

	private JTable table;
	private GetTotalSub gettotalsub;
	private String accountName = null;

	public Account_Info(ResultSet rs, Statement stm, int accno) {
		this.rs = rs;
		this.stm = stm;
		// this.accno = accno;
		gettotalsub = new GetTotalSub(rs, stm);
		accountName = gettotalsub.getNameComm(accno);
		double subs = gettotalsub.totalSub(accno);
		double debts = gettotalsub.totalDebt(accno);
		double realcash = new DoubleForm().form(subs - debts);

		rightCollumn.add(subs);
		rightCollumn.add(debts);
		rightCollumn.add(realcash);

	}

	@SuppressWarnings("serial")
	public void showDialog() {
		JDialog dialog = new JDialog((JFrame) null, "Account Info", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel notelbl = new JLabel(accountName.toUpperCase() + "'s Account Info \u2193", SwingConstants.CENTER);
		notelbl.setFont(new Font("", Font.BOLD, 19));

		int rows = 3, i = 0;
		Object[][] data = new Object[rows][header.length];
		while (i < rows) {
			data[i][0] = leftCollumn[i];
			data[i][1] = rightCollumn.get(i);
			i++;
		}

		DefaultTableModel model = new DefaultTableModel(data, header);
		table = new JTable(model) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
				Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
				// even index, selected or not selected
				if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
					comp.setBackground(new Color(235, 235, 235));
				} else {
					comp.setBackground(new Color(204, 204, 204));
				}
				if (isCellSelected(Index_row, Index_col)) {
					comp.setBackground(new Color(0.5f, 0.5f, 1f));
				}
				if (Index_row == 0 && Index_col == 0) {
					comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					comp.setForeground(new Color(0.1f, 0.1f, 1f));
				}
				if (Index_row > 1 && Index_col == 0)
					comp.setFont(new java.awt.Font("", Font.BOLD, 15));
				if (Index_row > 1 && Index_col == 1) {
					comp.setFont(new java.awt.Font("", Font.BOLD, 15));
					comp.setBackground(Color.RED);
				}
				if (Index_row > 2 && Index_col == 2) {
					comp.setFont(new java.awt.Font("", Font.BOLD, 15));
					comp.setBackground(Color.GREEN);
				}
				if (Index_row > 0 && Index_col == 0) {
					comp.setFont(new java.awt.Font("", Font.BOLD, 15));
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
					e1.printStackTrace();
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
		TableRenderer.setJTableColumnsWidth(table, 480, 70, 30);
		table.setRowHeight(30);
		table.setAutoCreateRowSorter(true);

		new TableColumnResizer(table);
		new TableRowResizer(table);
		table.setShowGrid(true);

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(table);
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		panel.add(scroll, BorderLayout.CENTER);

		// comm
		dialog.getContentPane().add(notelbl, BorderLayout.NORTH);
		dialog.getContentPane().add(panel, BorderLayout.CENTER);

		JButton find = new JButton("OK");
		find.addActionListener((event) -> {
			dialog.dispose();
		});

		Box lowerbox = Box.createHorizontalBox();

		lowerbox.add(Box.createHorizontalGlue());
		lowerbox.add(find);
		dialog.getRootPane().setDefaultButton(find);
		dialog.getContentPane().add(lowerbox, BorderLayout.SOUTH);

		// dialog.setSize(400, 200);
		dialog.pack();
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}
}
