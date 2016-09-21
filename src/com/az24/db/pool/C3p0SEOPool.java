package com.az24.db.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;
import com.mysql.jdbc.Statement;

public class C3p0SEOPool {
	public static DataSource unpooled = null;

	static {
		setupDataSource();
	}

	private static DataSource setupDataSource() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			unpooled = DataSources
					.unpooledDataSource("jdbc:mysql://192.168.1.101:3306/account?autoReconnect=true&characterEncoding=UTF-8",
							"aZ24aCCounT", "aZ@4accCounT!@$&");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return unpooled;
	}

	public static Connection getConnection() throws SQLException {
		return unpooled.getConnection();
	}

	public static void destroy() {
		try {
			DataSources.destroy(unpooled);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void attemptClose(ResultSet o) {
		try {
			if (o != null)
				o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void attemptClose(Statement o) {
		try {
			if (o != null)
				o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void attemptClose(PreparedStatement o) {
		try {
			if (o != null)
				o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void attemptClose(Connection o) {
		try {
			if (o != null)
				o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
