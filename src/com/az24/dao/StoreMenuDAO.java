package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.StoreMenu;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class StoreMenuDAO {
	public int saveMenuStore(StoreMenu storeMenu) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.store_menu (name,url,open_type,is_top_menu,is_bottom,store_id,parent_id," +
								" sub_cate,level,order_menu,status,create_user,create_date,	modify_user,modify_date) VALUES	(" +
								" 	?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
				ps.setString(1, storeMenu.name);
				ps.setString(2, storeMenu.url);
				ps.setInt(3, storeMenu.open_type);
				ps.setInt(4, storeMenu.is_top_menu);
				ps.setInt(5,storeMenu.is_bottom);
				ps.setInt(6,storeMenu.store_id);
				ps.setInt(7, storeMenu.parent_id);
				ps.setString(8,storeMenu.sub_cate);
				ps.setInt(9, storeMenu.level);
				ps.setInt(10, storeMenu.order_menu);
				ps.setInt(11, storeMenu.status);
				ps.setString(12, storeMenu.create_user);
				ps.setInt(13, storeMenu.create_date);
				ps.setString(14, storeMenu.modify_user);
				ps.setInt(15, storeMenu.modify_date);
				
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
}
