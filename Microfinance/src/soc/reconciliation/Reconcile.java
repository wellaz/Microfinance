package soc.reconciliation;

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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

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
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

import soc.deco.AnimateDialog;
import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.IconImage;
import soc.helpers.PopupTrigger;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;

@SuppressWarnings("serial")
public class Reconcile extends JPanel {

	Statement stm;
	Statement stmt;
	ResultSet rs;
	JFrame comp;
	ResultSet rs1;
	private JProgressBar prog;
	private JLabel waitlbl;
	JPanel panel;
	JPanel midpanel, mainpanel;
	// private JTable table;
	JLabel timelbl;
	JTabbedPane tabs;
	private JDateChooser datechooser;
	private JDialog dialog;
	private JDateChooser datechoosert;
	private JButton find;
	private JLabel error;
	String datefrom, dateto, prevday;
	JTable table;
	DoubleForm df;

	public Reconcile(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt, JFrame comp) {
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
		df = new DoubleForm();
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Reconciliation")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Reconciliation   ", null, this, "Reconciliation Statement");
				tabs.setSelectedIndex(numberoftabs);
				Worker w = new Worker();
				w.execute();
			}
		});
	}

	public Object[][] tableData(double ledgerbal) {
		GetTotalSub ts = new GetTotalSub(rs, stm, comp);
		double totalsub = ts.getTotalSub(datefrom, dateto);

		GetDebts gd = new GetDebts(rs, stm, comp);
		double totaldebt = gd.getTotalDebt(datefrom, dateto);

		GetTotalExp exp = new GetTotalExp(rs, stm, comp);
		double totalexp = exp.getTotalExp(datefrom, dateto);

		GetInstallments ins = new GetInstallments(rs, stm, comp);
		double totalinst = ins.getTotalInst(datefrom, dateto);

		GetCommissions commissions = new GetCommissions(rs, stm, comp);
		double get_comm = commissions.getTotalComm(datefrom, dateto);

		GetLedgerDep ledger_dep = new GetLedgerDep(rs, stm, comp);
		double get_ledger_dep = ledger_dep.getTotalLedgerDep(datefrom, dateto);

		GetLedgerWith ledger_with = new GetLedgerWith(rs, stm, comp);
		double get_ledger_with = ledger_with.getTotalLedgerWith(datefrom, dateto);

		GetFullRe fullre = new GetFullRe(rs, stm, comp);
		double full_re = fullre.getTotalDebt(datefrom, dateto);

		GetTermination term = new GetTermination(rs, stm, comp);
		double term_v = term.getTotalDebt(datefrom, dateto);

		CashierDep cdep = new CashierDep(rs, stm, comp);
		double cdp = cdep.getTotalDep(datefrom, dateto);

		CashierWith cwith = new CashierWith(rs, stm, comp);
		double cwt = cwith.getTotalWith(datefrom, dateto);

		Object[][] data = new Object[34][Header.header.length];

		// int tablesize = size1 + size3 + size4 + 15;

		data[0][0] = "Revenue Suspense ";
		data[0][1] = "";
		data[0][2] = "";
		data[0][3] = ledgerbal;

		data[1][0] = "Add Subscriptions";
		data[1][1] = "";
		data[1][2] = "";
		data[1][3] = "";

		data[2][0] = "TOTAL";
		data[2][1] = "";
		data[2][2] = totalsub;
		data[2][3] = "";

		data[3][0] = "POSITION A";
		data[3][1] = "";
		data[3][2] = "";
		double posa = ledgerbal + totalsub;
		data[3][3] = df.form(posa);

		data[4][0] = "Less Borrowers";
		data[4][1] = "";
		data[4][2] = "";
		data[4][3] = "";

		data[5][0] = "TOTAL";
		data[5][1] = totaldebt;
		data[5][2] = "";
		data[5][3] = "";

		data[6][0] = "POSITION B";
		data[6][1] = "";
		data[6][2] = "";
		double posb = posa - totaldebt;
		data[6][3] = df.form(posb);

		data[7][0] = "Less Expenses";
		data[7][1] = "";
		data[7][2] = "";
		data[7][3] = "";

		data[8][0] = "TOTAL";
		data[8][1] = totalexp;
		data[8][2] = "";
		data[8][3] = "";

		data[9][0] = "POSITION C";
		data[9][1] = "";
		data[9][2] = "";
		double posc = posb - totalexp;
		data[9][3] = df.form(posc);

		data[10][0] = "Add Installments";
		data[10][1] = "";
		data[10][2] = "";
		data[10][3] = "";

		data[11][0] = "TOTAL";
		data[11][1] = "";
		data[11][2] = totalinst;
		data[11][3] = "";

		data[12][0] = "POSITION D";
		data[12][1] = "";
		data[12][2] = "";
		double posd = posc + totalinst;
		data[12][3] = df.form(posd);

		data[13][0] = "Add Paid Commissions";
		data[13][1] = "";
		data[13][2] = "";
		data[13][3] = "";

		data[14][0] = "TOTAL";
		data[14][1] = "";
		data[14][2] = get_comm;
		data[14][3] = "";

		data[15][0] = "POSITION E";
		data[15][1] = "";
		data[15][2] = "";
		double pose = posd + get_comm;
		data[15][3] = df.form(pose);

		data[16][0] = "Add Investors Deposits";
		data[16][1] = "";
		data[16][2] = "";
		data[16][3] = "";

		data[17][0] = "TOTAL";
		data[17][1] = "";
		data[17][2] = get_ledger_dep;
		data[17][3] = "";

		data[18][0] = "POSITION F";
		data[18][1] = "";
		data[18][2] = "";
		double posf = pose + get_ledger_dep;
		data[18][3] = df.form(posf);

		data[19][0] = "Less Investors Withdrawals";
		data[19][1] = "";
		data[19][2] = "";
		data[19][3] = "";

		data[20][0] = "TOTAL";
		data[20][1] = get_ledger_with;
		data[20][2] = "";
		data[20][3] = "";

		data[21][0] = "POSITION G";
		data[21][1] = "";
		data[21][2] = "";
		double posg = posf - get_ledger_with;
		data[21][3] = df.form(posg);

		data[22][0] = "Add Fully Paid Debts";
		data[22][1] = "";
		data[22][2] = "";
		data[22][3] = "";

		data[23][0] = "TOTAL";
		data[23][1] = "";
		data[23][2] = full_re;
		data[23][3] = "";

		data[24][0] = "POSITION H";
		data[24][1] = "";
		data[24][2] = "";
		double posh = posg + full_re;
		data[24][3] = df.form(posh);

		data[25][0] = "Less Member Termination Amounts";
		data[25][1] = "";
		data[25][2] = "";
		data[25][3] = "";

		data[26][0] = "TOTAL";
		data[26][1] = term_v;
		data[26][2] = "";
		data[26][3] = "";

		data[27][0] = "POSITION I";
		data[27][1] = "";
		data[27][2] = "";
		double posi = posh - term_v;
		data[27][3] = df.form(posi);

		data[28][0] = "Less Cashier Withdrawals";
		data[28][1] = "";
		data[28][2] = "";
		data[28][3] = "";

		data[29][0] = "TOTAL";
		data[29][1] = cwt;
		data[29][2] = "";
		data[29][3] = "";

		data[30][0] = "POSITION J";
		data[30][1] = "";
		data[30][2] = "";
		double posj = posi - cwt;
		data[30][3] = df.form(posj);

		data[31][0] = "Add Cashier Deposits";
		data[31][1] = "";
		data[31][2] = "";
		data[31][3] = "";

		data[32][0] = "TOTAL";
		data[32][1] = "";
		data[32][2] = cdp;
		data[32][3] = "";

		data[33][0] = "POSITION K";
		data[33][1] = "";
		data[33][2] = "";
		double posk = posj + cdp;
		data[33][3] = df.form(posk);

		return data;
	}

	public JPanel midPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		double ledgerbal = new GetLedgerBalance(rs, stm).getLedgerBalance(datefrom);
		if (ledgerbal >= 0) {
			Object[][] data = tableData(ledgerbal);
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
					if (Index_col == 1) {
						comp.setBackground(Color.RED);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					}
					if (Index_col == 2) {
						comp.setBackground(Color.GREEN);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					}
					if (Index_col == 3) {
						comp.setBackground(Color.GRAY);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
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
			TableRenderer.setJTableColumnsWidth(table, 480, 40, 20, 20, 20);
			table.setRowHeight(30);
			table.addMouseListener(new PopupTrigger(new RecoPopup(datefrom, dateto, rs, stm, comp)));
			new TableColumnResizer(table);
			new TableRowResizer(table);
			table.setShowGrid(true);
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(table);
			panel.add(scroll, BorderLayout.CENTER);
			JPanel lowerpanel = new TranslucentJPanel(Color.BLUE);
			lowerpanel.setLayout(new FlowLayout());
			lowerpanel.add(new JLabel());
			JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
			lowerpanel.add(generate);
			generate.addActionListener((ActionEvent event) -> {
				GenerateRecoPDF gnpdf = new GenerateRecoPDF(datefrom, dateto, comp, data, table);
				GenerateRecoPDF.Worker wk = gnpdf.new Worker();
				wk.execute();
			});

			panel.add(lowerpanel, BorderLayout.SOUTH);

			JLabel rc = new JLabel(
					"Reconciliation Statement For Period " + datefrom + " - " + dateto + " On 4131 (Revenue Suspense)");
			rc.setForeground(Color.WHITE);
			rc.setFont(new Font("", Font.BOLD, 16));
			panel.add(rc, BorderLayout.NORTH);
		} else

		{
			JOptionPane.showMessageDialog(comp, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		return panel;
	}

	public class Worker extends SwingWorker<Void, Void> {

		public Worker() {
			return;
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
			panel.revalidate();
			panel.repaint();
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

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});
		Date todaysdate = new Date();
		JLabel datelbl = new JLabel("From (date):");
		JLabel datelblt = new JLabel("To (date):");
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechooser.setDate(todaysdate);
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert.setDate(todaysdate);
		JPanel datepanel = new JPanel(new GridLayout(2, 2));
		datepanel.setOpaque(false);
		datepanel.add(datelbl);
		datepanel.add(datechooser);
		datepanel.add(datelblt);
		datepanel.add(datechoosert);
		dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		JLabel top = new JLabel("");

		find = new JButton("Find");
		find.addActionListener((event) -> {
			Date fdate = datechooser.getDate();
			datefrom = String.format("%1$tY-%1$tm-%1$td", fdate);
			Date tdate = datechoosert.getDate();
			dateto = String.format("%1$tY-%1$tm-%1$td", tdate);
			insertTab();
			dialog.setVisible(false);

			Calendar cal = Calendar.getInstance();
			cal.setTime(fdate);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Date oneDayBefore = cal.getTime();

			prevday = String.format("%1$tY-%1$tm-%1$td", oneDayBefore);

		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(event -> new AnimateDialog().fadeOut(dialog, 100));
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(find);
		box.add(cancel);
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(box);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		dialog.getRootPane().setDefaultButton(find);
		dialog.getContentPane().setBackground(Color.BLUE);

		dialog.setSize(300, 155);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		new AnimateDialog().fadeIn(dialog, 100);
		dialog.setAlwaysOnTop(true);
	}
}
