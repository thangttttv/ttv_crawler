package com.az24.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.az24.crawler.store.StoreHomeIntro;
import com.az24.db.pool.C3p0ClassifiedPool;
import com.az24.db.pool.C3p0StorePool;

public class StoreHomeIntroDAO {
	
	public int saveIntroBanner(StoreHomeIntro homeIntro) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0StorePool.getConnection();
			connection.setAutoCommit(false);
			ps = connection	.prepareStatement("INSERT INTO az24_store.store_home_intro 	(store_id, type, active, width, height," +
								"	image_path,	image_name,	order_image,target, link, create_user, create_date,	modify_user, modify_date" +
								"   )  VALUES (?, ?, ? , ?, ?, ?, ?, ?," +
								" 	?, ?, ?, ?,	?,?)");
				
				ps.setInt(1, homeIntro.store_id);
				ps.setInt(2, homeIntro.type);
				ps.setInt(3, homeIntro.active);
				ps.setInt(4, homeIntro.width);
				ps.setInt(5,homeIntro.height);
				ps.setInt(6,homeIntro.store_id);
				ps.setString(7, homeIntro.image_path);
				ps.setString(8,homeIntro.image_name);
				ps.setInt(9, homeIntro.order_image);
				ps.setString(10, homeIntro.target);
				ps.setString(11, homeIntro.link);
				ps.setString(12, homeIntro.create_user);
				ps.setInt(13, homeIntro.create_date);
				ps.setString(14, homeIntro.modify_user);
				ps.setInt(15, homeIntro.modify_date);
				
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
