package soc.overdue.check;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.Font;

import soc.baddebts.BadDebtsPopup;
import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.PopupTrigger;
import soc.helpers.SetDateCreated;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

@SuppressWarnings("serial")
public class OverDue extends JPanel implements ActionListener {
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;
	JPanel mainp;
	private JTable table;
	private JButton go;
	private JButton can;
	DoubleForm df;
	double com = 0, bal = 0;

	public OverDue(JTabbedPane tabs, ResultSet rs, Statement stm, ResultSet rs1, Statement stmt, JFrame frame) {
		this.stm = stm;
		this.rs = rs;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.frame = frame;
		this.tabs = tabs;

		// mainp = new TranslucentJPanel(Color.BLUE);
		// mainp.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		JLabel o = new JLabel("Overdue Debtors");
		o.setForeground(Color.RED);
		o.setFont(new java.awt.Font("", Font.BOLD, 30));
		JPanel p = new TranslucentJPanel(Color.BLUE);
		p.setLayout(new FlowLayout());
		p.add(o, SwingConstants.CENTER);
		this.add(p, BorderLayout.NORTH);
		df = new DoubleForm();
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("OverDue")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("OverDue   ", null, this, "OverDue");
				tabs.setSelectedIndex(numberoftabs);
				// Worker w = new Worker();
				// w.execute();
				this.add(check(), BorderLayout.CENTER);
			}
		});
	}

	public JPanel check() {
		JPanel mainp = new TranslucentJPanel(Color.BLUE);
		mainp.setLayout(new BorderLayout());
		String a = "SELECT * FROM debts WHERE year = '" + new SetDateCreated().getYear() + "' AND balance > '" + 0
				+ "' AND  due  < CURDATE() AND bad_debt = '"+"no"+"'";
		try {
			rs = stm.executeQuery(a);
			rs.last();
			int rows = rs.getRow(), i = 0;
			Object[][] data = new Object[rows][Header.header.length];
			if (rows > 0) {
				String az = "SELECT member_id,amount,due,paid,balance,commission,date,time FROM debts WHERE balance > '"
						+ 0 + "' AND  due  < CURDATE() AND bad_debt = '"+"no"+"'  ";
				rs = stm.executeQuery(az);
				while (rs.next()) {
					data[i][0] = Boolean.FALSE;
					int account = rs.getInt(1);
					data[i][1] = account;
					double amounts = rs.getDouble(2);
					data[i][4] = amounts;
					data[i][5] = rs.getString(3);
					data[i][6] = rs.getDouble(4);
					double balance = rs.getDouble(5);
					double commissions = rs.getDouble(6);
					data[i][7] = balance;
					data[i][8] = commissions;
					data[i][9] = rs.getString(7);
					data[i][10] = rs.getString(8);

					String querry = "SELECT first_name,last_name FROM members WHERE member_id = '" + account + "'";
					rs1 = stmt.executeQuery(querry);
					rs1.next();
					data[i][2] = rs1.getString(1);
					data[i][3] = rs1.getString(2);

					com += commissions;
					bal += balance;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, Header.header);
				table = new JTable(model) {
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
						switch (colIndex) {
						case 0:
							return true;
						default:
							return false;

						}
					}

					@Override
					public Class<?> getColumnClass(int columnIndex) {
						if (getColumnName(columnIndex).equals("Freeze The Debt")) {
							return Boolean.class;
						}
						return super.getColumnClass(columnIndex);
					}
				};
				TableRenderer.setJTableColumnsWidth(table, 480, 10, 10, 10, 10, 10, 10, 10, 10, 10, 5, 5);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);
				table.addMouseListener(new PopupTrigger(new BadDebtsPopup(rs, stm, frame, table)));

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);

				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				mainp.add(scroll, BorderLayout.CENTER);
				go = new JButton("Proceed");
				can = new JButton("Cancel");
				can.addActionListener(this);
				go.addActionListener(this);
				JPanel canp = new TranslucentJPanel(Color.BLUE);
				canp.setLayout(new FlowLayout());
				canp.add(go);
				canp.add(Box.createHorizontalStrut(50));
				canp.add(can);

				JLabel label = new JLabel(" Outstanding balance $" + df.form(bal) + ". Outstanding Commission $"
						+ df.form(com) + ". " + table.getRowCount() + " records found.");
				label.setForeground(Color.WHITE);
				label.setFont(new java.awt.Font("", Font.BOLD, 15));
				mainp.add(label, BorderLayout.NORTH);

				mainp.add(canp, BorderLayout.SOUTH);

			} else {
				JOptionPane.showMessageDialog(frame, "No Data.", "Warning", JOptionPane.WARNING_MESSAGE);
				mainp.removeAll();
				mainp.setLayout(new GridBagLayout());
				mainp.add(new JLabel("NO DATA"));
				mainp.revalidate();
				mainp.repaint();
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
			mainp.removeAll();
			mainp.setLayout(new GridBagLayout());
			mainp.add(new JLabel("NO DATA"));
			mainp.revalidate();
			mainp.repaint();
		}
		return mainp;
	}

	public void processData(JTable table) {
		int count = table.getModel().getRowCount();
		String futuredate = new SetDateCreated().getThirtythDate();
		for (int i = 0; i < count; i++) {
			Boolean checked = Boolean.valueOf(table.getValueAt(i, 0).toString());
			String id = table.getValueAt(i, 1).toString();
			String balance = table.getValueAt(i, 7).toString();
			String commission = table.getValueAt(i, 8).toString();
			String date = table.getValueAt(i, 9).toString();
			String time = table.getValueAt(i, 10).toString();
			double paid = df.form(Double.parseDouble(table.getValueAt(i, 6).toString()));
			double newinterest = df.form((0.2 * Double.parseDouble(balance)));
			double interest = df.form(newinterest + Double.parseDouble(commission));
			double newbalance = df.form(Double.parseDouble(balance) + newinterest);

			String upq = "UPDATE debts SET due = '" + futuredate + "',balance = '" + newbalance + "',commission = '"
					+ interest + "' WHERE member_id = '" + id + "' AND balance = '" + balance + "' AND paid = '" + paid
					+ "' AND date = '" + date + "' AND time = '" + time + "'";
			if (checked) {

			} else {
				try {
					stm.executeUpdate(upq);
				} catch (SQLException ee) {
					ee.printStackTrace(System.err);
				}
			}
		}
	}

	class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		Worker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				dialog.setVisible(false);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			JPanel pann = new TranslucentJPanel(Color.BLUE);
			pann.setLayout(new BorderLayout());
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			dialog.setVisible(true);
			OverDue.this.add(check(), BorderLayout.CENTER);
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.dispose();
		}
	}

	class Worker1 extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;

		Worker1() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				dialog.setVisible(false);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			JPanel pann = new TranslucentJPanel(Color.BLUE);
			pann.setLayout(new BorderLayout());
			pann.add(box, BorderLayout.CENTER);
			dialog.getContentPane().add(pann, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			dialog.setVisible(true);
			processData(OverDue.this.table);
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.dispose();
			JOptionPane.showMessageDialog(frame, "All necessary Updates were processed.\nDone.", "Done",
					JOptionPane.INFORMATION_MESSAGE);
			tabs.removeTabAt(tabs.getSelectedIndex());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == go) {
			Worker1 w = new Worker1();
			w.execute();
			// processData(table);

		}
		if (e.getSource() == can) {
			tabs.removeTabAt(tabs.getSelectedIndex());
		}
	}
}
