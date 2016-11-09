package soc.ledger.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.GetAccountNumbers;
import soc.helpers.IconImage;
import soc.helpers.PopupTrigger;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.helpers.TextValidator;
import soc.months.MonthsList;

@SuppressWarnings("serial")
public class Withdrawal extends JPanel implements ActionListener {

	JTextField amounttxt;
	JComboBox<Object> memberids;
	JComboBox<Object> months;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JTable table;

	public Withdrawal(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Withdrawal Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));
		JLabel acclbl = new JLabel("");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[new GetAccountNumbers(rs, stm).getAccounts().size()];
		for (int i = 0; i < new GetAccountNumbers(rs, stm).getAccounts().size(); i++) {
			da[i] = "" + new GetAccountNumbers(rs, stm).getAccounts().get(i);
		}

		memberids = new JComboBox<>(da);

		Object[] da1 = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da1[i] = MonthsList.getMonths().get(i);
		}

		midpanel.add(acclbl);
		midpanel.add(new JLabel(" "));
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("Proceed");
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Investor Withdrawal")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Investor Withdrawal   ", null, this, "Withdrawal");
				tabs.setSelectedIndex(count);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {

		}

	}

	public void getAllValues() {
		String text = "SELECT * FROM investors WHERE valid = '" + "true" + "'";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int rows = rs.getRow(), i = 0;
			if (rows > 0) {
				double sum = 0;
				String text1 = "SELECT * FROM investors WHERE valid = '" + "true" + "'";
				rs = stm.executeQuery(text1);
				Object[][] data = new Object[rows][Header.header.length];
				while (rs.next()) {
					data[i][0] = rs.getString(1);
					double val = rs.getDouble(2);
					data[i][1] = val;
					data[i][2] = rs.getString(3);
					data[i][3] = rs.getString(4);
					data[i][4] = rs.getString(5);
					data[i][5] = rs.getString(6);
					data[i][6] = rs.getString(7);
					sum += val;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, Header.header);
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
				TableRenderer.setJTableColumnsWidth(table, 480, 30, 20, 10, 10, 10, 10, 10);
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
				/*
				 * JButton generate = new JButton(
				 * "<html><p>Download<br>PDF File</p></html>");
				 * lowerpanel.add(generate);
				 * generate.addActionListener((ActionEvent event) -> { DebtsPDF
				 * gnpdf = new DebtsPDF(rs, stm, table, date, date1);
				 * DebtsPDF.Worker wk = gnpdf.new Worker(); wk.execute(); });
				 */
				JLabel label = new JLabel("Total Investment : $" + sum);
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 30));
				panel.add(label, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

				JDialog dialog = new JDialog();
				table.addMouseListener(new PopupTrigger(new Popup(tabs, rs, stm, frame, table, dialog)));
				dialog.setTitle("Investors");
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

}
