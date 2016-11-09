package soc.users;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import soc.deco.TranslucentJPanel;
import soc.deco.TranslucentJPanel1;

@SuppressWarnings("serial")
public class NewUser extends JPanel implements ActionListener {

	JTextField amounttxt, memberids;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public NewUser(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All User Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));
		JLabel acclbl = new JLabel("User Name");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Password :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();

		memberids = new JTextField();

		midpanel.add(acclbl);
		midpanel.add(memberids);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("Proceed");
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("New User")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("New User   ", null, this, "Create New User");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void debitLedger(String uname, String pwd) {
		String query = "SELECT password FROM users WHERE password = '" + pwd + "'";
		try {
			rs = stm.executeQuery(query);
			if (rs.next()) {
				JOptionPane.showMessageDialog(frame,
						"The password characters already exist! Try another character combination.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				String text = "INSERT INTO users(username,password)VALUES('" + uname + "','" + pwd + "')";
				stm.execute(text);
				JOptionPane.showMessageDialog(frame, "Done.", "Success", JOptionPane.INFORMATION_MESSAGE);

			}

		} catch (SQLException ee) {

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
			dialog.dispose();
		}

	}

	public void proceed() {
		String uname = memberids.getText();
		String pwd = amounttxt.getText();
		if (!pwd.equals("") && !uname.equals("")) {
			debitLedger(uname, pwd);
			memberids.setText("");
			amounttxt.setText("");
		} else
			JOptionPane.showMessageDialog(frame, "Null user cannot be created ", "Warning",
					JOptionPane.WARNING_MESSAGE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			Worker w = new Worker();
			w.execute();
		}

	}
}