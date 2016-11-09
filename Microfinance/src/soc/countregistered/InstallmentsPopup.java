package soc.countregistered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.BlinkingLabel;
import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class InstallmentsPopup extends JPopupMenu implements ActionListener {
	public JMenuItem open, viewmark, viewperf, prop;
	JTable accJTable;
	String from, to;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	private JTable table;

	public InstallmentsPopup(String from, String to, ResultSet rs, Statement stm, JFrame frame, JTable accJTable) {
		init();
		this.accJTable = accJTable;
		this.from = from;
		this.to = to;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public final void init() {
		open = new JMenuItem("Open");
		open.setEnabled(false);
		viewmark = new JMenuItem("View Installments");
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

	public void getInstallments(int acc) {
		String text = "SELECT amount,date,time FROM debt_installments WHERE member_id = '" + acc
				+ "' AND date BETWEEN '" + from + "' AND '" + to + "'";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				double sum = 0;
				String text1 = "SELECT amount,date,time FROM debt_installments WHERE member_id = '" + acc
						+ "' AND date BETWEEN '" + from + "' AND '" + to + "'";

				rs = stm.executeQuery(text1);
				Object[][] data = new Object[rows][InstallmentHeader.header.length];
				while (rs.next()) {
					double val = rs.getDouble(1);
					data[i][0] = val;
					data[i][1] = rs.getString(2) + " " + rs.getString(3);
					sum += val;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, InstallmentHeader.header);
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
							comp.setFont(new java.awt.Font("", Font.BOLD, 15 + Index_row));

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
					InstallmentsPDF gnpdf = new InstallmentsPDF(rs, stm, table, from, to, acc);
					InstallmentsPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});
				JLabel label = new BlinkingLabel("");
				label.setText("Total Amount : $" + sum);
				label.setFont(new Font("", Font.BOLD, 30));
				panel.add(label, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

				JDialog dialog = new JDialog((JFrame) null, "Installments for " + acc, true);
				dialog.setLayout(new BorderLayout());
				dialog.setIconImage(new IconImage().createIconImage());
				dialog.getContentPane().add(panel, BorderLayout.CENTER);
				Box lowerBox = Box.createHorizontalBox();
				JButton defaultButton = new JButton("OK");
				defaultButton.addActionListener((event) -> {
					dialog.dispose();
				});
				lowerBox.add(Box.createHorizontalGlue());
				lowerBox.add(defaultButton);
				dialog.getContentPane().add(lowerBox, BorderLayout.SOUTH);
				dialog.getRootPane().setDefaultButton(defaultButton);

				dialog.setSize(400, 450);
				Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
				int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
				dialog.setLocation(a, b);
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);

			} else {
				JOptionPane.showMessageDialog(frame, "No installments found for member account " + acc, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewmark) {
			String val = accJTable.getValueAt(accJTable.getSelectedRow(), 0).toString();
			getInstallments(Integer.parseInt(val));
		}

	}

}
