package soc.reconciliation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class RecoPopup extends JPopupMenu implements ActionListener {

	public JMenuItem sub_summ, bor_summ, ex_summ, prop, paid_comm, inv_dep, inv_with, full_re,term;
	String from, to;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JMenuItem inst_summ;
	private JMenuItem cdp;
	private JMenuItem cwt;

	public RecoPopup(String from, String to, ResultSet rs, Statement stm, JFrame frame) {

		this.from = from;
		this.to = to;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		init();
	}

	public final void init() {
		sub_summ = new JMenuItem("Subscriptions Summary");
		sub_summ.addActionListener(this);
		bor_summ = new JMenuItem("Borrowers Summary");
		bor_summ.addActionListener(this);
		ex_summ = new JMenuItem("Expenses Summary");
		ex_summ.addActionListener(this);
		inst_summ = new JMenuItem("Debt Installments Summary");
		inst_summ.addActionListener(this);
		paid_comm = new JMenuItem("Paid Commissions Summary");
		paid_comm.addActionListener(this);
		inv_dep = new JMenuItem("Investors' Deposit");
		inv_dep.addActionListener(this);
		inv_with = new JMenuItem("Investors' Withdrawals");
		inv_with.addActionListener(this);
		full_re = new JMenuItem("Full Debt Payments");
		full_re.addActionListener(this);
		term = new JMenuItem("Terminated Members");
		term.addActionListener(this);
		cdp = new JMenuItem("Cashier Deposits");
		cdp.addActionListener(this);
		cwt = new JMenuItem("Cashier Withdrawals");
		cwt.addActionListener(this);
		prop = new JMenuItem("Others...");
		prop.setEnabled(false);

		this.add(sub_summ);
		this.addSeparator();
		this.add(bor_summ);
		this.addSeparator();
		this.add(ex_summ);
		this.addSeparator();
		this.add(inst_summ);
		this.addSeparator();
		this.add(paid_comm);
		this.addSeparator();
		this.add(inv_dep);
		this.addSeparator();
		this.add(inv_with);
		this.addSeparator();
		this.add(full_re);
		this.addSeparator();
		this.add(term);
		this.addSeparator();
		this.add(cdp);
		this.addSeparator();
		this.add(cwt);
		this.addSeparator();
		this.add(prop);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sub_summ) {
			GetTotalSub ts = new GetTotalSub(rs, stm, frame);
			ts.getAllValues(from, to);
		}
		if (e.getSource() == bor_summ) {
			GetDebts ds = new GetDebts(rs, stm, frame);
			ds.getAllValues(from, to);
		}
		if (e.getSource() == ex_summ) {
			GetTotalExp ex = new GetTotalExp(rs, stm, frame);
			ex.getAllExValues(from, to);
		}
		if (e.getSource() == inst_summ) {
			GetInstallments ins = new GetInstallments(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == paid_comm) {
			GetCommissions ins = new GetCommissions(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == inv_dep) {
			GetLedgerDep ins = new GetLedgerDep(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == inv_with) {
			GetLedgerWith ins = new GetLedgerWith(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == full_re) {
			GetFullRe ins = new GetFullRe(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == term) {
			GetTermination ins = new GetTermination(rs, stm, frame);
			ins.getAllValues(from, to);
		}
		if (e.getSource() == cdp) {
			CashierDep d = new CashierDep(rs, stm, frame);
			d.getAllValues(from, to);
		}
		if (e.getSource() == cwt) {
			CashierWith w = new CashierWith(rs, stm, frame);
			w.getAllValues(from, to);
		}


	}

}
