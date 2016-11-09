package soc.ledger.manage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class Popup extends JPopupMenu implements ActionListener {

	public JMenuItem withd, bor_summ, ex_summ, prop;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JMenuItem inst_summ;
	JTable table;
	JDialog d;

	public Popup(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame, JTable table, JDialog d) {
		this.table = table;
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.d = d;
		init();
	}

	public final void init() {
		withd = new JMenuItem("Process A Withdrawal");
		withd.addActionListener(this);
		bor_summ = new JMenuItem("Borrowers Summary");
		bor_summ.setEnabled(false);
		ex_summ = new JMenuItem("Expenses Summary");
		ex_summ.setEnabled(false);
		inst_summ = new JMenuItem("Debt Installments Summary");
		inst_summ.setEnabled(false);
		prop = new JMenuItem("Others...");
		prop.setEnabled(false);

		this.add(withd);
		this.addSeparator();
		this.add(bor_summ);
		this.addSeparator();
		this.add(ex_summ);
		this.addSeparator();
		this.add(inst_summ);
		this.addSeparator();
		this.add(prop);

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
			processor();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.setVisible(false);
		}

	}

	public void processor() {
		ProcessWithdrawal p = new ProcessWithdrawal(table, tabs, rs, stm, frame, d);
		p.posting();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == withd) {
			Worker w = new Worker();
			w.execute();
		}

	}

}
