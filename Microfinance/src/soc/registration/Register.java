/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.registration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import soc.helpers.PauseThread;
import soc.helpers.SetDateCreated;
import soc.helpers.SplitPane;
import soc.helpers.TextValidator;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Register extends JPanel implements ActionListener {

	JTextField fname, mname, lname, parentnm, contact, email, filepath;
	JTextArea address;
	@SuppressWarnings("rawtypes")
	JComboBox gender;
	JDateChooser birth;
	JButton submit, cancel, uploadimage;
	JLabel imagelbl;
	UploadImage up;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	PreparedStatement pstmt;
	Connection conn;
	JFrame frame;
	private JTextField natidtxt;

	public Register(JTabbedPane tabs, ResultSet rs, Statement stm, PreparedStatement pstmt, Connection conn,
			JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.pstmt = pstmt;
		this.conn = conn;

		init();
	}

	public final void init() {
		this.setLayout(new BorderLayout());

		JPanel toppan = new JPanel(new FlowLayout());
		toppan.setBackground(new Color(0.5f, 0.5f, 1f));
		JLabel lbl = new JLabel("NEW MEMBER REGISTRATION");
		lbl.setFont(new Font("", Font.BOLD, 15));
		lbl.setForeground(Color.WHITE);

		toppan.add(lbl, SwingConstants.CENTER);

		this.add(toppan, BorderLayout.NORTH);
		this.add(createMidPanel(), BorderLayout.CENTER);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createMidPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		JPanel grid1 = new JPanel(new GridLayout(7, 2));
		grid1.setBorder(new TitledBorder(""));

		JLabel fnamelbl = new JLabel("First Name *:");
		JLabel mlbl = new JLabel("Middle Name :");
		JLabel lnamelbl = new JLabel("Surname *:");
		JLabel genderlbl = new JLabel("Gender :");
		JLabel bdatelbl = new JLabel("Date of Birth :");
		JLabel toplbl = new JLabel("Nationl ID :");
		toplbl.setFont(new Font("", Font.BOLD, 15));

		fname = new JTextField();
		mname = new JTextField();
		lname = new JTextField();
		natidtxt = new JTextField();

		Object[] da = { "", "Male", "Female" };
		gender = new JComboBox(da);
		birth = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		grid1.add(fnamelbl);
		grid1.add(fname);
		grid1.add(mlbl);
		grid1.add(mname);
		grid1.add(lnamelbl);
		grid1.add(lname);
		grid1.add(genderlbl);
		grid1.add(gender);
		grid1.add(bdatelbl);
		grid1.add(birth);
		grid1.add(new JLabel(""));
		grid1.add(new JLabel(""));

		grid1.add(toplbl);
		grid1.add(natidtxt);
		JPanel toppan = new JPanel(new BorderLayout());

		SplitPane split = new SplitPane();

		split.split.setLeftComponent(imagePanel());
		split.split.setRightComponent(grid1);

		toppan.add(split.split, BorderLayout.CENTER);

		// JLabel parentnamelbl = new JLabel("Parent / Guardian Name
		// (Mr/Mrs/Miss) :");
		JLabel contactlbl = new JLabel("Contact Number *:");
		JLabel emaillbl = new JLabel("Email Address :");
		JLabel addd = new JLabel("Physical Address Details :");
		addd.setFont(new Font("", Font.BOLD, 18));
		JPanel grid11 = new JPanel(new GridLayout(4, 2));

		// parentnm = new JTextField();
		contact = new JTextField();
		contact.addKeyListener(new TextValidator());
		email = new JTextField();
		// grid11.add(parentnamelbl);
		// grid11.add(parentnm);
		grid11.add(contactlbl);
		grid11.add(contact);
		grid11.add(emaillbl);
		grid11.add(email);

		grid11.add(addd);

		address = new JTextArea();
		address.setLineWrap(true);
		address.setWrapStyleWord(true);
		JPanel grid3 = new JPanel(new BorderLayout());
		grid3.setBorder(new TitledBorder(""));
		grid3.add(grid11, BorderLayout.NORTH);
		grid3.add(address, BorderLayout.CENTER);

		submit = new JButton("Submit");
		submit.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel grid4 = new JPanel(new FlowLayout());
		Box ob = Box.createHorizontalBox();
		ob.add(submit);
		ob.add(new JLabel("      "));
		ob.add(cancel);
		grid4.add(ob, SwingConstants.CENTER);
		grid3.add(grid4, BorderLayout.SOUTH);

		panel.add(toppan);
		panel.add(grid3);

		return panel;
	}

	public JPanel imagePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JDesktopPane dsk = new JDesktopPane();
		JPanel picpan = new JPanel(new BorderLayout());
		picpan.setBackground(new Color(0.5f, 0.5f, 1f));
		imagelbl = new JLabel();
		picpan.add(imagelbl, BorderLayout.CENTER);
		dsk.add(picpan);
		panel.add(picpan, BorderLayout.CENTER);
		panel.add(uploadImage(), BorderLayout.SOUTH);

		/*
		 * JPanel panel = new JPanel(new FlowLayout()); final JFXPanel fxPanel =
		 * new JFXPanel(); Platform.runLater(() -> { init(fxPanel); });
		 * panel.add(fxPanel, SwingConstants.CENTER);
		 */

		return panel;
	}

	public JPanel uploadImage() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		filepath = new JTextField();
		filepath.setBorder(null);
		filepath.setEditable(false);
		uploadimage = new JButton("Upload Image(Headshot)");
		uploadimage.addActionListener(this);
		up = new UploadImage(frame, filepath);
		panel.add(filepath);
		panel.add(uploadimage);
		return panel;
	}

	public void postData() {
		Date bdate = birth.getDate();
		String form = String.format("%1$tY-%1$tm-%1$td", bdate);

		String query = "INSERT INTO members(member_id,first_name,middle_name,last_name,gender,dob,natid,datecreated)VALUES('"
				+ incrementAcc() + "','" + fname.getText() + "','" + mname.getText() + "','" + lname.getText() + "','"
				+ gender.getSelectedItem().toString() + "','" + form + "','" + natidtxt.getText() + "','"
				+ new SetDateCreated().setDate() + "')";

		if (fname.getText().equals("") || lname.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please fill important the fields", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				stm.execute(query);

				new PauseThread().pause(18);

				String query5 = "SELECT member_id FROM members WHERE first_name = '" + fname.getText()
						+ "' AND last_name = '" + lname.getText() + "' AND dob = '" + form + "'";
				rs = stm.executeQuery(query5);
				rs.next();
				int id = rs.getInt(1);

				new PauseThread().pause(18);

				String qr = "INSERT INTO contacts(member_id,mobile,email,physical_add)VALUES('" + id + "','"
						+ contact.getText() + "','" + email.getText() + "','" + address.getText() + "')";
				stm.execute(qr);

				new PauseThread().pause(18);
				/*
				 * if (filepath.getText() != null) { String query13 =
				 * "INSERT INTO headshots(member_id,file)VALUES(?,?)"; pstmt =
				 * conn.prepareStatement(query13); pstmt.setString(1,
				 * Integer.toString(id)); pstmt.setBytes(2,
				 * up.imageConverter()); pstmt.execute(); }
				 */
				new PauseThread().pause(18);
				JOptionPane.showMessageDialog(frame,
						"First Name: " + fname.getText() + "\nSurname: " + lname.getText() + "\nAcc Number : " + id
								+ "\nGender : " + gender.getSelectedItem().toString() + "\nDate Of Birth : " + form
								+ "\nThank You!!",
						"Done", JOptionPane.INFORMATION_MESSAGE);

				fname.setText("");
				mname.setText("");
				lname.setText("");
				email.setText("");
				natidtxt.setText("");
				// parentnm.setText("");
				contact.setText("");
				address.setText("");
			} catch (SQLException ee) {
				ee.printStackTrace(System.err);
				JOptionPane.showMessageDialog(frame,
						"Error code 100\nImportant fields should not be left blank. \n" + ee.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Registration")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Registration   ", null, this, "New Registration");
				tabs.setSelectedIndex(count);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			postData();
		}
		if (e.getSource() == cancel) {

		}
		if (e.getSource() == uploadimage) {
			EventQueue.invokeLater(() -> {
				imagelbl.setIcon(new ImageIcon(up.getImage()));
			});
		}
	}

	public int incrementAcc() {
		String query = "SELECT member_id FROM members";
		int initial = 0;
		try {
			rs = stm.executeQuery(query);
			rs.last();
			initial = rs.getRow();
		} catch (SQLException ee) {

		}
		return initial + 1001;
	}

}
