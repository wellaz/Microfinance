package soc.termination;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soc.deco.AnimateDialog;
import soc.deco.BlinkingButton;
import soc.deco.BlinkingLabel;
import soc.deco.TranslucentJPanel;
import soc.helpers.DoubleForm;
import soc.helpers.IconImage;
import soc.helpers.RemoveTab;
import soc.helpers.SetDateCreated;
import soc.helpers.TableColumnResizer;
import soc.helpers.TableRenderer;
import soc.helpers.TableRowResizer;
import soc.helpers.TextValidator;
import soc.subscribe.AffectLedger;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Terminate extends JPanel {
	private JDialog dialog;
	private JLabel error;
	private JTextField cashierid;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	JTable table;
	private JLabel label;
	String id, name, grant;
	DoubleForm df;
	private double diff = 0;
	Object[][] data;
	private double netcash = 0;
	private JRadioButton withholdComm;
	private JRadioButton releaseEverything;
	private double totalsubscriptions;
	private double totalcommission;
	private String description;
	private double subslessDebts;
	private JButton continueBtn;
	private double remainingCommission;
	private JRadioButton partialterminate;
	private JDialog dialog3;
	private JTextField cashierid3;
	private BlinkingLabel error3;
	private double partialValue;
	private double debt;

	public Terminate(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		df = new DoubleForm();
		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Termination")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Termination   ", null, this, "Termination Form");
				tabs.setSelectedIndex(count);
				showDialog();
			}
		});
	}

	public JPanel createMidPanel(int accno) {
		JPanel panel = new TranslucentJPanel(Color.BLACK);
		panel.setLayout(new BorderLayout());
		GetTotalSub ts = new GetTotalSub(rs, stm);
		totalsubscriptions = ts.totalSub(accno);
		totalcommission = ts.totalComm(accno);
		debt = ts.totalDebt(accno);
		double bondNotesAndCoins = ts.getSubscriptionsInBonds(accno);

		diff = df.form((totalsubscriptions + totalcommission) - debt);
		double netUSDollar = df.form(diff - bondNotesAndCoins);
		subslessDebts = df.form(totalsubscriptions - debt);

		data = new Object[12][2];

		data[0][0] = "Name";
		data[0][1] = ts.getNameComm(accno).toUpperCase();
		data[1][0] = "Number Of Shares";
		int individualtotalshares = ts.totalShare(accno);
		data[1][1] = individualtotalshares;
		data[2][0] = "Subscriptions";
		data[2][1] = "$" + totalsubscriptions;
		data[3][0] = "Total Commission(Individual + Club Interest)";
		data[3][1] = "$" + totalcommission;
		data[4][0] = "Outstanding Debt";
		data[4][1] = "$" + debt;
		data[5][0] = (diff > 0) ? "Cash Payout" : "Owing";
		data[5][1] = "$" + diff;
		data[6][0] = "";
		data[6][1] = "";
		data[7][0] = "Net Cash In Bond Notes And Coins";
		data[7][1] = bondNotesAndCoins;
		data[8][0] = "";
		data[8][1] = "";
		data[9][0] = "Less Bad Debt Recovery Fee";
		double baddebtrecoveryfee = df.form(ts.getDeduction(accno));
		data[9][1] = baddebtrecoveryfee;
		data[10][0] = "";
		data[10][1] = "";

		data[11][0] = "Net Cash In US Dollar";
		netcash = df.form(netUSDollar - baddebtrecoveryfee);
		data[11][1] = netcash;
		grant = Double.toString((netcash <= 0) ? 0.00 : netcash);

		remainingCommission = df.form(totalcommission - baddebtrecoveryfee);

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
		TableRenderer.setJTableColumnsWidth(table, 480, 50, 50);
		table.setRowHeight(30);

		new TableColumnResizer(table);
		new TableRowResizer(table);
		table.setShowGrid(true);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(table);
		panel.add(scroll, BorderLayout.CENTER);
		JPanel lowerpanel = new JPanel(new FlowLayout());
		lowerpanel.add(new JLabel());
		Box b = Box.createVerticalBox();

		lowerpanel.add(b, SwingConstants.CENTER);

		label = new BlinkingLabel("");
		label.setText("Termination Form for " + name.toUpperCase() + " Account " + id + ", "
				+ (DateFormat.getDateInstance(DateFormat.FULL)).format(new Date()));

		label.setFont(new Font("", Font.BOLD, 20));

		// continueBtn = new BlinkingButton(Color.BLUE);
		b.add(continueBtn);
		b.add(Box.createVerticalStrut(18));

		panel.add(label, BorderLayout.NORTH);
		panel.add(lowerpanel, BorderLayout.SOUTH);
		panel.add(createChoicesPanel(), BorderLayout.WEST);

		return panel;
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
		JLabel datelbl = new JLabel("Terminating Membership");
		datelbl.setFont(new Font("", Font.BOLD, 15));
		datelbl.setForeground(Color.RED);

		dialog.getContentPane().add(datelbl, BorderLayout.NORTH);
		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");

		cashierid = new JTextField();
		cashierid.setFont(new Font("Dialog", Font.PLAIN, 19));
		cashierid.addKeyListener(new TextValidator());

		error = new BlinkingLabel("");
		cashierid.addActionListener((event) -> {
			actionListenerMethod();
		});

		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(cashierid);

		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		Box lowerBox = Box.createHorizontalBox();
		JButton defaultButton = new JButton("OK");
		defaultButton.addActionListener(event -> {
			actionListenerMethod();
		});
		lowerBox.add(Box.createHorizontalGlue());
		lowerBox.add(defaultButton);
		lowerBox.add(Box.createVerticalStrut(8));
		JButton cancel = new JButton("Cancel");
		lowerBox.add(cancel);
		cancel.addActionListener(event -> new AnimateDialog().fadeOut(dialog, 100));
		dialog.getContentPane().setBackground(Color.BLUE);
		dialog.getContentPane().add(lowerBox, BorderLayout.SOUTH);
		dialog.getRootPane().setDefaultButton(defaultButton);

		dialog.setSize(300, 200);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		new AnimateDialog().fadeIn(dialog, 100);
		dialog.setAlwaysOnTop(true);
	}

	// this is the method that is invoked whenever a button action is needed
	public void actionListenerMethod() {
		EventQueue.invokeLater(() -> {
			error.setText("");
			id = cashierid.getText();
			String text = "SELECT first_name,last_name FROM members  WHERE member_id = '" + id + "' ";
			try {
				rs = stm.executeQuery(text);
				if (rs.next()) {
					name = rs.getString(1) + " " + rs.getString(2);
					int acc = Integer.parseInt(id);
					String text1 = "SELECT member_id,collected,amount FROM cease  WHERE member_id = '" + id
							+ "' AND year = '" + new SetDateCreated().getYear() + "'";
					rs = stm.executeQuery(text1);
					if (rs.last()) {
						String collected = rs.getString(2);
						double collectedamount = rs.getDouble(3);
						if (collected.equals("sub_only")) {
							this.removeAll();
							this.setLayout(new BorderLayout());
							continueBtn = new BlinkingButton(Color.RED);
							this.add(createMidPanel(acc), BorderLayout.CENTER);
							continueBtn.setText("<html>Release the <br>Remaining Commission</html>");
							continueBtn.setBackground(Color.RED);
							continueBtn.setForeground(Color.BLACK);
							continueBtn.setFont(new Font("Dialog", Font.PLAIN, 18));
							withholdComm.setEnabled(false);
							releaseEverything.setSelected(true);
							continueBtn.addActionListener((event) -> {
								if (remainingCommission > 0) {
									int c = JOptionPane.showConfirmDialog(frame,
											"Name : " + name.toUpperCase() + "\nAccount " + id + " is to be granted $"
													+ remainingCommission
													+ " remaining commission.\nContinue to terminate membership?",
											"Confirm Termination", JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE);
									if (c == JOptionPane.YES_OPTION) {
										continueBtn.setEnabled(false);
										String colllected = "everything";
										cease(id, remainingCommission, colllected);
										AffectLedger a = new AffectLedger(rs, stm);
										a.termination(id, remainingCommission);
										JOptionPane.showMessageDialog(frame, "Member Termination Successful.\nDone!",
												"Termination Successful", JOptionPane.INFORMATION_MESSAGE);
										closeTab();
										SubscriptionOnlyPDF subpdf = new SubscriptionOnlyPDF();
										description = "The account holder has collected everything. A commission of $"
												+ remainingCommission
												+ ", less any bad Debt Recovery Fee, is granted. All amounts here are system generated!";
										subpdf.generatePDF(name, label, table, id, remainingCommission, description);
									} else {
										return;
									}
								} else {
									JOptionPane.showMessageDialog(frame,
											"A Shortfall of $" + Math.abs(remainingCommission) + " is detected.",
											"Error", JOptionPane.ERROR_MESSAGE);
								}
							});
							this.revalidate();
							this.repaint();
							new AnimateDialog().fadeOut(dialog, 100);
						} else if (collected.equals("everything")) {
							error.setText("Account " + id + " already terminated!");
						} else {
							this.removeAll();
							this.setLayout(new BorderLayout());
							continueBtn = new BlinkingButton(Color.RED);
							this.add(createMidPanel(acc), BorderLayout.CENTER);
							continueBtn.setText("<html>Continue to Terminate</html>");
							continueBtn.setBackground(Color.RED);
							continueBtn.setForeground(Color.BLACK);
							continueBtn.setFont(new Font("Dialog", Font.PLAIN, 18));
							withholdComm.setEnabled(false);
							partialterminate.setEnabled(false);
							releaseEverything.setSelected(true);
							continueBtn.addActionListener((event) -> {
								double remainingSub = df.form(totalsubscriptions - collectedamount);
								double actualSub = df.form(remainingSub - debt);
								double takeHome = df.form(actualSub + remainingCommission);

								if (takeHome > 0) {
									int c = JOptionPane.showConfirmDialog(frame,
											"Name : " + name.toUpperCase() + "\nAccount " + id
													+ " once did a Partial termination of $" + collectedamount
													+ ".\nRemaining Subscription $" + remainingSub
													+ "\nNow the Member is to be granted $" + takeHome
													+ " remaining sum of Subscriptions And Commission.\nContinue to terminate membership?",
											"Confirm Termination", JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE);
									if (c == JOptionPane.YES_OPTION) {
										continueBtn.setEnabled(false);
										String colllected = "everything";
										cease(id, takeHome, colllected);
										AffectLedger a = new AffectLedger(rs, stm);
										a.termination(id, takeHome);
										JOptionPane.showMessageDialog(frame, "Member Termination Successful.\nDone!",
												"Termination Successful", JOptionPane.INFORMATION_MESSAGE);
										closeTab();
										SubscriptionOnlyPDF subpdf = new SubscriptionOnlyPDF();
										description = "The account holder has collected everything. A sum of $"
												+ takeHome
												+ ", less any bad Debt Recovery Fee, is granted. All amounts here are system generated!";
										subpdf.generatePDF(name, label, table, id, takeHome, description);
									} else {
										return;
									}
								} else {
									JOptionPane.showMessageDialog(frame,
											"Partial termination amount $" + collectedamount
													+ "\nRemaining Subscription $"
													+ df.form(totalsubscriptions - collectedamount)
													+ "\nOutstanding Debt $" + debt + "\nTotal Commission $"
													+ totalcommission + "\nA Shortfall of $" + Math.abs(takeHome)
													+ " implies insufficient funds for termination.\nThe member is Owing!",
											"Error", JOptionPane.ERROR_MESSAGE);
								}
							});
							this.revalidate();
							this.repaint();
							new AnimateDialog().fadeOut(dialog, 100);
						}
					} else {
						this.removeAll();
						this.setLayout(new BorderLayout());
						continueBtn = new BlinkingButton(Color.BLUE);
						this.add(createMidPanel(acc), BorderLayout.CENTER);
						continueBtn.setText("Continue To Terminate Membership");
						continueBtn.setBackground(Color.BLUE);
						continueBtn.setForeground(Color.BLACK);
						continueBtn.setFont(new Font("Dialog", Font.PLAIN, 18));
						continueBtn.addActionListener((event) -> {
							if (subslessDebts > 0) {
								String colllected = null;
								int c = JOptionPane.showConfirmDialog(frame,
										"Name : " + name.toUpperCase() + "\nAccount " + id
												+ " is terminating.\nContinue to terminate membership?",
										"Confirm Termination", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (c == JOptionPane.YES_OPTION) {
									continueBtn.setEnabled(false);
									if (withholdComm.isSelected()) {
										colllected = "sub_only";
										cease(id, subslessDebts, colllected);
										AffectLedger a = new AffectLedger(rs, stm);
										a.termination(id, subslessDebts);
										JOptionPane.showMessageDialog(frame, "Member Termination Successful.\nDone!",
												"Termination Successful", JOptionPane.INFORMATION_MESSAGE);
										SubscriptionOnlyPDF subpdf = new SubscriptionOnlyPDF();
										description = "Only a total Subscription sum of " + totalsubscriptions
												+ ", less any Outstanding debts, has been granted to the account holder. A COMMISSION of "
												+ totalcommission + " is yet to be processed!";
										subpdf.generatePDF(name, label, table, id, subslessDebts, description);
									} else if (releaseEverything.isSelected()) {
										colllected = "everything";
										cease(id, Double.parseDouble(grant), colllected);
										AffectLedger a = new AffectLedger(rs, stm);
										a.termination(id, Double.parseDouble(grant));
										JOptionPane.showMessageDialog(frame, "Member Termination Successful.\nDone!",
												"Termination Successful", JOptionPane.INFORMATION_MESSAGE);
										closeTab();
										SubscriptionOnlyPDF subpdf = new SubscriptionOnlyPDF();
										description = "The account holder has collected everything. All amounts here are system generated!";
										subpdf.generatePDF(name, label, table, id, Double.parseDouble(grant),
												description);
									} else if (partialterminate.isSelected()) {
										showDialog3();
									} else {
										JOptionPane.showMessageDialog(frame,
												"You forgot to specify the transaction type.", "Warning",
												JOptionPane.WARNING_MESSAGE);
									}
								} else {
									return;
								}
							} else {
								JOptionPane.showMessageDialog(frame,
										name + " is owing $" + Math.abs(subslessDebts)
												+ " to the club!\nNo amount should be given to the member.\nTermination denied!",
										"Termination denied", JOptionPane.ERROR_MESSAGE);
							}
						});
						this.revalidate();
						this.repaint();
						new AnimateDialog().fadeOut(dialog, 100);
					}
				} else {
					new AnimateDialog().fadeIn(dialog, 100);
					error.setText("Account " + id + " DO NOT HONOR!");
				}
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		});
	}

	public JPanel createChoicesPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		Box box = Box.createVerticalBox();
		withholdComm = new JRadioButton("Withhold Commission Only");
		withholdComm.setForeground(Color.WHITE);
		withholdComm.setFont(new Font("Dialog", Font.PLAIN, 17));
		releaseEverything = new JRadioButton("Release Everything");
		releaseEverything.setForeground(Color.WHITE);
		releaseEverything.setFont(new Font("Dialog", Font.PLAIN, 17));
		partialterminate = new JRadioButton("Partial Termination");
		partialterminate.setForeground(Color.WHITE);
		partialterminate.setFont(new Font("Dialog", Font.PLAIN, 17));
		ButtonGroup bg = new ButtonGroup();
		bg.add(withholdComm);
		bg.add(releaseEverything);
		bg.add(partialterminate);
		withholdComm.setSelected(true);
		box.add(Box.createVerticalStrut(50));
		box.add(withholdComm);
		box.add(Box.createVerticalStrut(30));
		box.add(partialterminate);
		box.add(Box.createVerticalStrut(30));
		box.add(releaseEverything);
		panel.add(box, BorderLayout.CENTER);
		return panel;
	}

	public void cease(String id, double diff, String collected) {
		String date = new SetDateCreated().getDate();
		String time = new SetDateCreated().getTime();
		String test = "INSERT INTO cease(member_id,amount,date,time,year,collected)VALUES('" + id + "','" + diff + "','"
				+ date + "','" + time + "','" + new SetDateCreated().getYear() + "','" + collected + "')";
		try {
			stm.execute(test);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void showDialog3() {
		dialog3 = new JDialog((JFrame) null, "Search", true);
		dialog3.setLayout(new BorderLayout());
		dialog3.setIconImage(new IconImage().createIconImage());
		dialog3.setUndecorated(true);
		dialog3.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog3.setShape(new RoundRectangle2D.Double(0, 0, dialog3.getWidth(), dialog3.getHeight(), 5, 5));
			}
		});
		JLabel datelbl = new JLabel("Terminating Membership");
		datelbl.setFont(new Font("", Font.BOLD, 15));
		datelbl.setForeground(Color.RED);

		dialog3.getContentPane().add(datelbl, BorderLayout.NORTH);
		JLabel top = new JLabel("<html><h3>How much do you want to release?, <i>(eg 100.05)</i><h3>");

		cashierid3 = new JTextField();
		cashierid3.setFont(new Font("Dialog", Font.PLAIN, 19));
		cashierid3.addKeyListener(new TextValidator());

		error3 = new BlinkingLabel("");
		cashierid3.addActionListener((event) -> {
			processPartialTermination();
		});

		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(cashierid3);

		error3.setForeground(Color.red);
		midpanel.add(error3);
		dialog3.getContentPane().add(midpanel, BorderLayout.CENTER);
		Box lowerBox = Box.createHorizontalBox();
		JButton defaultButton = new JButton("OK");
		defaultButton.addActionListener(event -> {
			processPartialTermination();
		});
		lowerBox.add(Box.createHorizontalGlue());
		lowerBox.add(defaultButton);
		lowerBox.add(Box.createVerticalStrut(8));
		JButton cancel = new JButton("Cancel");
		lowerBox.add(cancel);
		cancel.addActionListener(event -> new AnimateDialog().fadeOut(dialog3, 100));
		dialog3.getContentPane().setBackground(new Color(10, 75, 100));
		dialog3.getContentPane().add(lowerBox, BorderLayout.SOUTH);
		dialog3.getRootPane().setDefaultButton(defaultButton);

		dialog3.setSize(300, 165);
		Dimension d = dialog3.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog3.setLocation(x, y);
		new AnimateDialog().fadeIn(dialog3, 100);
		dialog3.setAlwaysOnTop(true);
	}

	public double returnPartialAmount() {
		double value = 0;
		if (!cashierid3.equals("")) {
			String amnt = cashierid3.getText();
			value = Double.parseDouble(amnt);
			new AnimateDialog().fadeOut(dialog3, 100);

		} else {
			error3.setText("Enter a valid amount!");
			new AnimateDialog().fadeIn(dialog3, 100);
		}
		return value;
	}

	public void processPartialTermination() {
		partialValue = returnPartialAmount();
		String colllected = "partial_sub";

		if (totalsubscriptions > partialValue) {
			if (partialValue > 0) {
				cease(id, partialValue, colllected);
				AffectLedger a = new AffectLedger(rs, stm);
				a.termination(id, partialValue);
				JOptionPane.showMessageDialog(frame, "Partial Member Termination Successful.\nDone!",
						"Termination Successful", JOptionPane.INFORMATION_MESSAGE);
				closeTab();
				SubscriptionOnlyPDF subpdf = new SubscriptionOnlyPDF();
				description = "Only a Total Partial Subscription sum of " + partialValue
						+ ", less any Outstanding debts, has been granted to the account holder. A COMMISSION of "
						+ totalcommission + " is yet to be processed!";
				subpdf.generatePDF(name, label, table, id, partialValue, description);
			} else {
				JOptionPane.showMessageDialog(frame, "We cannot process a Partial Termination of $0.00",
						"Invalid Amount", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane
					.showMessageDialog(frame,
							"$" + partialValue + " is greater than floating balance of $" + subslessDebts
									+ ". \nReduce the cash payout amount!",
							"Invalid Amount", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void closeTab() {
		new RemoveTab(tabs).removeTab("Termination");
	}
}
