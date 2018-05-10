package gui;

import java.sql.*;

class jdbcTest {
	private static final String url = "jdbc:mysql://localhost";
	private static final String user = "root";
	private static final String password = "admin";
	
	public static void main (String args[]) throws SQLException {
		Connection con = null;
		try {
			con = DriverManager.getConnection (url, user, password);
			System.out.println ("Success");
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		String dbName = "SEAT";
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		String queryPreference = "select *" + "from " + dbName + ".studentDetails";
		String queryAllotted = "select *" + "from" + dbName + ".studentwiseAllotedElectiveDetails";
		String queryRejected = "select *" + "from" + dbName + ".studentwiseRejectedElectiveDetails";
		try {
			stmt = con.createStatement ();
			stmt1 = con.createStatement ();
			stmt2 = con.createStatement ();
			ResultSet rsPreference = stmt.executeQuery (queryPreference);
			ResultSet rsAllottment = stmt1.executeQuery (queryAllotted);
			ResultSet rsRejected = stmt2.executeQuery (queryRejected);
			while (rsPreference.next ()) {
				String studentType = rsPreference.getString ("type");
				String id = rsPreference.getString ("id");
				String courseName = rsPreference.getString ("courseName");
				System.out.println (studentType + "\t" + id + "\t" + courseName + "\t");
			}
			while (rsAllottment.next ()) {
				String allottedCourseName = rsAllottment.getString ("courseName");
				System.out.println (allottedCourseName + "\t");
			}
			while (rsRejected.next ()) {
				String rejectedCourseName = rsRejected.getString ("courseName");
				System.out.println (rejectedCourseName + "\t");
			}
		} catch (SQLException e) {
			System.out.println (e);
		} finally {
			if (stmt != null) { stmt.close (); }
			if (stmt1 != null) { stmt1.close (); }
			if (stmt2 != null) { stmt2.close (); }
		}
	}
}