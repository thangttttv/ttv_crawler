package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.az24.crawler.store.Store;
import com.az24.db.pool.C3p0StorePool;

public class StoreDAO {
	
	public int saveStore(Store store) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO az24_store.store	(user_id,name," +
								" mobile,tel,address,website,area,store_alias,province_id," +
								" province_code,province_name,status,active_mobile,is_store_warranty,type," +
								" create_user,	create_date, modify_user, modify_date, last_upgrade, active_date," +
								" end_date,fax,domain,is_auto)" +
								"VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)");
				
				ps.setInt(1, store.user_id);
				ps.setString(2, store.name);
				ps.setString(3,store.mobile);
				ps.setString(4, store.tel);
				ps.setString(5, store.address);
				ps.setString(6, store.website);
				ps.setString(7, store.area);
				ps.setString(8, store.store_alias);
				ps.setInt(9, store.province_id);
				ps.setString(10, store.province_code);
				ps.setString(11, store.province_name);
				
				ps.setInt(12, store.status);
				ps.setInt(13, store.active_mobile);
				ps.setInt(14, store.is_store_warranty);
				ps.setInt(15, store.type);
				
				ps.setString(16, store.create_user);
				ps.setLong(17, store.create_date);
				ps.setString(18, store.modify_user);
				ps.setLong(19, store.modify_date);
				ps.setLong(20, store.last_upgrade);
				ps.setLong(21, store.active_date);
				ps.setLong(22, store.end_date);
				ps.setString(23, store.fax);
				ps.setString(24, store.domain);
				
				
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				String sql = "SELECT LAST_INSERT_ID()";
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
					id = resultSet.getInt(1);
				C3p0StorePool.attemptClose(connection);
				C3p0StorePool.attemptClose(ps);
				C3p0StorePool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int saveStoreCategory(int store_id,int cat_id) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO az24_store.store_area	(store_id, cat_id ) VALUES (?," +
					" 	? ) ");
			ps.setInt(1, store_id);
			ps.setInt(2, cat_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			C3p0StorePool.attemptClose(connection);
			C3p0StorePool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int checkStore(String alias) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
				ps = connection.prepareStatement("SELECT id  FROM az24_store.store	WHERE LOWER(store_alias) = ? ");
				ps.setString(1, alias.toLowerCase());
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					id = rs.getInt(1);
				}
				C3p0StorePool.attemptClose(connection);
				C3p0StorePool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public String getStoreAlias(int id) {
		PreparedStatement ps;
		String alias ="";
			try {
				Connection connection = C3p0StorePool.getConnection();
				ps = connection.prepareStatement("SELECT store_alias  FROM az24_store.store	WHERE id = ? ");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					alias = rs.getString(1);
				}
				C3p0StorePool.attemptClose(connection);
				C3p0StorePool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alias;
	}
	
	public List<Store> updateProductStore() {
		PreparedStatement ps=null;
		List<Store> stores = new ArrayList<Store>();
		Store store = null;
		try {
				Connection connection = C3p0StorePool.getConnection();
				ps = connection.prepareStatement("SELECT id  FROM az24_store.store order by id ");
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					store = new Store();
					store.id =  rs.getInt(1);
					stores.add(store);
					int number_of_pro = countPro(connection, store.id);
					updateNumberProduct(connection, store.id, number_of_pro);
					System.out.println(store.id);
				}
				C3p0StorePool.attemptClose(connection);
				C3p0StorePool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stores;
	}
	
	public int updateNumberProduct(Connection connection,int store_id,int number_of_pro) {
		PreparedStatement ps;
		int id = 0;
		try {
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("Update  az24_store.store	SET num_of_product = ? Where id = ? ");
			ps.setInt(1, number_of_pro);
			ps.setInt(2, store_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int countPro(Connection connection ,int store_id) {
		PreparedStatement ps;
		int count =0;
		try {
				ps = connection.prepareStatement("SELECT count(id)  FROM az24_store.products_store	WHERE status = 1 And store_id = ? ");
				ps.setInt(1, store_id);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					count = rs.getInt(1);
				}
				C3p0StorePool.attemptClose(ps);
				C3p0StorePool.attemptClose(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static void main(String[] args) {
		StoreDAO storeDAO = new StoreDAO();
		storeDAO.updateProductStore();
	}
	
}
