package com.amazonaws.lambda.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	public Connection getConnection() throws SQLException {
		// String myUrl =
		// "jdbc:mysql://coinanalysis.cdkj0sozbvys.us-east-1.rds.amazonaws.com/coinanalysis";
		String myUrl = "jdbc:mysql://mysql-server/db";
		Connection conn = DriverManager.getConnection(myUrl, "username", "password");

		// String myUrl = "jdbc:mysql://localhost/coinanalysis";
		// Connection conn = DriverManager.getConnection(myUrl, "root", "");
		return conn;
	}

}
