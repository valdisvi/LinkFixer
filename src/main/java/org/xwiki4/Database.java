package org.xwiki4;

import java.sql.*;

import org.apache.log4j.Logger;

public class Database {
	private Connection con = null;
	private Logger log;

	public Database() {
		try {
			log = Logger.getLogger(Database.class);
			Class.forName("com.mysql.cj.jdbc.Driver"); // Load the driver class

			// Create connection
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3307/xwiki?autoReconnect=true&serverTimezone=UTC&characterEncoding=utf8",
					"xwiki", "xwiki");

		} catch (Exception e) {
			log.error(e);
		}
	}

	public String getDocumentValue(String fullname, String language, String fieldName) {
		String sql = "";
		if (language == null || "".equals(language))
			sql = "select * from xwiki.xwikidoc where XWD_FULLNAME = ? and XWD_TRANSLATION = 0";
		else
			sql = "SELECT * FROM xwiki.xwikidoc where XWD_FULLNAME = ? and (XWD_LANGUAGE=? or (XWD_DEFAULT_LANGUAGE = ? and XWD_TRANSLATION = 0));";
		if (fieldName == null || "".equals(fieldName))
			fieldName = "XWD_CONTENT";
		try {

			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, fullname);
			if (language != null && !"".equals(language)) {
				preparedStatement.setString(2, language);
				preparedStatement.setString(3, language);
			}
			log.trace(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) // Return, empty if result set is empty
				return "";
			traceValues(rs);
			return rs.getString(fieldName);
		} catch (Exception e) {
			log.error(e);
		}
		return "";
	}

	public String getDocument(String fullname, String language) {
		return getDocumentValue(fullname, language, "XWD_CONTENT");
	}

	public void putDocument(String fullName, String language, String value) {
		String sql = "";
		if (language == null || "".equals(language))
			sql = "update xwiki.xwikidoc set XWD_CONTENT = ? where XWD_FULLNAME = ? and XWD_TRANSLATION = 0";
		else
			sql = "update xwiki.xwikidoc set XWD_CONTENT = ? where XWD_FULLNAME = ? and (XWD_LANGUAGE = ? or (XWD_DEFAULT_LANGUAGE = ? and XWD_TRANSLATION = 0));";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, value);
			preparedStatement.setString(2, fullName);
			if (language != null && !"".equals(language)) {
				preparedStatement.setString(3, language);
				preparedStatement.setString(4, language);
			}
			log.trace(preparedStatement);
			preparedStatement.executeUpdate();
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

	public void traceValues(ResultSet rs) {
		StringBuilder sb = new StringBuilder();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNo = rsmd.getColumnCount();
			sb.append("\n----------\n");
			for (int i = 1; i <= colNo; i++) {
				sb.append(rsmd.getColumnName(i) + ": '" + rs.getString(i) + "'\n");
			}
			sb.append("----------");
			log.trace(sb.toString());
		} catch (Exception e) {
			log.error(e);
		}
	}
}
