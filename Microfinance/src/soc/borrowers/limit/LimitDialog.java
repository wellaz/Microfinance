package soc.borrowers.limit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soc.helpers.IconImage;
import soc.helpers.SetDateCreated;
import soc.helpers.TextValidator;

/**
 *
 * @author Wellington
 */
public class LimitDialog {

	private JDialog dialog;
	private JTextField amountfld;
	private JButton find;
	private JLabel error;
	Statement stm;
	ResultSet rs;
	SetDateCreated setdate;

	public LimitDialog(Statement stm, ResultSet rs) {
		this.stm = stm;
		this.rs = rs;
		setdate = new SetDateCreated();
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Set Borrowers Limit", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel notelbl = new JLabel("Enter New Borrowers Limit Here \u2192", SwingConstants.CENTER);

		amountfld = new JTextField();
		amountfld.setBackground(Color.BLACK);
		amountfld.setForeground(Color.WHITE);
		amountfld.setFont(new Font("", Font.PLAIN, 16));
		amountfld.addKeyListener(new TextValidator());
		amountfld.setText(Double.toString(getLimitValue()));

		dialog.getContentPane().add(notelbl, BorderLayout.NORTH);
		dialog.getContentPane().add(amountfld, BorderLayout.CENTER);

		find = new JButton("OK");
		find.addActionListener((event) -> {
			String date = setdate.getDate();
			String time = setdate.getTime();

			if (amountfld.getText().equals(""))
				error.setText("The limit field is empty!");
			else {
				double val = Double.parseDouble(amountfld.getText());
				UpdateLimitData update = new UpdateLimitData(stm, rs);
				update.updateData(date, time, val);
				dialog.dispose();
				JOptionPane.showMessageDialog(null, "Done! \nThanks.", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		error = new JLabel();
		error.setForeground(Color.red);
		Box lowerbox = Box.createHorizontalBox();
		lowerbox.add(error);
		lowerbox.add(Box.createHorizontalGlue());
		lowerbox.add(find);
		dialog.getRootPane().setDefaultButton(find);
		dialog.getContentPane().add(lowerbox, BorderLayout.SOUTH);

		dialog.setSize(300, 155);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);

	}

	public double getLimitValue() {
		GetLimitValue getlimitvalue = new GetLimitValue(stm, rs);
		return getlimitvalue.getLimitValue();
	}

}
