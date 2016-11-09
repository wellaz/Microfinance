package soc.borrowingwindow;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

import soc.commission.swip.ManagementCommision;
import soc.commission.swip.MemberShare;
import soc.deco.BlinkingButton;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.helpers.DoubleForm;
import soc.helpers.IconImage;
import soc.helpers.PopupTrigger;
import soc.helpers.SetDateCreated;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.helpers.TextValidator;
import soc.subscribe.ActivityAccPosting;
import soc.subscribe.AffectLedger;
import soc.subscribe.CommissionAccPosting;
import soc.subscribe.CommissionSusPosting;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class Reimbursement extends JPanel implements ActionListener {
	JTextField amounttxt;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JFrame frame;
	private JRadioButton interest_only;
	private JRadioButton debt_inst;
	private JRadioButton full_amm;
	private JDateChooser whendate;
	DoubleForm df;
	private JTextField find;
	String id, name;
	private JLabel error;
	private JTable table;

	public Reimbursement(JTabbedPane tabs, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.frame = frame;

		df = new DoubleForm();

		this.setLayout(new BorderLayout());

		// init();
	}

	public JPanel init() {
		JPanel p = new JPanel(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Reimbursement Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(2, 2, 1, 10));

		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		JLabel datelbl = new JLabel("Date :");
		datelbl.setForeground(Color.WHITE);
		datelbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		whendate = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		whendate.setDate(new Date());

		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(datelbl);
		midpanel.add(whendate);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new BlinkingButton(Color.BLUE);
		submit.setText("<html><h3>Proceed</h3></html>");
		submit.setBackground(Color.BLUE);
		submit.setForeground(Color.BLACK);
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		p.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		p.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		p.add(topp, BorderLayout.NORTH);

		Box box = Box.createVerticalBox();
		JLabel label = new JLabel("Specify the type of Debt Repayment that you are processing?");
		label.setFont(new Font("", Font.BOLD, 15));
		label.setForeground(Color.WHITE);

		interest_only = new JRadioButton("<html><h3>Interest ONLY</h3></html>");
		debt_inst = new JRadioButton("<html><h3>Debt Installment ONLY</h3></html>");
		full_amm = new JRadioButton("<html><h3>Full Principal amount + Interest</h3></html>");

		ButtonGroup gr = new ButtonGroup();
		gr.add(interest_only);
		gr.add(debt_inst);
		gr.add(full_amm);

		box.add(new JLabel(""));
		box.add(Box.createVerticalStrut(30));
		box.add(label);
		box.add(Box.createVerticalStrut(30));
		box.add(interest_only);
		box.add(Box.createVerticalStrut(30));
		box.add(debt_inst);
		box.add(Box.createVerticalStrut(30));
		box.add(full_amm);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(box, BorderLayout.EAST);
		temp.add(bp, BorderLayout.SOUTH);
		p.add(temp, BorderLayout.CENTER);
		return p;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Reimbursement")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				this.add(midPanel(), BorderLayout.CENTER);
				tabs.addTab("Reimbursement   ", null, this, "Reimbursement");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public JPanel midPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		double sum = 0;
		String rowsQuerry = "SELECT amount FROM debts WHERE member_id = '" + id + "' AND balance > '" + 0 + "'";
		getName(id);
		try {
			rs = stm.executeQuery(rowsQuerry);
			rs.last();
			int rows = rs.getRow();
			Object[][] data = new Object[rows][ReHeader.header.length];
			if (rows > 0) {
				String previewQuerry = "SELECT member_id,amount,due,paid,balance,commission,date,time,month_of FROM debts WHERE member_id = '"
						+ id + "' AND balance > '" + 0 + "'";
				rs = stm.executeQuery(previewQuerry);
				int i = 0;
				while (rs.next()) {
					data[i][0] = Boolean.FALSE;
					String account = rs.getString(1);
					data[i][1] = account;
					double amounts = rs.getDouble(2);
					data[i][2] = amounts;
					data[i][3] = rs.getString(9);
					data[i][4] = rs.getString(3);
					data[i][5] = rs.getDouble(4);
					double balance = rs.getDouble(5);
					data[i][6] = balance;
					data[i][7] = rs.getDouble(6);
					String date = rs.getString(7);
					String time = rs.getString(8);
					data[i][8] = date;
					data[i][9] = time;

					// data[i][10] = getBen(date, time);
					sum += balance;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, ReHeader.header);
				table = new JTable(model) {
					private boolean ImInLoop = false;

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
							// catch null pointer exception if mouse is over an
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
						if (getColumnName(columnIndex).equals("Choose")) {
							return Boolean.class;
						}
						return super.getColumnClass(columnIndex);
					}

					@Override
					public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
						if (columnIndex == 0) {
							if (!ImInLoop) {
								ImInLoop = true;
								Boolean bol = (Boolean) aValue;
								super.setValueAt(aValue, rowIndex, columnIndex);
								for (int i = 0; i < this.getRowCount(); i++) {
									if (i != rowIndex) {
										super.setValueAt(!bol, i, columnIndex);
									}
								}
								ImInLoop = false;
							}
						} else {
							super.setValueAt(aValue, rowIndex, columnIndex);
						}
					}
				};
				TableRenderer.setJTableColumnsWidth(table, 480, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);
				table.addMouseListener(new PopupTrigger(new BorrowersBondPopup(rs, stm, frame, table)));

				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				panel.add(scroll, BorderLayout.CENTER);

				JLabel label = new JLabel("Name : " + name.toUpperCase() + ". Total Amount : $" + sum);
				label.setForeground(Color.WHITE);
				label.setFont(new Font("", Font.BOLD, 30));
				panel.add(label, BorderLayout.NORTH);
				panel.add(init(), BorderLayout.SOUTH);

			} else {
				JOptionPane.showMessageDialog(frame, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);
				panel.removeAll();
				panel.setLayout(new GridBagLayout());
				panel.add(new JLabel("NO DATA"));
				panel.revalidate();
				panel.repaint();

			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
			JOptionPane.showMessageDialog(frame, "Error code 761\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return panel;
	}

	public void postings(int accno, double amount, String dt, String tm) {

		PostComm pc = new PostComm(rs, stm);

		int count = table.getModel().getRowCount();
		for (int i = 0; i < count; i++) {
			Boolean checked = Boolean.valueOf(table.getValueAt(i, 0).toString());

			double am = df.form(Double.parseDouble(table.getValueAt(i, 2).toString()));
			String month_of = table.getValueAt(i, 3).toString();
			double paid = df.form(Double.parseDouble(table.getValueAt(i, 5).toString()));
			double balan = df.form(Double.parseDouble(table.getValueAt(i, 6).toString()));
			String due = table.getValueAt(i, 4).toString();
			double commisssion = df.form(Double.parseDouble(table.getValueAt(i, 7).toString()));
			String date = table.getValueAt(i, 8).toString();
			String time = table.getValueAt(i, 9).toString();

			double newbalan = df.form(balan - amount);
			double newpaid = df.form(paid + amount);

			if (checked) {

				if (interest_only.isSelected()) {
					if (commisssion == amount) {
						double indi_interest = df.form(0.5 * amount);
						double mg_int = df.form(0.03 * indi_interest);
						double remainder = df.form(indi_interest - mg_int);
						mgmtPostings(Integer.toString(accno), df.form(mg_int));
						affectLedger(accno, amount);
						pc.postCommissionLump(amount);
						postInteresrOnly(accno, df.form(indi_interest), month_of);
						ActivityAccPosting accp = new ActivityAccPosting(rs, stm);
						accp.commPosting(accno, df.form(remainder));
						updateCommDebts(df.form(newpaid), df.form(newbalan), accno, month_of, df.form(am), date, time);
						// updateDebts(newpaid, newbalan, accno);

						MemberShare m = new MemberShare(rs, stm);
						m.postMemberComm(accno, df.form(remainder), month_of);
						JOptionPane.showMessageDialog(frame,
								"The reimbursement date is still " + due
										+ "\nWould you please remind the member of this date!\nAnd update them of $"
										+ newbalan + " outstanding figure.\nWe're almost there!!",
								"Information", JOptionPane.INFORMATION_MESSAGE);
						amounttxt.setText("");
						tabs.removeTabAt(tabs.getSelectedIndex());
					} else {
						JOptionPane.showMessageDialog(frame,
								"There is a problem with the entered figure. Interest should be exactly $" + commisssion
										+ "\nYou may consider correcting the figure entered!",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				} else if (debt_inst.isSelected()) {
					if (balan > 0) {
						if (amount <= balan) {
							updateDebts(df.form(newpaid), df.form(newbalan), accno, month_of, df.form(am), date, time);

							postInstallment(accno, amount, dt, tm);
							affectLedger(accno, amount);

							JOptionPane.showMessageDialog(frame,
									"The reimbursement date is still " + due
											+ "\nWould you please remind the member of this date!\nAnd update them of $"
											+ newbalan + " outstanding figure.\nWe're almost there!!",
									"Information", JOptionPane.INFORMATION_MESSAGE);
							amounttxt.setText("");
							tabs.removeTabAt(tabs.getSelectedIndex());
						} else {
							JOptionPane.showMessageDialog(frame,
									"The entered value is greater than the outstanding $" + balan
											+ " balance.\nConsider removing $" + df.form(amount - balan)
											+ " from the value you've entered!",
									"Warning", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(frame, "The balance is already cleared!!.", "Warning",
								JOptionPane.WARNING_MESSAGE);
						amounttxt.setText("");
					}

				} else if (full_amm.isSelected()) {
					double amountt = balan;
					if (amountt < amount) {
						double bal = amount - amountt;
						JOptionPane.showMessageDialog(frame,
								"The posted amount is $" + bal
										+ " MORE THAN the\nrequired amount. Postings will not be processed!",
								"Warning", JOptionPane.WARNING_MESSAGE);
					} else if (amountt > amount) {
						double bal = amountt - amount;
						JOptionPane.showMessageDialog(frame,
								"The posted amount is $" + bal
										+ " LESS THAN the\nrequired amount. Consider correcting the enterd figure!",
								"Warning", JOptionPane.WARNING_MESSAGE);

					} else if (commisssion <= 0) {
						JOptionPane.showMessageDialog(frame, "Your Transaction type should be an installment!",
								"Warning", JOptionPane.WARNING_MESSAGE);

					} else {
						double newbalanc = df.form(amount - amountt);
						String qqr = "UPDATE debts SET paid = '" + (amount + paid) + "',balance = '" + newbalanc
								+ "', commission = '" + 0 + "' WHERE member_id = '" + accno + "' AND month_of = '"
								+ month_of + "' AND year = '" + new SetDateCreated().getYear() + "' AND amount = '" + am
								+ "'AND date = '" + date + "' AND time = '" + time + "'";
						try {
							stm.executeUpdate(qqr);
						} catch (SQLException ee) {
							ee.printStackTrace();
						}
						double interest = commisssion;
						double ind = df.form(0.5 * interest);
						double mgint = df.form(0.03 * ind);
						double indv = df.form(ind - mgint);
						postInteresrOnly(accno, df.form(ind), month_of);
						affectLedger(accno, amount);
						mgmtPostings(Integer.toString(accno), df.form(mgint));
						pc.postCommissionLump(df.form(interest));
						ActivityAccPosting accp = new ActivityAccPosting(rs, stm);
						accp.commPosting(accno, df.form(indv));

						MemberShare m = new MemberShare(rs, stm);
						m.postMemberComm(accno, df.form(indv), month_of);
						postFullAmm(accno, am, date, time, month_of);

						JOptionPane.showMessageDialog(frame,
								"$" + indv + " credited to Account " + accno + "\n$" + mgint
										+ " credited to management account(4181)\n$" + ind
										+ " credited to the commission suspense(4161)\n$" + amount
										+ " seated in the Revenue Suspense(4131)\nDONE!",
								"Information", JOptionPane.INFORMATION_MESSAGE);
						amounttxt.setText("");
						tabs.removeTabAt(tabs.getSelectedIndex());
					}

				} else {
					JOptionPane.showMessageDialog(frame, "You forgot to specify the transaction type.", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	public void updateDebts(double newpaid, double newbalan, int accno, String month, double amount, String date,
			String time) {
		String qr = "UPDATE debts SET paid = '" + newpaid + "',balance = '" + newbalan + "'  WHERE member_id ='" + accno
				+ "' AND month_of = '" + month + "' AND year = '" + new SetDateCreated().getYear() + "' AND amount = '"
				+ amount + "' AND date = '" + date + "' AND time = '" + time + "'";
		try {
			stm.executeUpdate(qr);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void updateCommDebts(double newpaid, double newbalan, int accno, String month, double amount, String date,
			String time) {
		String qr = "UPDATE debts SET paid = '" + newpaid + "',balance = '" + newbalan + "',commission = '" + 0
				+ "'  WHERE member_id = '" + accno + "' AND month_of = '" + month + "' AND year = '"
				+ new SetDateCreated().getYear() + "' AND amount = '" + amount + "' AND date = '" + date
				+ "' AND time = '" + time + "'";
		try {
			stm.executeUpdate(qr);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void postInteresrOnly(int accno, double amount, String month) {
		CommissionSusPosting css = new CommissionSusPosting(rs, stm);
		css.postings(accno, amount, month);
		CommissionAccPosting cas = new CommissionAccPosting(rs, stm);
		cas.postings(accno, amount, month);
	}

	public void mgmtPostings(String acc, double balance) {
		ManagementCommision mg = new ManagementCommision(rs, stm);
		mg.crMgntAcc(acc, balance);
	}

	public void affectLedger(int accno, double amount) {
		AffectLedger aff = new AffectLedger(rs, stm);
		aff.creditLedgerwithComm(amount, accno);
	}

	public void postInstallment(int accno, double amm, String date, String time) {
		String text = "INSERT INTO debt_installments(member_id,amount,date,time,year)VALUES('" + accno + "','" + amm
				+ "','" + date + "','" + time + "','" + new SetDateCreated().getYear() + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void postFullAmm(int accno, double amm, String date, String time, String month) {
		String text = "INSERT INTO full_debt_re(member_id,amount,month_of,date,time,year)VALUES('" + accno + "','" + amm
				+ "','" + month + "','" + date + "','" + time + "','" + new SetDateCreated().getYear() + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;
		JTable table;

		public Worker() {
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
			dialog.getContentPane().add(box, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			dialog.setVisible(true);
			proceed();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.setVisible(false);
			dialog.dispose();
		}
	}

	public void proceed() {
		String amount = amounttxt.getText();
		String dt = new SetDateCreated().getExactDate(whendate);
		String tm = new SetDateCreated().getTime();
		if (!amount.trim().equals("") && !dt.trim().equals(""))
			postings(Integer.parseInt(id), Double.parseDouble(amount), dt, tm);

		else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	public void showDialog() {
		JDialog dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		dialog.setTitle("Search");

		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");

		find = new JTextField();
		find.addKeyListener(new TextValidator());
		find.addActionListener((event) -> {
			buttonAction(dialog);
		});
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.add(top);
		midpanel.add(find);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		Box lowerBox = Box.createHorizontalBox();
		JButton defaultButton = new JButton("OK");
		defaultButton.addActionListener((event) -> {
			buttonAction(dialog);
		});
		lowerBox.add(Box.createHorizontalGlue());
		lowerBox.add(defaultButton);
		dialog.getContentPane().add(lowerBox, BorderLayout.SOUTH);
		dialog.getRootPane().setDefaultButton(defaultButton);

		dialog.setSize(300, 155);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

	public void buttonAction(JDialog dialog) {
		EventQueue.invokeLater(() -> {
			id = find.getText();
			insertTab();
			dialog.dispose();
		});
	}

	public String getName(String account) {
		String querry = "SELECT first_name,last_name FROM members WHERE member_id = '" + account + "'";
		try {
			rs = stm.executeQuery(querry);
			if (rs.next()) {
				name = rs.getString(1) + " " + rs.getString(2);
			} else {
				name = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public String getBen(String date, String time) {
		String ben = null;
		String qr = "SELECT name FROM beneficiary WHERE member_id = '" + id + "' AND date = '" + date
				+ "' AND time =  '" + time + "'";
		try {
			rs1 = stm.executeQuery(qr);

			if (rs1.next())
				ben = rs1.getString(1);
			else
				ben = "-";
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return ben;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			Worker w = new Worker();
			w.execute();
		}
	}
}
