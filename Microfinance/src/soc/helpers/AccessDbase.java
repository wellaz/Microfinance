/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 *
 * @author Wellington
 */
public class AccessDbase {
	public Statement stmt, stmt1, stm;
	public Connection conn, conn1;
	public ResultSet rs, rs1;
	public PreparedStatement pstmt;

	public AccessDbase() {
	}

	public void connectionDb() {
		try {
			String url = "jdbc:mysql://localhost/esociety";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, "root", "");
			conn1 = DriverManager.getConnection(url, "root", "");
			stmt = conn1.createStatement();
			stm = conn.createStatement();
			stmt1 = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null, "The Server is Offline ", "Offline Mode", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
