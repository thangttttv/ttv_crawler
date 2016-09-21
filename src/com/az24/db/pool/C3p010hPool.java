package com.az24.db.pool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class C3p010hPool {
	public static DataSource unpooled = null;

	static {
		setupDataSource();
	}

	private static DataSource setupDataSource() {
		try {
			DBConfig.loadProperties();
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println(DBConfig.db_xoso_url_service);
			System.out.println(DBConfig.db_xoso_user_service);
			System.out.println(DBConfig.db_xoso_pass_service);
			
			/*unpooled = DataSources
				.unpooledDataSource("jdbc:mysql://localhost:3306/xosothantai?autoReconnect=true&characterEncoding=UTF-8",
						"xosothantai","hF6CRVFmDUWH3P8n");*/
			unpooled = DataSources
					.unpooledDataSource(DBConfig.db_xoso_url_service,
							DBConfig.db_xoso_user_service,DBConfig.db_xoso_pass_service);
			
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
