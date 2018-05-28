package com.sjjybsgj.core.enums;

public enum DBType {
	/**
	 * mysql
	 */
	MYSQL("jdbc:mysql:", "com.mysql.jdbc.Driver"),
	/**
	 * postgresql
	 */
	POSTGRESQL("jdbc:postgresql:", "org.postgresql.Driver"),
	/**
	 * sql server
	 */
	SQLSERVER("jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	/**
	 * oracle
	 */
	ORACLE("jdbc:oracle:", "oracle.jdbc.driver.OracleDriver");

	private String jdbcUrl;
	private String driverClass;

	DBType(String jdbcUrl, String driverClass) {
		this.jdbcUrl = jdbcUrl;
		this.driverClass = driverClass;
	}

	/**
	 * 获取连接地址
	 */
	public String getJDBCURL() {
		return jdbcUrl;
	}

	/**
	 * 获取驱动信息
	 * 
	 * @return
	 */
	public String getDriverClass() {
		return driverClass;
	}
}
