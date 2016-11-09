package soc.reversals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import soc.deco.TranslucentJPanel;
import soc.helpers.SplitPane;

@SuppressWarnings("serial")
public class ReversalTemplete extends JPanel implements ActionListener {
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JButton subreversal;
	private JButton exreversal;
	private JButton commreversal;
	private JButton instreversal;
	private JButton fulldreversal;
	SplitPane split;
	JPanel rightpanel;

	public ReversalTemplete(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;

		this.setLayout(new BorderLayout());
		split = new SplitPane();
		split.split.setLeftComponent(leftPanel());
		rightpanel = new TranslucentJPanel(Color.BLUE);
		split.split.setRightComponent(rightpanel);

		this.add(split.split);
	}

	public JPanel leftPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLACK);
		panel.setLayout(new GridLayout(5, 1, 1, 10));
		subreversal = new JButton("Reverse Incorrect Subscription");
		subreversal.addActionListener(this);
		exreversal = new JButton("Reverse Incorrect Expense");
		exreversal.addActionListener(this);
		commreversal = new JButton("Reverse Incorrect Commission Posted");
		commreversal.addActionListener(this);
		instreversal = new JButton("Reverse Incorrect Debt Installment Posted");
		instreversal.addActionListener(this);
		fulldreversal = new JButton("Reverse Incorrect Full Debt Payment");
		fulldreversal.addActionListener(this);

		panel.add(subreversal);
		panel.add(exreversal);
		panel.add(commreversal);
		panel.add(instreversal);
		panel.add(fulldreversal);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Reversals")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Reversals   ", null, this, "Reversals Templete");
				tabs.setSelectedIndex(count);
			}
		});
	}

}
