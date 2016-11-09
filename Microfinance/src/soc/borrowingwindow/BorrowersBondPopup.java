package soc.borrowingwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
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
import soc.deco.TransparentScrollPane;
import soc.deco.TrasparentTable;
import soc.helpers.DefaultDialogClose;
import soc.helpers.IconImage;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class BorrowersBondPopup extends JPopupMenu implements ActionListener {
	public JMenuItem open, viewbond, viewperf, prop;
	JTable accJTable;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	private JTable table;

	public BorrowersBondPopup(ResultSet rs, Statement stm, JFrame frame, JTable accJTable) {
		init();
		this.accJTable = accJTable;

		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public final void init() {
		open = new JMenuItem("Open");
		open.setEnabled(false);
		viewbond = new JMenuItem("View Bond Notes");
		viewperf = new JMenuItem("Properties");
		viewperf.setEnabled(false);
		prop = new JMenuItem("Others...");
		prop.setEnabled(false);

		this.add(open);
		open.setEnabled(false);
		this.addSeparator();
		this.add(viewbond);
		viewbond.addActionListener(this);
		this.add(viewperf);
		viewperf.addActionListener(this);
		this.addSeparator();
		this.add(prop);
		prop.addActionListener(this);
	}

	public void getBondNotesAndCoins(int acc, String due) {
		String text = "SELECT amount,month_of,due,date,time FROM bonds_debts WHERE member_id = '" + acc
				+ "' AND due  = '" + due + "'";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				double sum = 0;
				String text1 = "SELECT amount,month_of,date,time FROM bonds_debts WHERE member_id = '" + acc
						+ "' AND due  = '" + due + "'";

				rs = stm.executeQuery(text1);
				Object[][] data = new Object[rows][BondsHeader.header.length];
				while (rs.next()) {
					double val = rs.getDouble(1);
					String month = rs.getString(2);
					String date = rs.getString(3);
					String time = rs.getString(4);

					data[i][0] = val;
					data[i][1] = month;
					data[i][2] = due;
					data[i][3] = date;
					data[i][4] = time;
					sum += val;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, BondsHeader.header);
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
				TableRenderer.setJTableColumnsWidth(table, 480, 20, 20, 20, 20, 20);
				table.setRowHeight(30);
				TrasparentTable.transparentTable(table);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(false);

				JScrollPane scroll = new JScrollPane();
				TransparentScrollPane.transparentScrollPane(scroll);
				scroll.setViewportView(table);
				JPanel panel = new JPanel(new BorderLayout());
				panel.setOpaque(false);
				panel.add(scroll, BorderLayout.CENTER);

				JLabel label = new BlinkingLabel("");
				label.setText("Total Bond Notes And Coins Amount : $" + sum);
				label.setFont(new Font("", Font.BOLD, 20));
				panel.add(label, BorderLayout.NORTH);

				JDialog dialog = new JDialog((JFrame) null, "Bond Notes And Coins trace for " + acc, true);
				dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
				dialog.setUndecorated(true);
				dialog.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent evvt) {
						dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
					}
				});
				dialog.setLayout(new BorderLayout());
				dialog.setIconImage(new IconImage().createIconImage());
				dialog.getContentPane().add(panel, BorderLayout.CENTER);

				Box lowerBox = Box.createHorizontalBox();
				JButton defaultButton = new JButton("OK");
				defaultButton.addActionListener((event) -> {
					dialog.dispose();
					// new AnimateDialog().fadeOut(dialog);
				});
				lowerBox.add(Box.createHorizontalGlue());
				lowerBox.add(defaultButton);
				dialog.getContentPane().add(lowerBox, BorderLayout.SOUTH);
				dialog.getRootPane().setDefaultButton(defaultButton);

				dialog.setSize(450, 150);
				// dialog.pack();
				Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
				int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
				dialog.setLocation(a, b);
				dialog.setAlwaysOnTop(true);
				new DefaultDialogClose(dialog);
				dialog.setVisible(true);
				// new AnimateDialog().fadeIn(dialog,100);

			} else {
				JOptionPane.showMessageDialog(frame, "No Bond Notes And Coins found for account " + acc, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewbond) {
			String due = accJTable.getValueAt(accJTable.getSelectedRow(), 4).toString();
			String acc = accJTable.getValueAt(accJTable.getSelectedRow(), 1).toString();
			getBondNotesAndCoins(Integer.parseInt(acc), due);
		}
	}
}
