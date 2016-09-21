package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.StoreSupport;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class StoreSupportDAO {
	public int saveSupport(StoreSupport storeSupport) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.store_support_online	(store_id, nick,type,store_support_online.order,status," +
								"  create_user,	create_date, modify_user, modify_date, display ) VALUES" +
								" (	?, ?, ?, ?,  ?, ?, ?, ? , ?, ? )");
				
				ps.setInt(1, storeSupport.store_id);
				ps.setString(2, storeSupport.nick);
				ps.setInt(3, storeSupport.type);
				ps.setInt(4, storeSupport.order);
				ps.setInt(5,storeSupport.status);
				ps.setString(6,storeSupport.create_user);
				ps.setInt(7, storeSupport.create_date);
				ps.setString(8,storeSupport.modify_user);
				ps.setInt(9, storeSupport.modify_date);
				ps.setString(10, storeSupport.display);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				String sql = "SELECT LAST_INSERT_ID()";
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
					id = resultSet.getInt(1);
				C3p0ClassifiedPool.attemptClose(connection);
				C3p0ClassifiedPool.attemptClose(ps);
				C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public boolean checkSupport(String nick,int type) {
		
		PreparedStatement ps;		
		boolean kq = false;
		try {
			Connection connection = C3p0StorePool.getConnection();
			ps = connection.prepareStatement("SELECT id FROM store_support_online  WHERE nick = ? AND type = ? ");
			ps.setString(1, nick);
			ps.setInt(2, type);
			ResultSet resultSet =	ps.executeQuery();
			if(resultSet.next())
			{
				kq = true;
			}
			C3p0ClassifiedPool.attemptClose(connection);
			C3p0ClassifiedPool.attemptClose(ps);
			C3p0ClassifiedPool.attemptClose(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kq;
	}
}
