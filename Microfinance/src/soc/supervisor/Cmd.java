/*
 * To change this template,

 choose Tools | Templates
 * and open the template in the editor.
 */
package soc.supervisor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.Timer;

import com.toedter.calendar.JDateChooser;

import externals.apps.OpenPackages;
import soc.accounts.search.GenerateAccPDF;
import soc.accounts.search.SearchAccount;
import soc.activity.search.SearchActivity;
import soc.backup.BackUp;
import soc.baddebts.BadDebtsList;
import soc.bank.Chooser;
import soc.bank.TransactionsPanel;
import soc.borrowers.limit.LimitDialog;
import soc.borrowingwindow.BorrowWindow;
import soc.borrowingwindow.Reimbursement;
import soc.commi.search.CommissionAccSearch;
import soc.commission.swip.Swip;
import soc.countregistered.Borrowers;
import soc.countregistered.FindMember;
import soc.countregistered.MemberShares;
import soc.countregistered.RegisteredMembers;
import soc.countregistered.Subscribers;
import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;
import soc.expenses.post.PostExpense;
import soc.expenses.search.SearchExpenses;
import soc.helpers.AccessDbase;
import soc.helpers.ComputerExplorer;
import soc.helpers.CustomTabbedPaneUI;
import soc.helpers.DeleteFile;
import soc.helpers.IconImage;
import soc.helpers.LookAndFeelClass;
import soc.helpers.RemoveTab;
import soc.helpers.Ribbon1;
import soc.helpers.SetDateCreated;
import soc.helpers.TabMouseMotionListener;
import soc.helpers.TextFinder;
import soc.helpers.TextValidator;
import soc.helpers.ToolbarPopup2;
import soc.helpers.TreeMouseListener;
import soc.ledger.manage.Deposit;
import soc.ledger.manage.Withdrawal;
import soc.ledger.search.SearchLedger;
import soc.managementacc.search.ManagementAccSearch;
import soc.overdue.check.OverDue;
import soc.reconciliation.Reconcile;
import soc.registration.Register;
import soc.reports.generate.Find;
import soc.subscribe.Subscribe;
import soc.subscriptions.search.SearchSubscriptions;
import soc.termination.Terminate;
import soc.users.NewUser;
import socl.portal.browse.BrowseMenu;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Cmd extends JFrame implements ActionListener {
	public JToolBar toolbar;
	JPanel mainPanel, upperPanel, centerPanel, Panel;

	JLabel allLabels[], hintlabels[], error;
	JTextArea display;
	JScrollPane scroller;
	public JMenuBar mnb;
	JMenu fl, ed, wnd, hlp, con, sa;
	JMenuItem ns, of, s, pr, ph, exi;
	JMenuItem und, red, cut, cop, past, del, find, repl, post_reitem, particular, commswip, endofdatp, d_etails;
	JMenuItem nc, rec, tc, disc;
	JMenuItem hc, ks, search, report, about;
	JMenuItem ccl, aut, max, min, clw, rsw, all_mem;
	public AccessDbase adbase;
	LookAndFeelClass looks;
	public JTextField cashierid, navi;
	JDialog dialog;
	JSplitPane split;
	public JTabbedPane tabs, lefttabs;
	JTree tree;
	public SetDateCreated setdate;
	JLabel tellername, numberoftrans, timelbl;

	String labels[] = { "Transactions", "", "Debit", "Credit", "Balance" };
	String notes[] = { "Ecs:Exit, ", "PgUp:Scroll Up, ", "PgDn:Scroll Down, ", "End:Last Record, ", "F1:More Help",
			"[ENTER]:Next Page" };
	private JButton find_trans;
	private JButton post_reimb;
	private JButton teller_totals;
	private JButton all_members;
	private JButton sub_mem;
	private JButton st_o_day;
	private JButton e_o_day;
	private JButton new_conn;
	private JButton re_conn;
	private JButton test_conn;
	private JButton dis_conn;
	private JButton conf;
	private JDateChooser datechooser;
	private String dateString;

	private JButton new_reg;
	private JMenuItem newreg;
	private JButton subscribe;
	private JDateChooser datechoosert;
	private String tdateString;
	private JButton lend_money;
	private JButton post_exp;
	private JButton sub_sc;
	private JMenuItem bo_item;
	private JMenuItem memsh;
	private JButton mem_share;
	private JMenuItem prpdf;
	private JButton wtled;
	private JButton dbled;
	private JMenuItem new_user;
	private JButton find_mem;
	private JButton defaultButton;
	private JButton overdue;
	private JButton back_up;
	private JButton extract;
	private JButton gen_rev;
	private JMenuItem borr_limit;
	static String basicWindowTitle = "Cashier - FMS Session AA: CMD";
	String changetitle = null;
	private JButton bad_debts;
	private JButton commit_trans;

	public Cmd() {
		super(basicWindowTitle);
		adbase = new AccessDbase();
		adbase.connectionDb();
		looks = new LookAndFeelClass();
		looks.setLookAndFeels();
		setdate = new SetDateCreated();
		begin();
		// lp = new ListPopup(this);
		tabs.addChangeListener((listen) -> {
			EventQueue.invokeLater(() -> {
				int count = tabs.getTabCount();
				for (int x = 0; x < count; x++) {
					changetitle = tabs.getTitleAt(x).trim();
					this.setTitle(basicWindowTitle + " - " + changetitle);
				}
			});
		});

		tabs.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ev) {
				for (int i = 0; i < tabs.getTabCount(); i++) {
					changetitle = tabs.getTitleAt(i).trim();
					setTitle(basicWindowTitle + " - " + changetitle);
				}
			}
		});
	}

	// this is the main window conponent build up that constructs all components
	// and widgets
	public final void begin() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setBackground(Color.BLUE);
		mainPanel = new JPanel();
		centerPanel = new JPanel();
		Panel = new JPanel();
		upperPanel = new JPanel();

		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				DeleteFile d = new DeleteFile(adbase.rs, adbase.stm);
				DeleteFile.CleanUpWorker w = d.new CleanUpWorker();
				w.execute();
			}
		};
		// adding a window listener to the actual main top level container
		addWindowListener(listener);
		// setting the default close operation for the top level window here
		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mnb = new JMenuBar();
		setJMenuBar(mnb);

		fl = new JMenu("File");
		mnb.add(fl);
		JMenu newmenu = new JMenu("New");
		newreg = new JMenuItem("Member Registration");
		newreg.addActionListener(this);
		new_user = new JMenuItem("System User");
		new_user.addActionListener(this);
		newmenu.add(newreg);
		newmenu.addSeparator();
		newmenu.add(new_user);
		fl.add(newmenu);

		of = new JMenuItem("Open File");
		of.setEnabled(false);
		fl.add(of);
		fl.addSeparator();
		sa = new JMenu("Cashiers...");
		sa.setEnabled(false);
		fl.add(sa);

		post_reitem = new JMenuItem("Post Reimbursement");
		post_reitem.addActionListener(this);

		sa.add(post_reitem);
		// sa.add(particular);

		s = new JMenuItem("Save");
		s.addActionListener(this);
		fl.add(s);
		fl.addSeparator();

		JMenu printmenu = new JMenu("Print...");

		prpdf = new JMenuItem("Print To PDF");
		prpdf.addActionListener(this);

		ph = new JMenuItem("Print to HTML...");
		ph.setEnabled(false);
		pr = new JMenuItem("Send To Printer");
		pr.addActionListener(this);

		printmenu.add(prpdf);
		printmenu.addSeparator();
		printmenu.add(ph);
		printmenu.addSeparator();
		printmenu.add(pr);

		fl.add(printmenu);

		fl.addSeparator();
		exi = new JMenuItem("Exit");
		fl.add(exi);
		exi.addActionListener(this);

		ed = new JMenu("Edit");
		mnb.add(ed);
		und = new JMenuItem("Undo");
		und.setEnabled(false);
		ed.add(und);
		red = new JMenuItem("Redo");
		red.setEnabled(false);
		ed.add(red);
		ed.addSeparator();
		cut = new JMenuItem("Cut");
		ed.add(cut);
		cop = new JMenuItem("Copy");
		ed.add(cop);
		past = new JMenuItem("Paste");
		ed.add(past);
		ed.addSeparator();
		del = new JMenuItem("Delete");
		del.setEnabled(false);
		ed.add(del);
		find = new JMenuItem("Find");
		find.addActionListener(this);
		ed.add(find);
		repl = new JMenuItem("Replace...");
		repl.setEnabled(false);
		borr_limit = new JMenuItem("Borrowers Limit");
		borr_limit.addActionListener(this);
		ed.add(repl);
		ed.addSeparator();
		ed.add(borr_limit);

		JMenu repr = new JMenu("Search");
		ns = new JMenuItem("New Session");
		ns.addActionListener(this);
		repr.add(ns);
		repr.addSeparator();
		all_mem = new JMenuItem("All Members");
		all_mem.addActionListener(this);
		repr.add(all_mem);
		repr.addSeparator();
		d_etails = new JMenuItem("Subscribed Members");
		d_etails.addActionListener(this);
		repr.add(d_etails);
		repr.addSeparator();
		bo_item = new JMenuItem("Debtors");
		bo_item.addActionListener(this);
		repr.add(bo_item);
		repr.addSeparator();
		memsh = new JMenuItem("Member Shares List");
		memsh.addActionListener(this);
		repr.add(memsh);
		mnb.add(repr);

		JMenu pstngs = new JMenu("Run");
		mnb.add(pstngs);
		commswip = new JMenuItem("Commission Sweep");
		commswip.addActionListener(this);
		endofdatp = new JMenuItem("Membership Termination");
		endofdatp.addActionListener(this);
		particular = new JMenuItem("Reconciliation Statement");
		particular.addActionListener(this);
		pstngs.add(commswip);
		pstngs.addSeparator();
		pstngs.add(endofdatp);
		pstngs.addSeparator();
		pstngs.add(particular);

		con = new JMenu("Connections");
		mnb.add(con);
		nc = new JMenuItem("New Connection");
		nc.setEnabled(false);
		con.add(nc);
		rec = new JMenuItem("Reconnect");
		rec.setEnabled(false);
		con.add(rec);
		tc = new JMenuItem("Test Connection");
		tc.setEnabled(false);
		con.add(tc);
		disc = new JMenuItem("Disconnect");
		disc.addActionListener(this);
		con.add(disc);

		// mnb.add(ExecuteMessanger.chatMenu());

		wnd = new JMenu("Window");
		mnb.add(wnd);
		ccl = new JMenuItem("Change Background");
		ccl.addActionListener(this);
		wnd.add(ccl);
		aut = new JMenuItem("Auto Scroll");
		wnd.add(aut);
		wnd.addSeparator();
		max = new JMenuItem("Maximise Window");
		max.setEnabled(false);
		wnd.add(max);
		min = new JMenuItem("Minimise Window");
		min.setEnabled(false);
		wnd.add(min);
		clw = new JMenuItem("Close Window");
		clw.setEnabled(false);
		wnd.add(clw);
		rsw = new JMenuItem("Reset Window");
		rsw.setEnabled(false);
		wnd.add(rsw);

		mnb.add(new BrowseMenu());

		hlp = new JMenu("Help");
		mnb.add(hlp);
		hc = new JMenuItem("Help Content");
		hlp.add(hc);
		search = new JMenuItem("Search...");
		hlp.add(search);
		hlp.addSeparator();
		ks = new JMenuItem("Keys Shortcuts");
		hlp.add(ks);
		report = new JMenuItem("Report An Issue");
		hlp.add(report);
		about = new JMenuItem("About...");
		hlp.add(about);

		allLabels = new JLabel[6];
		hintlabels = new JLabel[notes.length];
		display = new JTextArea("");
		display.setEditable(false);
		display.setLineWrap(true);
		display.setMargin(new Insets(30, 30, 30, 30));
		display.setBackground(Color.BLACK);
		display.setForeground(Color.white);
		display.setFont(new Font("", Font.ROMAN_BASELINE, 19));
		display.getCaret().setSelectionVisible(true);
		display.getCaret().setVisible(true);

		scroller = new JScrollPane(display);
		scroller.setBorder(null);
		mainPanel = new JPanel(new BorderLayout());
		Panel = new JPanel(new FlowLayout());
		upperPanel = new JPanel(new GridLayout(1, 6, 5, 5));

		for (int x = 0; x < labels.length; x++) {
			allLabels[x] = new JLabel(labels[x]);
			allLabels[x].setSize(3, 3);
			allLabels[x].setForeground(Color.RED);
			allLabels[x].setFont(new Font("", Font.BOLD, 20));
			upperPanel.add(allLabels[x]);
		}

		for (int y = 0; y < notes.length; y++) {
			hintlabels[y] = new JLabel(notes[y]);
			hintlabels[y].setSize(2, 3);
			hintlabels[y].setForeground(Color.BLUE);
			hintlabels[y].setFont(new Font("", Font.ITALIC + Font.BOLD, 13));
			Panel.add(hintlabels[y]);
		}
		navi = new JTextField(20);
		navi.addActionListener(this);
		Panel.add(new JLabel(" Search:"));
		Panel.add(navi);

		mainPanel.add(upperPanel, BorderLayout.NORTH);
		mainPanel.add(scroller, BorderLayout.CENTER);
		mainPanel.add(Panel, BorderLayout.SOUTH);

		tellername = new JLabel();
		tellername.setFont(new Font("Arial", Font.PLAIN, 20));
		tellername.setForeground(Color.WHITE);
		numberoftrans = new JLabel();
		numberoftrans.setFont(new Font("Arial", Font.PLAIN, 20));
		numberoftrans.setForeground(Color.WHITE);
		timelbl = new JLabel();
		timelbl.setFont(new Font("Arial", Font.PLAIN, 20));
		timelbl.setForeground(Color.WHITE);

		tree = ComputerExplorer.createExplorer();
		tree.addMouseListener(new TreeMouseListener(tree, this));

		JPanel mailpanel = new TranslucentJPanel1(Color.BLACK);
		mailpanel.setLayout(new BorderLayout());
		// mailpanel.setBorder(new TitledBorder(""));
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(tree);
		mailpanel.add(scroll, BorderLayout.CENTER);

		lefttabs = new JTabbedPane();
		lefttabs.setFont(new Font("Dialog", Font.PLAIN, 20));
		lefttabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		lefttabs.addMouseMotionListener(new TabMouseMotionListener());
		lefttabs.addTab("My Computer", null, mailpanel, "My Computer");
		lefttabs.addTab("MS Office", null, new OpenPackages(), "Write Reports, Documents And other Files");

		tabs = new JTabbedPane();
		tabs.setFont(new Font("Dialog", Font.PLAIN, 20));
		tabs.setForeground(Color.WHITE);
		tabs.setBackground(Color.BLUE);

		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabs.setUI(new CustomTabbedPaneUI());

		JPanel deco = new TranslucentJPanel1(Color.BLACK);
		deco.setLayout(new GridLayout(3, 1));
		JLabel eq4 = new JLabel("<html><u>C A S H I E R 1</u></html>");
		eq4.setFont(new Font("", Font.BOLD, 100));
		eq4.setForeground(Color.WHITE);
		JLabel lin = new JLabel(
				"_________________________________________________________________________________________________");
		lin.setForeground(Color.WHITE);
		JLabel md = new JLabel("Financial Management Service");
		md.setFont(new Font("", Font.ITALIC, 30));
		md.setForeground(Color.WHITE);
		deco.add(eq4);
		deco.add(lin);
		deco.add(md);

		JPanel innert = new JPanel();
		innert.setOpaque(false);
		JPanel innerb = new JPanel();
		innerb.setOpaque(false);
		JPanel innerl = new JPanel();
		innerl.setOpaque(false);
		JPanel innerr = new JPanel(new BorderLayout());
		Box versionBox = Box.createHorizontalBox();
		JLabel versionLabel = new JLabel("Product version 1.0.2 - Product ID 005");
		versionLabel.setFont(new Font("", Font.ITALIC, 17));
		JLabel serviceproviderLabel = new JLabel("Copyright @2016. (Wellington Mapiku)");
		serviceproviderLabel.setFont(new Font("", Font.ITALIC, 11));
		versionBox.add(serviceproviderLabel);
		versionBox.add(Box.createHorizontalGlue());
		versionBox.add(versionLabel);
		innerr.add(versionBox);
		innerr.setOpaque(false);

		JPanel tabp = new JPanel(new BorderLayout(100, 100));
		tabp.setBackground(new Color(10, 75, 100));
		// tabp.setBackground(new Color(150, 75, 190));

		tabp.add(deco, BorderLayout.CENTER);
		tabp.add(innert, BorderLayout.WEST);
		tabp.add(innerb, BorderLayout.EAST);
		tabp.add(innerl, BorderLayout.NORTH);
		tabp.add(innerr, BorderLayout.SOUTH);

		// starting of the tab, configuhring how the tab is going to display the
		// first window.
		tabs.addTab("Start   ", null, tabp, "Start");
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerSize(10);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(200);
		split.add(lefttabs);
		// split.add(tabs);
		Color cv = new Color(0.5f, 0.3f, 1f);
		JPanel centerpanel = new JPanel(new BorderLayout());
		centerpanel.setBackground(cv);
		centerpanel.setBorder(null);
		centerpanel.add(tabs, BorderLayout.CENTER);
		split.add(centerpanel);

		toolbar.add(createToolbar());
		toolbar.add(Box.createHorizontalGlue());
		toolbar.setComponentPopupMenu(new ToolbarPopup2(this));

		this.setLayout(new BorderLayout());
		JPanel toppanell = new JPanel();
		toppanell.setLayout(new BorderLayout());
		Color c = new Color(0.5f, 0.5f, 1f);
		toppanell.setBackground(c);
		toppanell.add(toolbar, BorderLayout.CENTER);
		/*
		 * JLayeredPane layeredPane = new JLayeredPane();
		 * 
		 * FadePanel fadePanel = new FadePanel(this); fadePanel.setLocation(0,
		 * 0); fadePanel.setSize(fadePanel.getPreferredSize());
		 * 
		 * JPanel bluePanel = new JPanel(new BorderLayout());
		 * bluePanel.setBackground(Color.blue);
		 * bluePanel.setSize(fadePanel.getPreferredSize());
		 * bluePanel.add(toppanell, BorderLayout.NORTH); bluePanel.add(split,
		 * BorderLayout.CENTER);
		 * 
		 * layeredPane.setPreferredSize(fadePanel.getPreferredSize());
		 * layeredPane.add(bluePanel, JLayeredPane.DEFAULT_LAYER);
		 * layeredPane.add(fadePanel, JLayeredPane.PALETTE_LAYER);
		 */

		this.getContentPane().add(toppanell, BorderLayout.NORTH);
		this.getContentPane().add(split, BorderLayout.CENTER);
		this.setIconImage(new IconImage().createIconImage());
		// this.getContentPane().add(layeredPane,BorderLayout.CENTER);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screen.width, screen.height);
		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Session AA_FND")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Session AA_FND   ", null, detPanel(), "Run queries, find transactions,e.t.c");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ccl) {
			EventQueue.invokeLater(() -> {
				Color cl = new Color(153, 153, 204);
				cl = JColorChooser.showDialog(this, "Choose Background Color", cl);
				display.setBackground(cl);
				display.repaint();
			});
		}
		if (e.getSource() == exi || e.getSource() == disc) {
			int x = JOptionPane.showConfirmDialog(this,
					"You are force quitting this session!\nYou will need to disconnect.\nAre you sure to proceed?",
					"Force Disconnect", JOptionPane.YES_NO_OPTION);
			if (x == JOptionPane.YES_OPTION) {
				System.exit(0);
			} else {
				return;
			}
		}
		if (e.getSource() == s) {
			EventQueue.invokeLater(() -> {
				JFileChooser fchooser = new JFileChooser();
				File selectedfile = new File("txn.txt");
				fchooser.setSelectedFile(selectedfile);
				fchooser.setDialogTitle("Save file as...");
				int option = fchooser.showSaveDialog(this);
				if (option != JFileChooser.APPROVE_OPTION) {
					return;
				}
				selectedfile = fchooser.getSelectedFile();
				if (selectedfile.exists()) {
					int response = JOptionPane.showConfirmDialog(this,
							"The file \"" + selectedfile.getName() + "\" already exist. \nDo you want to replace it?",
							"Saving...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (response != JOptionPane.YES_OPTION) {
						return;
					}
				}
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(selectedfile)));
					out.print(display.getText());
					out.close();
					if (out.checkError()) {
						throw new IOException("Error checking failed");
					}
				} catch (Exception xe) {
					JOptionPane.showMessageDialog(this, "File format Error \n" + xe.getMessage(), "Error code 99",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		if (e.getSource() == ns || e.getSource() == find || e.getSource() == find_trans) {
			EventQueue.invokeLater(() -> {
				display.setText("");
				insertTab();
				tellername.setText("");
				numberoftrans.setText("");
				showDialog();
				cashierid.requestFocusInWindow();
			});
		}
		if (e.getSource() == cashierid || e.getSource() == defaultButton) {
			EventQueue.invokeLater(() -> {
				display.setText("");
				tellername.setText("");
				numberoftrans.setText("");
				searchData();
				dialog.dispose();
			});
		}
		if (e.getSource() == pr) {
			EventQueue.invokeLater(() -> {
				try {
					// boolean complete = detPanel().print(null);
					boolean complete = display.print();
					if (complete) {
						JOptionPane.showMessageDialog(this, "Finished!", "Done", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "...printing...", "Progress", JOptionPane.WARNING_MESSAGE);
					}
				} catch (PrinterException ex) {
					JOptionPane.showMessageDialog(this, "Printer JAM", "Error Code 90\n" + ex.getMessage(),
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		if (e.getSource() == post_reitem || e.getSource() == post_reimb) {
			new RemoveTab(tabs).removeTab("Reimbursement");
			Reimbursement reimb = new Reimbursement(tabs, adbase.rs, adbase.rs1, adbase.stm, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				reimb.showDialog();
			});
		}
		if (e.getSource() == dbled) {
			new RemoveTab(tabs).removeTab("Investor Deposit");
			Deposit reimb = new Deposit(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				reimb.insertTab();
			});
		}
		if (e.getSource() == wtled) {
			new RemoveTab(tabs).removeTab("Investor Withdrawal");
			Withdrawal reimb = new Withdrawal(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				reimb.getAllValues();
			});
		}
		if (e.getSource() == particular || e.getSource() == teller_totals) {
			// reconcilliation statement right here
			new RemoveTab(tabs).removeTab("Reconciliation");
			Reconcile r = new Reconcile(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				r.showDialog();
			});
		}
		if (e.getSource() == prpdf) {
			// pdf
			GenerateAccPDF gnp = new GenerateAccPDF(adbase.rs, adbase.stm, dateString, tdateString, display,
					tellername);
			GenerateAccPDF.Worker w = gnp.new Worker();
			w.execute();
		}

		if (e.getSource() == lend_money) {
			new RemoveTab(tabs).removeTab("Lending");
			BorrowWindow brw = new BorrowWindow(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				brw.insertTab();
			});
		}

		if (e.getSource() == post_exp) {
			new RemoveTab(tabs).removeTab("Expenses");
			PostExpense pe = new PostExpense(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				pe.insertTab();
			});
		}
		if (e.getSource() == all_mem || e.getSource() == all_members) {
			new RemoveTab(tabs).removeTab("All Members");
			RegisteredMembers regm = new RegisteredMembers(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				regm.insertTab();
			});
		}
		if (e.getSource() == mem_share || e.getSource() == memsh) {
			new RemoveTab(tabs).removeTab("Shares");
			MemberShares regm = new MemberShares(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				regm.showDialog();
			});
		}
		if (e.getSource() == commswip || e.getSource() == st_o_day) {
			new RemoveTab(tabs).removeTab("Commission Sweep");
			Swip swp = new Swip(tabs, adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				swp.insertTab();
			});
		}
		if (e.getSource() == endofdatp || e.getSource() == e_o_day) {
			new RemoveTab(tabs).removeTab("Termination");
			Terminate t = new Terminate(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				t.insertTab();
			});
		}
		if (e.getSource() == navi) {
			EventQueue.invokeLater(() -> {
				new TextFinder().find(navi.getText(), display);
			});
		}
		if (e.getSource() == d_etails || e.getSource() == sub_mem) {
			new RemoveTab(tabs).removeTab("Subscribed");
			Subscribers scb = new Subscribers(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				scb.showDialog();
			});
		}
		if (e.getSource() == bo_item || e.getSource() == sub_sc) {
			new RemoveTab(tabs).removeTab("Borrowers");
			Borrowers b = new Borrowers(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				b.showDialog();
			});
		}
		if (e.getSource() == newreg || e.getSource() == new_reg) {
			EventQueue.invokeLater(() -> {
				Register register = new Register(tabs, adbase.rs, adbase.stm, adbase.pstmt, adbase.conn, this);
				register.insertTab();
			});
		}
		if (e.getSource() == subscribe) {
			EventQueue.invokeLater(() -> {
				Subscribe subsc = new Subscribe(tabs, adbase.rs, adbase.stm, adbase.pstmt, adbase.conn, this);
				subsc.insertTab();
			});
		}
		if (e.getSource() == new_user) {
			new RemoveTab(tabs).removeTab("New User");
			NewUser u = new NewUser(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				u.insertTab();
			});
		}
		if (e.getSource() == overdue) {
			new RemoveTab(tabs).removeTab("OverDue");
			OverDue u = new OverDue(tabs, adbase.rs, adbase.stm, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				u.insertTab();
			});
		}
		if (e.getSource() == bad_debts) {
			new RemoveTab(tabs).removeTab("Bad Debts");
			BadDebtsList bdlist = new BadDebtsList(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				bdlist.insertTab();
			});
		}
		if (e.getSource() == commit_trans) {
			TransactionsPanel t = new TransactionsPanel(adbase.rs, adbase.stm, tabs);
			EventQueue.invokeLater(() -> {
				t.insertTab();
			});
		}

		if (e.getSource() == find_mem) {
			// finding the individual members here
			new RemoveTab(tabs).removeTab("Find");
			FindMember f = new FindMember(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				f.showDialog();
			});
		}
		if (e.getSource() == back_up) {
			BackUp b = new BackUp(this);
			BackUp.Worker w = b.new Worker();
			w.execute();
		}
		if (e.getSource() == extract) {
			new RemoveTab(tabs).removeTab("General Info");
			Find f = new Find(tabs, adbase.rs, adbase.stm, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				f.showDialog();
			});
		}
		if (e.getSource() == gen_rev) {
			Chooser ch = new Chooser(tabs, adbase.rs, adbase.stm, this);
			EventQueue.invokeLater(() -> {
				ch.showDialog();
			});
		}
		if (e.getSource() == borr_limit) {
			LimitDialog l = new LimitDialog(adbase.stm, adbase.rs);
			l.showDialog();
		}
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel datelbl = new JLabel("From (date):");
		JLabel datelblt = new JLabel("To (date):");
		Date todaysdate = new Date();
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechooser.setDate(todaysdate);
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert.setDate(todaysdate);
		JPanel datepanel = new JPanel(new GridLayout(2, 2));
		datepanel.add(datelbl);
		datepanel.add(datechooser);
		datepanel.add(datelblt);
		datepanel.add(datechoosert);
		dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");

		cashierid = new JTextField();
		cashierid.addActionListener(this);
		cashierid.addKeyListener(new TextValidator());
		cashierid.requestFocusInWindow();
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.add(top);
		midpanel.add(cashierid);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		defaultButton = new JButton("Ok");
		defaultButton.addActionListener(this);
		dialog.getRootPane().setDefaultButton(defaultButton);
		Box defaultButtonBox = Box.createHorizontalBox();
		defaultButtonBox.add(Box.createHorizontalGlue());
		defaultButtonBox.add(defaultButton);
		dialog.getContentPane().add(defaultButtonBox, BorderLayout.SOUTH);

		dialog.setSize(300, 200);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

	private JPanel detPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(null);
		p.setBackground(Color.BLACK);
		// start the clock
		Timer timer = new Timer(1000, new Listener());
		timer.start();

		JLabel lbl = new JLabel("*** Transaction History Enquiry ***");
		lbl.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl.setForeground(Color.WHITE);

		Box bc = Box.createVerticalBox();
		bc.add(lbl);
		bc.add(tellername);

		p.add(bc, BorderLayout.WEST);
		p.add(timelbl, BorderLayout.CENTER);
		p.add(numberoftrans, BorderLayout.EAST);

		JPanel mp = new TranslucentJPanel(Color.BLUE);
		mp.setLayout(new BorderLayout());
		mp.add(p, BorderLayout.NORTH);
		mainPanel.setOpaque(false);
		mp.add(mainPanel, BorderLayout.CENTER);

		return mp;
	}

	public void searchData() {
		String id = cashierid.getText();
		// int cid = Integer.parseInt(id);
		Date fdate = datechooser.getDate();
		dateString = String.format("%1$tY-%1$tm-%1$td", fdate);
		Date tdate = datechoosert.getDate();
		tdateString = String.format("%1$tY-%1$tm-%1$td", tdate);
		if (id.equals("4131")) {
			tellername.setText("General Ledger 4131");
			SearchLedger searchledger = new SearchLedger(adbase.rs, adbase.stm, this);
			searchledger.search(dateString, tdateString, display);
		} else if (id.equals("4141")) {
			tellername.setText("Subscriptions Pool 4141");
			SearchSubscriptions searchsub = new SearchSubscriptions(adbase.rs, adbase.stm, this);
			searchsub.search(dateString, tdateString, display);
		} else if (id.equals("4151")) {
			tellername.setText("Main Activity Account 4151");
			SearchActivity sa = new SearchActivity(adbase.rs, adbase.stm, this);
			sa.search(dateString, tdateString, display);
		} else if (id.equals("4161")) {
			tellername.setText("Commission Suspense Account 4161");
			CommissionAccSearch cas = new CommissionAccSearch(adbase.rs, adbase.stm, this);
			cas.search(dateString, tdateString, display);
		} else if (id.equals("4171")) {
			tellername.setText("Expenses Account 4171");
			SearchExpenses ex = new SearchExpenses(adbase.rs, adbase.stm, this);
			ex.search(dateString, tdateString, display);
		} else if (id.equals("4181")) {
			tellername.setText("Mamagement Account  4181");
			ManagementAccSearch ex = new ManagementAccSearch(adbase.rs, adbase.stm, this);
			ex.search(dateString, tdateString, display);
		} else {
			SearchAccount s = new SearchAccount(adbase.rs, adbase.stm, this);
			s.search(id, dateString, tdateString, display, tellername);
		}
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

	public JTabbedPane createToolbar() {
		JTabbedPane pane = new JTabbedPane();
		pane.setUI(new Ribbon1());

		JToolBar file = new JToolBar();
		file.setBackground(Color.BLUE);
		file.setFloatable(false);
		file.setRollover(true);
		new_reg = new JButton("<html><p>New<br>Registration</p></html>");
		new_reg.addActionListener(this);
		subscribe = new JButton("<html><p>Subscribe<br>Top Up</p></html>");
		subscribe.addActionListener(this);

		lend_money = new JButton("<html><p>Lend<br>Money</p></html>");
		lend_money.addActionListener(this);
		post_exp = new JButton("<html><p> Post<br>Expenses</p></html>");
		post_exp.addActionListener(this);
		post_reimb = new JButton("<html><p>Post<br>Reimbursement</p></html>");
		post_reimb.addActionListener(this);

		wtled = new JButton("<html><p> Investor<br>Withdrawal</p></html>");
		wtled.addActionListener(this);
		dbled = new JButton("<html><p> Investor<br>Deposit</p></html>");
		dbled.addActionListener(this);
		file.add(new_reg);
		file.addSeparator();
		file.add(subscribe);
		file.addSeparator();
		// file.add(find_trans);
		// file.addSeparator();
		file.add(lend_money);
		file.addSeparator();
		file.add(post_exp);
		file.addSeparator();
		file.add(post_reimb);
		file.addSeparator();
		file.add(dbled);
		file.addSeparator();
		file.add(wtled);
		file.addSeparator();

		JToolBar seach = new JToolBar();
		seach.setFloatable(false);
		seach.setRollover(true);
		all_members = new JButton("<html><p>All<br>Members</p></html>");
		all_members.addActionListener(this);
		sub_mem = new JButton("<html><p>Subscribed<br>Members</p></html>");
		sub_mem.addActionListener(this);
		sub_sc = new JButton("<html><p>Borrowers/<br>Debtors</p></html>");
		sub_sc.addActionListener(this);
		mem_share = new JButton("<html><p>Member<br>Shares</p></html>");
		mem_share.addActionListener(this);
		find_trans = new JButton("<html><p>Session<br>Manager</p></html>");
		find_trans.addActionListener(this);
		find_mem = new JButton("<html><p>Find<br>Member</p></html>");
		find_mem.addActionListener(this);
		overdue = new JButton("<html><p>Search Overdue<br>Debtors</p></html>");
		overdue.addActionListener(this);
		extract = new JButton("<html><p>Extract Transactions<br>Report</p></html>");
		extract.addActionListener(this);
		bad_debts = new JButton("<html><p>Search<br>Bad Debts</p></html>");
		bad_debts.addActionListener(this);
		commit_trans = new JButton("<html><p>Committee Members<br>Transactions List</p></html>");
		commit_trans.addActionListener(this);
		seach.add(find_trans);
		seach.addSeparator();
		seach.addSeparator();
		seach.addSeparator();
		seach.add(find_mem);
		seach.addSeparator();
		seach.add(all_members);
		seach.addSeparator();
		seach.add(sub_mem);
		seach.addSeparator();
		seach.add(sub_sc);
		seach.addSeparator();
		seach.add(mem_share);
		seach.addSeparator();
		seach.add(extract);
		seach.addSeparator();
		seach.add(overdue);
		seach.addSeparator();
		seach.add(bad_debts);
		seach.addSeparator();
		seach.add(commit_trans);

		JToolBar run = new JToolBar();
		run.setFloatable(false);
		run.setRollover(true);
		st_o_day = new JButton("<html><p>Commission<br>Sweep</p></html>");
		st_o_day.addActionListener(this);
		e_o_day = new JButton("<html><p>Termination<br>Form</p></html>");
		e_o_day.addActionListener(this);
		teller_totals = new JButton("<html><p>Reconciliation<br>Statement</p></html>");
		teller_totals.addActionListener(this);
		back_up = new JButton("<html><p>System<br>BackUp</p></html>");
		back_up.addActionListener(this);
		gen_rev = new JButton("<html><p>General Revenue<br>Suspense Transaction</p></html>");
		gen_rev.addActionListener(this);
		run.add(st_o_day);
		run.addSeparator();
		run.add(e_o_day);
		run.addSeparator();
		run.add(teller_totals);
		run.add(gen_rev);
		run.addSeparator();
		run.add(back_up);
		run.addSeparator();

		JToolBar conn = new JToolBar();
		conn.setFloatable(false);
		conn.setRollover(true);
		new_conn = new JButton("<html><p>New<br>Connection</p></html>");
		new_conn.setEnabled(false);
		re_conn = new JButton("<html><p>New<br>Connection</p></html>");
		re_conn.setEnabled(false);
		test_conn = new JButton("<html><p>New<br>Connection</p></html>");
		test_conn.setEnabled(false);
		dis_conn = new JButton("<html><p>New<br>Connection</p></html>");
		conf = new JButton("<html><p>New<br>Conference</p></html>");

		conn.add(new_conn);
		conn.addSeparator();
		conn.add(re_conn);
		conn.addSeparator();
		conn.add(test_conn);
		conn.addSeparator();
		conn.add(dis_conn);
		conn.addSeparator();
		conn.add(conf);

		pane.addTab("<html><h4 style='padding:5px;background-color:#59599C;color:#FFFFFF;'>  File  </h4></html>", null,
				file, "File");
		pane.addTab("<html><h4 style='padding:5px;background-color:#628275;color:#FFFFFF;'>  Search  </h4></html>",
				null, seach, "Search");
		pane.addTab("<html><h4 style='padding:5px;background-color:#243806;color:#FFFFFF;'>  Run  </h4></html>", null,
				run, "Run");
		pane.addTab("<html><h4 style='padding:5px;background-color:#03014D;color:#FFFFFF;'>Connection</h4></html>",
				null, conn, "Connections");
		pane.addMouseMotionListener(new TabMouseMotionListener());

		return pane;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new Cmd().setVisible(true);
		});
	}
}
