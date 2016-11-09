package soc.countregistered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.TranslucentJPanel;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class RegisteredMembers extends JPanel {

	Statement stm;
	Statement stmt;
	ResultSet rs;
	JFrame comp;
	ResultSet rs1;
	private JProgressBar prog;
	private JLabel waitlbl;
	JPanel panel;
	JPanel midpanel, mainpanel;
	private JTable table;
	JLabel timelbl;
	JTabbedPane tabs;

	public RegisteredMembers(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt,
			JFrame comp) {
		this.rs = rs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.comp = comp;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
		panel = new JPanel(new BorderLayout());
		panel.add(progresspanel(), BorderLayout.CENTER);
		mainpanel = new JPanel(new GridBagLayout());
		mainpanel.add(new JLabel("WAIT"));

		this.add(mainpanel, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
	}

	public JPanel midPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		String rowsQuerry = "SELECT member_id,first_name,middle_name,last_name FROM members";
		String previewQuerry = "SELECT member_id,first_name,middle_name,last_name FROM members";
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][TableHeader.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					int account = rs1.getInt(1);
					data[i][0] = account;
					data[i][1] = rs1.getString(2);
					String midlename = rs1.getString(3);
					data[i][2] = midlename.equals("") ? "" : midlename;
					data[i][3] = rs1.getString(4);
					String querry = "SELECT mobile,email,physical_add FROM contacts WHERE member_id = '" + account
							+ "'";
					rs = stm.executeQuery(querry);
					if(rs.next()) {
						data[i][4] = rs.getString(1);
						data[i][5] = rs.getString(2);
						data[i][6] = rs.getString(3);
					}else {
						data[i][4] = "";
						data[i][5] = "";
						data[i][6] = "";
					}
					

					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, TableHeader.header);
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
							comp.setFont(new java.awt.Font("", Font.BOLD, 15));

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
				TableRenderer.setJTableColumnsWidth(table, 480, 10, 30, 5, 30, 5, 10, 10);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);
				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new JPanel(new FlowLayout());
				lowerpanel.add(new JLabel());
				JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
				lowerpanel.add(generate);
				generate.addActionListener((ActionEvent event) -> {
					GenerateRegisteredPDF gnpdf = new GenerateRegisteredPDF(rs, stm, table);
					GenerateRegisteredPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});
				
				
				JLabel all = new JLabel("All Members List, TOTAL = "+table.getRowCount());
				all.setFont(new Font("",Font.BOLD,19));
				all.setForeground(Color.WHITE);

				panel.add(all, BorderLayout.NORTH);
				panel.add(lowerpanel, BorderLayout.SOUTH);

			} else {
				prog.setIndeterminate(false);
				JOptionPane.showMessageDialog(comp, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);

			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
			JOptionPane.showMessageDialog(comp, " Error code 761\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return panel;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("All Members")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("All Members   ", null, this, "All Members");
				tabs.setSelectedIndex(numberoftabs);
				Worker w = new Worker();
				w.execute();
			}
		});
	}

	public class Worker extends SwingWorker<Void, Void> {

		public Worker() {

		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			midpanel = midPanel();
			mainpanel.removeAll();
			mainpanel.setLayout(new BorderLayout());
			mainpanel.add(midpanel, BorderLayout.CENTER);
			mainpanel.revalidate();
			mainpanel.repaint();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			panel.removeAll();
			panel.setLayout(new BorderLayout());
			panel.add(createLowerPanel(), BorderLayout.CENTER);
			Timer timer = new Timer(1000, new Listener());
			timer.start();
		}

	}

	public JPanel progresspanel() {
		JPanel panel = new JPanel(new FlowLayout());
		prog = new JProgressBar();

		waitlbl = new JLabel("Processing....");

		Box box = Box.createHorizontalBox();
		box.add(waitlbl);
		box.add(prog);
		panel.add(box);
		return panel;
	}

	public JPanel createLowerPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		timelbl = new JLabel();
		timelbl.setFont(new Font("Arial", Font.PLAIN, 20));
		timelbl.setForeground(Color.WHITE);

		panel.setBackground(Color.BLUE);
		// start the clock

		JLabel lbl = new JLabel();
		lbl.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl.setForeground(Color.WHITE);
		panel.add(lbl);
		panel.add(Box.createHorizontalGlue());
		panel.add(timelbl);

		return panel;
	}

	// timer class
	class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Calendar now = Calendar.getInstance();
			int hr = now.get(Calendar.HOUR_OF_DAY);
			int min = now.get(Calendar.MINUTE);
			int sec = now.get(Calendar.SECOND);
			int AM_PM = now.get(Calendar.AM_PM);

			String day_night;
			if (AM_PM == 1) {
				day_night = "PM";
			} else {
				day_night = "AM";
			}
			timelbl.setText("TIME " + hr + ":" + min + ":" + sec + " " + day_night);
		}
	}

}
