package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.StoreConfig;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class StoreConfigDAO {
	public int saveConfig(StoreConfig storeConfig) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
						.prepareStatement("INSERT INTO az24_store.store_config	( store_id,	layout_type,module,banner_logo," +
								" banner_logo_w,banner_logo_h,left_menu_type,home_intro_type,number_product,number_product_home," +
								" number_classified,number_news,price_inform,tab_top_view,tab_pro_new,tab_sale_off,	pro_home_type_view," +
								" pro_type_view,theme_id,background_image,bg_user_fixed,intro,contact,footer_info,create_user," +
								" create_date,	modify_user,modify_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
								"  ?,?,?,?,?,?,?,?,?,?,?)");
				
				ps.setInt(1, storeConfig.store_id);
				ps.setInt(2, storeConfig.layout_type);
				ps.setString(3, storeConfig.module);
				ps.setString(4, storeConfig.banner_logo);
				ps.setInt(5,storeConfig.banner_logo_w);
				ps.setInt(6,storeConfig.banner_logo_h);
				ps.setInt(7, storeConfig.left_menu_type);
				ps.setInt(8,storeConfig.home_intro_type);
				ps.setInt(9, storeConfig.number_product);
				ps.setInt(10, storeConfig.number_product_home);
				ps.setInt(11, storeConfig.number_classified);
				ps.setInt(12, storeConfig.number_news);
				ps.setString(13, storeConfig.price_inform);
				ps.setInt(14, storeConfig.tab_top_view);
				ps.setInt(15, storeConfig.tab_pro_new);
				ps.setInt(16, storeConfig.tab_sale_off);
				ps.setInt(17, storeConfig.pro_home_type_view);
				ps.setInt(18, storeConfig.pro_type_view);
				ps.setInt(19, storeConfig.theme_id);				
				ps.setString(20, storeConfig.background_image);
				ps.setInt(21, storeConfig.bg_user_fixed);
				ps.setString(22, storeConfig.intro);
				ps.setString(23, storeConfig.contact);
				ps.setString(24, storeConfig.footer_info);
				ps.setString(25, storeConfig.create_user);
				ps.setInt(26, storeConfig.create_date);
				ps.setString(27, storeConfig.modify_user);
				ps.setInt(28, storeConfig.modify_date);
				
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
	
	
	public int updateStore(int id,String banner,int w,int h) {
		PreparedStatement ps;
		
		try {
				Connection connection = C3p0StorePool.getConnection();
				connection.setAutoCommit(false);
				ps = connection.prepareStatement("UPDATE  az24_store.store_config SET banner_logo = ? , banner_logo_w = ?" +
						"  , banner_logo_h = ?  Where store_id = ?  ");
				
				ps.setString(1, banner);
				ps.setInt(2, w);
				ps.setInt(3,h);
				ps.setInt(4, id);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				C3p0ClassifiedPool.attemptClose(connection);
				C3p0ClassifiedPool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int updateStore(int id,String banner,int w,int h,int theme_id) {
		PreparedStatement ps;
		
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE  az24_store.store_config SET banner_logo = ? , banner_logo_w = ?" +
					"  , banner_logo_h = ? , theme_id = ? Where store_id = ?  ");
				
				ps.setString(1, banner);
				ps.setInt(2, w);
				ps.setInt(3,h);
				ps.setInt(4,theme_id);
				ps.setInt(5, id);
				ps.execute();
				connection.commit();
				connection.setAutoCommit(true);
				C3p0ClassifiedPool.attemptClose(connection);
				C3p0ClassifiedPool.attemptClose(ps);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
}

