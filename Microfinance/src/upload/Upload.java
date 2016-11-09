package upload;

import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soc.helpers.MyNativeFileView;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class Upload extends JFrame {
	String Database = "esociety";
	String backUpPath = null;
	Connection con;
	Statement st;
	ResultSet rs;

	public Upload() {
		extract();
	}

	public String getBackUpPath() {

		JFileChooser fc = null;
		if (fc == null) {
			fc = new JFileChooser();
			fc.setDialogTitle("Selekct File to Upload");
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileView(new MyNativeFileView());
			fc.setPreferredSize(new Dimension(500, 350));
		}
		int returnVal = fc.showDialog(this, "Select File");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			backUpPath = file.getAbsolutePath();
		}
		return backUpPath;
	}

	public void extract() {
		String backuppath = getBackUpPath();

		String Password = "";
		String user = "root";
		String Mysqlpath = getMysqlBinPath(user, Password, Database);
		try {
			String[] executeCmd = new String[] { Mysqlpath, Database, "-u" + user, "-p" + Password, "-e",
					" source " + backuppath };
			/*
			 * NOTE: processComplete=0 if correctly executed, will contain other
			 * values if not
			 */
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();
			/*
			 * NOTE: processComplete=0 if correctly executed, will contain other
			 */
			if (processComplete == 0) {
				JOptionPane.showMessageDialog(null, "Successfully restored from SQL : ");
			} else {
				JOptionPane.showMessageDialog(null, "Error at restoring");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Back Up Failed.\nThe backup process might have been interrupted",
					"Database BackUp Wizard", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public String getMysqlBinPath(String user, String password, String db) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/" + db, user, password);
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String a = "";

		try {
			rs = st.executeQuery("select @@basedir");
			while (rs.next()) {
				a = rs.getString(1);
			}
		} catch (Exception eee) {
			eee.printStackTrace();
		}
		a = a + "\\bin\\";
		// System.err.println("Mysql path is :" + a);
		return a;
	}

	public static void main(String[] args) {
		new Upload();
	}

}
