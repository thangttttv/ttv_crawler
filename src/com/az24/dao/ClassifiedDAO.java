package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.az24.crawler.model.Classified;
import com.az24.crawler.store.City;
import com.az24.db.pool.C3p0ClassifiedDesPool;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.util.Logger;

public class ClassifiedDAO {
	
	private com.az24.util.Logger logger = new Logger(this.getClass().getName());

	public Classified getById(int id) {
		
		PreparedStatement ps;
		Classified classified = null;
		try {
			Connection connection = C3p0ClassifiedPool.getConnection();
			ps = connection
					.prepareStatement("SELECT cla_id,cla_name,cla_userid,cla_username FROM raovat2011.classified Where cla_id = ?  ");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				classified = new Classified();
				classified.cla_id = resultSet.getInt("cla_id");
				classified.cla_name = resultSet.getString("cla_name");
				classified.cla_userid = resultSet.getInt("cla_userid");
				classified.cla_username = resultSet.getString("cla_username");

			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classified;
	}

	public List<City> getCitis() {
		PreparedStatement ps;
		City city = null;
		List<City> citys = new ArrayList<City>();
		try {
			Connection connection = C3p0ClassifiedPool.getConnection();
			ps = connection
					.prepareStatement("SELECT cit_id,cit_name, cit_parent_id, cit_order, lang_id, cit_short, admin_id, "
							+ "cit_description, cit_show, cit_rewrite FROM raovat2011.city Where cit_parent_id = 0  ");

			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				city = new City();
				city.id = resultSet.getInt("cit_id");
				city.name = resultSet.getString("cit_name");
				city.code = resultSet.getString("cit_rewrite");
				citys.add(city);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return citys;
	}

	public List<City> getCitisQuan() {
		PreparedStatement ps;
		City city = null;
		List<City> citys = new ArrayList<City>();
		try {
			Connection connection = C3p0ClassifiedPool.getConnection();
			ps = connection
					.prepareStatement("SELECT cit_id,cit_name, cit_parent_id, cit_order, lang_id, cit_short, admin_id, "
							+ "cit_description, cit_show, cit_rewrite FROM raovat2011.city Where cit_parent_id >0  ");

			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				city = new City();
				city.id = resultSet.getInt("cit_id");
				city.name = resultSet.getString("cit_name");
				city.code = resultSet.getString("cit_rewrite");
				citys.add(city);
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return citys;
	}

	public int saveClassified(Classified classified) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		try {
			Connection conn = C3p0ClassifiedPool.getConnection();
			conn.setAutoCommit(false);
			strSQL = new StringBuffer(
					"INSERT INTO classified("
							+ "cla_category,cla_city,cla_date,cla_expired,cla_auto,cate_parent1,cate_parent2,cate_parent3,cate_parent4,"
							+ "cla_update_date,cla_picture,cla_name,cla_description,cla_contact,cla_userid,cla_username,cla_rewrite" +
							  ",cla_quan,cla_cityname,cla_price,cla_email,cla_active) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, classified.cla_category);
			preStmt.setInt(2, classified.cla_city);
			preStmt.setLong(3, classified.cla_date);
			preStmt.setLong(4, classified.cla_expired);
			preStmt.setInt(5, classified.cla_auto);
			preStmt.setInt(6, classified.cate_parent1);

			preStmt.setInt(7, classified.cate_parent2);
			preStmt.setInt(8, classified.cate_parent3);
			preStmt.setInt(9, classified.cate_parent4);

			preStmt.setLong(10, classified.cla_update_date);
			preStmt.setString(11, classified.cla_picture);
			preStmt.setString(12, classified.cla_name);

			preStmt.setString(13, classified.cla_description);
			preStmt.setString(14, classified.cla_contact);
			preStmt.setInt(15, classified.cla_userid);
			preStmt.setString(16, classified.cla_username);

			preStmt.setString(17, classified.cla_rewrite);
			preStmt.setInt(18, classified.cla_district);
			preStmt.setString(19, classified.cla_cityname);
			preStmt.setDouble(20, classified.cla_price);
			preStmt.setString(21, classified.cla_email);
			preStmt.setInt(22, classified.cla_active);
			preStmt.execute();

			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(preStmt);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return id;
	}

	public int saveClassifiedDesc(int cla_id, String content, int idTable) {
		PreparedStatement preStmt = null;
		StringBuffer strSQL = null;
		int id = 0;
		try {
			Connection conn = C3p0ClassifiedDesPool.getConnection();
			conn.setAutoCommit(false);
			strSQL = new StringBuffer("INSERT INTO classified_description_"
					+ idTable + "(id,description) VALUES (?,?)");

			preStmt = conn.prepareStatement(strSQL.toString());
			preStmt.setInt(1, cla_id);
			preStmt.setString(2, content);
			preStmt.execute();
			conn.commit();
			conn.setAutoCommit(true);

			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0ClassifiedDesPool.attemptClose(conn);
			C3p0ClassifiedDesPool.attemptClose(preStmt);
			C3p0ClassifiedDesPool.attemptClose(resultSet);
		} catch (NoSuchElementException nse) {
			logger.error("save: Error executing >>>" + nse.toString());
		} catch (SQLException se) {
			logger.error("save: Error executing SQL >>>" + strSQL.toString()
					+ se.toString());
		} catch (Exception e) {
			logger.error("save: Error executing >>>" + e.toString());
		}
		return id;
	}

	public int getClassified(String subject) {
		int id = 0;
		String table = "classified";
		PreparedStatement ps;

		try {
			Connection conn = C3p0ClassifiedPool.getConnection();
			ps = conn.prepareStatement("Select cla_id from  " + table
					+ " Where LOWER(trim(cla_name)) = ? ");
			ps.setString(1, subject.toLowerCase().trim());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	public Map<Integer, Integer> getCategoriesParent(int cat,
			Map<Integer, Integer> holder) {
		int cat_parent = 0;
		int level = 0;		
		try {
			Connection conn = C3p0ClassifiedPool.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT cat_parent_id, level FROM categories_multi WHERE cat_id = "
							+ cat);
			if (rs.next()) {
				cat_parent = rs.getInt(1);
				level = rs.getInt(2);
				holder.put(level, cat);
			}
			if (cat_parent == 0)
				return holder;
			else
				getCategoriesParent(cat_parent, holder);
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	String subcate = "";

	public void getSubCategory(int catid) {
		try {
			Connection conn = C3p0ClassifiedPool.getConnection();
			ResultSet rs = conn
					.createStatement()
					.executeQuery(
							"SELECT cat_id FROM categories_multi WHERE cat_active = 1 And cat_parent_id =  "
									+ catid);
			while (rs.next()) {
				this.subcate += rs.getInt(1) + ",";
				getSubCategory(rs.getInt(1));

			}
			C3p0ClassifiedPool.attemptClose(conn);
			C3p0ClassifiedPool.attemptClose(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void incrementClassified(int cat_id) {
		try {
			Connection conn = C3p0ClassifiedPool.getConnection();
			ResultSet tmpRs = conn.createStatement().executeQuery(
					"SELECT cla_number FROM categories_multi WHERE cat_id = "
							+ cat_id);
			
			tmpRs.next();
			int numberLv1 = tmpRs.getInt(1);
			tmpRs.close();
			conn.createStatement().executeUpdate(
					"UPDATE categories_multi SET cla_number = "
							+ (numberLv1 + 1) + " WHERE cat_id = " + cat_id);
			tmpRs.close();
			C3p0ClassifiedPool.attemptClose(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
