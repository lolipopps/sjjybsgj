package com.sjjybsgj.core.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sjjybsgj.source_db.model.SourceDb;

public class DBUtils {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private DBType dbtype;

	public void setDbtype(String dbtype) {
		dbtype = dbtype.toUpperCase();
		if (dbtype.toUpperCase().equals("POSTGRESQL")) {
			this.dbtype = DBType.POSTGRESQL;
		} else if (dbtype.toUpperCase().equals("MYSQL")) {
			this.dbtype = DBType.MYSQL;
		} else if (dbtype.toUpperCase().equals("SQLSERVER")) {
			this.dbtype = DBType.SQLSERVER;
		} else if (dbtype.toUpperCase().equals("ORACLE")) {
			this.dbtype = DBType.ORACLE;
		} else {
			throw new RuntimeException("不支持的数据库类型");
		}
	}

	public DBUtils(SourceDb sourcedb) {
		try {
			setDbtype(sourcedb.getDbType());
			if (sourcedb.getDbType().toUpperCase().equals("SQLSERVER")) {
				sourcedb.setDbName(";DatabaseName=" + sourcedb.getDbName());
			} else {
				sourcedb.setDbName("/" + sourcedb.getDbName());
			}
			System.out.println(sourcedb.getDbType());
			Class.forName(dbtype.getDriverClass());
			Properties props = new Properties();
			props.setProperty("user", sourcedb.getDbUserName());
			props.setProperty("password", sourcedb.getDbPassword());

			System.out.println(String.format("%s%s:%s%s", dbtype.getJDBCURL(), sourcedb.getIp(), sourcedb.getPort(),
					sourcedb.getDbName()));
			Connection con = DriverManager.getConnection(String.format("%s%s:%s%s", dbtype.getJDBCURL(),
					sourcedb.getIp(), sourcedb.getPort(), sourcedb.getDbName()), props);
			this.conn = con;
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			System.out.println("数据库连接失败" + e);
		}
	}

	public void closeConn() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getRangeSql(String string, String dbName,String tableName, String cloumnName,String dbType) {
		StringBuffer conditions = new StringBuffer();
		if(dbType.toUpperCase().equals("SQLSERVER")) {
			tableName = "dbo." + tableName;
		}
		if(dbType.toUpperCase().equals("ORACLE")) {
			dbName = "";
		}else {
			dbName = dbName+".";
		}
		String[] strs = string.split(",");
		for (String str : strs) {
			if (str.contains("-")) {
				String[] sts = str.split("-");
				for (int i = Integer.parseInt(sts[0]); i <= Integer.parseInt(sts[1]); i++) {
					conditions.append(i + ",");
				}
			} else {
				conditions.append(str + ",");
			}
		}
		conditions.replace(conditions.length() - 1, conditions.length(), ")");
		String sql = "SELECT * FROM " +dbName + tableName + " where " + cloumnName + " not in (" + conditions.toString();
		return sql;
	}

	public String execQuery(String sql) {
		try {
			Statement  stmt = conn.createStatement();
			stmt.execute(sql);
			rs = stmt.getResultSet();
			if(rs.next()) {
				return "有不满足的数据";
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "查询失败";
		}

	}

	public void getResult() {

	}

}
