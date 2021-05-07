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

	public String getDocument(String fullname, String language) {
		try {
			String sql = "";
			if (language == null || "".equals(language))
				sql = "select XWD_CONTENT from xwikidoc where XWD_FULLNAME=?";
			else
				sql = "SELECT * FROM xwiki.xwikidoc where XWD_FULLNAME = ? and (XWD_LANGUAGE=? or (XWD_DEFAULT_LANGUAGE=? and XWD_TRANSLATION=0));";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, fullname);
			if (language != null && !"".equals(language)) {
				preparedStatement.setString(2, language);
				preparedStatement.setString(3, language);
			}
			ResultSet rs = preparedStatement.executeQuery();

			rs.next();
			logValues(rs);

			return rs.getString("XWD_CONTENT");

		} catch (Exception e) {
			log.error(e);
		}
		return "";

	}

	public void closeConnection() {
		try {
			con.close();// Close the connection
		} catch (SQLException e) {
			log.error(e);
		}

	}

	public void logValues(ResultSet rs) {
		StringBuilder sb = new StringBuilder();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNo = rsmd.getColumnCount();
			sb.append("\n----------\n");
			for (int i = 1; i <= colNo; i++) {
				sb.append(rsmd.getColumnName(i) + ": '" + rs.getString(i) + "'\n");
			}
			sb.append("----------");
			log.debug(sb.toString());
		} catch (Exception e) {
			log.error(e);
		}
	}
}
