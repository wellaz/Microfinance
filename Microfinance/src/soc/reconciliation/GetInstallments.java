package soc.reconciliation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

public class GetInstallments {
	ResultSet rs;
	Statement stm;
	JFrame frame;
	ArrayList<Integer> ids = new ArrayList<>();
	ArrayList<Double> amm = new ArrayList<>();
	private JTable table;

	public GetInstallments(ResultSet rs, Statement stm, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public double getTotalInst(String date, String date1) {
		String text = "SELECT SUM(amount) FROM debt_installments WHERE date BETWEEN '" + date + "' AND '" + date1
				+ "' ";
		double balance = 0;
		try {
			rs = stm.executeQuery(text);
			if (rs.next())
				balance = rs.getDouble(1);
			else
				balance = 0;
		} catch (SQLException ee) {

		}
		return balance;
	}

	@SuppressWarnings("serial")
	public void getAllValues(String date, String date1) {
		String text = "SELECT member_id,amount FROM debt_installments WHERE date BETWEEN '" + date + "' AND '" + date1
				+ "' ";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				double sum = 0;
				String text1 = "SELECT member_id,amount FROM debt_installments WHERE date BETWEEN '" + date + "' AND '"
						+ date1 + "' ";
				rs = stm.executeQuery(text1);
				Object[][] data = new Object[rows][CommonHeader.header.length];
				while (rs.next()) {
					data[i][0] = rs.getInt(1);
					double val = rs.getDouble(2);
					data[i][1] = val;
					sum += val;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, CommonHeader.header);
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
							comp.setForeground(new Color(0.3f, 0.2f, 1f));
						}
						if (Index_row == 1 && Index_col == 0) {
							comp.setFont(new java.awt.Font("", Font.BOLD, 18));
							comp.setForeground(new Color(0.1f, 0.1f, 1f));
						}
						if (Index_row > 1 && Index_col == 0)
							comp.setFont(new java.awt.Font("", Font.BOLD, 15 ));

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
				TableRenderer.setJTableColumnsWidth(table, 480, 40, 60);
				table.setRowHeight(30);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(false);

				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				JPanel panel = new JPanel(new BorderLayout());
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new TranslucentJPanel(Color.BLUE);
				lowerpanel.setLayout(new FlowLayout());
				lowerpanel.add(new JLabel());
				JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
				lowerpanel.add(generate);
				generate.addActionListener((ActionEvent event) -> {
					InstPDF gnpdf = new InstPDF(rs, stm, table, date, date1);
					InstPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});
				JLabel label = new JLabel("Total Amount : $" + sum);
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 30));
				panel.add(label, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

				JDialog dialog = new JDialog();
				dialog.setTitle("Installments");
				dialog.setLayout(new BorderLayout());
				dialog.setIconImage(new IconImage().createIconImage());
				dialog.getContentPane().add(panel, BorderLayout.CENTER);
				dialog.setSize(400, 450);
				Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
				int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
				dialog.setLocation(a, b);
				dialog.setVisible(true);

			} else {
				JOptionPane.showMessageDialog(frame, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public ArrayList<Integer> getIds() {
		return ids;
	}

	public ArrayList<Double> getAmounts() {
		return amm;
	}

}
