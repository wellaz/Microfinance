package soc.bank;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import soc.deco.BlinkingButton;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class TransactionsPanel extends JPanel {
	Statement stm;
	ResultSet rs;
	JTabbedPane tabs;

	public TransactionsPanel(ResultSet rs, Statement stm, JTabbedPane tabs) {
		this.rs = rs;
		this.stm = stm;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Committee Transactions")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				DebtorsTable d = new DebtorsTable(rs, stm);
				JPanel topp = d.getDebtorsTable();
				CreditorsTable c = new CreditorsTable(rs, stm);
				JPanel bottompanel = c.getCreditorsTable();

				JPanel panel3 = new JPanel(new GridLayout(2, 1));
				panel3.add(topp);
				panel3.add(bottompanel);
				Box bottom = Box.createHorizontalBox();
				JButton b = new BlinkingButton(Color.BLUE);
				b.setText("<html><h3>Download PDF <br>Report</h3></html>");
				bottom.add(Box.createHorizontalGlue());
				bottom.add(b);
				this.add(panel3, BorderLayout.CENTER);
				this.add(bottom, BorderLayout.SOUTH);
				tabs.addTab("Committee Transactions   ", null, this,
						"General Information on an Committee Transactions.");
				tabs.setSelectedIndex(numberoftabs);
				b.addActionListener((event) -> {
					ReportPDF pdf = new ReportPDF(d.getTable(), c.getTable());
					ReportPDF.Worker w = pdf.new Worker();
					w.execute();
				});
			}
		});
	}
}
