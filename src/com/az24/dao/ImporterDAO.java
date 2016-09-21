package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import com.az24.db.pool.C3p0LogPool;

public class ImporterDAO {

	public int getProductID(String product_vg_id) {

		PreparedStatement ps;
		int product_id = 0;
		try {
			Connection connection = C3p0LogPool.getConnection();
			ps = connection
					.prepareStatement("SELECT product_id FROM tbl_importer   "
							+ " WHERE url like '%/" + product_vg_id
							+ "/%'  limit 1 ");
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				product_id = resultSet.getInt(1);
			}
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
			C3p0LogPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product_id;
	}

	public void saveLog(int product_id, int product_store_id, int cat_id,
			String url_id, String url, String source, int store_id) {
		PreparedStatement ps;

		try {
			Connection connection = C3p0LogPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO crawler.crawler_vatgia_product_store ( product_id, "
							+ "	product_store_id, cat_id, url_id, url, source, create_date,store_id) VALUES	(?,?, "
							+ "	?,?,?,?,?,?)");
			ps.setInt(1, product_id);
			ps.setInt(2, product_store_id);
			ps.setInt(3, cat_id);
			ps.setString(4, url_id);
			ps.setString(5, url);
			ps.setString(6, source);
			ps.setTimestamp(7, new Timestamp(Calendar.getInstance()
					.getTimeInMillis()));
			ps.setInt(8, store_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkProductStore(String url_id, int store_cat_id) {

		PreparedStatement ps;

		boolean kt = false;
		try {
			Connection connection = C3p0LogPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("SELECT id FROM crawler_vatgia_product_store    WHERE url_id  = ? and cat_id = ?  limit 1 ");
			ps.setString(1, url_id);
			ps.setInt(2, store_cat_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				kt = true;
			}
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
			C3p0LogPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kt;
	}

	public int getProductStore(String url_id, int store_cat_id) {

		PreparedStatement ps;

		int id = 0;
		try {
			Connection connection = C3p0LogPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("SELECT product_store_id FROM crawler_vatgia_product_store  "
							+ " WHERE url_id  = ? and cat_id = ?  limit 1 ");
			ps.setString(1, url_id);
			ps.setInt(2, store_cat_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt(1);
			}
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
			C3p0LogPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public void saveLogProduct(String source, int pro_id, int cat_id,
			String url_id, String url) {
		try {
			Connection connection = C3p0LogPool.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO crawler.tbl_importer (product_id, cat_id, url_id, url, source)"
							+ "  VALUES (?,?,?,?,?)  ");
			ps.setInt(1, pro_id);
			ps.setInt(2, cat_id);
			ps.setString(3, url_id);
			ps.setString(4, url);
			ps.setString(5, source);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			C3p0LogPool.attemptClose(connection);
			C3p0LogPool.attemptClose(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
