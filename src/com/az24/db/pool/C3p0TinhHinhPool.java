package com.az24.db.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class C3p0TinhHinhPool {
	public static DataSource unpooled = null;

	static {
		setupDataSource();
	}

	private static DataSource setupDataSource() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			unpooled = DataSources
				.unpooledDataSource(
							"jdbc:mysql://192.168.1.103:3306/hdc_Tinhhinh?autoReconnect=true&characterEncoding=UTF-8",
							"hDcTinHhinh", "HDCtiNhhinh!@#&");
			/*unpooled = DataSources
			.unpooledDataSource("jdbc:mysql://210.211.97.16:3306/hdc_Tinhhinh?autoReconnect=true&characterEncoding=UTF-8",
					"quangpn", "@!!!quang!(*^");*/
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
