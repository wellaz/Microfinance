package soc.countregistered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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

import com.toedter.calendar.JDateChooser;

import soc.deco.BlinkingPanel;
import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.months.MonthsList;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Subscribers extends JPanel {

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
	private JDateChooser datechooser;
	private JDialog dialog;
	private JDateChooser datechoosert;
	private JButton find;
	private JLabel error;
	String datefrom, dateto, whichmonth;
	JComboBox<Object> months;

	public Subscribers(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt, JFrame comp) {
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
		double sum = 0;
		String rowsQuerry = "SELECT member_id,subscription,month_of FROM subscriptions WHERE month_of = '" + whichmonth
				+ "'";
		String previewQuerry = "SELECT member_id,subscription,month_of FROM subscriptions WHERE month_of = '"
				+ whichmonth + "'";
		try {
			rs = stm.executeQuery(rowsQuerry);
			if (rs.last()) {
				int rows = rs.getRow(), i = 0;
				Object[][] data = new Object[rows][TableHeader1.header.length];
				rs1 = stmt.executeQuery(previewQuerry);
				while (rs1.next()) {
					int account = rs1.getInt(1);
					data[i][0] = account;
					double amounts = rs1.getDouble(2);
					data[i][4] = amounts;
					data[i][5] = rs1.getString(3);
					String querry = "SELECT first_name,middle_name,last_name FROM members WHERE member_id = '" + account
							+ "'";
					rs = stm.executeQuery(querry);
					rs.next();
					data[i][1] = rs.getString(1);
					String midlename = rs.getString(2);
					data[i][2] = midlename.equals("") ? "" : midlename;
					data[i][3] = rs.getString(3);
					sum += amounts;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, TableHeader1.header);
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
				TableRenderer.setJTableColumnsWidth(table, 480, 10, 10, 10, 10, 30, 30);
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
					GenerateSubscribedPDF gnpdf = new GenerateSubscribedPDF(rs, stm, table);
					GenerateSubscribedPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				JLabel label = new JLabel("Total Subscriptions: $" + sum + " For " + whichmonth + ". "
						+ table.getRowCount() + " records found.");
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 30));
				panel.add(label, BorderLayout.NORTH);

				panel.add(lowerpanel, BorderLayout.SOUTH);

			} else {
				JOptionPane.showMessageDialog(comp, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);
				JPanel errorpanel = new BlinkingPanel("No Records Found !!!");
				panel.add(errorpanel, BorderLayout.CENTER);
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
				if (tabs.getTitleAt(a).trim().equals("Subscribed")) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Subscribed   ", null, this, "Subscribed Members");
				tabs.setSelectedIndex(numberoftabs);
				Worker w = new Worker();
				w.execute();
			}
		});
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel datelbl = new JLabel("Select Month:");
		datelbl.setFont(new Font("", Font.BOLD, 17));
		// datelbl.setForeground(Color.WHITE);
		// JLabel datelblt = new JLabel("To (date):");
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		JPanel datepanel = new JPanel(new GridLayout(1, 2));

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		datepanel.add(datelbl);
		datepanel.add(months);
		dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		// JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg
		// 4100)</i><h3>");

		find = new JButton("Find");
		find.addActionListener((event) -> {
			Date fdate = datechooser.getDate();
			datefrom = String.format("%1$tY-%1$tm-%1$td", fdate);
			Date tdate = datechoosert.getDate();
			dateto = String.format("%1$tY-%1$tm-%1$td", tdate);
			whichmonth = months.getSelectedItem().toString();
			insertTab();
			// dialog.setVisible(false);
			dialog.dispose();
		});
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		// midpanel.add(top);
		midpanel.add(find);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		dialog.getRootPane().setDefaultButton(find);

		dialog.setSize(300, 155);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);

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
