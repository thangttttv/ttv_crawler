package com.az24.util.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.az24.db.pool.DBConfig;
import com.mchange.v2.c3p0.DataSources;

public class C3p0SwapHubPool {
	public static DataSource unpooled = null;

	static {
		setupDataSource();
	}

	private static DataSource setupDataSource() {
		try {
			DBConfig.loadProperties();
			Class.forName("com.mysql.jdbc.Driver");
			unpooled = DataSources
				.unpooledDataSource(DBConfig.db_shop_url_service,
						DBConfig.db_shop_user_service,DBConfig.db_shop_pass_service);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
