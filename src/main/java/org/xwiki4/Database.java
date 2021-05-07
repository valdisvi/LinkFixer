package org.xwiki4;

import java.sql.*;

import org.apache.log4j.Logger;

public class Database {
	Connection con = null;
	Logger log;

	public Database() {
		try {
			log = Logger.getLogger(Database.class);
			Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver class

			// Create connection
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/xwiki", "xwiki", "xwiki");

		} catch (Exception e) {
			log.error(e);
		}
	}

	public void getDocument() {
		try {
			Statement stmt = con.createStatement(); // Create a Statement
			// Execute the statement and store results in ResultSet object
			ResultSet rs = stmt.executeQuery("select count(XWD_ID) from xwikidoc");
			while (rs.next())
				System.out.println(rs.getInt(1)); // Iterate through ResultSet
		} catch (Exception e) {
			log.error(e);
		}

	}

	public void closeConnection() {
		try {
			con.close();// Close the connection
		} catch (SQLException e) {
			log.error(e);
		}

	}
}
