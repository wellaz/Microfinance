package soc.countregistered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import soc.deco.BlinkingPanel;
import soc.deco.TranslucentJPanel;
import soc.helpers.IconImage;
import soc.helpers.TextValidator;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class FindMember extends JPanel {
	Statement stm;
	Statement stmt;
	ResultSet rs;
	JFrame comp;
	ResultSet rs1;
	private JProgressBar prog;
	private JLabel waitlbl;
	JPanel panel;
	JPanel midpanel, mainpanel;
	JLabel timelbl;
	JTabbedPane tabs;
	private JDialog dialog;
	private JTextField find;
	private JLabel error;
	String id;
	String[] notes = { "Member ID", "First Name", "Middle Name", "Last Name", "Gender", "DOB", "National ID" };
	String[] notes1 = { "Mobile", "Email", "Address" };
	JLabel[] label;
	JLabel[] data;
	JLabel[] label1;
	JLabel[] data1;

	public FindMember(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt, JFrame comp) {
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

		JPanel panel1 = new JPanel(new GridLayout(7, 2));
		panel1.setOpaque(false);
		JPanel panel2 = new JPanel(new GridLayout(3, 2));
		panel2.setOpaque(false);
		String text = "SELECT * FROM members WHERE member_id = '" + id + "'";
		try {
			rs = stm.executeQuery(text);
			if (!rs.next()) {
				JOptionPane.showMessageDialog(comp, "No Details found for this account " + id, "Information",
						JOptionPane.INFORMATION_MESSAGE);
				JPanel errorpanel = new BlinkingPanel("No Records Found !!!");
				panel.setLayout(new BorderLayout());
				panel.add(errorpanel, BorderLayout.CENTER);
			} else {
				panel.setLayout(new GridLayout(2, 1));
				int lengt = notes.length;
				label = new JLabel[lengt];
				data = new JLabel[lengt];
				for (int i = 0; i < lengt; i++) {
					label[i] = new JLabel(notes[i]);
					label[i].setForeground(Color.WHITE);
					label[i].setFont(new Font("", Font.BOLD, 15));
					data[i] = new JLabel(rs.getString(i + 1));
					panel1.add(label[i]);
					panel1.add(data[i]);
				}

				int lengt1 = notes1.length;
				label1 = new JLabel[lengt1];
				data1 = new JLabel[lengt1];
				String text1 = "SELECT mobile,email,physical_add FROM contacts WHERE member_id = '" + id + "'";
				rs1 = stmt.executeQuery(text1);

				for (int i = 0; i < lengt1; i++) {
					label1[i] = new JLabel(notes1[i]);
					label1[i].setForeground(Color.WHITE);
					label1[i].setFont(new Font("", Font.BOLD, 15));
					if (rs1.next()) {
						data1[i] = new JLabel(rs1.getString(i + 1));
					} else {
						data1[i] = new JLabel("");
					}
					panel2.add(label1[i]);
					panel2.add(data1[i]);

				}
				panel.add(panel1);
				panel.add(panel2);

			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
			JOptionPane.showMessageDialog(comp, "Error code 76\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return panel;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Find")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Find   ", null, this, "Find Account Details");
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

		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");

		find = new JTextField();
		find.addKeyListener(new TextValidator());
		find.addActionListener((event) -> {
			id = find.getText();
			EventQueue.invokeLater(() -> {
				insertTab();
				// dialog.setVisible(false);
				dialog.dispose();
			});
		});
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.add(top);
		midpanel.add(find);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);

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
